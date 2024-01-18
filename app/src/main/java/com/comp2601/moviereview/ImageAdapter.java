package com.comp2601.moviereview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;

public class ImageAdapter extends BaseAdapter {

    private final Context mContext;
    private final Movie[] movies;

    //constructor for ImageAdapter
    public ImageAdapter(Context context, Movie[] movies){
        this.mContext = context;
        this.movies = movies;
    }

    //get image count
    @Override
    public int getCount() {
        return movies.length;
    }

    //get item
    @Override
    public Object getItem(int position) {
        return null;
    }

    //get item id
    @Override
    public long getItemId(int position) {
        return 0;
    }

    //get view
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //inflate the context
        MovieHolder movieHolder;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null){
            movieHolder = new MovieHolder();
            convertView = inflater.inflate(R.layout.single_movie, null);
            //find text and image view
            movieHolder.textView = (TextView) convertView.findViewById(R.id.single_movie_text);
            movieHolder.imageView = (ImageView) convertView.findViewById(R.id.single_movie_image);
            convertView.setTag(movieHolder);
        }else{
            movieHolder = (MovieHolder) convertView.getTag();
        }
        //use Picasso to display image by URL
        Picasso.with(mContext).load(movies[position].getImageURL()).into(movieHolder.imageView);
        //set movie title
        movieHolder.textView.setText(movies[position].getName());
        return convertView;
    }

    //MovieHolder to store movie picture and title view
    static class MovieHolder{
        ImageView imageView;
        TextView textView;
    }
}
