package com.thermatk.android.instaviewer.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.squareup.picasso.Picasso;
import com.thermatk.android.instaviewer.R;
import com.thermatk.android.instaviewer.controllers.HashTagController;
import com.thermatk.android.instaviewer.controllers.PhotoController;
import com.thermatk.android.instaviewer.controllers.PhotosListController;
import com.thermatk.android.instaviewer.controllers.VideoPlayController;
import com.thermatk.android.instaviewer.data.InstagramPhoto;
import com.thermatk.android.instaviewer.interfaces.ILoadMore;

import static com.thermatk.android.instaviewer.utils.TextViewLinks.setupLinkAuthor;
import static com.thermatk.android.instaviewer.utils.TextViewLinks.setupLinkHashtags;
import static com.thermatk.android.instaviewer.utils.TextViewLinks.setupLinkMentions;

/**
 * Created by thermatk on 17.11.17.
 */
/*
class PhotosListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private ILoadMore mOnLoadMoreListener;

    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private final LayoutInflater inflater;

    public PhotosListAdapter(LayoutInflater inflater) {
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
            final InstagramPhoto photo = photos.get(position);

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
            // show overlay if a video
            if (photo.isVideo == true) {
                photoViewHolder.imgPhotoPlay.setVisibility(View.VISIBLE);
            } else {
                photoViewHolder.imgPhotoPlay.setVisibility(View.GONE);
            }
            Picasso.with(ctx).load(photo.imageUrl).placeholder(R.drawable.instagram_glyph_on_white).into(photoViewHolder.imgPhoto);
            photoViewHolder.imgPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // retain in case it's a one-off thing
                    // TODO: better retain handling
                    setRetainViewMode(Controller.RetainViewMode.RETAIN_DETACH);
                    if (photo.isVideo) {
                        // TODO: fix and rewrite video
                        getRouter().pushController(
                                RouterTransaction.with(new VideoPlayController(photo.videoUrl))
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

        } else if (holder instanceof PhotosListController.PhotosAdapter.LoadingViewHolder) {
            PhotosListController.PhotosAdapter.LoadingViewHolder loadingViewHolder = (PhotosListController.PhotosAdapter.LoadingViewHolder) holder;
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

        public ImageView imgPhotoPlay;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            // Lookup the subview within the template
            imgProfile = itemView.findViewById(R.id.imgProfile);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            tvCaption = itemView.findViewById(R.id.tvCaption);
            tvViewAllComments = itemView.findViewById(R.id.tvViewAllComments);
            tvComment1 = itemView.findViewById(R.id.tvComment1);
            tvComment2 = itemView.findViewById(R.id.tvComment2);

            imgPhotoPlay = itemView.findViewById(R.id.imgPhotoPlay);
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
*/