
package com.thermatk.android.instaviewer.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data public class PageInfo_ implements Parcelable
{

    @SerializedName("has_next_page")
    @Expose
    public Boolean hasNextPage;
    @SerializedName("end_cursor")
    @Expose
    public Object endCursor;
    public final static Parcelable.Creator<PageInfo_> CREATOR = new Creator<PageInfo_>() {


        @SuppressWarnings({
            "unchecked"
        })
        public PageInfo_ createFromParcel(Parcel in) {
            return new PageInfo_(in);
        }

        public PageInfo_[] newArray(int size) {
            return (new PageInfo_[size]);
        }

    }
    ;

    protected PageInfo_(Parcel in) {
        this.hasNextPage = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.endCursor = ((Object) in.readValue((Object.class.getClassLoader())));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(hasNextPage);
        dest.writeValue(endCursor);
    }

    public int describeContents() {
        return  0;
    }

}
