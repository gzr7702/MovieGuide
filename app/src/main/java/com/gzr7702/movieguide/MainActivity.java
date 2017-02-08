package com.gzr7702.movieguide;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/*
    * Main entry point to the application, holds MovieFragment
 */

public class MainActivity extends AppCompatActivity {
    private boolean mTwoPane;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new DetailFragment())
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
    }

}
