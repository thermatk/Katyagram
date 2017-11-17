package com.thermatk.android.instaviewer.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NodeComments implements Parcelable
{

    @Expose
    public String id;
    @Expose
    public String text;
    @SerializedName("created_at")
    @Expose
    public Integer createdAt;
    @Expose
    public Owner owner;
    public final static Parcelable.Creator<NodeComments> CREATOR = new Creator<NodeComments>() {


        @SuppressWarnings({
                "unchecked"
        })
        public NodeComments createFromParcel(Parcel in) {
            return new NodeComments(in);
        }

        public NodeComments[] newArray(int size) {
            return (new NodeComments[size]);
        }

    }
            ;

    protected NodeComments(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.text = ((String) in.readValue((String.class.getClassLoader())));
        this.createdAt = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.owner = ((Owner) in.readValue((Owner.class.getClassLoader())));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(text);
        dest.writeValue(createdAt);
        dest.writeValue(owner);
    }

    public int describeContents() {
        return 0;
    }

}