package com.gzr7702.movieguide;

/**
 *  Api interface for the Movie client
 */

import com.gzr7702.movieguide.models.MoviesResponse;
import com.gzr7702.movieguide.models.ReviewResponse;
import com.gzr7702.movieguide.models.VideoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface MovieApiInterface {
    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<MoviesResponse> getMovie(@Path("id") String id, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<ReviewResponse> getReview(@Path("id") String id, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<VideoResponse> getVideo(@Path("id") String id, @Query("api_key") String apiKey);
}
