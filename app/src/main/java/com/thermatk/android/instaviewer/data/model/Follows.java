
package com.thermatk.android.instaviewer.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data public class Follows implements Parcelable
{

    @Expose
    public Integer count;
    public final static Parcelable.Creator<Follows> CREATOR = new Creator<Follows>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Follows createFromParcel(Parcel in) {
            return new Follows(in);
        }

        public Follows[] newArray(int size) {
            return (new Follows[size]);
        }

    }
    ;

    protected Follows(Parcel in) {
        this.count = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(count);
    }

    public int describeContents() {
        return  0;
    }

}
