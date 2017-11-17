
package com.thermatk.android.instaviewer.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import lombok.Data;

@Parcel
@Data
public class User {

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
    public String externalUrl;
    @SerializedName("external_url_linkshimmed")
    @Expose
    public String externalUrlLinkshimmed;
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
    @Expose
    public Media media;
}
