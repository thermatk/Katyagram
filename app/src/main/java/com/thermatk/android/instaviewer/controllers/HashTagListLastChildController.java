package com.thermatk.android.instaviewer.controllers;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.ControllerChangeHandler;
import com.bluelinelabs.conductor.ControllerChangeType;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.thermatk.android.instaviewer.R;
import com.thermatk.android.instaviewer.data.InstagramPhoto;
import com.thermatk.android.instaviewer.interfaces.ILoadMore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;

import static com.thermatk.android.instaviewer.utils.BuildBundle.createBundleWithString;

public class HashTagListLastChildController extends Controller{
    private Unbinder unbinder;
    @BindView(R.id.lvPhotos) RecyclerView mRecyclerView;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;

    private final static String BUNDLE_KEY = "tag";

    private ArrayList<InstagramPhoto> photos;
    private HashTagLastAdapter aPhotos;
    private boolean moreAvailable;
    private String endId;
    private String tag;

    public HashTagListLastChildController(@Nullable Bundle args) {
        super(args);
    }

    public HashTagListLastChildController(String tag) {
        this(createBundleWithString(BUNDLE_KEY,tag));
    }

    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = inflater.inflate(R.layout.controller_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        Context ctx = view.getContext();

        tag = getArgs().getString("tag");

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

        mRecyclerView.setLayoutManager(new GridLayoutManager(ctx, 2));

        aPhotos = new HashTagLastAdapter(LayoutInflater.from(ctx));
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
                    fetchPhotosAdditional();
                }
            }
        });


        fetchPhotosInitial();
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


    private void fetchPhotosInitial() {
        photos.add(null);
        aPhotos.notifyItemInserted(photos.size() - 1);

        // do the network request
        String popularUrl = "https://www.instagram.com/explore/tags/"+tag+"/?__a=1";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(popularUrl, new JsonHttpResponseHandler() {
            // define success and failure callbacks
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                photos.remove(photos.size() - 1);
                aPhotos.notifyItemRemoved(photos.size());

                JSONArray photosJSON;
                try {
                    photos.clear();
                    JSONObject pageInfo = response.getJSONObject("tag").getJSONObject("media").getJSONObject("page_info");
                    moreAvailable = pageInfo.getBoolean("has_next_page");
                    endId = pageInfo.getString("end_cursor");
                    photosJSON = response.getJSONObject("tag").getJSONObject("media").getJSONArray("nodes");
                    for (int i = 0; i < photosJSON.length(); i++) {
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.fromJSONHashTagList(photosJSON.getJSONObject(i));
                        photos.add(photo);
                    }
                    // notify adapter
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
    private void fetchPhotosAdditional() {
        // do the network request
        String popularUrl = "https://www.instagram.com/explore/tags/"+tag+"/?__a=1&max_id="+endId;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(popularUrl, new JsonHttpResponseHandler() {
            // define success and failure callbacks
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                photos.remove(photos.size() - 1);
                aPhotos.notifyItemRemoved(photos.size());

                JSONArray photosJSON;
                try {
                    JSONObject pageInfo = response.getJSONObject("tag").getJSONObject("media").getJSONObject("page_info");
                    moreAvailable = pageInfo.getBoolean("has_next_page");
                    endId = pageInfo.getString("end_cursor");
                    photosJSON = response.getJSONObject("tag").getJSONObject("media").getJSONArray("nodes");
                    for (int i = 0; i < photosJSON.length(); i++) {
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.fromJSONHashTagList(photosJSON.getJSONObject(i));
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
    class HashTagLastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final int VIEW_TYPE_ITEM = 0;
        private final int VIEW_TYPE_LOADING = 1;

        private boolean isLoading;
        private final LayoutInflater inflater;

        private ILoadMore mOnLoadMoreListener;
        private int visibleThreshold = 2;
        private int lastVisibleItem, totalItemCount;
        private final View.OnClickListener mOnClickListener = new PhotoOnClickListener();

        public HashTagLastAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = gridLayoutManager.getItemCount();
                    lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
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
                View view = inflater.inflate(R.layout.item_grid_photo, parent, false);
                view.setOnClickListener(mOnClickListener);
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


                final PhotoViewHolder photoViewHolder = (PhotoViewHolder) holder;

                Context ctx = photoViewHolder.imgPhoto.getContext();

                // use device width for photo height
                DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
                photoViewHolder.imgPhoto.getLayoutParams().height = displayMetrics.widthPixels;

                // Reset the images from the recycled view
                photoViewHolder.imgPhoto.setImageResource(0);

                // Ask for the photo to be added to the imageview based on the photo url
                Picasso.with(ctx).load(photo.imageUrl).placeholder(R.drawable.ic_photo_camera).into(photoViewHolder.imgPhoto);

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
            @BindView(R.id.imgPhoto) ImageView imgPhoto;

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

        class PhotoOnClickListener implements View.OnClickListener {
            @Override
            public void onClick(View view) {
                int itemPosition = mRecyclerView.getChildLayoutPosition(view);
                InstagramPhoto item = photos.get(itemPosition);
                Log.d("katyagram", "Grid clicked on "  + item.code);

                getParentController().getRouter().pushController(
                        RouterTransaction.with(new PhotoController(item.code))
                                .pushChangeHandler(new FadeChangeHandler())
                                .popChangeHandler(new FadeChangeHandler()));
            }
        }

    }
}
