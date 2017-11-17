package com.thermatk.android.instaviewer.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Owner implements Parcelable
{

    @Expose
    public String id;
    @SerializedName("profile_pic_url")
    @Expose
    public String profilePicUrl;
    @Expose
    public String username;
    public final static Parcelable.Creator<Owner> CREATOR = new Creator<Owner>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Owner createFromParcel(Parcel in) {
            return new Owner(in);
        }

        public Owner[] newArray(int size) {
            return (new Owner[size]);
        }

    }
            ;

    protected Owner(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.profilePicUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.username = ((String) in.readValue((String.class.getClassLoader())));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(profilePicUrl);
        dest.writeValue(username);
    }

    public int describeContents() {
        return 0;
    }

}