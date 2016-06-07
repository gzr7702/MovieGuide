
package com.gzr7702.movieguide.data;

import android.provider.BaseColumns;


/**
 * Defines table and column names for the movie database.
 */
public class MovieContract {

     //   Inner class that defines the contents of the movie table
    public static final class MovieEntry implements BaseColumns {

         public static final String TABLE_NAME = "movies";
         public static final String COLUMN_TITLE = "title";
         public static final String COLUMN_POSTER_PATH = "poster_path";
         public static final String COLUMN_RELEASE_DATE = "release_date";
         public static final String COLUMN_PLOT_SUMMARY = "plot_summary";
         public static final String COLUMN_RATING = "rating";
    }
}
