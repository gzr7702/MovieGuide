package com.gzr7702.movieguide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gzr7702.movieguide.models.Movie;
import com.gzr7702.movieguide.models.Review;
import com.gzr7702.movieguide.models.ReviewResponse;
import com.gzr7702.movieguide.models.Video;
import com.gzr7702.movieguide.models.VideoResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailFragment extends Fragment {
    private final String LOG_TAG = DetailFragment.class.getSimpleName();
    Movie mMovie;
    private final String API_KEY = Info.getKey();
    ArrayList<Video> mVideoList = new ArrayList<>();
    ArrayList<Review> mReviewList = new ArrayList<>();

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle onSavedInstanceState) {
        super.onCreate(onSavedInstanceState);
        Intent intent = getActivity().getIntent();
        mMovie = intent.getExtras().getParcelable("movie");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        TextView titleView = (TextView) rootView.findViewById(R.id.detail_title_textview);
        ImageView posterView = (ImageView) rootView.findViewById(R.id.detail_movie_poster);
        TextView releaseDateView = (TextView) rootView.findViewById(R.id.detail_movie_year);
        TextView ratingView = (TextView) rootView.findViewById(R.id.detail_user_rating);
        TextView summaryView = (TextView) rootView.findViewById(R.id.detail_movie_blurb);
        final Button favoriteButton = (Button) rootView.findViewById(R.id.detail_rating_button);
        final LinearLayout videoLayout = (LinearLayout) rootView.findViewById(R.id.detail_video_container);
        final LinearLayout reviewLayout = (LinearLayout) rootView.findViewById(R.id.detail_review_container);

        titleView.setText(mMovie.getTitle());
        releaseDateView.setText(mMovie.getReleaseDate());
        Double rating = mMovie.getVoteAverage();
        ratingView.setText(rating.toString() + "/10");
        summaryView.setText(mMovie.getOverview());

        // We're going to get the pic from the internet
        String BASE_URL = "http://image.tmdb.org/t/p/";
        String imageSize = "w342";
        String mImageUrl = BASE_URL + imageSize + mMovie.getPosterPath();

        posterView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        posterView.setPadding(0, 0, 0, 0);
        Picasso.with(this.getContext())
                .load(mImageUrl)
                .placeholder(R.drawable.placeholder)
                .into(posterView);

        // Callback used to save favorite movie IDs
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newId = new Integer(mMovie.getID()).toString();

                // Get saved set of movie ids
                SharedPreferences sharedPref = getActivity().getPreferences(getContext().MODE_PRIVATE);
                Set<String> movieIds = sharedPref.getStringSet(getString(R.string.saved_movie), new HashSet<String>());
                Log.v(LOG_TAG, "ids: " + movieIds.toString());

                // Add new Id to the set and save
                movieIds.add(newId);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putStringSet(getString(R.string.saved_movie), movieIds);
                editor.commit();

                Toast.makeText(getContext(), "Saved " + mMovie.getTitle() + " as favorite", Toast.LENGTH_SHORT).show();
            }
        });

        // TODO hook up back end for videos
        // ===============================================
        Call<VideoResponse> videoCall;

        if (isOnline()) {
            MovieApiInterface apiService = MovieApiClient.getClient().create(MovieApiInterface.class);
            String movieId = Integer.toString(mMovie.getID());
            videoCall = apiService.getVideo(movieId, API_KEY);

            videoCall.enqueue(new Callback<VideoResponse>() {
                @Override
                public void onResponse(Call<VideoResponse> videoCall, Response<VideoResponse> response) {
                    int status = response.code();
                    // TODO: not able to fetch reviews, start debugging here
                    if (status == 200) {
                        mVideoList = response.body().getResults();

                        // ===============================================
                        for (final Video video : mVideoList) {

                            View videoContainer = LayoutInflater.from(getActivity()).inflate(
                                    R.layout.video_view, null);

                            videoContainer.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(getContext(), "Playing a video", Toast.LENGTH_SHORT).show();
                                }
                            });

                            TextView videoTitle = (TextView) videoContainer.findViewById(R.id.video_title);
                            videoTitle.setText(video.toString());

                            videoLayout.addView(videoContainer);
                        }
                        // ========================================================
                    } else {
                        String errorMessadge = "We're having trouble with the Cyber, please check your connection";
                        Toast.makeText(getContext(), errorMessadge, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<VideoResponse> videoCall, Throwable t) {
                    Log.e(LOG_TAG, t.toString());
                }
            });
        } else {
            String message = "Sorry, the internet is unreachable. " + "" +
                    "Please check you're connection and try again!";
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
        // ========================================================

        // TODO create separate activity or dialog
        Call<ReviewResponse> reviewCall;

        if (isOnline()) {
            MovieApiInterface apiService = MovieApiClient.getClient().create(MovieApiInterface.class);
            String movieId = new Integer(mMovie.getID()).toString();
            reviewCall = apiService.getReview(movieId, API_KEY);

            reviewCall.enqueue(new Callback<ReviewResponse>() {
                @Override
                public void onResponse(Call<ReviewResponse> reviewCall, Response<ReviewResponse> response) {
                    int status = response.code();
                    // TODO: not able to fetch reviews, start debugging here
                    if (status == 200) {
                        mReviewList = response.body().getResults();

                        // create review list
                        for (final Review review: mReviewList) {

                            View reviewContainer = LayoutInflater.from(getActivity()).inflate(
                                    R.layout.review_view, null);

                            reviewContainer.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(getContext(), review.getContent(), Toast.LENGTH_SHORT).show();
                                }
                            });

                            TextView reviewAuthor = (TextView) reviewContainer.findViewById(R.id.review_textview);
                            reviewAuthor.setText(review.getAuthor());

                            reviewLayout.addView(reviewContainer);
                        }

                    } else {
                        String errorMessadge = "We're having trouble with the Cyber, please check your connection";
                        Toast.makeText(getContext(), errorMessadge, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ReviewResponse> reviewCall, Throwable t) {
                    Log.e(LOG_TAG, t.toString());
                }
            });
        } else {
            String message = "Sorry, the internet is unreachable. " + "" +
                    "Please check you're connection and try again!";
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }

        return rootView;
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

}
