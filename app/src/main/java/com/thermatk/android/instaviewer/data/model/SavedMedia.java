
package com.thermatk.android.instaviewer.data.model;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data public class SavedMedia implements Parcelable
{

    @Expose
    public List<Object> nodes = null;
    @Expose
    public Integer count;
    @SerializedName("page_info")
    @Expose
    public PageInfo_ pageInfo;
    public final static Parcelable.Creator<SavedMedia> CREATOR = new Creator<SavedMedia>() {


        @SuppressWarnings({
            "unchecked"
        })
        public SavedMedia createFromParcel(Parcel in) {
            return new SavedMedia(in);
        }

        public SavedMedia[] newArray(int size) {
            return (new SavedMedia[size]);
        }

    }
    ;

    protected SavedMedia(Parcel in) {
        in.readList(this.nodes, (java.lang.Object.class.getClassLoader()));
        this.count = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.pageInfo = ((PageInfo_) in.readValue((PageInfo_.class.getClassLoader())));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(nodes);
        dest.writeValue(count);
        dest.writeValue(pageInfo);
    }

    public int describeContents() {
        return  0;
    }

}
