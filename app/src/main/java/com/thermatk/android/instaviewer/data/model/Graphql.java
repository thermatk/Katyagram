package com.thermatk.android.instaviewer.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import lombok.Data;

@Parcel
@Data
public class Graphql {

    @SerializedName("shortcode_media")
    @Expose
    public ShortcodeMedia shortcodeMedia;
}