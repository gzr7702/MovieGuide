
package com.gzr7702.movieguide;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/*
    * Used to fetch data from themoviedb.org's API
    * Implements a call
 */

public class GetMovieDataTask extends AsyncTask<String, Void, Movie[]> {

    private final String LOG_TAG = GetMovieDataTask.class.getSimpleName();
    final int MAX_MOVIES = 20;
    AsyncCallback mAsyncCallback;

    public GetMovieDataTask(AsyncCallback asyncCallback) {
        mAsyncCallback = asyncCallback;
    }

    public interface AsyncCallback {
        void updateData(Movie[] movies);
    }

    @Override
    protected Movie[] doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            // Construct the URL
            final String BASE_URL = "http://api.themoviedb.org/3/movie/";

            // Sort by rating or popularity
            final String sortOrder = params[0];
            Log.v(LOG_TAG, sortOrder);
            final String APPID_PARAM = "api_key";

            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendPath(sortOrder)
                    .appendQueryParameter(APPID_PARAM, Info.getKey())
                    .build();

            URL url = new URL(builtUri.toString());

            // Create the request to movieDB, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                Log.e(LOG_TAG, "input stream was null");
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

            return parseMovieData(jsonStr);
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

    /*
        * This let's the main fragment know the data has been updated
     */
    @Override
    protected void onPostExecute(Movie[] result) {
        super.onPostExecute(result);
        mAsyncCallback.updateData(result);
    }

    /*
      * Parse the JSON data for what we need and stash it in the database
     */
    private Movie[] parseMovieData(String jsonStr)
            throws JSONException {

        Movie[] movies = new Movie[MAX_MOVIES];
        // Movie objects (keys)
        final String RESULTS = "results";

        final String TITLE = "original_title";
        final String POSTER_PATH = "poster_path";
        final String RELEASE_DATE = "release_date";
        final String RATING = "vote_average";
        final String PLOT_SUMMARY = "overview";


        try {
            JSONObject pages = new JSONObject(jsonStr);
            JSONArray jsonMovieArray = pages.getJSONArray(RESULTS);


            for (int i = 0; i < jsonMovieArray.length(); i++) {
                JSONObject movie = jsonMovieArray.getJSONObject(i);

                String title = movie.getString(TITLE);

                String posterPath = movie.getString(POSTER_PATH);
                String releaseDate = movie.getString(RELEASE_DATE);
                Double rating = movie.getDouble(RATING);
                String plot = movie.getString(PLOT_SUMMARY);

                movies[i] = new Movie(title, posterPath, releaseDate, rating, plot);

                Log.v(LOG_TAG, "Retrieved movie data, created Movie object for " + title);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return movies;
    }
}