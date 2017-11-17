
package com.thermatk.android.instaviewer.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;

public class EdgeLikers implements Parcelable
{

    @Expose
    public Owner node;
    public final static Parcelable.Creator<EdgeLikers> CREATOR = new Creator<EdgeLikers>() {


        @SuppressWarnings({
                "unchecked"
        })
        public EdgeLikers createFromParcel(Parcel in) {
            return new EdgeLikers(in);
        }

        public EdgeLikers[] newArray(int size) {
            return (new EdgeLikers[size]);
        }

    }
            ;

    protected EdgeLikers(Parcel in) {
        this.node = ((Owner) in.readValue((Owner.class.getClassLoader())));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(node);
    }

    public int describeContents() {
        return 0;
    }

}