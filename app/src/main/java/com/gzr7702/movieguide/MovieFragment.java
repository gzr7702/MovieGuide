package com.gzr7702.movieguide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.gzr7702.movieguide.data.MovieContract;
import com.gzr7702.movieguide.data.MovieDbHelper;


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
            updateMovieData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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
        mLatestSortOrder = sharedPref.getString(SettingsActivity.KEY_PREF_SORT_ORDER, "");

        movieTask.execute(mLatestSortOrder);
        updatePosterList();
        
    }

    /*
      * Query the database to get all poster paths, then make a list of urls
     */
    private void updatePosterList() {
        Log.v(LOG_TAG, "Started updatePosterList()");
        MovieDbHelper mMovieDbHelper = new MovieDbHelper(this.getContext());
        SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();

        String[] projection = {
                MovieContract.MovieEntry.COLUMN_POSTER_PATH
        };

        Cursor c = db.query(
                MovieContract.MovieEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        // Loop through 20 movies and build an
        // array of urls
        if (c != null && c.moveToFirst()) {
            c.moveToFirst();
            for (int i = 0; i < MAX_MOVIES; i++) {
                mPosterPaths[i] = c.getString(0);
                c.moveToNext();
            }
        }

        db.close();
    }


    @Override
    public void onResume(){
        super.onResume();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sortOrder = sharedPref.getString(SettingsActivity.KEY_PREF_SORT_ORDER, "");
        if (sortOrder != mLatestSortOrder) {
            updateMovieData();
        }
    }

}
