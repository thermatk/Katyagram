package com.thermatk.android.instaviewer.data.model;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EdgeMediaToComment implements Parcelable
{

    @Expose
    public Integer count;
    @SerializedName("page_info")
    @Expose
    public PageInfo pageInfo;
    @Expose
    public List<EdgeComments> edges = null;
    public final static Parcelable.Creator<EdgeMediaToComment> CREATOR = new Creator<EdgeMediaToComment>() {


        @SuppressWarnings({
                "unchecked"
        })
        public EdgeMediaToComment createFromParcel(Parcel in) {
            return new EdgeMediaToComment(in);
        }

        public EdgeMediaToComment[] newArray(int size) {
            return (new EdgeMediaToComment[size]);
        }

    }
            ;

    protected EdgeMediaToComment(Parcel in) {
        this.count = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.pageInfo = ((PageInfo) in.readValue((PageInfo.class.getClassLoader())));
        in.readList(this.edges, (EdgeComments.class.getClassLoader()));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(count);
        dest.writeValue(pageInfo);
        dest.writeList(edges);
    }

    public int describeContents() {
        return 0;
    }

}