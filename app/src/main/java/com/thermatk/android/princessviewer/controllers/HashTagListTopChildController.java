package com.thermatk.android.princessviewer.controllers;

import android.content.Context;
import android.os.Bundle;
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
import com.thermatk.android.princessviewer.R;
import com.thermatk.android.princessviewer.data.InstagramPhoto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.thermatk.android.princessviewer.utils.BuildBundle.createBundleWithString;

public class HashTagListTopChildController extends Controller{
    private ArrayList<InstagramPhoto> photos;
    private HashTagTopAdapter aPhotos;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeContainer;
    private final static String BUNDLE_KEY = "tag";
    private String tag;

    public HashTagListTopChildController(@Nullable Bundle args) {
        super(args);
    }

    public HashTagListTopChildController(String tag) {
        this(createBundleWithString(BUNDLE_KEY,tag));
    }

    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = inflater.inflate(R.layout.controller_photos_list, container, false);
        Context ctx = view.getContext();

        tag = getArgs().getString("tag");
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
        mRecyclerView.setLayoutManager(new GridLayoutManager(ctx, 2));

        aPhotos = new HashTagTopAdapter(LayoutInflater.from(ctx));
        mRecyclerView.setAdapter(aPhotos);

        aPhotos.isLoading = true;

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
                    photosJSON = response.getJSONObject("tag").getJSONObject("top_posts").getJSONArray("nodes");
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

    class HashTagTopAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final int VIEW_TYPE_ITEM = 0;
        private final int VIEW_TYPE_LOADING = 1;

        private boolean isLoading;
        private final LayoutInflater inflater;
        private final View.OnClickListener mOnClickListener = new PhotoOnClickListener();

        public HashTagTopAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
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
                Picasso.with(ctx).load(photo.imageUrl).placeholder(R.drawable.instagram_glyph_on_white).into(photoViewHolder.imgPhoto);

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
            public ImageView imgPhoto;

            public PhotoViewHolder(View itemView) {
                super(itemView);
                // Lookup the subview within the template
                imgPhoto = (ImageView) itemView.findViewById(R.id.imgPhoto);
            }
        }

        class LoadingViewHolder extends RecyclerView.ViewHolder {
            public ProgressBar progressBar;

            public LoadingViewHolder(View itemView) {
                super(itemView);
                progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
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
