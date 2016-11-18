package com.gzr7702.movieguide;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * client used to get movie data using Retrofit
 */

public class MovieApiClient {

    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
