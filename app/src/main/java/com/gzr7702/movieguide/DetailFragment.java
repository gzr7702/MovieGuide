package com.gzr7702.movieguide;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gzr7702.movieguide.data.MovieContract;
import com.gzr7702.movieguide.data.MovieDbHelper;


public class DetailFragment extends Fragment {
    String LOG_TAG = DetailFragment.class.getSimpleName();

    private String mPosterPath;
    private String mTitle;
    private String mReleaseDate;
    private String mRating;
    private String mPlotSummary;

    private TextView mPosterView;
    private TextView mTitleView;
    private TextView mReleaseDateView;
    private TextView mRatingView;
    private TextView mSummaryView;

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
                MovieContract.MovieEntry.COLUMN_POSTER_PATH,
                MovieContract.MovieEntry.COLUMN_TITLE,
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
                MovieContract.MovieEntry.COLUMN_RATING,
                MovieContract.MovieEntry.COLUMN_PLOT_SUMMARY
        };

        String selection = MovieContract.MovieEntry.COLUMN_POSTER_PATH + "=?";
        String[] selectionArgs = {
                mPosterPath
        };


        Cursor cursor = db.query(
                MovieContract.MovieEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if( cursor != null && cursor.moveToFirst() ){
            mTitle = cursor.getString(cursor.getColumnIndex("title"));
            mPosterPath = cursor.getString(cursor.getColumnIndex("poster_path"));
            mReleaseDate = cursor.getString(cursor.getColumnIndex("release_date"));
            mRating = cursor.getString(cursor.getColumnIndex("rating"));
            mPlotSummary = cursor.getString(cursor.getColumnIndex("plot_summary"));
            cursor.close();
            Log.v(LOG_TAG, mTitle);
            Log.v(LOG_TAG, mPosterPath);
            Log.v(LOG_TAG, mReleaseDate);
            Log.v(LOG_TAG, mRating);
            Log.v(LOG_TAG, mPlotSummary);
        } else {
            Log.v(LOG_TAG, "query failed");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        //TODO: Set up the rest of the views using DB
        mTitleView = (TextView) rootView.findViewById(R.id.detail_title_textview);
        //mPosterView =
        mReleaseDateView = (TextView) rootView.findViewById(R.id.detail_movie_year);
        mRatingView = (TextView) rootView.findViewById(R.id.detail_user_rating);
        mSummaryView = (TextView) rootView.findViewById(R.id.detail_movie_blurb);

        mTitleView.setText(mTitle);
        mReleaseDateView.setText(mReleaseDate);
        mRatingView.setText(mRating);
        mSummaryView.setText(mPlotSummary);

        return rootView;

    }
}
