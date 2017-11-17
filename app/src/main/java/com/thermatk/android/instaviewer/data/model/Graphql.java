package com.thermatk.android.instaviewer.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Graphql implements Parcelable
{

    @SerializedName("shortcode_media")
    @Expose
    public ShortcodeMedia shortcodeMedia;
    public final static Parcelable.Creator<Graphql> CREATOR = new Creator<Graphql>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Graphql createFromParcel(Parcel in) {
            return new Graphql(in);
        }

        public Graphql[] newArray(int size) {
            return (new Graphql[size]);
        }

    }
            ;

    protected Graphql(Parcel in) {
        this.shortcodeMedia = ((ShortcodeMedia) in.readValue((ShortcodeMedia.class.getClassLoader())));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(shortcodeMedia);
    }

    public int describeContents() {
        return 0;
    }

}