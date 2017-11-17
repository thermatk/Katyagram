
package com.thermatk.android.instaviewer.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;

import lombok.Data;

@Data public class Dimensions implements Parcelable
{

    @Expose
    public Integer height;
    @Expose
    public Integer width;
    public final static Parcelable.Creator<Dimensions> CREATOR = new Creator<Dimensions>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Dimensions createFromParcel(Parcel in) {
            return new Dimensions(in);
        }

        public Dimensions[] newArray(int size) {
            return (new Dimensions[size]);
        }

    }
    ;

    protected Dimensions(Parcel in) {
        this.height = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.width = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(height);
        dest.writeValue(width);
    }

    public int describeContents() {
        return  0;
    }

}
