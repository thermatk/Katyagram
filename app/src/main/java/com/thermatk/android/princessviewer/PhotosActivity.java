package com.thermatk.android.princessviewer;

import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PhotosActivity extends AppCompatActivity {
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter aPhotos;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchPopularPhotos();
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        fetchPopularPhotos();
    }

    private void fetchPopularPhotos() {
        photos = new ArrayList<>();

        // create adapter and bind it to the data in arraylist
        aPhotos = new InstagramPhotosAdapter(this, photos);
        // populate the data into the listview
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        // Set the adapter to the listview (population of items)
        lvPhotos.setAdapter(aPhotos);

        // do the network request
        String popularUrl = "https://www.instagram.com/katekoti/media/";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(popularUrl, new JsonHttpResponseHandler() {
            // define success and failure callbacks
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray photosJSON;
                JSONArray commentsJSON;
                try {
                    photos.clear();
                    photosJSON = response.getJSONArray("items");
                    for (int i = 0; i < photosJSON.length(); i++) {
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.profileUrl = photoJSON.getJSONObject("user").getString("profile_picture");
                        photo.username = photoJSON.getJSONObject("user").getString("username");
                        // caption may be null
                        if (photoJSON.has("caption") && !photoJSON.isNull("caption")) {
                            photo.caption = photoJSON.getJSONObject("caption").getString("text");
                        }
                        photo.createdTime = photoJSON.getString("created_time");
                        photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
                        // get comments
                        if (photoJSON.has("comments") && !photoJSON.isNull("comments")) {
                            photo.commentsCount = photoJSON.getJSONObject("comments").getInt("count");
                            commentsJSON = photoJSON.getJSONObject("comments").getJSONArray("data");
                            if (commentsJSON.length() > 0) {
                                photo.comment1 = commentsJSON.getJSONObject(commentsJSON.length() - 1).getString("text");
                                photo.user1 = commentsJSON.getJSONObject(commentsJSON.length() - 1).getJSONObject("from").getString("username");
                                if (commentsJSON.length() > 1) {
                                    photo.comment2 = commentsJSON.getJSONObject(commentsJSON.length() - 2).getString("text");
                                    photo.user2 = commentsJSON.getJSONObject(commentsJSON.length() - 2).getJSONObject("from").getString("username");
                                }
                            } else {
                                photo.commentsCount = 0;
                            }
                        }
                        photo.id = photoJSON.getString("id");
                        photos.add(photo);
                    }
                    // notify adapter
                    aPhotos.notifyDataSetChanged();
                } catch (JSONException e ) {
                    // json failed
                    e.printStackTrace();
                }
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

    }

}
