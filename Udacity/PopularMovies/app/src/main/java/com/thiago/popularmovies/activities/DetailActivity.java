package com.thiago.popularmovies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.thiago.popularmovies.R;
import com.thiago.popularmovies.fragments.MovieDetailFragment;
import com.thiago.popularmovies.fragments.MovieGridFragment;

/**
 * Created by tmagalhaes on 06-Jan-17.
 */

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if(savedInstanceState == null) {
            Intent intent = getIntent();

            if(intent != null && intent.hasExtra(MovieGridFragment.DETAIL_MOVIE)) {
                Bundle arguments = new Bundle();
                arguments.putParcelable(MovieGridFragment.DETAIL_MOVIE, intent.getParcelableExtra(MovieGridFragment.DETAIL_MOVIE));

                MovieDetailFragment fragment = new MovieDetailFragment();
                fragment.setArguments(arguments);

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.movie_detail_container, fragment)
                        .commit();
            }
        }
    }
}
