package com.thiago.popularmovies.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.thiago.popularmovies.R;
import com.thiago.popularmovies.Utility;
import com.thiago.popularmovies.fragments.MovieGridFragment;

public class MainActivity extends AppCompatActivity {

    private String mSearchOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!Utility.isOnline(this)) {
            Utility.setSearchOrder(this, getString(R.string.pref_movies_favorites));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String currentSearchOrder = Utility.getSortOrder(this);
        if(currentSearchOrder != null && !currentSearchOrder.equalsIgnoreCase(mSearchOrder)) {
            MovieGridFragment fragment = (MovieGridFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_movie_grid);
            if(fragment != null) {
                fragment.changedSearchOrder();
            }
            mSearchOrder = currentSearchOrder;
        }
    }
}
