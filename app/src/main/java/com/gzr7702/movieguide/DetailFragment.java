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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        MovieDbHelper mMovieDbHelper = new MovieDbHelper(this.getContext());
        SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();

        String[] projection = {
                MovieContract.MovieEntry.COLUMN_TITLE,
                MovieContract.MovieEntry.COLUMN_POSTER_PATH,
                MovieContract.MovieEntry.COLUMN_REALEASE_DATE,
                MovieContract.MovieEntry.COLUMN_RATING,
                MovieContract.MovieEntry.COLUMN_PLOT_SUMMARY
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

        //TODO: Set up the rest of the views using DB
        mTitleView = (TextView) rootView.findViewById(R.id.detail_title_textview);
        mTitleView.setText("ASDFASDFASDf");

        return rootView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent intent = getActivity().getIntent();
        mPosterPath = intent.getStringExtra("posterPath");
    }
}
