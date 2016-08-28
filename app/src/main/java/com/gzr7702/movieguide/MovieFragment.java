package com.gzr7702.movieguide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.util.TimeUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.concurrent.TimeUnit;

/**
 * Fragment that displays page of movie posters
 */
public class MovieFragment extends Fragment {
    private final String LOG_TAG = MovieFragment.class.getSimpleName();
    static final String SORT_ORDER = "sortOrder";
    final int MAX_MOVIES = 20;
    private String[] mPosterPaths = new String[MAX_MOVIES];
    private String mLatestSortOrder = null;

    public MovieFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "onCreate");
        setHasOptionsMenu(true);
        if (savedInstanceState == null) {
            updateMovieData();
        }
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreateView()");

        final ImageAdapter mImageAdapter = new ImageAdapter(getActivity(), mPosterPaths, MAX_MOVIES);
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);
        GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
        gridview.setAdapter(mImageAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra("posterPath", mPosterPaths[position]);
                startActivity(intent);
            }
        });

        return rootView;
    }

    /*
       * Get data from MovieDB
    */
    private void updateMovieData() {
        Log.v(LOG_TAG, "in updateMovieData MovieFrag");
        GetMovieDataTask movieTask = new GetMovieDataTask(this.getContext());

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sortOrder = sharedPref.getString(SettingsActivity.KEY_PREF_SORT_ORDER, "");
        mLatestSortOrder = sortOrder;

        movieTask.execute(sortOrder);
        // Workaround for race condition ========================
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        // ======================================================
        mPosterPaths = movieTask.GetPosterPaths();
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

}
