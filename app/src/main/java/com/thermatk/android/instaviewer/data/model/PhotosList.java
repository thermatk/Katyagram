
package com.thermatk.android.instaviewer.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data public class PhotosList implements Parcelable
{

    @Expose
    public User user;
    @SerializedName("logging_page_id")
    @Expose
    public String loggingPageId;
    public final static Parcelable.Creator<PhotosList> CREATOR = new Creator<PhotosList>() {


        @SuppressWarnings({
            "unchecked"
        })
        public PhotosList createFromParcel(Parcel in) {
            return new PhotosList(in);
        }

        public PhotosList[] newArray(int size) {
            return (new PhotosList[size]);
        }

    }
    ;

    protected PhotosList(Parcel in) {
        this.user = ((User) in.readValue((User.class.getClassLoader())));
        this.loggingPageId = ((String) in.readValue((String.class.getClassLoader())));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(user);
        dest.writeValue(loggingPageId);
    }

    public int describeContents() {
        return  0;
    }

}
