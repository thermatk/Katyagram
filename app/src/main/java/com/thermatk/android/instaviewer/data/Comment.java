package com.thermatk.android.instaviewer.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Comment {
    public String profileUrl;
    public String username;
    public String text;
    public Long createdTime;

    public void fromJSON(JSONObject commentJSON) throws JSONException {
        profileUrl = commentJSON.getJSONObject("user").getString("profile_pic_url");
        username = commentJSON.getJSONObject("user").getString("username");
        text = commentJSON.getString("text");
        createdTime = commentJSON.getLong("created_at");
    }
}
