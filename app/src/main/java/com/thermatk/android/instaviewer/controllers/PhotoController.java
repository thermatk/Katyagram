package com.thermatk.android.instaviewer.controllers;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
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
import com.thermatk.android.instaviewer.R;
import com.thermatk.android.instaviewer.data.Comment;
import com.thermatk.android.instaviewer.data.InstagramPhoto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.thermatk.android.instaviewer.utils.BuildBundle.createBundleWithString;
import static com.thermatk.android.instaviewer.utils.TextViewLinks.setupLinkAuthor;
import static com.thermatk.android.instaviewer.utils.TextViewLinks.setupLinkHashtags;
import static com.thermatk.android.instaviewer.utils.TextViewLinks.setupLinkMentions;

public class PhotoController extends Controller{
    private InstagramPhoto photo;
    private ArrayList<Comment> comments;
    private CommentsAdapter aComments;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeContainer;
    private final static String BUNDLE_KEY = "code";
    private String code;


    private ImageView imgProfileAuthor;
    private ImageView imgPhoto;
    private TextView tvUsername;
    private TextView tvTime;
    private TextView tvLikes;
    private TextView tvCaption;
    private TextView tvViewAllComments;
    public PhotoController(@Nullable Bundle args) {
        super(args);
    }

    public PhotoController(String code) {
        this(createBundleWithString(BUNDLE_KEY,code));
    }

    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = inflater.inflate(R.layout.controller_photo, container, false);
        Context ctx = view.getContext();

        code = getArgs().getString(BUNDLE_KEY);
        swipeContainer = view.findViewById(R.id.swipeContainer);



        imgProfileAuthor = view.findViewById(R.id.imgProfile);
        imgPhoto = view.findViewById(R.id.imgPhoto);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvTime = view.findViewById(R.id.tvTime);
        tvLikes = view.findViewById(R.id.tvLikes);
        tvCaption = view.findViewById(R.id.tvCaption);
        tvViewAllComments = view.findViewById(R.id.tvViewAllComments);

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


        mRecyclerView = view.findViewById(R.id.lvComments);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        mRecyclerView.setNestedScrollingEnabled(false);


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

    private void updateData() {

        tvUsername.setVisibility(View.VISIBLE);
        tvTime.setVisibility(View.VISIBLE);
        tvLikes.setVisibility(View.VISIBLE);
        imgProfileAuthor.setVisibility(View.VISIBLE);
        imgPhoto.setVisibility(View.VISIBLE);

        Context ctx = tvUsername.getContext();
        // Populate the subviews (textfield, imageview) with the correct data
        tvUsername.setText(photo.username);
        LinkBuilder.on(tvUsername)
                .addLink(setupLinkAuthor(photo.username, ctx, new Link.OnClickListener() {
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
        imgProfileAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRouter().pushController(
                        RouterTransaction.with(new PhotosListController(photo.username))
                                .pushChangeHandler(new FadeChangeHandler())
                                .popChangeHandler(new FadeChangeHandler()));
            }
        });
        tvTime.setText(
                DateUtils.getRelativeTimeSpanString(
                        Long.parseLong(photo.createdTime)*1000,
                        System.currentTimeMillis(),
                        DateUtils.MINUTE_IN_MILLIS,
                        DateUtils.FORMAT_ABBREV_RELATIVE));
        tvLikes.setText(String.format("\uD83D\uDC96: %d", photo.likesCount));

        if (photo.caption != null) {
            tvCaption.setText(photo.caption);

            LinkBuilder.on(tvCaption)
                    .addLink(setupLinkHashtags(ctx,new Link.OnClickListener() {
                        @Override
                        public void onClick(String clickedText) {
                            // single clicked
                            //
                            getRouter().pushController(
                                    RouterTransaction.with(new HashTagController(clickedText.substring(1)))
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
                    .build();
            tvCaption.setVisibility(View.VISIBLE);
        } else {
            tvCaption.setVisibility(View.GONE);
        }
        if (photo.commentsCount > 0) {
            tvViewAllComments.setText(String.format("\uD83D\uDCAD (%d):", photo.commentsCount));
            tvViewAllComments.setVisibility(View.VISIBLE);
        } else {
            tvViewAllComments.setVisibility(View.GONE);
        }

        // use device width for photo height
        DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
        imgPhoto.getLayoutParams().height = displayMetrics.widthPixels;

        // Reset the images from the recycled view
        imgProfileAuthor.setImageResource(0);
        imgPhoto.setImageResource(0);

        // Ask for the photo to be added to the imageview based on the photo url
        // Background: Send a network request to the url, download the image bytes, convert into bitmap, insert bitmap into the imageview
        Picasso.with(ctx).load(photo.profileUrl).into(imgProfileAuthor);
        Picasso.with(ctx).load(photo.imageUrl).placeholder(R.drawable.instagram_glyph_on_white).into(imgPhoto);

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
                    int i = commentsJSON.length();
                    if(i>0) {
                        i--;
                        // put newest at the top
                        for (; i >= 0; i--) {
                            JSONObject commentJSON = commentsJSON.getJSONObject(i);
                            Comment comment = new Comment();
                            comment.fromJSON(commentJSON);
                            comments.add(comment);
                        }
                    }

                    photo = new InstagramPhoto();
                    photo.fromJSONPhoto(response.getJSONObject("media"));

                    // Notified the adapter that it should populate new changes into the listview
                    aComments.notifyDataSetChanged();
                    aComments.setLoaded();
                    updateData();
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
                final Comment comment = comments.get(position);


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
                                        RouterTransaction.with(new HashTagController(clickedText.substring(1)))
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

                commentViewHolder.imgProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getRouter().pushController(
                                RouterTransaction.with(new PhotosListController(comment.username))
                                        .pushChangeHandler(new FadeChangeHandler())
                                        .popChangeHandler(new FadeChangeHandler()));
                    }
                });

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

                imgProfile = itemView.findViewById(R.id.imgCommentProfile);
                tvComment = itemView.findViewById(R.id.tvComment);
                tvCommentTime = itemView.findViewById(R.id.tvCommentTime);
            }
        }

        class LoadingViewHolder extends RecyclerView.ViewHolder {
            public ProgressBar progressBar;

            public LoadingViewHolder(View itemView) {
                super(itemView);
                progressBar = itemView.findViewById(R.id.progressBar1);
            }
        }

    }

}
