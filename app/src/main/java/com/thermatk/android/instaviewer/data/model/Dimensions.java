
package com.thermatk.android.instaviewer.data.model;

import com.google.gson.annotations.Expose;

import org.parceler.Parcel;

import lombok.Data;

@Parcel
@Data
public class Dimensions {

    @Expose
    public Integer height;
    @Expose
    public Integer width;
}
