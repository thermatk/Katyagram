package com.thermatk.android.instaviewer.data.model;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;

public class EdgeMediaPreviewLike implements Parcelable
{

    @Expose
    public Integer count;
    @Expose
    public List<EdgeLikers> edges = null;
    public final static Parcelable.Creator<EdgeMediaPreviewLike> CREATOR = new Creator<EdgeMediaPreviewLike>() {


        @SuppressWarnings({
                "unchecked"
        })
        public EdgeMediaPreviewLike createFromParcel(Parcel in) {
            return new EdgeMediaPreviewLike(in);
        }

        public EdgeMediaPreviewLike[] newArray(int size) {
            return (new EdgeMediaPreviewLike[size]);
        }

    }
            ;

    protected EdgeMediaPreviewLike(Parcel in) {
        this.count = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.edges, (EdgeLikers.class.getClassLoader()));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(count);
        dest.writeList(edges);
    }

    public int describeContents() {
        return 0;
    }

}