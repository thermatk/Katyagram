
package com.thermatk.android.instaviewer.data.model.request;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.thermatk.android.instaviewer.data.model.Graphql;

public class OnePhoto implements Parcelable
{

    @Expose
    public Graphql graphql;
    public final static Parcelable.Creator<OnePhoto> CREATOR = new Creator<OnePhoto>() {


        @SuppressWarnings({
                "unchecked"
        })
        public OnePhoto createFromParcel(Parcel in) {
            return new OnePhoto(in);
        }

        public OnePhoto[] newArray(int size) {
            return (new OnePhoto[size]);
        }

    }
            ;

    protected OnePhoto(Parcel in) {
        this.graphql = ((Graphql) in.readValue((Graphql.class.getClassLoader())));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(graphql);
    }

    public int describeContents() {
        return 0;
    }

}