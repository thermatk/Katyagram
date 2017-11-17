
package com.thermatk.android.instaviewer.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;

import lombok.Data;

@Data public class Owner implements Parcelable
{

    @Expose
    public String id;
    public final static Parcelable.Creator<Owner> CREATOR = new Creator<Owner>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Owner createFromParcel(Parcel in) {
            return new Owner(in);
        }

        public Owner[] newArray(int size) {
            return (new Owner[size]);
        }

    }
    ;

    protected Owner(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
    }

    public int describeContents() {
        return  0;
    }

}
