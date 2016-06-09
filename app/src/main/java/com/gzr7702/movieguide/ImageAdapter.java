package com.gzr7702.movieguide;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Adapter to sync images with gridview
 */

public class ImageAdapter extends BaseAdapter {
    private final String LOG_TAG = ImageAdapter.class.getSimpleName();

    private Context mContext;
    private int mNumberMovies;
    private String mImageUrlList[];

    public ImageAdapter(Context c, String[] posterPaths, int numberMovies) {
        mContext = c;
        mNumberMovies = numberMovies;
        mImageUrlList = new String[mNumberMovies];

        // Take poster paths and create a list of full URLs
        final String BASE_URL = "http://image.tmdb.org/t/p/";
        // Image size: "w92", "w154", "w185", "w342", "w500", "w780", or "original"
        final String imageSize = "w185";
        for (int i = 0; i < numberMovies; i++) {
            mImageUrlList[i] = BASE_URL + imageSize + posterPaths[i];
        }
    }

    public int getCount() {
        return mImageUrlList.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(540, 850));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }

        //Log.v(LOG_TAG, mImageUrl[position].toString());
        // TODO: cache image when we first get it.
        Picasso.with(mContext)
                .load(mImageUrlList[position].toString())
                .placeholder(R.drawable.placeholder)
                .into(imageView);
        return imageView;
    }

}
