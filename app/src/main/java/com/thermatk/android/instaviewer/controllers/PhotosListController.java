package com.thermatk.android.instaviewer.controllers;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Toast;

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
import com.thermatk.android.instaviewer.data.model.Node;
import com.thermatk.android.instaviewer.data.model.request.PhotosList;
import com.thermatk.android.instaviewer.data.remote.InstaApiService;
import com.thermatk.android.instaviewer.interfaces.ILoadMore;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thermatk.android.instaviewer.utils.BuildBundle.createBundleWithString;
import static com.thermatk.android.instaviewer.utils.TextViewLinks.setupLinkHashtags;
import static com.thermatk.android.instaviewer.utils.TextViewLinks.setupLinkMentions;

public class PhotosListController extends Controller{
    private Unbinder unbinder;
    @BindView(R.id.lvPhotos) RecyclerView mRecyclerView;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;

    private final static String BUNDLE_KEY = "user";

    private MainActivity activity;

    private PhotosList photosList;
    private List<Node> photosNodes;
    private PhotosAdapter aPhotos;
    private boolean moreAvailable;
    private String user;
    private String maxId;

    public PhotosListController(@Nullable Bundle args) {
        super(args);
    }

    public PhotosListController(String user) {
        this(createBundleWithString(BUNDLE_KEY,user));
    }

    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = inflater.inflate(R.layout.controller_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        Context ctx = view.getContext();

        user = getArgs().getString("user");
        maxId = "";

        activity = (MainActivity) getRouter().getActivity();

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                maxId = "";
                fetchPhotos();
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        photosNodes = new ArrayList<>();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(ctx));

        aPhotos = new PhotosAdapter(LayoutInflater.from(ctx));
        mRecyclerView.setAdapter(aPhotos);

