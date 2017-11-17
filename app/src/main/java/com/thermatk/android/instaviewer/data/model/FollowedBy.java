
package com.thermatk.android.instaviewer.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;

import lombok.Data;

@Data public class FollowedBy implements Parcelable
{

    @Expose
    public Integer count;
    public final static Parcelable.Creator<FollowedBy> CREATOR = new Creator<FollowedBy>() {


        @SuppressWarnings({
            "unchecked"
        })
        public FollowedBy createFromParcel(Parcel in) {
            return new FollowedBy(in);
        }

        public FollowedBy[] newArray(int size) {
            return (new FollowedBy[size]);
        }

    }
    ;

    protected FollowedBy(Parcel in) {
        this.count = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(count);
    }

    public int describeContents() {
        return  0;
    }

}
