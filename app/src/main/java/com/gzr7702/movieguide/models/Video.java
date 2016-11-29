package com.gzr7702.movieguide.models;

/**
 * Class to hold movie data using Retrofit.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Video implements Parcelable {
    @SerializedName("id")
    private String id;
    @SerializedName("key")
    private String key;
    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private String type;

    public Video(String id, String key, String name, String type) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.type = type;
    }

    Video(Parcel in) {

        id = in.readString();
        key = in.readString();
        name = in.readString();
        type = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(key);
        parcel.writeString(name);
        parcel.writeString(type);
    }

    public static final Creator<Video> CREATOR
            = new Creator<Video>() {

        @Override
        public Video createFromParcel(Parcel parcel) {
            return new Video(parcel);
        }

        @Override
        public Video[] newArray(int i) {
            return new Video[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getID() { return this.id; }

    public void setKey(String id) {
        this.key = key;
    }

    public String getKey() { return this.key; }

    public String getName () {
        return name;
    }

    public void setName(String author) {
        this.name = name;
    }

    public String getType(){
        return type;
    }

    public void setType(String content) {
        this.type = type;
    }
}
