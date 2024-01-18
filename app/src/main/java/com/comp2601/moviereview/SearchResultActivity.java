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
import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity {

    public static final String SEARCH_BUNDLE = "SearchBundle";
    public static final String SEARCH = "search";
    public static final String MOVIE_DETAIL_BUNDLE = "MovieDetailBundle";
    private static final String TAG = "Search";

    private Movie[] moviesArray;
    private static final String APIKEY = "5f628c68";

    private HttpURLConnection mConnection = null;
    private BufferedReader mReader = null;
    private StringBuffer mStrBuffer;
    private String mMovieString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        GridView movieGrid = (GridView) findViewById(R.id.movie_grid_view);

        Intent mIntent = getIntent();
        if(mIntent != null){
            //get search result movie string
            Bundle BUNDLE_SEARCH = mIntent.getBundleExtra(MainActivity.SEARCH_BUNDLE);
            String mMovie = BUNDLE_SEARCH.getString(MainActivity.SEARCH);

            try {
                //convert Strings into JSON Object
                JSONObject mJSONMovieData = new JSONObject(mMovie);
                ArrayList<Movie> movies = new ArrayList<Movie>();

                //get movie arrays that match search names
                JSONArray mJSONMovieArray = mJSONMovieData.getJSONArray("Search");
                int length = mJSONMovieArray.length();

                //for each movie in result movie array, store title, poster, imdbID
                //and add to movie arraylist
                for (int i = 0; i < length; i++) {
                    JSONObject mJSONMovieObject = mJSONMovieArray.getJSONObject(i);
                    String title = mJSONMovieObject.getString("Title");
                    String poster = mJSONMovieObject.getString("Poster");
                    String imdbID = mJSONMovieObject.getString("imdbID");
                    movies.add(new Movie(title, poster, imdbID));
                }

                //convert movies arraylist to array
                moviesArray = new Movie[movies.size()];
                for(int i = 0; i < movies.size(); i++){
                    moviesArray[i] = movies.get(i);
                }

                //use image adapter to display images in grid view
                ImageAdapter imageAdapter = new ImageAdapter(getApplicationContext(), moviesArray);
                movieGrid.setAdapter(imageAdapter);

                movieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    //set each movie item clickable, get movie's imdbID and start
                    //new intent to display movie's detail
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String imdbID = moviesArray[position].getImdbID();
                        Intent intent = new Intent(SearchResultActivity.this, MovieDetailActivity.class);
                        intent.putExtra(MOVIE_DETAIL_BUNDLE, imdbID);
                        startActivity(intent);
                    }
                });

            }catch(JSONException e){
                e.printStackTrace();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);

        //create menu item
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //when the search text submitted, display search results
            @Override
            public boolean onQueryTextSubmit(String query) {
                //string url for search query
                String url = "http://www.omdbapi.com/?apikey=" + APIKEY + "&s=" +query;

                //use movie downloader to download JSON data for search result
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

    class MovieDataDownload extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args){
            try {

                //get movie url
                URL mMovieUrl = new URL(args[0]);

                try{
                    mConnection = (HttpURLConnection) mMovieUrl.openConnection();
                    InputStream mInStream = new BufferedInputStream(mConnection.getInputStream());
                    mReader = new BufferedReader(new InputStreamReader(mInStream));
                    mStrBuffer = new StringBuffer();
                    String line = "";

                    //store JSON data into mStrBuffer
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
                Log.i(TAG, mStrBuffer.toString());

                //convert result into Json Object
                try{
                    mJSONMovieData = new JSONObject(result);

                    //check if response is true
                    if(mJSONMovieData.getString("Response").equalsIgnoreCase("True")){

                        //get movie arrays that match search names
                        JSONArray mJSONMovieArray = mJSONMovieData.getJSONArray("Search");
                        int length =mJSONMovieArray.length();

                        //put result into Bundle
                        Bundle BUNDLE_SEARCH_RESULT = new Bundle();
                        BUNDLE_SEARCH_RESULT.putString(SEARCH, result);

                        //start new Intent to show result movies
                        Intent intent = new Intent(SearchResultActivity.this, SearchResultActivity.class);
                        intent.putExtra(SEARCH_BUNDLE, BUNDLE_SEARCH_RESULT);

                        startActivity(intent);
                    }
                    //if response is False, then start ErrorActivity
                    else{
                        Log.i(TAG, "False");
                        //get error message
                        String mErrorMessage = mJSONMovieData.getString("Error");

                        //make toast to display error message
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
}