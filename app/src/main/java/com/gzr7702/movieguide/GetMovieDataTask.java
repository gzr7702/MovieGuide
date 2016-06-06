
package com.gzr7702.movieguide;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.gzr7702.movieguide.data.MovieContract.MovieEntry;
import com.gzr7702.movieguide.data.MovieDbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetMovieDataTask extends AsyncTask<String, Void, Void> {

    private final String LOG_TAG = GetMovieDataTask.class.getSimpleName();
    public Context mContext;

    // We use a constructor so we can pass in the context for our DB helper.
    public GetMovieDataTask(Context context) {
        mContext = context;
    }
    /*
      * Parse the JSON data for what we need and stash it in the database
     */
    private void getDataFromJson(String jsonStr)
            throws JSONException {

        // Keys for List of results
        final String RESULTS = "results";

        // Movie objects (keys)
        final String POSTER_PATH = "poster_path";
        final String TITLE = "original_title";
        final String RELEASE_DATE = "release_date";
        final String RUN_TIME = "release_date";
        final String RATING = "vote_average";
        final String PLOT_SUMMARY = "overview";

        MovieDbHelper mMovieDbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();

        try {
            JSONObject pages = new JSONObject(jsonStr);
            JSONArray movieArray = pages.getJSONArray(RESULTS);

            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject movie = movieArray.getJSONObject(i);

                String title = movie.getString(TITLE);
                String posterPath = movie.getString(POSTER_PATH);
                String releaseDate = movie.getString(RELEASE_DATE);
                String runTime = movie.getString(RUN_TIME);
                Double rating = movie.getDouble(RATING);
                String plot = movie.getString(PLOT_SUMMARY);

                ContentValues values = new ContentValues();
                values.put(MovieEntry.COLUMN_TITLE, title);
                values.put(MovieEntry.COLUMN_POSTER_PATH, posterPath);
                values.put(MovieEntry.COLUMN_REALEASE_DATE, releaseDate);
                values.put(MovieEntry.COLUMN_RUNNING_TIME, runTime);
                values.put(MovieEntry.COLUMN_RATING, rating);
                values.put(MovieEntry.COLUMN_PLOT_SUMMARY, plot);

                db.insert(MovieEntry.TABLE_NAME, null, values);
            }

            Log.v(LOG_TAG, "Retrieved and stashed movie data.");

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    @Override
    protected Void doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            // Construct the URL
            final String BASE_URL = "http://api.themoviedb.org/3/movie/";

            // Sort by rating or popularity
            final String sortOrder = params[0];
            final String APPID_PARAM = "api_key";

            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendPath(sortOrder)
                    .appendQueryParameter(APPID_PARAM, Info.getKey())
                    .build();

            URL url = new URL(builtUri.toString());

            //Log.v(LOG_TAG, url.toString());

            // Create the request to movieDB, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Add end of line for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }

            String jsonStr = buffer.toString();
            Log.v(LOG_TAG, jsonStr);
            getDataFromJson(jsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        return null;
    }
}