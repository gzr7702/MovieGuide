package com.gzr7702.movieguide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;


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
}
