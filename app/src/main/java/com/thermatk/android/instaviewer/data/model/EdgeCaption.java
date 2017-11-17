package com.thermatk.android.instaviewer.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;

public class EdgeCaption implements Parcelable
{

    @Expose
    public CaptionText node;
    public final static Parcelable.Creator<EdgeCaption> CREATOR = new Creator<EdgeCaption>() {


        @SuppressWarnings({
                "unchecked"
        })
        public EdgeCaption createFromParcel(Parcel in) {
            return new EdgeCaption(in);
        }

        public EdgeCaption[] newArray(int size) {
            return (new EdgeCaption[size]);
        }

    }
            ;

    protected EdgeCaption(Parcel in) {
        this.node = ((CaptionText) in.readValue((CaptionText.class.getClassLoader())));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(node);
    }

    public int describeContents() {
        return 0;
    }

}