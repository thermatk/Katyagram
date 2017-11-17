package com.thermatk.android.instaviewer.data.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import lombok.Data;

@Parcel
@Data
public class ShortcodeMedia {

    @SerializedName("__typename")
    @Expose
    public String typename;
    @Expose
    public String id;
    @Expose
    public String shortcode;
    @Expose
    public Dimensions dimensions;
    @SerializedName("media_preview")
    @Expose
    public String mediaPreview;
    @SerializedName("display_url")
    @Expose
    public String displayUrl;
    @SerializedName("display_resources")
    @Expose
    public List<ImageResource> displayResources = null;
    @SerializedName("video_url")
    @Expose
    public String videoUrl;
    @SerializedName("video_view_count")
    @Expose
    public Integer videoViewCount;
    @SerializedName("is_video")
    @Expose
    public Boolean isVideo;
    @SerializedName("should_log_client_event")
    @Expose
    public Boolean shouldLogClientEvent;
    @SerializedName("tracking_token")
    @Expose
    public String trackingToken;
    @SerializedName("edge_media_to_caption")
    @Expose
    public EdgeMediaToCaption edgeMediaToCaption;
    @SerializedName("caption_is_edited")
    @Expose
    public Boolean captionIsEdited;
    @SerializedName("edge_media_to_comment")
    @Expose
    public EdgeMediaToComment edgeMediaToComment;
    @SerializedName("comments_disabled")
    @Expose
    public Boolean commentsDisabled;
    @SerializedName("taken_at_timestamp")
    @Expose
    public Integer takenAtTimestamp;
    @SerializedName("edge_media_preview_like")
    @Expose
    public EdgeMediaPreviewLike edgeMediaPreviewLike;
    @Expose
    public Location location;
    @SerializedName("viewer_has_liked")
    @Expose
    public Boolean viewerHasLiked;
    @SerializedName("viewer_has_saved")
    @Expose
    public Boolean viewerHasSaved;
    @SerializedName("viewer_has_saved_to_collection")
    @Expose
    public Boolean viewerHasSavedToCollection;
    @Expose
    public User owner;
    @SerializedName("is_ad")
    @Expose
    public Boolean isAd;
}