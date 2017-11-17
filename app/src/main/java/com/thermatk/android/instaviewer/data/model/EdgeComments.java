
package com.thermatk.android.instaviewer.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;

public class EdgeComments implements Parcelable
{

    @Expose
    public NodeComments node;
    public final static Parcelable.Creator<EdgeComments> CREATOR = new Creator<EdgeComments>() {


        @SuppressWarnings({
                "unchecked"
        })
        public EdgeComments createFromParcel(Parcel in) {
            return new EdgeComments(in);
        }

        public EdgeComments[] newArray(int size) {
            return (new EdgeComments[size]);
        }

    }
            ;

    protected EdgeComments(Parcel in) {
        this.node = ((NodeComments) in.readValue((NodeComments.class.getClassLoader())));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(node);
    }

    public int describeContents() {
        return 0;
    }

}