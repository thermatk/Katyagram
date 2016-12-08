package com.thermatk.android.princessviewer.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InstagramPhoto {
    public String username;
    public String caption;
    public String createdTime;
    public String imageUrl;
    public String profileUrl;
    public String comment1;
    public String user1;
    public String comment2;
    public String user2;
    public String id;
    public String code;
    public int imageHeight;
    public int likesCount;
    public int commentsCount;

    public String getRelativeTime() {
        long ct = Long.parseLong(createdTime);
        long now = System.currentTimeMillis() / 1000;
        long elapsedSeconds = now - ct;

        if (elapsedSeconds < 60) { // less than a minute
            return String.format("%.0fс", elapsedSeconds);
        } else if (elapsedSeconds < 3600) { // less than an hour
            return String.format("%.0fм", Math.floor(elapsedSeconds / 60));
        } else if (elapsedSeconds < 86400) { // less than a day
            return String.format("%.0fч", Math.floor(elapsedSeconds / 3600));
        } else {
            return String.format("%.0fд", Math.floor(elapsedSeconds / 86400));
        }
    }

    public void fromJSON(JSONObject photoJSON) throws JSONException {
        profileUrl = photoJSON.getJSONObject("user").getString("profile_picture");
        username = photoJSON.getJSONObject("user").getString("username");
        // caption may be null
        if (photoJSON.has("caption") && !photoJSON.isNull("caption")) {
            caption = photoJSON.getJSONObject("caption").getString("text");
        }
        createdTime = photoJSON.getString("created_time");
        imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
        imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
        likesCount = photoJSON.getJSONObject("likes").getInt("count");
        // get comments
        if (photoJSON.has("comments") && !photoJSON.isNull("comments")) {
            commentsCount = photoJSON.getJSONObject("comments").getInt("count");
            JSONArray commentsJSON = photoJSON.getJSONObject("comments").getJSONArray("data");
            if (commentsJSON.length() > 0) {
                comment1 = commentsJSON.getJSONObject(commentsJSON.length() - 1).getString("text");
                user1 = commentsJSON.getJSONObject(commentsJSON.length() - 1).getJSONObject("from").getString("username");
                if (commentsJSON.length() > 1) {
                    comment2 = commentsJSON.getJSONObject(commentsJSON.length() - 2).getString("text");
                    user2 = commentsJSON.getJSONObject(commentsJSON.length() - 2).getJSONObject("from").getString("username");
                }
            } else {
                commentsCount = 0;
            }
        }
        id = photoJSON.getString("id");
        code = photoJSON.getString("code");
    }
}