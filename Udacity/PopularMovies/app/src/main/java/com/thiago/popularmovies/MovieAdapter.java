package com.thiago.popularmovies;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.thiago.popularmovies.dto.Movie;

import java.util.List;

/**
 * Created by tmagalhaes on 05-Jan-17.
 */

public class MovieAdapter extends ArrayAdapter<Movie>{

    private static final String URL_LOAD_IMAGE = "http://image.tmdb.org/t/p/w185/";

    public MovieAdapter(Activity context, List<Movie> movies) {
        super(context, 0, movies);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.grid_item_movie, parent, false);
        }

        Movie currentMovie = getItem(position);

        ImageView poster = (ImageView) convertView.findViewById(R.id.grid_item_movie_image);
        if(currentMovie.getPosterPath() == null
                || (currentMovie.getPosterPath() != null && "".equalsIgnoreCase(currentMovie.getPosterPath()))) {
            if(currentMovie.getPosterImage() != null) {
                byte[] imageAsByte = currentMovie.getPosterImage();
                poster.setImageBitmap(BitmapFactory.decodeByteArray(imageAsByte, 0, imageAsByte.length));
            }
        } else {
            Picasso.with(getContext()).load(URL_LOAD_IMAGE + currentMovie.getPosterPath()).fit().into(poster);
        }
        return convertView;
    }
}
