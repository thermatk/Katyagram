package com.thermatk.android.instaviewer.data.model;

import com.google.gson.annotations.Expose;

import org.parceler.Parcel;

import lombok.Data;

@Parcel
@Data
public class CaptionText {

    @Expose
    public String text;
}