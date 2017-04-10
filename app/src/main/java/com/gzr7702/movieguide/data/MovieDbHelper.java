package com.gzr7702.movieguide.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.gzr7702.movieguide.data.MovieContract.MovieEntry;

/**
 * Manages a local database for movie data.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    private String LOG_TAG = MovieDbHelper.class.getSimpleName();
    private static final int DATABASE_VERSION = 5;

    static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                // We use the movie id instead of an auto id
                MovieEntry.COLUMN_ID + " INTEGER PRIMARY KEY, " +

                MovieEntry.COLUMN_TITLE + " REAL NOT NULL, " +
                MovieEntry.COLUMN_POSTER_PATH + " REAL NOT NULL, " +
                MovieEntry.COLUMN_RELEASE_DATE+ " REAL NOT NULL, " +
                MovieEntry.COLUMN_PLOT_SUMMARY + " REAL NOT NULL, " +
                MovieEntry.COLUMN_RATING + " REAL NOT NULL);";

        Log.v(LOG_TAG, SQL_CREATE_MOVIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
