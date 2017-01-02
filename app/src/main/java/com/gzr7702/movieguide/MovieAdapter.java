package com.gzr7702.movieguide;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.content.res.Configuration;


import com.gzr7702.movieguide.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Adapter to sync images with gridview for main fragment
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{
    private final String LOG_TAG = MovieAdapter.class.getSimpleName();

    private List<Movie> mMovieList;
    private final OnItemClickListener mListener;
    private int orientation;
    int mRowLayout;
    Context mContext;

    public interface OnItemClickListener{
        void onItemClick(Movie movie);
    }


    public MovieAdapter(List<Movie> movieList, int rowLayout,
                        Context context, OnItemClickListener listener) {
        mMovieList = movieList;
        mRowLayout = rowLayout;
        mContext = context;
        mListener = listener;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        public ImageView movieImage;

        public MovieViewHolder(View view) {
            super(view);
            movieImage = (ImageView) view.findViewById(R.id.movie_image);
        }

        public void bind(final Movie movie, final OnItemClickListener listener){
            final String BASE_URL = "http://image.tmdb.org/t/p/";
            final String imageSize;

            orientation = mContext.getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageSize = "w780";
            } else {
                imageSize = "w500";
            }

            String posterPath = BASE_URL + imageSize + movie.getPosterPath();

            Log.v(LOG_TAG, posterPath);
            Picasso.with(mContext)
                    .load(posterPath)
                    .into(this.movieImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(movie);
                }
            });
        }
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_cell, parent, false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = mMovieList.get(position);

        holder.bind(movie, mListener);
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

}
