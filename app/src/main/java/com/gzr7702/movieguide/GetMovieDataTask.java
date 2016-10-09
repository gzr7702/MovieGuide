
package com.gzr7702.movieguide;

import android.content.Context;
import android.database.Cursor;
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
import java.util.HashMap;
import java.util.StringTokenizer;

public class GetMovieDataTask extends AsyncTask<String, Void, HashMap> {

    private final String LOG_TAG = GetMovieDataTask.class.getSimpleName();
    public Context mContext;
    private HashMap<String, String> mPosterData = new HashMap<String, String>();
    final int MAX_MOVIES = 20;
    AsyncCallback mAsyncCallback;

    // We use a constructor so we can pass in the context for our DB helper.
    public GetMovieDataTask(Context context, AsyncCallback asyncCallback) {
        mContext = context;
        mAsyncCallback = asyncCallback;
    }

    public interface AsyncCallback {
        void updateData(HashMap<String, String> posterData);
    }

    @Override
    protected HashMap doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        final String dataType = params[1];

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

            if (dataType.equals("posterData")) {
                getPosterData(jsonStr);
            } else {
                return getMovieData(jsonStr);
            }
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

    @Override
    protected void onPostExecute(HashMap result) {
        super.onPostExecute(result);
        Log.v(LOG_TAG, "onPostExectute()");
        mAsyncCallback.updateData(mPosterData);
    }

    /*
      * Parse the JSON data for what we need and stash it in the database
     */
    // TODO: refactor to get only one movies info for detail frag
    private HashMap getMovieData(String jsonStr)
            throws JSONException {

        // Movie objects (keys)
        HashMap movieData = new HashMap<String, String>();

        final String RESULTS = "results";

        final String TITLE = "original_title";
        final String POSTER_PATH = "poster_path";
        final String RELEASE_DATE = "release_date";
        final String RATING = "vote_average";
        final String PLOT_SUMMARY = "overview";


        try {
            JSONObject pages = new JSONObject(jsonStr);
            JSONArray movieArray = pages.getJSONArray(RESULTS);

            /*
            JSONObject movie = movieArray.getJSONObject(i);

            movieData.put(TITLE, movie.getString(TITLE));
            movieData.put(POSTER_PATH, movie.getString(POSTER_PATH));
            movieData.put(RELEASE_DATE, movie.getString(RELEASE_DATE));
            movieData.put(RATING, movie.getString(RATING));
            movieData.put(PLOT_SUMMARY, movie.getString(PLOT_SUMMARY));

            Log.v(LOG_TAG, "Retrieved and stashed movie data.");
            */

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return movieData;
    }

    /*
  * Query the database to get all poster paths, then make a list of urls
 */
    //TODO: refactor to grab only poster paths
    private void getPosterData(String jsonStr)
        throws JSONException {

            // Keys for List of results
            final String RESULTS = "results";

            // Movie objects (keys)
            final String POSTER_PATH = "poster_path";
            final String TITLE = "title";


            try {
                JSONObject pages = new JSONObject(jsonStr);
                JSONArray movieArray = pages.getJSONArray(RESULTS);

                for (int i = 0; i < movieArray.length(); i++) {
                    JSONObject movie = movieArray.getJSONObject(i);

                    String title = movie.getString(TITLE);
                    String posterPath = movie.getString(POSTER_PATH);
                    mPosterData.put(title, posterPath);
                    Log.v(LOG_TAG, title + posterPath);
                }

            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
        }
}