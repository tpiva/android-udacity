package com.thiago.popularmovies.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.thiago.popularmovies.MovieAdapter;
import com.thiago.popularmovies.R;
import com.thiago.popularmovies.dto.Movie;
import com.thiago.popularmovies.webservice.FetchMovies;

import java.util.ArrayList;

/**
 * Created by tmagalhaes on 04-Jan-17.
 */

public class MovieGridFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView movieGrid = (GridView) rootView.findViewById(R.id.movies_grid);

        MovieAdapter adapter = new MovieAdapter(getActivity(), new ArrayList<Movie>());
        movieGrid.setAdapter(adapter);

        FetchMovies fetchMovies = new FetchMovies(adapter);
        fetchMovies.execute();

        return rootView;
    }
}
