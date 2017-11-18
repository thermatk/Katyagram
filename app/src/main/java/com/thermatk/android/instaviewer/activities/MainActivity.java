package com.thermatk.android.instaviewer.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.ControllerChangeHandler;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;

import com.mikepenz.materialdrawer.DrawerBuilder;
import com.thermatk.android.instaviewer.R;
import com.thermatk.android.instaviewer.controllers.EntryController;
import com.thermatk.android.instaviewer.data.remote.ApiClient;
import com.thermatk.android.instaviewer.data.remote.InstaApiService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private Router router;
    @BindView(R.id.controller_container)
    ViewGroup container;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.fabMini)
    FloatingActionButton fabUp;

    public InstaApiService instaApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        instaApiService = ApiClient.getAPIService();

        router = Conductor.attachRouter(this, container, savedInstanceState);

        new DrawerBuilder().withActivity(this).build();

        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(new EntryController("")));
        }

        router.addChangeListener(new ControllerChangeHandler.ControllerChangeListener() {
            @Override
            public void onChangeStarted(@Nullable Controller to, @Nullable Controller from, boolean isPush, @NonNull ViewGroup container, @NonNull ControllerChangeHandler handler) {
                fabUp.hide();
                fabUp.setOnClickListener(null);
            }

            @Override
            public void onChangeCompleted(@Nullable Controller to, @Nullable Controller from, boolean isPush, @NonNull ViewGroup container, @NonNull ControllerChangeHandler handler) {

            }
        });
    }

    public void fabUpBindRecyclerView(final RecyclerView recyclerView) {
        showFabWhenScrolled(recyclerView);

        fabUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerView != null) {
                    recyclerView.smoothScrollToPosition(0);
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && fabUp.isShown()) {
                    fabUp.hide();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

                    showFabWhenScrolled(recyclerView);
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    private void showFabWhenScrolled(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        if (layoutManager instanceof LinearLayoutManager) {
            if (((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition() > 0) {
                fabUp.show();
            }
        } else if (layoutManager instanceof GridLayoutManager) {
            if (((GridLayoutManager) layoutManager).findFirstVisibleItemPosition() > 0) {
                fabUp.show();
            }
        }
    }

    public void fabUpBindNestedScrollView(final NestedScrollView nestedScrollView) {
        fabUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nestedScrollView != null) {
                    nestedScrollView.fullScroll(View.FOCUS_UP);
                    nestedScrollView.smoothScrollTo(0,0);
                }
            }
        });

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == 0) {
                    fabUp.hide();
                } else if (scrollY > oldScrollY && !fabUp.isShown()) {
                    fabUp.show();
                }
            }
        });
    }

    @OnClick(R.id.fab)
    public void fabClicked() {
        Toast.makeText(this, "Clicked FAB, test", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed();
        }
    }
}
