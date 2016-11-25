package com.gzr7702.movieguide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class DetailFragment extends Fragment {
    Movie mMovie;
    String[] mVideoList = {"Here is Video 1", "Here is Video 2", "Here is Video 3", "Here is Video 4"};
    HashMap<String, String> mReviewList = new HashMap<String, String>();

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle onSavedInstanceState) {
        super.onCreate(onSavedInstanceState);
        Intent intent = getActivity().getIntent();
        mMovie = intent.getExtras().getParcelable("movie");
        // Temp reviews ==============================
        mReviewList.put("Joe", "Hated it!");
        mReviewList.put("Chuck", "Loved it!");
        mReviewList.put("Phil", "Unconscionable!");
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
        LinearLayout videoLayout = (LinearLayout) rootView.findViewById(R.id.detail_video_container);
        LinearLayout reviewLayout = (LinearLayout) rootView.findViewById(R.id.detail_review_container);

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

        // TODO make this movie a favorite
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = getActivity().getPreferences(getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(getString(R.string.saved_movie), mMovie.getID());
                editor.commit();
                Toast.makeText(getContext(), new Integer(mMovie.getID()).toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // TODO hook up back end for videos
        for (final String video : mVideoList) {

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

        // TODO hookup backend review list
        Set reviewSet = mReviewList.entrySet();
        Iterator reviewIter = reviewSet.iterator();

        while (reviewIter.hasNext()) {

            final Map.Entry reviewEntry = (Map.Entry) reviewIter.next();

            View reviewContainer = LayoutInflater.from(getActivity()).inflate(
                    R.layout.review_view, null);

            reviewContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), (String) reviewEntry.getValue(), Toast.LENGTH_SHORT).show();
                }
            });

            TextView reviewAuthor = (TextView) reviewContainer.findViewById(R.id.review_textview);
            reviewAuthor.setText((String) reviewEntry.getKey());

            reviewLayout.addView(reviewContainer);
        }

        return rootView;
    }

}
