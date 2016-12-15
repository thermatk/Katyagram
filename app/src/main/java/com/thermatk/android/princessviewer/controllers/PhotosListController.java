package com.thermatk.android.princessviewer.controllers;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Toast;

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
import com.thermatk.android.princessviewer.data.InstagramPhoto;
import com.thermatk.android.princessviewer.interfaces.ILoadMore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.thermatk.android.princessviewer.utils.BuildBundle.createBundleWithString;
import static com.thermatk.android.princessviewer.utils.TextViewLinks.setupLinkAuthor;
import static com.thermatk.android.princessviewer.utils.TextViewLinks.setupLinkHashtags;
import static com.thermatk.android.princessviewer.utils.TextViewLinks.setupLinkMentions;

public class PhotosListController extends Controller{
    private ArrayList<InstagramPhoto> photos;
    private PhotosAdapter aPhotos;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeContainer;
    private boolean moreAvailable;
    private final static String BUNDLE_KEY = "user";
    private String user;

    public PhotosListController(@Nullable Bundle args) {
        super(args);
    }

    public PhotosListController(String user) {
        this(createBundleWithString(BUNDLE_KEY,user));
    }

    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = inflater.inflate(R.layout.controller_list, container, false);
        Context ctx = view.getContext();

        user = getArgs().getString("user");
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchPhotosInitial();
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        photos = new ArrayList<>();


