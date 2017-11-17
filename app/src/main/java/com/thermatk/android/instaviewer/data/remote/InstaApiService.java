package com.thermatk.android.instaviewer.data.remote;

import com.thermatk.android.instaviewer.data.model.PhotosList;

import retrofit2.http.GET;
import retrofit2.Call;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface InstaApiService {
    @GET("/{username}/")
    Call<PhotosList> getPhotosList(@Path("username") String username, @Query("max_id") String maxId, @Query("__a") int a);
}
