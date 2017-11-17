package com.thermatk.android.instaviewer.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import lombok.Data;

@Parcel
@Data
public class NodeComments {

    @Expose
    public String id;
    @Expose
    public String text;
    @SerializedName("created_at")
    @Expose
    public Integer createdAt;
    @Expose
    public Owner owner;
}