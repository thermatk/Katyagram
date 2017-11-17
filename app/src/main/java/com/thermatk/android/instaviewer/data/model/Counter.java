
package com.thermatk.android.instaviewer.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;

import lombok.Data;

@Data public class Counter implements Parcelable
{

    @Expose
    public Integer count;
    public final static Parcelable.Creator<Counter> CREATOR = new Creator<Counter>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Counter createFromParcel(Parcel in) {
            return new Counter(in);
        }

        public Counter[] newArray(int size) {
            return (new Counter[size]);
        }

    }
    ;

    protected Counter(Parcel in) {
        this.count = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(count);
    }

    public int describeContents() {
        return  0;
    }

}
