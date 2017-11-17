
package com.thermatk.android.instaviewer.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data public class ThumbnailResource implements Parcelable
{

    @Expose
    public String src;
    @SerializedName("config_width")
    @Expose
    public Integer configWidth;
    @SerializedName("config_height")
    @Expose
    public Integer configHeight;
    public final static Parcelable.Creator<ThumbnailResource> CREATOR = new Creator<ThumbnailResource>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ThumbnailResource createFromParcel(Parcel in) {
            return new ThumbnailResource(in);
        }

        public ThumbnailResource[] newArray(int size) {
            return (new ThumbnailResource[size]);
        }

    }
    ;

    protected ThumbnailResource(Parcel in) {
        this.src = ((String) in.readValue((String.class.getClassLoader())));
        this.configWidth = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.configHeight = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(src);
        dest.writeValue(configWidth);
        dest.writeValue(configHeight);
    }

    public int describeContents() {
        return  0;
    }

}
