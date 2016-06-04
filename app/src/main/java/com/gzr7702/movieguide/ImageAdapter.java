package com.gzr7702.movieguide;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.gzr7702.movieguide.data.MovieContract;
import com.gzr7702.movieguide.data.MovieDbHelper;
import com.squareup.picasso.Picasso;

/**
 * Adapter to sync images with gridview
 */

public class ImageAdapter extends BaseAdapter {
    private final String LOG_TAG = ImageAdapter.class.getSimpleName();

    private Context mContext;
    private String mImageUrl[] = new String[20];

    public ImageAdapter(Context c) {
        mContext = c;
        createPosterList();
    }

    public int getCount() {
        //return mImageUrl.length;
        return tempImageUrl.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    private void createPosterList() {
        Log.v(LOG_TAG, "Started createPosterList()");
        MovieDbHelper mMovieDbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();
        final int MAX_MOVIES = 20;

        final String BASE_URL = "http://image.tmdb.org/t/p/";
        // Image size: "w92", "w154", "w185", "w342", "w500", "w780", or "original"
        final String imageSize = "w185";

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
            mImageUrl[i] = BASE_URL + imageSize + posterPath;

            c.moveToNext();
        }

        db.close();
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(500, 800));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }

        Log.v(LOG_TAG, mImageUrl[position].toString());
        Picasso.with(mContext)
                .load(mImageUrl[position].toString())
                .into(imageView);
        return imageView;
    }

    // references to our images
    // Random internet images for now
    private String[] tempImageUrl = {
            "https://i.ytimg.com/vi/tntOCGkgt98/maxresdefault.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/1/1e/Large_Siamese_cat_tosses_a_mouse.jpg",
            "http://cdn.grumpycats.com/wp-content/uploads/2016/02/12654647_974282002607537_7798179861389974677_n-758x758.jpg",
            "https://pbs.twimg.com/profile_images/378800000532546226/dbe5f0727b69487016ffd67a6689e75a.jpeg"
    };
}