        mRecyclerView = (RecyclerView) view.findViewById(R.id.lvPhotos);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ctx));

        aPhotos = new PhotosAdapter(LayoutInflater.from(ctx));
        mRecyclerView.setAdapter(aPhotos);

        aPhotos.isLoading = true;
        aPhotos.setOnLoadMoreListener(new ILoadMore() {
            @Override
            public void onLoadMore() {
                Log.e("katyagram", "Load More");
                if (moreAvailable) {
                    photos.add(null);
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        public void run() {
                            aPhotos.notifyItemInserted(photos.size() - 1);
                        }
                    });
                    fetchPhotosAdditional(photos.get(photos.size() - 2).id);
                }
            }
        });

        fetchPhotosInitial();
        return view;
    }

    @Override
    protected void onChangeStarted(@NonNull ControllerChangeHandler changeHandler, @NonNull ControllerChangeType changeType) {
        setOptionsMenuHidden(!changeType.isEnter);

        if (changeType.isEnter) {
            Log.d("katyagram", "first opening");
        }
    }


    private void fetchPhotosInitial() {
        photos.add(null);
        aPhotos.notifyItemInserted(photos.size() - 1);

        // do the network request
        String popularUrl = "https://www.instagram.com/" +user+ "/media/";
        Log.d("katyagram", popularUrl);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(popularUrl, new JsonHttpResponseHandler() {
            // define success and failure callbacks
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                photos.remove(photos.size() - 1);
                aPhotos.notifyItemRemoved(photos.size());

                JSONArray photosJSON;
                try {
                    moreAvailable = response.getBoolean("more_available");
                    photos.clear();
                    photosJSON = response.getJSONArray("items");
                    for (int i = 0; i < photosJSON.length(); i++) {
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.fromJSONMediaList(photosJSON.getJSONObject(i));
                        photos.add(photo);
                    }
                    // notify adapter
                    if(photos.size() == 0) {
                        Log.d("katyagram", "Private profile");
                        Toast.makeText(getApplicationContext(),
                                R.string.error_restricted, Toast.LENGTH_LONG).show();
                        getRouter().handleBack();
                    }
                    aPhotos.notifyDataSetChanged();
                    aPhotos.setLoaded();
                } catch (JSONException e ) {
                    // json failed
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

    private void fetchPhotosAdditional(String maxId) {

        // do the network request
        String popularUrl = "https://www.instagram.com/" +user+ "/media/?max_id="+maxId;
        Log.d("katyagram" , popularUrl);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(popularUrl, new JsonHttpResponseHandler() {
            // define success and failure callbacks
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                photos.remove(photos.size() - 1);
                aPhotos.notifyItemRemoved(photos.size());

                JSONArray photosJSON;
                try {
                    moreAvailable = response.getBoolean("more_available");
                    photosJSON = response.getJSONArray("items");
                    for (int i = 0; i < photosJSON.length(); i++) {
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.fromJSONMediaList(photosJSON.getJSONObject(i));
                        photos.add(photo);
                    }
                    // notify adapter
                    aPhotos.notifyDataSetChanged();
                    aPhotos.setLoaded();
                } catch (JSONException e ) {
                    // json failed
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("katyagram",statusCode + responseString);
            }
        });

    }

    class PhotosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final int VIEW_TYPE_ITEM = 0;
        private final int VIEW_TYPE_LOADING = 1;

        private ILoadMore mOnLoadMoreListener;

        private boolean isLoading;
        private int visibleThreshold = 5;
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
            return photos.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
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
                InstagramPhoto photo = photos.get(position);

                final String code = photo.code;

                final PhotoViewHolder photoViewHolder = (PhotoViewHolder) holder;


                // Populate the subviews (textfield, imageview) with the correct data
                photoViewHolder.tvUsername.setText(photo.username);

                Context ctx = photoViewHolder.tvCaption.getContext();

                photoViewHolder.tvTime.setText(
                        DateUtils.getRelativeTimeSpanString(
                                Long.parseLong(photo.createdTime)*1000,
                                System.currentTimeMillis(),
                                DateUtils.MINUTE_IN_MILLIS,
                                DateUtils.FORMAT_ABBREV_RELATIVE));
                photoViewHolder.tvLikes.setText(String.format("\uD83D\uDC96: %d", photo.likesCount));
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
                if (photo.commentsCount > 0) {
                    photoViewHolder.tvViewAllComments.setText(String.format("\uD83D\uDCAD (%d):", photo.commentsCount));
                    // set click handler for view all comments
                    photoViewHolder.tvViewAllComments.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            InstagramPhoto photo = photos.get(curPos);
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

                // Set last 2 comments
                if (photo.comment1 != null) {
                    photoViewHolder.tvComment1.setText(photo.user1 + " " + photo.comment1);
                    LinkBuilder.on(photoViewHolder.tvComment1)
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
                            .addLink(setupLinkAuthor(photo.user1, ctx, new Link.OnClickListener() {
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
                    photoViewHolder.tvComment1.setVisibility(View.VISIBLE);
                } else {
                    photoViewHolder.tvComment1.setVisibility(View.GONE);
                }

                if (photo.comment2 != null) {
                    photoViewHolder.tvComment2.setText(photo.user2 + " " + photo.comment2);
                    LinkBuilder.on(photoViewHolder.tvComment2)
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
                            .addLink(setupLinkMentions(ctx,new Link.OnClickListener() {
                                @Override
                                public void onClick(String clickedText) {
                                    // single clicked
                                    getRouter().pushController(
                                            RouterTransaction.with(new PhotosListController(clickedText.substring(1)))
                                                    .pushChangeHandler(new FadeChangeHandler())
                                                    .popChangeHandler(new FadeChangeHandler()));
                                }
                            }))
                            .addLink(setupLinkAuthor(photo.user2, ctx,new Link.OnClickListener() {
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

                    photoViewHolder.tvComment2.setVisibility(View.VISIBLE);
                } else {
                    photoViewHolder.tvComment2.setVisibility(View.GONE);
                }

                // use device width for photo height
                DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
                photoViewHolder.imgPhoto.getLayoutParams().height = displayMetrics.widthPixels;

                // Reset the images from the recycled view
                photoViewHolder.imgProfile.setImageResource(0);
                photoViewHolder.imgPhoto.setImageResource(0);

                // Ask for the photo to be added to the imageview based on the photo url
                // Background: Send a network request to the url, download the image bytes, convert into bitmap, insert bitmap into the imageview
                Picasso.with(ctx).load(photo.profileUrl).into(photoViewHolder.imgProfile);
                Picasso.with(ctx).load(photo.imageUrl).placeholder(R.drawable.instagram_glyph_on_white).into(photoViewHolder.imgPhoto);
                photoViewHolder.imgPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getRouter().pushController(
                                RouterTransaction.with(new PhotoController(code))
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
            return photos == null ? 0 : photos.size();
        }

        public void setLoaded() {
            isLoading = false;
        }
        class PhotoViewHolder extends RecyclerView.ViewHolder {
            public ImageView imgProfile;
            public ImageView imgPhoto;
            public TextView tvUsername;
            public TextView tvTime;
            public TextView tvLikes;
            public TextView tvCaption;
            public TextView tvViewAllComments;
            public TextView tvComment1;
            public TextView  tvComment2;

            public PhotoViewHolder(View itemView) {
                super(itemView);
                // Lookup the subview within the template
                imgProfile = (ImageView) itemView.findViewById(R.id.imgProfile);
                imgPhoto = (ImageView) itemView.findViewById(R.id.imgPhoto);
                tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
                tvTime = (TextView) itemView.findViewById(R.id.tvTime);
                tvLikes = (TextView) itemView.findViewById(R.id.tvLikes);
                tvCaption = (TextView) itemView.findViewById(R.id.tvCaption);
                tvViewAllComments = (TextView) itemView.findViewById(R.id.tvViewAllComments);
                tvComment1 = (TextView) itemView.findViewById(R.id.tvComment1);
                tvComment2 = (TextView) itemView.findViewById(R.id.tvComment2);
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
