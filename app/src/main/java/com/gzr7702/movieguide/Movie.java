package com.gzr7702.movieguide;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class to hold movie data. Implements Parcelable so it can be used
 * to exchange data in onSavedInstanceState()
 */

class Movie implements Parcelable {
    private String title;
    private String posterPath;
    private String releaseDate;
    private double rating;
    private String summary;

    Movie(String title, String posterPath, String releaseDate,
          double rating, String summary) {
        this.title = title;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.summary = summary;
    }

    Movie(Parcel in){
        title = in.readString();
        posterPath = in.readString();
        releaseDate = in.readString();
        rating = in.readDouble();
        summary = in.readString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPosterPath() {
        return this.posterPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(posterPath);
        parcel.writeString(releaseDate);
        parcel.writeDouble(rating);
        parcel.writeString(summary);
    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };
}
