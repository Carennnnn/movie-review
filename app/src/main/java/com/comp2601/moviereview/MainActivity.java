package com.comp2601.moviereview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main";
    private static final String APIKEY = "5f628c68";

    public static final String SEARCH_BUNDLE = "SearchBundle";
    public static final String MOVIE_DETAIL_BUNDLE = "MovieDetailBundle";
    public static final String SEARCH = "search";

    private String mMovieString;
    private HttpURLConnection mConnection = null;
    private BufferedReader mReader = null;
    private StringBuffer mStrBuffer;

    //Download JSON by movie name from OMDB API
    class MovieDataDownload extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args){
            try {
                //get movie URL from the first argument
                URL mMovieUrl = new URL(args[0]);

                 try{
                    mConnection = (HttpURLConnection) mMovieUrl.openConnection();
                    InputStream mInStream = new BufferedInputStream(mConnection.getInputStream());
                    mReader = new BufferedReader(new InputStreamReader(mInStream));
                    mStrBuffer = new StringBuffer();
                    String line = "";

                    //store json data into mStrBuffer
                    while((line = mReader.readLine()) != null) {
                        mStrBuffer.append(line);
                    }
                }catch (UnknownHostException e){
                    e.printStackTrace();
                    Log.i(TAG, "Unknown Host Exception");
                }catch (IOException e){
                    e.printStackTrace();
                }
                finally {
                    if( mConnection != null){
                        mConnection.disconnect();
                    }
                    try {
                        if(mReader != null){
                            mReader.close();
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
            catch (MalformedURLException e){
                e.printStackTrace();
            }
            if(mStrBuffer != null){
                mMovieString = mStrBuffer.toString();
            }else{
                Log.i(TAG, "ERROR: not response data received");
            }
            return mMovieString;
        }


        @Override
        protected  void onPostExecute(String result){
            super.onPostExecute(result);
            JSONObject mJSONMovieData = null;

            if (mStrBuffer != null){

                //convert mStrBuffer into String
                Log.i(TAG, mStrBuffer.toString());

                //convert result into Json Object
                try{
                    mJSONMovieData = new JSONObject(result);

                    //check if response is true
                    if(mJSONMovieData.getString("Response").equalsIgnoreCase("True")){

                        //get movie arrays that match search names
                        JSONArray mJSONMovieArray = mJSONMovieData.getJSONArray("Search");
                        int length =mJSONMovieArray.length();

                        //use Bundle to store search result
                        Bundle BUNDLE_SEARCH_RESULT = new Bundle();
                        BUNDLE_SEARCH_RESULT.putString(SEARCH, result);

                        //start new Intent to show result movies
                        Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                        intent.putExtra(SEARCH_BUNDLE, BUNDLE_SEARCH_RESULT);

                        startActivity(intent);

                    }//if response is False, then make a toast to show the error message
                    else{
                        Log.i(TAG, "False");
                        //get error message
                        String mErrorMessage = mJSONMovieData.getString("Error");
                        //show error message in toast
                        Toast.makeText(getApplicationContext(), mErrorMessage, Toast.LENGTH_LONG).show();
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                }
            } else {
                Log.i(TAG, "ERROR: not response data received");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get movies array from class Movie
        Movie[] movies = Movie.movies;

        //display movies in grid view by passing movies to ImageAdapter
        GridView movieGrid = (GridView) findViewById(R.id.movie_grid_view);
        ImageAdapter imageAdapter = new ImageAdapter(getApplicationContext(), movies);
        movieGrid.setAdapter(imageAdapter);

        //set item click listener for each movie in grid view
        movieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //get movies imdbID
                String imdbID = movies[position].getImdbID();
                //start new intent to display movie detail
                Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
                intent.putExtra(MOVIE_DETAIL_BUNDLE, imdbID);
                startActivity(intent);
            }
        });

    }

    //create search button on action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        //set menu
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //invoke when text sumbmit
            @Override
            public boolean onQueryTextSubmit(String query) {

                //create url with search text
                String url = "http://www.omdbapi.com/?apikey=" + APIKEY + "&s=" +query;

                //use movie downloader to download returned search results
                MovieDataDownload dataDownload = new MovieDataDownload();
                dataDownload.execute(url);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }
}