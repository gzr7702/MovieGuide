
package com.gzr7702.movieguide.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;


/**
 * Defines table and column names for the movie database.
 */
public class MovieContract {

    // For content provider URIs
    public static final String CONTENT_AUTHORITY = "com.gzr7702.movieguide.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";

     //   Inner class that defines the contents of the movie table
    public static final class MovieEntry implements BaseColumns {


         public static final Uri CONTENT_URI =
                 BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

         public static final String CONTENT_TYPE =
                 ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

         // Our columns
         public static final String TABLE_NAME = "movies";
         public static final String COLUMN_TITLE = "title";
         public static final String COLUMN_POSTER_PATH = "poster_path";
         public static final String COLUMN_RELEASE_DATE = "release_date";
         public static final String COLUMN_PLOT_SUMMARY = "plot_summary";
         public static final String COLUMN_RATING = "rating";

         // Movie Uri
         public static Uri buildMovieUri(long id) {
             return ContentUris.withAppendedId(CONTENT_URI, id);
         }
    }
}
