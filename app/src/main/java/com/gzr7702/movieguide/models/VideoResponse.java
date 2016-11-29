package com.gzr7702.movieguide.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


/*
   * Class that sorts the results of the mdb query
 */

public class VideoResponse {
    @SerializedName("id")
    private int id;
    @SerializedName("results")
    private ArrayList<Video> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Video> getResults() {
        return results;
    }

    public void setResults(ArrayList<Video> results) {
        this.results = results;
    }
}
