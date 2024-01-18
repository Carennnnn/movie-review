package com.comp2601.moviereview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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

public class MovieDetailActivity extends AppCompatActivity {

    private static final String APIKEY = "5f628c68";
    private static final String TAG = "Detail";

    private ImageView movie_image_view;
    private TextView title_text_view;
    private TextView rate_text_view;
    private TextView released_text_view;
    private TextView runtime_text_view;
    private TextView plot_text_view;
    private TextView genre_content_text_view;

    private String mMovieString;
    private HttpURLConnection mConnection = null;
    private BufferedReader mReader = null;
    private StringBuffer mStrBuffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        //find views
        movie_image_view = (ImageView) findViewById(R.id.movie_image_view);
        title_text_view = (TextView) findViewById(R.id.title_text_view);
        rate_text_view = (TextView) findViewById(R.id.rate_text_view);
        released_text_view = (TextView) findViewById(R.id.released_text_view);
        runtime_text_view = (TextView) findViewById(R.id.runtime_text_view);
        plot_text_view = (TextView) findViewById(R.id.plot_text_view);
        genre_content_text_view = (TextView) findViewById(R.id.genre_content_text_view);

        //get imdbID from intent
        Intent intent = getIntent();
        String imdbID = intent.getStringExtra(MainActivity.MOVIE_DETAIL_BUNDLE);

        //create URL for the movie with specific imdbID
        String url = "http://www.omdbapi.com/?i=" + imdbID + "&apikey=" + APIKEY;

        //use movie downloader to download JSON data of the movie and display its content
        MovieDataDownload dataDownload = new MovieDataDownload();
        dataDownload.execute(url);
    }

    class MovieDataDownload extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args){
            try {
                //get single movie URL
                URL mMovieUrl = new URL(args[0]);

                try{
                    mConnection = (HttpURLConnection) mMovieUrl.openConnection();
                    InputStream mInStream = new BufferedInputStream(mConnection.getInputStream());
                    mReader = new BufferedReader(new InputStreamReader(mInStream));
                    mStrBuffer = new StringBuffer();
                    String line = "";
                    //use mStrBuffer to store returned JSON data
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

                //convert result into Json Object
                try{
                    mJSONMovieData = new JSONObject(result);

                    //check if response is true
                    if(mJSONMovieData.getString("Response").equalsIgnoreCase("True")){

                        //set movie image
                        String imageURL = mJSONMovieData.getString("Poster");
                        Picasso.with(getApplicationContext()).load(imageURL).into(movie_image_view);

                        //set movie title
                        String title = mJSONMovieData.getString("Title");
                        title_text_view.setText(title);

                        //set rating
                        String rate =  mJSONMovieData.getString("imdbRating");
                        rate_text_view.setText(rate);

                        //set released date
                        String date =  mJSONMovieData.getString("Released");
                        released_text_view.setText(date);

                        //set runtime
                        String runtime =  mJSONMovieData.getString("Runtime");
                        runtime_text_view.setText(runtime);

                        //set plot
                        String plot =  mJSONMovieData.getString("Plot");
                        plot_text_view.setText(plot);

                        //set genre
                        String genre =  mJSONMovieData.getString("Genre");
                        genre_content_text_view.setText(genre);


                    }//if response is False, then start ErrorActivity
                    else{
                        Log.i(TAG, "False");
                        //get error message
                        String mErrorMessage = mJSONMovieData.getString("Error");
                        //make toast for that error message
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