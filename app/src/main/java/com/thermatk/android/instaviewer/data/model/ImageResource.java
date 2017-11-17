
package com.thermatk.android.instaviewer.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data public class ImageResource implements Parcelable
{

    @Expose
    public String src;
    @SerializedName("config_width")
    @Expose
    public Integer configWidth;
    @SerializedName("config_height")
    @Expose
    public Integer configHeight;
    public final static Parcelable.Creator<ImageResource> CREATOR = new Creator<ImageResource>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ImageResource createFromParcel(Parcel in) {
            return new ImageResource(in);
        }

        public ImageResource[] newArray(int size) {
            return (new ImageResource[size]);
        }

    }
    ;

    protected ImageResource(Parcel in) {
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
