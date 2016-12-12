package com.thermatk.android.princessviewer.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.thermatk.android.princessviewer.R;
import com.thermatk.android.princessviewer.data.InstagramPhoto;
import com.thermatk.android.princessviewer.interfaces.ILoadMore;

import cz.msebera.android.httpclient.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PhotosActivity extends AppCompatActivity {
    private ArrayList<InstagramPhoto> photos;
    private PhotosAdapter aPhotos;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeContainer;
    private Context mContext;
    private boolean moreAvailable;
    private String user = "katekoti";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        mContext = this;
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

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


        mRecyclerView = (RecyclerView) findViewById(R.id.lvPhotos);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        aPhotos = new PhotosAdapter();
        mRecyclerView.setAdapter(aPhotos);

        aPhotos.setOnLoadMoreListener(new ILoadMore() {
            @Override
            public void onLoadMore() {
                Log.e("katyagram", "Load More");
                if (moreAvailable) {
                    photos.add(null);
                    aPhotos.notifyItemInserted(photos.size() - 1);
                    fetchPhotosAdditional(photos.get(photos.size() - 2).id);
                }
            }
        });

        fetchPhotosInitial();
    }

    private void fetchPhotosInitial() {
        photos.add(null);
        aPhotos.notifyItemInserted(photos.size() - 1);

        // do the network request
        String popularUrl = "https://www.instagram.com/" +user+ "/media/";
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
                        photo.fromJSON(photosJSON.getJSONObject(i));
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
                super.onFailure(statusCode, headers, responseString, throwable);
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
                        photo.fromJSON(photosJSON.getJSONObject(i));
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
                super.onFailure(statusCode, headers, responseString, throwable);
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

        public PhotosAdapter() {
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
                View view = LayoutInflater.from(PhotosActivity.this).inflate(R.layout.item_photo, parent, false);
                return new PhotoViewHolder(view);
            } else if (viewType == VIEW_TYPE_LOADING) {
                View view = LayoutInflater.from(PhotosActivity.this).inflate(R.layout.item_loading, parent, false);
                return new LoadingViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof PhotoViewHolder) {
                InstagramPhoto photo = photos.get(position);


                PhotoViewHolder photoViewHolder = (PhotoViewHolder) holder;


                // Populate the subviews (textfield, imageview) with the correct data
                photoViewHolder.tvUsername.setText(photo.username);
                //photoViewHolder.tvTime.setText(photo.getRelativeTime());
                photoViewHolder.tvTime.setText(
                        DateUtils.getRelativeTimeSpanString(
                                Long.parseLong(photo.createdTime)*1000,
                                System.currentTimeMillis(),
                                DateUtils.DAY_IN_MILLIS,
                                DateUtils.FORMAT_ABBREV_RELATIVE));
                photoViewHolder.tvLikes.setText(String.format("\uD83D\uDC96: %d", photo.likesCount));
                if (photo.caption != null) {
                    photoViewHolder.tvCaption.setText(Html.fromHtml("<font color='#3f729b'><b>" + photo.username + "</b></font> " + photo.caption));
                    photoViewHolder.tvCaption.setVisibility(View.VISIBLE);
                } else {
                    photoViewHolder.tvCaption.setVisibility(View.GONE);
                }
                final int curPos = position;
                if (photo.commentsCount > 0) {
                    photoViewHolder.tvViewAllComments.setText(String.format("\uD83D\uDCAD (%d)", photo.commentsCount));
                    // set click handler for view all comments
                    photoViewHolder.tvViewAllComments.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, CommentsActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            InstagramPhoto photo = photos.get(curPos);
                            intent.putExtra("code", photo.code);
                            mContext.startActivity(intent);
                        }
                    });
                    photoViewHolder.tvViewAllComments.setVisibility(View.VISIBLE);
                } else {
                    photoViewHolder.tvViewAllComments.setVisibility(View.GONE);
                }

                // Set last 2 comments
                if (photo.comment1 != null) {
                    photoViewHolder.tvComment1.setText(Html.fromHtml("<font color='#3f729b'><b>" + photo.user1 + "</b></font> " + photo.comment1));
                    photoViewHolder.tvComment1.setVisibility(View.VISIBLE);
                } else {
                    photoViewHolder.tvComment1.setVisibility(View.GONE);
                }

                if (photo.comment2 != null) {
                    photoViewHolder.tvComment2.setText(Html.fromHtml("<font color='#3f729b'><b>" + photo.user2 + "</b></font> " + photo.comment2));
                    photoViewHolder.tvComment2.setVisibility(View.VISIBLE);
                } else {
                    photoViewHolder.tvComment2.setVisibility(View.GONE);
                }

                // use device width for photo height
                DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
                photoViewHolder.imgPhoto.getLayoutParams().height = displayMetrics.widthPixels;

                // Reset the images from the recycled view
                photoViewHolder.imgProfile.setImageResource(0);
                photoViewHolder.imgPhoto.setImageResource(0);

                // Ask for the photo to be added to the imageview based on the photo url
                // Background: Send a network request to the url, download the image bytes, convert into bitmap, insert bitmap into the imageview
                Picasso.with(mContext).load(photo.profileUrl).into(photoViewHolder.imgProfile);
                Picasso.with(mContext).load(photo.imageUrl).placeholder(R.drawable.instagram_glyph_on_white).into(photoViewHolder.imgPhoto);

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
    }
    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgProfile;
        public ImageView imgPhoto;
        public TextView tvUsername;
        public TextView tvTime;
        public TextView tvLikes;
        public TextView tvCaption;
        public TextView tvViewAllComments;
        public TextView tvComment1;
        public TextView tvComment2;

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

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }
}
