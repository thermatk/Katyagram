package com.thermatk.android.instaviewer.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CaptionText implements Parcelable
{

    @Expose
    public String text;
    public final static Parcelable.Creator<CaptionText> CREATOR = new Creator<CaptionText>() {


        @SuppressWarnings({
                "unchecked"
        })
        public CaptionText createFromParcel(Parcel in) {
            return new CaptionText(in);
        }

        public CaptionText[] newArray(int size) {
            return (new CaptionText[size]);
        }

    }
            ;

    protected CaptionText(Parcel in) {
        this.text = ((String) in.readValue((String.class.getClassLoader())));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(text);
    }

    public int describeContents() {
        return 0;
    }

}