package com.thermatk.android.instaviewer.data.model;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;

public class EdgeMediaToCaption implements Parcelable
{

    @Expose
    public List<EdgeCaption> edges = null;
    public final static Parcelable.Creator<EdgeMediaToCaption> CREATOR = new Creator<EdgeMediaToCaption>() {


        @SuppressWarnings({
                "unchecked"
        })
        public EdgeMediaToCaption createFromParcel(Parcel in) {
            return new EdgeMediaToCaption(in);
        }

        public EdgeMediaToCaption[] newArray(int size) {
            return (new EdgeMediaToCaption[size]);
        }

    }
            ;

    protected EdgeMediaToCaption(Parcel in) {
        in.readList(this.edges, (EdgeCaption.class.getClassLoader()));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(edges);
    }

    public int describeContents() {
        return 0;
    }

}