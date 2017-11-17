package com.thermatk.android.instaviewer.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import org.parceler.Parcel;

import lombok.Data;

@Parcel
@Data
public class Location {

    @Expose
    public String id;
    @SerializedName("has_public_page")
    @Expose
    public Boolean hasPublicPage;
    @Expose
    public String name;
    @Expose
    public String slug;
}