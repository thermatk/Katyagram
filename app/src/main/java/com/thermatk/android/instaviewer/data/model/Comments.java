
package com.thermatk.android.instaviewer.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;

import lombok.Data;

@Data public class Comments implements Parcelable
{

    @Expose
    public Integer count;
    public final static Parcelable.Creator<Comments> CREATOR = new Creator<Comments>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Comments createFromParcel(Parcel in) {
            return new Comments(in);
        }

        public Comments[] newArray(int size) {
            return (new Comments[size]);
        }

    }
    ;

    protected Comments(Parcel in) {
        this.count = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(count);
    }

    public int describeContents() {
        return  0;
    }

}
