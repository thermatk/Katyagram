
package com.thermatk.android.instaviewer.data.model;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data public class Node implements Parcelable
{

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
    public final static Parcelable.Creator<Node> CREATOR = new Creator<Node>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Node createFromParcel(Parcel in) {
            return new Node(in);
        }

        public Node[] newArray(int size) {
            return (new Node[size]);
        }

    }
    ;

    protected Node(Parcel in) {
        this.typename = ((String) in.readValue((String.class.getClassLoader())));
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.commentsDisabled = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.dimensions = ((Dimensions) in.readValue((Dimensions.class.getClassLoader())));
        this.mediaPreview = ((String) in.readValue((String.class.getClassLoader())));
        this.owner = ((Owner) in.readValue((Owner.class.getClassLoader())));
        this.thumbnailSrc = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.thumbnailResources, (ImageResource.class.getClassLoader()));
        this.isVideo = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.code = ((String) in.readValue((String.class.getClassLoader())));
        this.date = ((long) in.readValue((Integer.class.getClassLoader())));
        this.displaySrc = ((String) in.readValue((String.class.getClassLoader())));
        this.caption = ((String) in.readValue((String.class.getClassLoader())));
        this.comments = ((Counter) in.readValue((Counter.class.getClassLoader())));
        this.likes = ((Counter) in.readValue((Counter.class.getClassLoader())));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(typename);
        dest.writeValue(id);
        dest.writeValue(commentsDisabled);
        dest.writeValue(dimensions);
        dest.writeValue(mediaPreview);
        dest.writeValue(owner);
        dest.writeValue(thumbnailSrc);
        dest.writeList(thumbnailResources);
        dest.writeValue(isVideo);
        dest.writeValue(code);
        dest.writeValue(date);
        dest.writeValue(displaySrc);
        dest.writeValue(caption);
        dest.writeValue(comments);
        dest.writeValue(likes);
    }

    public int describeContents() {
        return  0;
    }

}
