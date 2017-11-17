package com.thermatk.android.instaviewer.data.model;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShortcodeMedia implements Parcelable
{

    @SerializedName("__typename")
    @Expose
    public String typename;
    @Expose
    public String id;
    @Expose
    public String shortcode;
    @Expose
    public Dimensions dimensions;
    @SerializedName("gating_info")
    @Expose
    public Object gatingInfo;
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
    public final static Parcelable.Creator<ShortcodeMedia> CREATOR = new Creator<ShortcodeMedia>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ShortcodeMedia createFromParcel(Parcel in) {
            return new ShortcodeMedia(in);
        }

        public ShortcodeMedia[] newArray(int size) {
            return (new ShortcodeMedia[size]);
        }

    }
            ;

    protected ShortcodeMedia(Parcel in) {
        this.typename = ((String) in.readValue((String.class.getClassLoader())));
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.shortcode = ((String) in.readValue((String.class.getClassLoader())));
        this.dimensions = ((Dimensions) in.readValue((Dimensions.class.getClassLoader())));
        this.gatingInfo = ((Object) in.readValue((Object.class.getClassLoader())));
        this.mediaPreview = ((String) in.readValue((String.class.getClassLoader())));
        this.displayUrl = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.displayResources, (ImageResource.class.getClassLoader()));
        this.videoUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.videoViewCount = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.isVideo = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.shouldLogClientEvent = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.trackingToken = ((String) in.readValue((String.class.getClassLoader())));
        this.edgeMediaToCaption = ((EdgeMediaToCaption) in.readValue((EdgeMediaToCaption.class.getClassLoader())));
        this.captionIsEdited = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.edgeMediaToComment = ((EdgeMediaToComment) in.readValue((EdgeMediaToComment.class.getClassLoader())));
        this.commentsDisabled = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.takenAtTimestamp = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.edgeMediaPreviewLike = ((EdgeMediaPreviewLike) in.readValue((EdgeMediaPreviewLike.class.getClassLoader())));
        this.location = ((Location) in.readValue((Location.class.getClassLoader())));
        this.viewerHasLiked = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.viewerHasSaved = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.viewerHasSavedToCollection = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.owner = ((User) in.readValue((User.class.getClassLoader())));
        this.isAd = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(typename);
        dest.writeValue(id);
        dest.writeValue(shortcode);
        dest.writeValue(dimensions);
        dest.writeValue(gatingInfo);
        dest.writeValue(mediaPreview);
        dest.writeValue(displayUrl);
        dest.writeList(displayResources);
        dest.writeValue(videoUrl);
        dest.writeValue(videoViewCount);
        dest.writeValue(isVideo);
        dest.writeValue(shouldLogClientEvent);
        dest.writeValue(trackingToken);
        dest.writeValue(edgeMediaToCaption);
        dest.writeValue(captionIsEdited);
        dest.writeValue(edgeMediaToComment);
        dest.writeValue(commentsDisabled);
        dest.writeValue(takenAtTimestamp);
        dest.writeValue(edgeMediaPreviewLike);
        dest.writeValue(location);
        dest.writeValue(viewerHasLiked);
        dest.writeValue(viewerHasSaved);
        dest.writeValue(viewerHasSavedToCollection);
        dest.writeValue(owner);
        dest.writeValue(isAd);
    }

    public int describeContents() {
        return 0;
    }

}