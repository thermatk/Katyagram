package com.thermatk.android.instaviewer.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import org.parceler.Parcel;

import lombok.Data;

@Parcel
@Data
public class Owner {

    @Expose
    public String id;
    @SerializedName("profile_pic_url")
    @Expose
    public String profilePicUrl;
    @Expose
    public String username;
}