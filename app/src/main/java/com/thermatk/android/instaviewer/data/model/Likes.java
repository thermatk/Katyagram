
package com.thermatk.android.instaviewer.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;

import lombok.Data;

@Data public class Likes implements Parcelable
{

    @Expose
    public Integer count;
    public final static Parcelable.Creator<Likes> CREATOR = new Creator<Likes>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Likes createFromParcel(Parcel in) {
            return new Likes(in);
        }

        public Likes[] newArray(int size) {
            return (new Likes[size]);
        }

    }
    ;

    protected Likes(Parcel in) {
        this.count = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(count);
    }

    public int describeContents() {
        return  0;
    }

}
