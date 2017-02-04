package com.gzr7702.movieguide;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gzr7702.movieguide.models.Movie;
import com.gzr7702.movieguide.models.MoviesResponse;

import retrofit2.Call;
import retrofit2.Response;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Fragment that displays page of movie posters, nested within MainActivity
 */

public class MovieFragment extends Fragment {
    private final String LOG_TAG = MovieFragment.class.getSimpleName();
    private final String API_KEY = Info.getKey();
    private String mLatestSortOrder = null;
    private RecyclerView mRecyclerView;
    private ArrayList<Movie> mMovieList;

    private MovieAdapter mAdapter;

    public MovieFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("movies", mMovieList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(layoutManager);

        if (savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            updateMovieData();
        } else {
            mMovieList = savedInstanceState.getParcelableArrayList("movies");
        }


        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        super.onResume();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());

        String sortOrder = sharedPref.getString(SettingsActivity.KEY_PREF_SORT_ORDER, "popular");

        if (sortOrder != mLatestSortOrder) {
            updateMovieData();
        }
    }

    /*
       * Get data from MovieDB
    */
    private void updateMovieData() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        // TODO: check preferences id
        String sortOrder = sharedPref.getString(SettingsActivity.KEY_PREF_SORT_ORDER, "");
        mLatestSortOrder = sortOrder;
        Call<MoviesResponse> call;

        if (isOnline()) {
            MovieApiInterface apiService = MovieApiClient.getClient().create(MovieApiInterface.class);
            String moviesJSON  = sharedPref.getString(getString(R.string.saved_movies), "");
            Log.v(LOG_TAG, "saved movies: " + moviesJSON.toString());

            // TODO: if favorites works only after app is started, not on startup
            if (sortOrder.contentEquals("favorites") && moviesJSON != null) {
                Log.v(LOG_TAG, "favorites? " + sortOrder);

                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<Movie>>(){}.getType();
                mMovieList = gson.fromJson(moviesJSON, type);
                Log.v(LOG_TAG, mMovieList.toString());

                mAdapter = new MovieAdapter(mMovieList, R.layout.movie_cell, getContext(),
                        new MovieAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Movie movie) {
                                Intent intent = new Intent(getActivity(), DetailActivity.class)
                                        .putExtra("movie", movie);
                                startActivity(intent);
                            }
                        });
                mRecyclerView.setAdapter(mAdapter);

            } else if (sortOrder.equals("top_rated")) {
                Log.v(LOG_TAG, "top rated? " + sortOrder);
                call = apiService.getTopRatedMovies(API_KEY);
                call.enqueue(new MovieCallback());
            } else{
                // Sort order is popular
                Log.v(LOG_TAG, "popular? " + sortOrder);
                call = apiService.getPopularMovies(API_KEY);
                call.enqueue(new MovieCallback());
            }

        } else {
            String message = "Sorry, the internet is unreachable. " + "" +
                    "Please check you're connection and try again!";
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    /*
        * Callback used by retrofit for Movies
        * Takes a boolean to indicate if we should assign the entire list
        * or just one element
     */

    private class MovieCallback implements retrofit2.Callback<MoviesResponse> {

        @Override
        public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
            int status = response.code();
            if (status == 200) {
                mMovieList = response.body().getResults();
                mAdapter = new MovieAdapter(mMovieList, R.layout.movie_cell, getContext(),
                        new MovieAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Movie movie) {
                                Intent intent = new Intent(getActivity(), DetailActivity.class)
                                        .putExtra("movie", movie);
                                startActivity(intent);
                            }
                        });
                mRecyclerView.setAdapter(mAdapter);
            } else {
                String errorMessadge = "We couldn't reach the interwebs, please check your connection";
                Toast.makeText(getContext(), errorMessadge, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onFailure(Call<MoviesResponse> call, Throwable t) {
            Log.e(LOG_TAG, t.toString());
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
