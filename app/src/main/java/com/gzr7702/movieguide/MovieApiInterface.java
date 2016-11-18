package com.gzr7702.movieguide;

/**
 *  Api interface for the Movie client
 */

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface MovieApiInterface {
    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey);
}
