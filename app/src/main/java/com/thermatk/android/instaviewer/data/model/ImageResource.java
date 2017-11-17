
package com.thermatk.android.instaviewer.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import lombok.Data;

@Parcel
@Data
public class ImageResource{

    @Expose
    public String src;
    @SerializedName("config_width")
    @Expose
    public Integer configWidth;
    @SerializedName("config_height")
    @Expose
    public Integer configHeight;
}
