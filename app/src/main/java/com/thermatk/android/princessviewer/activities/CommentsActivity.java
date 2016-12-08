package com.thermatk.android.princessviewer.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.thermatk.android.princessviewer.CommentsAdapter;
import com.thermatk.android.princessviewer.R;
import com.thermatk.android.princessviewer.data.Comment;

import cz.msebera.android.httpclient.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CommentsActivity extends AppCompatActivity {
    private ArrayList<Comment> comments;
    private CommentsAdapter aComments;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        id = getIntent().getStringExtra("id");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        fetchComments();
    }

    private void fetchComments() {
        comments = new ArrayList<>(); // initialize arraylist
        // Create adapter bind it to the data in arraylist
        aComments = new CommentsAdapter(this, comments);
        // Populate the data into the listview
        ListView lvComments = (ListView) findViewById(R.id.lvComments);
        // Set the adapter to the listview (population of items)
        lvComments.setAdapter(aComments);
        // https://api.instagram.com/v1/media/<id>/comments?client_id=<clientid>
        // Setup comments url endpoint
        //String commentsUrl = "https://api.instagram.com/v1/media/" + id + "/comments?client_id=" + CLIENT_ID;
        String commentsUrl = "https://www.instagram.com/p/" + id + "/?__a=1";

        // Create the network client
        AsyncHttpClient client = new AsyncHttpClient();

        // Trigger the network request
        client.get(commentsUrl, new JsonHttpResponseHandler() {
            // define success and failure callbacks
            // Handle the successful response (popular photos JSON)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // fired once the successful response back
                // resonse is == comments json
                JSONArray commentsJSON;
                try {
                    comments.clear();
                    commentsJSON = response.getJSONArray("data");
                    // put newest at the top
                    for (int i = commentsJSON.length() - 1; i >= 0; i--) {
                        JSONObject commentJSON = commentsJSON.getJSONObject(i);
                        Comment comment = new Comment();
                        comment.profileUrl = commentJSON.getJSONObject("from").getString("profile_picture");
                        comment.username = commentJSON.getJSONObject("from").getString("username");
                        comment.text = commentJSON.getString("text");
                        comment.createdTime = commentJSON.getString("created_time");
                        comments.add(comment);
                    }
                    // Notified the adapter that it should populate new changes into the listview
                    aComments.notifyDataSetChanged();
                } catch (JSONException e ) {
                    // Fire if things fail, json parsing is invalid
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("LOL", responseString);
            }
        });

    }
}
