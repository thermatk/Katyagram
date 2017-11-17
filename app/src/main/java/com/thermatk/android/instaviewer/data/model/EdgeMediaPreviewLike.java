package com.thermatk.android.instaviewer.data.model;

import java.util.List;
import com.google.gson.annotations.Expose;

import org.parceler.Parcel;

import lombok.Data;

@Parcel
@Data
public class EdgeMediaPreviewLike {

    @Expose
    public Integer count;
    @Expose
    public List<EdgeLikers> edges = null;
}