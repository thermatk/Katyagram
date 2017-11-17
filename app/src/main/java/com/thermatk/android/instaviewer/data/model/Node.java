
package com.thermatk.android.instaviewer.data.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import lombok.Data;

@Parcel
@Data
public class Node {

    @SerializedName("__typename")
    @Expose
    public String typename;
    @Expose
    public String id;
    @SerializedName("comments_disabled")
    @Expose
    public Boolean commentsDisabled;
    @Expose
    public Dimensions dimensions;
    @SerializedName("media_preview")
    @Expose
    public String mediaPreview;
    @Expose
    public Owner owner;
    @SerializedName("thumbnail_src")
    @Expose
    public String thumbnailSrc;
    @SerializedName("thumbnail_resources")
    @Expose
    public List<ImageResource> thumbnailResources = null;
    @SerializedName("is_video")
    @Expose
    public Boolean isVideo;
    @Expose
    public String code;
    @Expose
    public long date;
    @SerializedName("display_src")
    @Expose
    public String displaySrc;
    @Expose
    public String caption;
    @Expose
    public Counter comments;
    @Expose
    public Counter likes;
}
