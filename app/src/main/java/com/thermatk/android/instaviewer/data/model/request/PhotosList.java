package com.thermatk.android.instaviewer.data.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.thermatk.android.instaviewer.data.model.User;

import org.parceler.Parcel;

import lombok.Data;

@Parcel
@Data
public class PhotosList {

    @Expose
    public User user;
    @SerializedName("logging_page_id")
    @Expose
    public String loggingPageId;

}
