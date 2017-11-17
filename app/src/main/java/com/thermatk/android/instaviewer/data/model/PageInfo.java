
package com.thermatk.android.instaviewer.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data public class PageInfo implements Parcelable
{

    @SerializedName("has_next_page")
    @Expose
    public Boolean hasNextPage;
    @SerializedName("end_cursor")
    @Expose
    public String endCursor;
    public final static Parcelable.Creator<PageInfo> CREATOR = new Creator<PageInfo>() {


        @SuppressWarnings({
            "unchecked"
        })
        public PageInfo createFromParcel(Parcel in) {
            return new PageInfo(in);
        }

        public PageInfo[] newArray(int size) {
            return (new PageInfo[size]);
        }

    }
    ;

    protected PageInfo(Parcel in) {
        this.hasNextPage = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.endCursor = ((String) in.readValue((String.class.getClassLoader())));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(hasNextPage);
        dest.writeValue(endCursor);
    }

    public int describeContents() {
        return  0;
    }

}
