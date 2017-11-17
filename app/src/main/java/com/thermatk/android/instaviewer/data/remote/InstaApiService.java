package com.thermatk.android.instaviewer.data.remote;

import com.thermatk.android.instaviewer.data.model.request.OnePhoto;
import com.thermatk.android.instaviewer.data.model.request.PhotosList;

import retrofit2.http.GET;
import retrofit2.Call;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface InstaApiService {
    int A = 1;

    @GET("/{username}/")
    Call<PhotosList> getPhotosList(@Path("username") String username, @Query("max_id") String maxId, @Query("__a") int a);

    @GET("/p/{code}/")
    Call<OnePhoto> getPhoto(@Path("code") String code, @Query("__a") int a);

    @GET("/explore/tags/{tag}/")
    Call<PhotosList> getHashTagPhotoList(@Path("tag") String tag, @Query("max_id") String maxId, @Query("__a") int a);

    @GET("/explore/locations/{locationId}/")
    Call<PhotosList> getLocationPhotoList(@Path("locationId") String locationId, @Query("max_id") String maxId, @Query("__a") int a);
}
