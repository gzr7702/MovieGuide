package com.gzr7702.movieguide;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Fragment that displays page of movie posters
 */
public class MovieFragment extends Fragment implements GetMovieDataTask.AsyncCallback {
    private final String LOG_TAG = MovieFragment.class.getSimpleName();
    static final String SORT_ORDER = "sortOrder";
    private String mLatestSortOrder = null;
    private ImageAdapter mImageAdapter;
    private GridView mGridview;

    private ArrayList<Movie> mMovieList;

    public MovieFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "onCreate");

        if (savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            updateMovieData();
        } else {
            mMovieList = savedInstanceState.getParcelableArrayList("movies");
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", mMovieList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreateView()");

        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);
        mGridview = (GridView) rootView.findViewById(R.id.gridview);

        mGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra("movie", mMovieList.get(position));
                startActivity(intent);
            }
        });

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
        Log.v(LOG_TAG, "onResume() has been called");

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());

        String sortOrder = sharedPref.getString(SettingsActivity.KEY_PREF_SORT_ORDER, "");
        String message1 = "Sort Order " + sortOrder;
        String message2 = "Latest Sort Order " + mLatestSortOrder;
        Log.v(LOG_TAG, message1);
        Log.v(LOG_TAG, message2);

        if (sortOrder != mLatestSortOrder) {
            updateMovieData();
        }
    }

    /*
        * Notify image adapter
     */
    @Override
    public void updateData(Movie[] movies) {
        mMovieList = new ArrayList<Movie>(Arrays.asList(movies));
        int max_movies = mMovieList.size();


        String[] posterPaths = new String[max_movies];
        for(int i = 0; i < max_movies; i++) {
            posterPaths[i] = mMovieList.get(i).getPosterPath();
        }

        Log.v(LOG_TAG, posterPaths.toString());

        mImageAdapter = new ImageAdapter(getActivity(), posterPaths, max_movies);
        mGridview.setAdapter(mImageAdapter);
        // Do we need this if it's being initialized?
        mImageAdapter.notifyDataSetChanged();
        Log.v(LOG_TAG, "updateData()");
    }

    /*
       * Get data from MovieDB
    */
    private void updateMovieData() {
        Log.v(LOG_TAG, "in updateMovieData MovieFrag");
        GetMovieDataTask movieTask = new GetMovieDataTask(this.getContext(), this);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sortOrder = sharedPref.getString(SettingsActivity.KEY_PREF_SORT_ORDER, "");
        mLatestSortOrder = sortOrder;

        if (isOnline()) {
            movieTask.execute(sortOrder);
        } else {
            Toast.makeText(getContext(), "We ain't online!", Toast.LENGTH_LONG).show();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
