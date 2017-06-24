package com.thiago.bakingapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.thiago.bakingapp.adapter.BakingRecipeAdapter;
import com.thiago.bakingapp.bean.Recipe;
import com.thiago.bakingapp.data.FetchRecipes;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Recipe>>{

    //TODO read again rules of implementation

    private static final int BAKING_RECIPE_LOADER_ID = 120;
    @BindView(R.id.recipe_list)
    private RecyclerView mRecycleViewRecipes;

    private ProgressDialog mProgressDialog;
    private BakingRecipeAdapter mAdpater;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mAdpater = new BakingRecipeAdapter();
        mRecycleViewRecipes.setLayoutManager(mLinearLayoutManager);
        mRecycleViewRecipes.setAdapter(mAdpater);

        getSupportLoaderManager().initLoader(BAKING_RECIPE_LOADER_ID, null, this);
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
            mProgressDialog.hide();
            mProgressDialog = null;
        }
    }

    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<Recipe>>(this) {
            @Override
            protected void onStartLoading() {
                showProgress();
                forceLoad();
            }

            @Override
            public List<Recipe> loadInBackground() {
                return FetchRecipes.getRecipes();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> data) {
        if (data != null) {
            hideProgress();
            mAdpater.setRecipes(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Recipe>> loader) {

    }
}
