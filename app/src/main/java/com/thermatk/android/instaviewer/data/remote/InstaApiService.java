package com.thermatk.android.instaviewer.data.remote;

import com.thermatk.android.instaviewer.data.model.PhotosList;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.Call;
import retrofit2.http.Path;

public interface InstaApiService {
    @GET("/{username}/?__a=1")
    @FormUrlEncoded
    Call<PhotosList> getPhotosList(@Path("username") String username);
}
