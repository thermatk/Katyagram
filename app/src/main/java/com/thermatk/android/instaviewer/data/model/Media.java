
package com.thermatk.android.instaviewer.data.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import lombok.Data;

@Parcel
@Data
public class Media {

    @Expose
    public List<Node> nodes = null;
    @Expose
    public Integer count;
    @SerializedName("page_info")
    @Expose
    public PageInfo pageInfo;
}
