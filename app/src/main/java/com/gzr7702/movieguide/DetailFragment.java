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
import android.widget.ImageView;
import android.widget.TextView;

import com.gzr7702.movieguide.data.MovieContract;
import com.gzr7702.movieguide.data.MovieDbHelper;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DetailFragment extends Fragment {
    String LOG_TAG = DetailFragment.class.getSimpleName();

    private String mPosterPath;
    private String mTitle;
    private String mReleaseDate;
    private String mRating;
    private String mPlotSummary;

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

            // Reformat date from yyyy-mm-dd to year only
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String rawDate = cursor.getString(cursor.getColumnIndex("release_date"));
            Date date = new Date();
            try{
                date = format.parse(rawDate);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int releaseYear = calendar.get(Calendar.YEAR);
            mReleaseDate = String.valueOf(releaseYear);

            String rating = cursor.getString(cursor.getColumnIndex("rating"));
            mRating = rating + "/10";
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

        TextView titleView = (TextView) rootView.findViewById(R.id.detail_title_textview);
        ImageView posterView = (ImageView) rootView.findViewById(R.id.detail_movie_poster);
        TextView releaseDateView = (TextView) rootView.findViewById(R.id.detail_movie_year);
        TextView ratingView = (TextView) rootView.findViewById(R.id.detail_user_rating);
        TextView summaryView = (TextView) rootView.findViewById(R.id.detail_movie_blurb);

        titleView.setText(mTitle);
        releaseDateView.setText(mReleaseDate);
        ratingView.setText(mRating);
        summaryView.setText(mPlotSummary);

        // We're going to get the pic from the internet
        String BASE_URL = "http://image.tmdb.org/t/p/";
        String imageSize = "w342";
        String mImageUrl = BASE_URL + imageSize + mPosterPath;

        posterView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        posterView.setPadding(0, 0, 0, 0);
        Picasso.with(this.getContext())
                .load(mImageUrl)
                .placeholder(R.drawable.placeholder)
                .into(posterView);

        return rootView;
    }

}
