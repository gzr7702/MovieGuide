package com.gzr7702.movieguide;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gzr7702.movieguide.data.MovieContract;
import com.gzr7702.movieguide.data.MovieDbHelper;


public class DetailFragment extends Fragment {

    private String mPosterPath;
    private TextView mTitleView;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle onSavedInstanceState) {
        super.onCreate(onSavedInstanceState);
        Intent intent = getActivity().getIntent();
        mPosterPath = intent.getStringExtra("posterPath");

        MovieDbHelper mMovieDbHelper = new MovieDbHelper(this.getContext());
        SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();

        String[] projection = {
                MovieContract.MovieEntry.COLUMN_TITLE,
                MovieContract.MovieEntry.COLUMN_POSTER_PATH,
                MovieContract.MovieEntry.COLUMN_REALEASE_DATE,
                MovieContract.MovieEntry.COLUMN_RUNNING_TIME,
                MovieContract.MovieEntry.COLUMN_RATING,
                MovieContract.MovieEntry.COLUMN_PLOT_SUMMARY
        };

        String selection = MovieContract.MovieEntry.COLUMN_POSTER_PATH + "=?";
        String[] selectionArgs = {
                mPosterPath
        };


        Cursor c = db.query(
                MovieContract.MovieEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        //TODO: Set up the rest of the views using DB
        mTitleView = (TextView) rootView.findViewById(R.id.detail_title_textview);
        mTitleView.setText("Bond");

        return rootView;

    }
}
