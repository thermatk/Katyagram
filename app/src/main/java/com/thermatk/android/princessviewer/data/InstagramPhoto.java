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
    public String location;
    public int imageHeight;
    public int likesCount;
    public int commentsCount;

    public void fromJSONBasic(JSONObject photoJSON) throws JSONException {
        /*
        	        "code": "BN8a7_Xh2bP",
					"dimensions": {
						"width": 750,
						"height": 750
					},
					"owner": {
						"id": "1317615455"
					},
					"comments": {
						"count": 70
					},
					"caption": "That smile 😍\n-\nTag 3 guys that would fall in love with her 👇",
					"likes": {
						"count": 11725
					},
					"date": 1481603651,
					"thumbnail_src": "",
					"is_video": false,
					"id": "1404115655531652815",
					"display_src": ""
         */
        // caption may be null
        if (photoJSON.has("caption") && !photoJSON.isNull("caption")) {
            caption = photoJSON.getString("caption");
        }
        createdTime = photoJSON.getLong("date") + "";
        imageUrl = photoJSON.getString("thumbnail_src");
        imageHeight = 640;
        likesCount = photoJSON.getJSONObject("likes").getInt("count");
        // get comments
        if (photoJSON.has("comments") && !photoJSON.isNull("comments")) {
            commentsCount = photoJSON.getJSONObject("comments").getInt("count");
        }
        id = photoJSON.getString("id");
        code = photoJSON.getString("code");
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

        if (photoJSON.has("location") && !photoJSON.isNull("location")) {
            location = photoJSON.getJSONObject("location").getString("name");
        }
    }
}
