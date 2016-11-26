package com.gzr7702.movieguide.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


/*
   * Class that sorts the results of the mdb query
 */

public class ReviewResponse {
    @SerializedName("id")
    private int id;
    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private ArrayList<Review> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }


    public ArrayList<Review> getResults() {
        return results;
    }

    public void setResults(ArrayList<Review> results) {
        this.results = results;
    }
}
