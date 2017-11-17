package com.thermatk.android.instaviewer.controllers;

import android.content.Context;
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
import com.squareup.picasso.Picasso;
import com.thermatk.android.instaviewer.R;
import com.thermatk.android.instaviewer.activities.MainActivity;
import com.thermatk.android.instaviewer.data.model.EdgeComments;
import com.thermatk.android.instaviewer.data.model.NodeComments;
import com.thermatk.android.instaviewer.data.model.ShortcodeMedia;
import com.thermatk.android.instaviewer.data.model.request.OnePhoto;
import com.thermatk.android.instaviewer.data.remote.InstaApiService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thermatk.android.instaviewer.utils.BuildBundle.createBundleWithString;
import static com.thermatk.android.instaviewer.utils.TextViewLinks.setupLinkAuthor;
import static com.thermatk.android.instaviewer.utils.TextViewLinks.setupLinkHashtags;
import static com.thermatk.android.instaviewer.utils.TextViewLinks.setupLinkMentions;

public class PhotoController extends Controller{
    private Unbinder unbinder;
    @BindView(R.id.lvComments) RecyclerView mRecyclerView;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.imgProfile) ImageView imgProfileAuthor;
    @BindView(R.id.imgPhoto) ImageView imgPhoto;
    @BindView(R.id.tvUsername) TextView tvUsername;
    @BindView(R.id.tvTime) TextView tvTime;
    @BindView(R.id.tvLikes) TextView tvLikes;
    @BindView(R.id.tvCaption) TextView tvCaption;
    @BindView(R.id.tvViewAllComments) TextView tvViewAllComments;

    private final static String BUNDLE_KEY = "code";

    private MainActivity activity;

    private OnePhoto onePhoto;
    private ShortcodeMedia photo;

    private List<EdgeComments> comments;
    private CommentsAdapter aComments;
    private String code;

    public PhotoController(@Nullable Bundle args) {
        super(args);
    }

    public PhotoController(String code) {
        this(createBundleWithString(BUNDLE_KEY,code));
    }

    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = inflater.inflate(R.layout.controller_photo, container, false);
        unbinder = ButterKnife.bind(this, view);
        Context ctx = view.getContext();

        code = getArgs().getString(BUNDLE_KEY);

        activity = (MainActivity) getRouter().getActivity();

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

        mRecyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setFocusable(false);

        aComments = new CommentsAdapter(LayoutInflater.from(ctx));
        mRecyclerView.setAdapter(aComments);

        aComments.isLoading = true;

        fetchPhoto();
        return view;
    }

    @Override
    protected void onDestroyView(@NonNull View view) {
        super.onDestroyView(view);
        unbinder.unbind();
        unbinder = null;
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
        tvUsername.setText(photo.getOwner().getUsername());
        LinkBuilder.on(tvUsername)
                .addLink(setupLinkAuthor(photo.getOwner().getUsername(), ctx, new Link.OnClickListener() {
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
                        RouterTransaction.with(new PhotosListController(photo.getOwner().getUsername()))
                                .pushChangeHandler(new FadeChangeHandler())
                                .popChangeHandler(new FadeChangeHandler()));
            }
        });
        tvTime.setText(
                DateUtils.getRelativeTimeSpanString(
                        photo.getTakenAtTimestamp()*1000,
                        System.currentTimeMillis(),
                        DateUtils.MINUTE_IN_MILLIS,
                        DateUtils.FORMAT_ABBREV_RELATIVE));
        tvLikes.setText(String.format("\uD83D\uDC96: %d", photo.getEdgeMediaPreviewLike().getCount()));

        if (photo.getEdgeMediaToCaption().getEdges().get(0).getNode().getText() != null) {
            tvCaption.setText(photo.getEdgeMediaToCaption().getEdges().get(0).getNode().getText());

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
        if (photo.getEdgeMediaToComment().getCount() > 0) {
            tvViewAllComments.setText(String.format("\uD83D\uDCAD (%d):", photo.getEdgeMediaToComment().getCount()));
            tvViewAllComments.setVisibility(View.VISIBLE);
        } else {
            tvViewAllComments.setVisibility(View.GONE);
        }

        // Reset the images from the recycled view
        imgProfileAuthor.setImageResource(0);
        imgPhoto.setImageResource(0);

        // Ask for the photo to be added to the imageview based on the photo url
        // Background: Send a network request to the url, download the image bytes, convert into bitmap, insert bitmap into the imageview
        Picasso.with(ctx).load(photo.getOwner().getProfilePicUrl()).into(imgProfileAuthor);
        Picasso.with(ctx).load(photo.getDisplayUrl()).placeholder(R.drawable.ic_photo_camera).into(imgPhoto);
    }

    private void fetchPhoto() {
        comments.add(null);
        aComments.notifyItemInserted(comments.size() - 1);

        Call<OnePhoto> photoCall = activity.instaApiService.getPhoto(code, InstaApiService.A);
        photoCall.enqueue(new Callback<OnePhoto>() {
            @Override
            public void onResponse(Call<OnePhoto> call, Response<OnePhoto> response) {

                if(response.isSuccessful()) {
                    Log.d("katyagram", "Api Success");

                    comments.remove(comments.size() - 1);
                    aComments.notifyItemRemoved(comments.size());

                    photo = response.body().getGraphql().getShortcodeMedia();

                    comments.clear();
                    comments = photo.getEdgeMediaToComment().getEdges();

                    // Notified the adapter that it should populate new changes into the listview
                    aComments.notifyDataSetChanged();
                    aComments.setLoaded();
                    updateData();
                    swipeContainer.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<OnePhoto> call, Throwable t) {
                Log.d("katyagram", "Api OnePhoto Failed");
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
                final NodeComments comment = comments.get(position).getNode();


                final CommentViewHolder commentViewHolder = (CommentViewHolder) holder;

                Context ctx = commentViewHolder.imgProfile.getContext();

                commentViewHolder.tvComment.setText(comment.getOwner().getUsername() + " " + comment.getText());
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
                        .addLink(setupLinkAuthor(comment.getOwner().getUsername(), ctx, new Link.OnClickListener() {
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
                        DateUtils.getRelativeTimeSpanString(comment.getCreatedAt()*1000,
                                System.currentTimeMillis(),
                                DateUtils.SECOND_IN_MILLIS,
                                DateUtils.FORMAT_ABBREV_RELATIVE));

                // Reset the images from the recycled view
                commentViewHolder.imgProfile.setImageResource(0);

                // Ask for the photo to be added to the imageview based on the photo url
                // Background: Send a network request to the url, download the image bytes, convert into bitmap, insert bitmap into the imageview
                Picasso.with(ctx).load(comment.getOwner().getProfilePicUrl()).into(commentViewHolder.imgProfile);

                commentViewHolder.imgProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getRouter().pushController(
                                RouterTransaction.with(new PhotosListController(comment.getOwner().getUsername()))
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
            @BindView(R.id.imgCommentProfile) ImageView imgProfile;
            @BindView(R.id.tvComment) TextView tvComment;
            @BindView(R.id.tvCommentTime) TextView  tvCommentTime;

            public CommentViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        class LoadingViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.progressBar1) ProgressBar progressBar;

            public LoadingViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

    }

}
