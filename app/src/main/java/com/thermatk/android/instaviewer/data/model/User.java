
package com.thermatk.android.instaviewer.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data public class User implements Parcelable
{

    @Expose
    public String biography;
    @SerializedName("blocked_by_viewer")
    @Expose
    public Boolean blockedByViewer;
    @SerializedName("country_block")
    @Expose
    public Boolean countryBlock;
    @SerializedName("external_url")
    @Expose
    public Object externalUrl;
    @SerializedName("external_url_linkshimmed")
    @Expose
    public Object externalUrlLinkshimmed;
    @SerializedName("followed_by")
    @Expose
    public Counter followedBy;
    @SerializedName("followed_by_viewer")
    @Expose
    public Boolean followedByViewer;
    @Expose
    public Counter follows;
    @SerializedName("follows_viewer")
    @Expose
    public Boolean followsViewer;
    @SerializedName("full_name")
    @Expose
    public String fullName;
    @SerializedName("has_blocked_viewer")
    @Expose
    public Boolean hasBlockedViewer;
    @SerializedName("has_requested_viewer")
    @Expose
    public Boolean hasRequestedViewer;
    @Expose
    public String id;
    @SerializedName("is_private")
    @Expose
    public Boolean isPrivate;
    @SerializedName("is_verified")
    @Expose
    public Boolean isVerified;
    @SerializedName("profile_pic_url")
    @Expose
    public String profilePicUrl;
    @SerializedName("profile_pic_url_hd")
    @Expose
    public String profilePicUrlHd;
    @SerializedName("requested_by_viewer")
    @Expose
    public Boolean requestedByViewer;
    @Expose
    public String username;
    @SerializedName("connected_fb_page")
    @Expose
    public Object connectedFbPage;
    @Expose
    public Media media;
    public final static Parcelable.Creator<User> CREATOR = new Creator<User>() {


        @SuppressWarnings({
            "unchecked"
        })
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return (new User[size]);
        }

    }
    ;

    protected User(Parcel in) {
        this.biography = ((String) in.readValue((String.class.getClassLoader())));
        this.blockedByViewer = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.countryBlock = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.externalUrl = ((Object) in.readValue((Object.class.getClassLoader())));
        this.externalUrlLinkshimmed = ((Object) in.readValue((Object.class.getClassLoader())));
        this.followedBy = ((Counter) in.readValue((Counter.class.getClassLoader())));
        this.followedByViewer = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.follows = ((Counter) in.readValue((Counter.class.getClassLoader())));
        this.followsViewer = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.fullName = ((String) in.readValue((String.class.getClassLoader())));
        this.hasBlockedViewer = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.hasRequestedViewer = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.isPrivate = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.isVerified = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.profilePicUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.profilePicUrlHd = ((String) in.readValue((String.class.getClassLoader())));
        this.requestedByViewer = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.username = ((String) in.readValue((String.class.getClassLoader())));
        this.connectedFbPage = ((Object) in.readValue((Object.class.getClassLoader())));
        this.media = ((Media) in.readValue((Media.class.getClassLoader())));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(biography);
        dest.writeValue(blockedByViewer);
        dest.writeValue(countryBlock);
        dest.writeValue(externalUrl);
        dest.writeValue(externalUrlLinkshimmed);
        dest.writeValue(followedBy);
        dest.writeValue(followedByViewer);
        dest.writeValue(follows);
        dest.writeValue(followsViewer);
        dest.writeValue(fullName);
        dest.writeValue(hasBlockedViewer);
        dest.writeValue(hasRequestedViewer);
        dest.writeValue(id);
        dest.writeValue(isPrivate);
        dest.writeValue(isVerified);
        dest.writeValue(profilePicUrl);
        dest.writeValue(profilePicUrlHd);
        dest.writeValue(requestedByViewer);
        dest.writeValue(username);
        dest.writeValue(connectedFbPage);
        dest.writeValue(media);
    }

    public int describeContents() {
        return  0;
    }

}
