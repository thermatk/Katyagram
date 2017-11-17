
package com.thermatk.android.instaviewer.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import lombok.Data;

@Parcel
@Data
public class PageInfo {

    @SerializedName("has_next_page")
    @Expose
    public Boolean hasNextPage;
    @SerializedName("end_cursor")
    @Expose
    public String endCursor;
}
