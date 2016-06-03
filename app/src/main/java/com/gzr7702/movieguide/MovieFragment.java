package com.gzr7702.movieguide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.gzr7702.movieguide.data.MovieContract;
import com.gzr7702.movieguide.data.MovieDbHelper;

import java.net.MalformedURLException;
import java.net.URL;


/**
 * Fragment that displays page of movie posters
 */
public class MovieFragment extends Fragment {

   private final String LOG_TAG = MainActivity.class.getSimpleName();

   public MovieFragment() {
   }

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      // Add this line in order for this fragment to handle menu events.
      setHasOptionsMenu(true);
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      // Handle action bar item clicks here. The action bar will
      // automatically handle clicks on the Home/Up button, so long
      // as you specify a parent activity in AndroidManifest.xml.
      int id = item.getItemId();

      //noinspection SimplifiableIfStatement
      if (id == R.id.action_settings) {
         startActivity(new Intent(getContext(), SettingsActivity.class));
         return true;
      }

      return super.onOptionsItemSelected(item);
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {

      View rootView = inflater.inflate(R.layout.fragment_movie, container, false);
      GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
      gridview.setAdapter(new ImageAdapter(getActivity()));

      gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         public void onItemClick(AdapterView<?> parent, View v,
                                 int position, long id) {
            startActivity(new Intent(getActivity(), DetailActivity.class));
         }
      });

      return rootView;
   }


   @Override
   public void onStart() {
      super.onStart();
      getMovieData();
      createPosterList();
   }

   /*
* Get data from MovieDB
 */
   private void getMovieData() {
      GetMovieDataTask movieTask = new GetMovieDataTask(this.getContext());
      SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
      String sortOrder = prefs.getString(getString(R.string.pref_sort_order_key), getString(R.string.pref_sort_order_default));
      movieTask.execute(sortOrder);
   }

   private URL[] createPosterList() {
       Log.v(LOG_TAG, "Started createPosterList()");
      MovieDbHelper mMovieDbHelper = new MovieDbHelper(getContext());
      SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();
       final int MAX_MOVIES = 20;

      final String BASE_URL = "http://image.tmdb.org/t/p/";
      // Image size: "w92", "w154", "w185", "w342", "w500", "w780", or "original"
      final String imageSize = "w185";
       URL posterUrls[] = new URL[20];

      String[] projection = {
              MovieContract.MovieEntry.COLUMN_POSTER_PATH
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

       // Loop through 20 movies and build an
       // array of urls
      c.moveToFirst();
      for (int i = 0; i < MAX_MOVIES; i++) {
          String posterPath = c.getString(0);
          Uri builtUri = Uri.parse(BASE_URL).buildUpon()
               .appendPath(imageSize)
               .appendPath(posterPath)
               .build();


          try {
              posterUrls[i] = new URL(builtUri.toString());
          } catch (MalformedURLException mue) {
              Log.e(LOG_TAG, "Error ", mue);
          }

          c.moveToNext();
      }

      db.close();

      Log.v(LOG_TAG, posterUrls.toString());

      return posterUrls;
   }
}
