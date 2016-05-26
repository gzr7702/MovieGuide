
package com.gzr7702.movieguide;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetMovieDataTask extends AsyncTask<String, Void, Void> {

    private final String LOG_TAG = GetMovieDataTask.class.getSimpleName();

    @Override
    protected Void doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String movieJSON = null;

        try {
            // Construct the URL
            //"http://api.themoviedb.org/3/movie/popular?api_key=asdfasdf;
            final String BASE_URL = "http://api.themoviedb.org/3/movie/";

            final String QUERY_TYPE = params[0];
            final String APPID_PARAM = "api_key";

            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendPath(QUERY_TYPE)
                    .appendQueryParameter(APPID_PARAM, Info.getKey())
                    .build();

            URL url = new URL(builtUri.toString());

            Log.v(LOG_TAG, url.toString());

            // Create the request to OpenWeatherMap, and open the connection
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

            //implement getDataFromJson ===============================================================
            //jsonStr = buffer.toString();
            //getDataFromJson(jsonStr, locationQuery);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
        //} catch (JSONException e) {
        //    Log.e(LOG_TAG, e.getMessage(), e);
        //    e.printStackTrace();
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