        aPhotos.isLoading = true;
        aPhotos.setOnLoadMoreListener(new ILoadMore() {
            @Override
            public void onLoadMore() {
                Log.d("katyagram", "Load More");
                if (moreAvailable) {
                    photosNodes.add(null);
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        public void run() {
                            aPhotos.notifyItemInserted(photosNodes.size() - 1);
                        }
                    });
                    fetchMorePhotos();
                }
            }
        });

        fetchPhotos();
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

    private void fetchPhotos() {
        photosNodes.add(null);
        aPhotos.notifyItemInserted(photosNodes.size() - 1);

        Call<PhotosList> photosListCall = activity.instaApiService.getPhotosList(user,maxId,InstaApiService.A);
        photosListCall.enqueue(new Callback<PhotosList>() {
            @Override
            public void onResponse(Call<PhotosList> call, Response<PhotosList> response) {

                if(response.isSuccessful()) {
                    Log.d("katyagram", "Api Success");
                    photosList = response.body();
                    moreAvailable = photosList.getUser().getMedia().getPageInfo().getHasNextPage();
                    maxId = photosList.getUser().getMedia().getPageInfo().getEndCursor();

                    photosNodes.remove(photosNodes.size() - 1);
                    aPhotos.notifyItemRemoved(photosNodes.size());

                    photosNodes = photosList.getUser().getMedia().getNodes();

                    if(photosNodes.size() == 0) {
                        Log.d("katyagram", "Private profile");
                        Toast.makeText(getApplicationContext(),
                                R.string.error_restricted, Toast.LENGTH_LONG).show();
                        getRouter().handleBack();
                    }

                    // notify adapter
                    aPhotos.notifyDataSetChanged();
                    aPhotos.setLoaded();

                    swipeContainer.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<PhotosList> call, Throwable t) {
                Log.d("katyagram", "Api Failed");
            }
        });
    }

    private void fetchMorePhotos() {

        Call<PhotosList> photosListCall = activity.instaApiService.getPhotosList(user,maxId, InstaApiService.A);
        photosListCall.enqueue(new Callback<PhotosList>() {
            @Override
            public void onResponse(Call<PhotosList> call, Response<PhotosList> response) {

                if(response.isSuccessful()) {
                    Log.d("katyagram", "Api Success");
                    photosNodes.remove(photosNodes.size() - 1);
                    aPhotos.notifyItemRemoved(photosNodes.size());

                    photosList = response.body();
                    photosNodes.addAll(photosList.getUser().getMedia().getNodes());

                    moreAvailable = photosList.getUser().getMedia().getPageInfo().getHasNextPage();
                    maxId = photosList.getUser().getMedia().getPageInfo().getEndCursor();
                    // notify adapter
                    aPhotos.notifyDataSetChanged();
                    aPhotos.setLoaded();
                }
            }

            @Override
            public void onFailure(Call<PhotosList> call, Throwable t) {
                Log.d("katyagram", "Api Failed");
            }
        });
    }

    class PhotosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final int VIEW_TYPE_ITEM = 0;
        private final int VIEW_TYPE_LOADING = 1;

        private ILoadMore mOnLoadMoreListener;

        private boolean isLoading;
        private final int visibleThreshold = 5;
        private int lastVisibleItem, totalItemCount;
        private final LayoutInflater inflater;

        public PhotosAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (mOnLoadMoreListener != null) {
                            mOnLoadMoreListener.onLoadMore();
                        }
                        isLoading = true;
                    }
                }
            });
        }

        public void setOnLoadMoreListener(ILoadMore mOnLoadMoreListener) {
            this.mOnLoadMoreListener = mOnLoadMoreListener;
        }

        @Override
        public int getItemViewType(int position) {
            return photosNodes.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == VIEW_TYPE_ITEM) {
                View view = inflater.inflate(R.layout.item_photo, parent, false);
                return new PhotoViewHolder(view);
            } else if (viewType == VIEW_TYPE_LOADING) {
                View view = inflater.inflate(R.layout.item_loading, parent, false);
                return new LoadingViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof PhotoViewHolder) {
                final Node photo = photosNodes.get(position);

                final String code = photo.code;

                final PhotoViewHolder photoViewHolder = (PhotoViewHolder) holder;

                photoViewHolder.tvUsername.setText(photosList.getUser().getUsername());

                Context ctx = photoViewHolder.tvCaption.getContext();

                photoViewHolder.tvTime.setText(
                        DateUtils.getRelativeTimeSpanString(photo.getDate()*1000,
                                System.currentTimeMillis(),
                                DateUtils.MINUTE_IN_MILLIS,
                                DateUtils.FORMAT_ABBREV_RELATIVE));
                photoViewHolder.tvLikes.setText(String.format("\uD83D\uDC96: %d", photo.getLikes().getCount()));
                if (photo.caption != null) {
                    photoViewHolder.tvCaption.setText(photo.caption);

                    LinkBuilder.on(photoViewHolder.tvCaption)
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
                    photoViewHolder.tvCaption.setVisibility(View.VISIBLE);
                } else {
                    photoViewHolder.tvCaption.setVisibility(View.GONE);
                }
                final int curPos = position;
                if (photo.getComments().getCount() > 0) {
                    photoViewHolder.tvViewAllComments.setText(String.format("\uD83D\uDCAD (%d)", photo.getComments().getCount()));
                    // set click handler for view all comments
                    photoViewHolder.tvViewAllComments.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Node photo = photosNodes.get(curPos);
                            getRouter().pushController(
                                    RouterTransaction.with(new PhotoController(photo.code))
                                            .pushChangeHandler(new FadeChangeHandler())
                                            .popChangeHandler(new FadeChangeHandler()));
                        }
                    });
                    photoViewHolder.tvViewAllComments.setVisibility(View.VISIBLE);
                } else {
                    photoViewHolder.tvViewAllComments.setVisibility(View.GONE);
                }

                // Reset the images from the recycled view
                photoViewHolder.imgProfile.setImageResource(0);
                photoViewHolder.imgPhoto.setImageResource(0);

                Picasso.with(ctx).load(photosList.getUser().getProfilePicUrlHd()).into(photoViewHolder.imgProfile);
                // show overlay if a video
                if (photo.isVideo) {
                    photoViewHolder.imgPhotoPlay.setVisibility(View.VISIBLE);
                } else if(photo.getTypename().contains("Sidecar")) {
                    photoViewHolder.imgPhotoPlay.setVisibility(View.VISIBLE);
                    photoViewHolder.imgPhotoPlay.setImageDrawable(ctx.getDrawable(R.drawable.ic_collections));
                    Log.d("katyagram","A sidecar!");
                } else {
                    photoViewHolder.imgPhotoPlay.setVisibility(View.GONE);
                }
                Picasso.with(ctx).load(photo.getDisplaySrc())
                        .placeholder(R.drawable.ic_photo_camera)
                        .into(photoViewHolder.imgPhoto);
                photoViewHolder.imgPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // retain in case it's a one-off thing
                        // TODO: better retain handling
                        setRetainViewMode(RetainViewMode.RETAIN_DETACH);
                        if (photo.isVideo) {
                            // TODO: fix and rewrite video
                            getRouter().pushController(
                                    // TODO: check video how
                                    RouterTransaction.with(new VideoPlayController(photo.getDisplaySrc()))
                                            .pushChangeHandler(new FadeChangeHandler())
                                            .popChangeHandler(new FadeChangeHandler()));

                            //photoViewHolder.imgPhoto.setVisibility(View.GONE);
                        } else {
                            getRouter().pushController(
                                    RouterTransaction.with(new PhotoController(code))
                                            .pushChangeHandler(new FadeChangeHandler())
                                            .popChangeHandler(new FadeChangeHandler()));
                        }
                    }
                });

            } else if (holder instanceof LoadingViewHolder) {
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setIndeterminate(true);
            }
        }

        @Override
        public int getItemCount() {
            return photosNodes == null ? 0 : photosNodes.size();
        }

        public void setLoaded() {
            isLoading = false;
        }

        class PhotoViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.imgProfile) ImageView imgProfile;
            @BindView(R.id.imgPhoto) ImageView imgPhoto;
            @BindView(R.id.tvUsername) TextView tvUsername;
            @BindView(R.id.tvTime) TextView tvTime;
            @BindView(R.id.tvLikes) TextView tvLikes;
            @BindView(R.id.tvCaption) TextView tvCaption;
            @BindView(R.id.tvViewAllComments) TextView tvViewAllComments;
            @BindView(R.id.imgPhotoPlay) ImageView imgPhotoPlay;

            public PhotoViewHolder(View itemView) {
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
