package com.gzr7702.movieguide.models;

/**
 * Class to hold movie data using Retrofit.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Review implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("author")
    private String author;
    @SerializedName("content")
    private String content;

    public Review(int id, String author, String content) {
        this.id = id;
        this.author = author;
        this.content = content;
    }

    Review(Parcel in) {

        id = in.readInt();
        author = in.readString();
        content = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(author);
        parcel.writeString(content);
    }

    public static final Creator<Review> CREATOR
            = new Creator<Review>() {

        @Override
        public Review createFromParcel(Parcel parcel) {
            return new Review(parcel);
        }

        @Override
        public Review[] newArray(int i) {
            return new Review[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getID() { return this.id; }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
