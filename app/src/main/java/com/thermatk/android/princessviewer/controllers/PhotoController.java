package com.thermatk.android.princessviewer.controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.ControllerChangeHandler;
import com.bluelinelabs.conductor.ControllerChangeType;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.thermatk.android.princessviewer.R;
import com.thermatk.android.princessviewer.data.Comment;
import com.thermatk.android.princessviewer.data.InstagramPhoto;
import com.thermatk.android.princessviewer.interfaces.ILoadMore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.thermatk.android.princessviewer.utils.TextViewLinks.setupLinkAuthor;
import static com.thermatk.android.princessviewer.utils.TextViewLinks.setupLinkHashtags;
import static com.thermatk.android.princessviewer.utils.TextViewLinks.setupLinkMentions;

public class PhotoController extends Controller{
    private InstagramPhoto photo;
    private ArrayList<Comment> comments;
    private CommentsAdapter aComments;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeContainer;
    private String code;

    public PhotoController(@Nullable Bundle args) {
        super(args);
    }

    public PhotoController(String user) {
        this(createBundle(user));
    }

    public static Bundle createBundle (String code) {
        Bundle b = new Bundle();
        b.putString("code", code);
        return b;
    }

    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = inflater.inflate(R.layout.controller_photo, container, false);
        Context ctx = view.getContext();

        code = getArgs().getString("code");
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchPhoto();
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        comments = new ArrayList<>();


        mRecyclerView = (RecyclerView) view.findViewById(R.id.lvComments);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ctx));

        aComments = new CommentsAdapter(LayoutInflater.from(ctx));
        mRecyclerView.setAdapter(aComments);

        aComments.isLoading = true;

        fetchPhoto();
        return view;
    }

    @Override
    protected void onChangeStarted(@NonNull ControllerChangeHandler changeHandler, @NonNull ControllerChangeType changeType) {
        setOptionsMenuHidden(!changeType.isEnter);

        if (changeType.isEnter) {
            Log.d("katyagram", "first opening");
        }
    }


    private void fetchPhoto() {
        comments.add(null);
        aComments.notifyItemInserted(comments.size() - 1);

        // do the network request
        String popularUrl = "https://www.instagram.com/p/" + code + "/?__a=1";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(popularUrl, new JsonHttpResponseHandler() {
            // define success and failure callbacks
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                comments.remove(comments.size() - 1);
                aComments.notifyItemRemoved(comments.size());

                JSONArray commentsJSON;
                try {
                    comments.clear();
                    commentsJSON = response.getJSONObject("media").getJSONObject("comments").getJSONArray("nodes");
                    // put newest at the top
                    for (int i = commentsJSON.length() - 1; i >= 0; i--) {
                        JSONObject commentJSON = commentsJSON.getJSONObject(i);
                        Comment comment = new Comment();
                        comment.fromJSON(commentJSON);
                        comments.add(comment);
                    }
                    // Notified the adapter that it should populate new changes into the listview
                    aComments.notifyDataSetChanged();
                    aComments.setLoaded();
                } catch (JSONException e ) {
                    // Fire if things fail, json parsing is invalid
                    e.printStackTrace();
                }
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("katyagram",statusCode + responseString);
            }
        });

    }

    class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final int VIEW_TYPE_ITEM = 0;
        private final int VIEW_TYPE_LOADING = 1;

        private boolean isLoading;
        private final LayoutInflater inflater;

        public CommentsAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
        }

        @Override
        public int getItemViewType(int position) {
            return comments.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == VIEW_TYPE_ITEM) {
                View view = inflater.inflate(R.layout.item_comment, parent, false);
                return new CommentViewHolder(view);
            } else if (viewType == VIEW_TYPE_LOADING) {
                View view = inflater.inflate(R.layout.item_loading, parent, false);
                return new LoadingViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof CommentViewHolder) {
                Comment comment = comments.get(position);


                final CommentViewHolder commentViewHolder = (CommentViewHolder) holder;

                Context ctx = commentViewHolder.imgProfile.getContext();

                commentViewHolder.tvComment.setText(comment.username + " " + comment.text);
                LinkBuilder.on(commentViewHolder.tvComment)
                        .addLink(setupLinkHashtags(ctx, new Link.OnClickListener() {
                            @Override
                            public void onClick(String clickedText) {
                                // single clicked
                                //
                                getRouter().pushController(
                                        RouterTransaction.with(new HashTagListController(clickedText.substring(1)))
                                                .pushChangeHandler(new FadeChangeHandler())
                                                .popChangeHandler(new FadeChangeHandler()));
                            }
                        }))
                        .addLink(setupLinkMentions(ctx, new Link.OnClickListener() {
                            @Override
                            public void onClick(String clickedText) {
                                // single clicked
                                getRouter().pushController(
                                        RouterTransaction.with(new PhotosListController(clickedText.substring(1)))
                                                .pushChangeHandler(new FadeChangeHandler())
                                                .popChangeHandler(new FadeChangeHandler()));
                            }
                        }))
                        .addLink(setupLinkAuthor(comment.username, ctx, new Link.OnClickListener() {
                            @Override
                            public void onClick(String clickedText) {
                                // single clicked
                                getRouter().pushController(
                                        RouterTransaction.with(new PhotosListController(clickedText))
                                                .pushChangeHandler(new FadeChangeHandler())
                                                .popChangeHandler(new FadeChangeHandler()));
                            }
                        }))
                        .build();

                commentViewHolder.tvCommentTime.setText(
                        DateUtils.getRelativeTimeSpanString(comment.createdTime*1000,
                                System.currentTimeMillis(),
                                DateUtils.SECOND_IN_MILLIS,
                                DateUtils.FORMAT_ABBREV_RELATIVE));

                // Reset the images from the recycled view
                commentViewHolder.imgProfile.setImageResource(0);

                // Ask for the photo to be added to the imageview based on the photo url
                // Background: Send a network request to the url, download the image bytes, convert into bitmap, insert bitmap into the imageview
                Picasso.with(ctx).load(comment.profileUrl).into(commentViewHolder.imgProfile);

            } else if (holder instanceof LoadingViewHolder) {
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setIndeterminate(true);
            }
        }

        @Override
        public int getItemCount() {
            return comments == null ? 0 : comments.size();
        }

        public void setLoaded() {
            isLoading = false;
        }
        class CommentViewHolder extends RecyclerView.ViewHolder {
            public ImageView imgProfile;
            public TextView tvComment;
            public TextView  tvCommentTime;

            public CommentViewHolder(View itemView) {
                super(itemView);
                // Lookup the subview within the template
                imgProfile = (ImageView) itemView.findViewById(R.id.imgCommentProfile);
                tvComment = (TextView) itemView.findViewById(R.id.tvComment);
                tvCommentTime = (TextView) itemView.findViewById(R.id.tvCommentTime);
            }
        }

        class LoadingViewHolder extends RecyclerView.ViewHolder {
            public ProgressBar progressBar;

            public LoadingViewHolder(View itemView) {
                super(itemView);
                progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
            }
        }

    }

}
