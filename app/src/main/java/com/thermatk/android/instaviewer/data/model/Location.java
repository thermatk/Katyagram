package com.thermatk.android.instaviewer.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location implements Parcelable
{

    @Expose
    public String id;
    @SerializedName("has_public_page")
    @Expose
    public Boolean hasPublicPage;
    @Expose
    public String name;
    @Expose
    public String slug;
    public final static Parcelable.Creator<Location> CREATOR = new Creator<Location>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        public Location[] newArray(int size) {
            return (new Location[size]);
        }

    }
            ;

    protected Location(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.hasPublicPage = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.slug = ((String) in.readValue((String.class.getClassLoader())));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(hasPublicPage);
        dest.writeValue(name);
        dest.writeValue(slug);
    }

    public int describeContents() {
        return 0;
    }

}