package com.amkrane.youssef.moviesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity implements moviesDataAdapter.onPosterClickHandler,
        MoviesDataUtils.UpdateUiInterface {

    private moviesDataAdapter mAdapter;
    private String mSortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // read shared preferences and load movies depending on user's choice : top rated | popular
        mSortOrder = getString(R.string.popular_movies);
        SharedPreferences sharedPreferences = getSharedPreferences("Movies", MODE_PRIVATE);
        if (sharedPreferences != null) {
            if (sharedPreferences.contains(getString(R.string.movies_list_preference))) {
                mSortOrder = sharedPreferences.getString(getString(R.string.movies_list_preference),
                        getString(R.string.popular_movies));
            }
        }

        mAdapter = new moviesDataAdapter(this);

        RecyclerView mMoviesPostersRV = findViewById(R.id.rv_moviesPosters);

        mMoviesPostersRV.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new GridAutofitLayoutManager(getApplicationContext(), 342);

        mMoviesPostersRV.setLayoutManager(mLayoutManager);

        mMoviesPostersRV.setAdapter(mAdapter);

        //this is used to update the UI from asyncTask in MoviesDataUtils
        MoviesDataUtils.updateUi = this;

        if (!isOnline()) {
            // not online
            findViewById(R.id.tv_notConnected).setVisibility(View.VISIBLE);
            return;
        }
        // shows progress bar and hide's recyclerView
        showProgressBar();
        MoviesDataUtils.getMoviesList(this, mAdapter, mSortOrder);
    }

    private void showProgressBar() {
        findViewById(R.id.rv_moviesPosters).setVisibility(View.INVISIBLE);
        ProgressBar pb = findViewById(R.id.pb_loading);
        pb.setVisibility(View.VISIBLE);
    }

    @Override
    public void showResults() {
        findViewById(R.id.rv_moviesPosters).setVisibility(View.VISIBLE);
        ProgressBar pb = findViewById(R.id.pb_loading);
        pb.setVisibility(View.INVISIBLE);
        findViewById(R.id.tv_notConnected).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPosterClicked(movie mMovie) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(getString(R.string.EXTRA_MOVIE), mMovie); // mMovie is Serializable
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater MenuItems = getMenuInflater();
        MenuItems.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences sharedPreferences = getSharedPreferences("Movies", MODE_PRIVATE);
        if (item.getItemId() == R.id.id_popular) {
            sharedPreferences.edit().putString(getString(R.string.movies_list_preference),
                    getString(R.string.popular_movies)).apply();
            if (!isOnline()) {
                // not online
                findViewById(R.id.rv_moviesPosters).setVisibility(View.INVISIBLE);
                findViewById(R.id.tv_notConnected).setVisibility(View.VISIBLE);
            } else {
                showProgressBar();
                MoviesDataUtils.getMoviesList(getApplicationContext(), mAdapter, getString(R.string.popular_movies));
            }


        } else if (item.getItemId() == R.id.id_topRated) {
            sharedPreferences.edit().putString(getString(R.string.movies_list_preference),
                    getString(R.string.movie_top_rated)).apply();
            if (!isOnline()) {
                // not online
                findViewById(R.id.rv_moviesPosters).setVisibility(View.INVISIBLE);
                findViewById(R.id.tv_notConnected).setVisibility(View.VISIBLE);
            } else {
                showProgressBar();
                MoviesDataUtils.getMoviesList(getApplicationContext(), mAdapter, getString(R.string.movie_top_rated));
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings({"BooleanMethodIsAlwaysInverted", "ConstantConditions"})
    private boolean isOnline() {
        try{
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo() != null &&
                    cm.getActiveNetworkInfo().isConnected();
        }catch (Exception e){
            return false;
        }
    }

    public void OnNotConnectedClick(View view) {
        if (!isOnline()) {
            return;
        }
        // shows progress bar and hide's recyclerView
        showProgressBar();
        MoviesDataUtils.getMoviesList(this, mAdapter, mSortOrder);
    }
}
