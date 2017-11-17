
package com.thermatk.android.instaviewer.data.model;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data public class Media implements Parcelable
{

    @Expose
    public List<Node> nodes = null;
    @Expose
    public Integer count;
    @SerializedName("page_info")
    @Expose
    public PageInfo pageInfo;
    public final static Parcelable.Creator<Media> CREATOR = new Creator<Media>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Media createFromParcel(Parcel in) {
            return new Media(in);
        }

        public Media[] newArray(int size) {
            return (new Media[size]);
        }

    }
    ;

    protected Media(Parcel in) {
        in.readList(this.nodes, (com.thermatk.android.instaviewer.data.model.Node.class.getClassLoader()));
        this.count = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.pageInfo = ((PageInfo) in.readValue((PageInfo.class.getClassLoader())));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(nodes);
        dest.writeValue(count);
        dest.writeValue(pageInfo);
    }

    public int describeContents() {
        return  0;
    }

}
