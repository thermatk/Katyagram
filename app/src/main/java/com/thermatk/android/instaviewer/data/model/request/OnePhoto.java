package com.thermatk.android.instaviewer.data.model.request;

import com.google.gson.annotations.Expose;
import com.thermatk.android.instaviewer.data.model.Graphql;

import org.parceler.Parcel;

import lombok.Data;

@Parcel
@Data
public class OnePhoto {

    @Expose
    public Graphql graphql;
}