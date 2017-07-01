package com.thiago.bakingapp.activities;

import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.thiago.bakingapp.R;
import com.thiago.bakingapp.adapter.RecipeAdapter;
import com.thiago.bakingapp.bean.Recipe;
import com.thiago.bakingapp.data.FetchRecipes;
import com.thiago.bakingapp.widget.BakingAppWidget;

import java.util.List;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.thiago.bakingapp.utils.Constants.COLUMNS_TABLET;
import static com.thiago.bakingapp.utils.Constants.EXTRA_FIRST_TIME_WIDGET;
import static com.thiago.bakingapp.utils.Constants.EXTRA_RECIPE_SELECTED;
import static com.thiago.bakingapp.utils.Constants.EXTRA_WIDGET_ID;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Recipe>>, RecipeAdapter.RecipeClickListener {

    private static final int BAKING_RECIPE_LOADER_ID = 120;
    private static final String BAKING_RECIPE_RV_STATE = "recipe_state_rv";
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.recipe_list)
    RecyclerView mRecycleViewRecipes;
    @BindBool(R.bool.tablet)
    boolean mIsTablet;

    private ProgressDialog mProgressDialog;
    private RecipeAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private GridLayoutManager mGridLayoutManager;
    private boolean isAlreadyFetching = false;

    private int mWidgetId = -1;
    private boolean mIsFirstLaunchWidget = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null ) {
                mWidgetId = extras.getInt(EXTRA_WIDGET_ID);
                mIsFirstLaunchWidget = extras.getBoolean(EXTRA_FIRST_TIME_WIDGET);
            }
        }

        if (mIsTablet ||
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mGridLayoutManager = new GridLayoutManager(this, COLUMNS_TABLET);
        } else {
            mLinearLayoutManager = new LinearLayoutManager(this);
            mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        }

        mAdapter = new RecipeAdapter(this, this);
        mRecycleViewRecipes.setLayoutManager((mIsTablet ||
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) ?
                mGridLayoutManager : mLinearLayoutManager);
        mRecycleViewRecipes.setAdapter(mAdapter);

        if (savedInstanceState != null) {
            mRecycleViewRecipes.getLayoutManager()
                    .onRestoreInstanceState(savedInstanceState.getParcelable(BAKING_RECIPE_RV_STATE));
            getSupportLoaderManager().restartLoader(BAKING_RECIPE_LOADER_ID, null, this);
        } else {
            getSupportLoaderManager().initLoader(BAKING_RECIPE_LOADER_ID, null, this);
        }
    }

    private void showProgress() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }
        mProgressDialog.setMessage(getString(R.string.loading));
        mProgressDialog.show();
    }

    private void hideProgress() {
        if (mProgressDialog != null
                && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isAlreadyFetching) {
            getSupportLoaderManager().restartLoader(BAKING_RECIPE_LOADER_ID, null, this);
        }
    }

    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<Recipe>>(this) {
            @Override
            protected void onStartLoading() {
                if (!isAlreadyFetching) {
                    showProgress();
                    forceLoad();
                }
            }

            @Override
            public List<Recipe> loadInBackground() {
                return FetchRecipes.getRecipes();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> data) {
        if (data != null
                && !isAlreadyFetching) {
            hideProgress();
            mAdapter.setRecipes(data);
            isAlreadyFetching = true;
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Recipe>> loader) {

    }

    @Override
    public void onItemClicked(Recipe recipe) {
        if (recipe != null) {
            updateWidget(recipe);
            if (!mIsFirstLaunchWidget) {
                // send to activity
                Intent intent = new Intent(this, RecipeDetailsActivity.class);
                intent.putExtra(EXTRA_RECIPE_SELECTED, recipe);
                startActivity(intent);
            } else {
                Toast.makeText(this, getString(R.string.widget_added),Toast.LENGTH_LONG).show();
                mIsFirstLaunchWidget = false;
                mWidgetId = -1;

                // dismiss dialog
                hideProgress();
                MainActivity.this.finish();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BAKING_RECIPE_RV_STATE,
                mRecycleViewRecipes.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mRecycleViewRecipes.getLayoutManager()
                .onRestoreInstanceState(savedInstanceState.getParcelable(BAKING_RECIPE_RV_STATE));
    }

    private void updateWidget(Recipe recipe) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] ids = null;
        if (mWidgetId != -1) {
            ids = new int[1];
            ids[0] = mWidgetId;
        } else {
            ids = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingAppWidget.class));
        }

        Log.d(TAG, "widget id: " + mWidgetId);

        BakingAppWidget.updateAppWidget(this, appWidgetManager, ids, recipe);
    }
}
