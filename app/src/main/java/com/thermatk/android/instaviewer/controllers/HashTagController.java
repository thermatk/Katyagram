package com.thermatk.android.instaviewer.controllers;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.ControllerChangeHandler;
import com.bluelinelabs.conductor.ControllerChangeType;
// https://github.com/bluelinelabs/Conductor/commit/a9bdf0dd06b5f2e0fd6a980cb0e86eb737cef457#diff-1219f1e886a4472258ed9221b8ccf78a
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.support.RouterPagerAdapter;
import com.thermatk.android.instaviewer.R;

import static com.thermatk.android.instaviewer.utils.BuildBundle.createBundleWithString;

public class HashTagController extends Controller {
    private String tag;
    private final static String BUNDLE_KEY = "tag";
    private ViewPager viewPager;
    private final RouterPagerAdapter pagerAdapter;

    public HashTagController(@Nullable Bundle args) {
        super(args);
        pagerAdapter = new RouterPagerAdapter(this) {
            @Override
            public void configureRouter(@NonNull Router router, int position) {
                if (!router.hasRootController()) {
                    Controller controller;

                    switch (position) {
                        case 0:
                            controller = new HashTagListTopChildController(tag);
                            break;
                        default:
                        case 1:
                            controller = new HashTagListLastChildController(tag);
                            break;
                    }
                    router.setRoot(RouterTransaction.with(controller));
                }
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    default:
                    case 0:
                        return "Top";
                    case 1:
                        return "Last";
                }
            }
        };
    }

    public HashTagController(String tag) {
        this(createBundleWithString(BUNDLE_KEY,tag));
    }

    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = inflater.inflate(R.layout.controller_hashtag_pager, container, false);
        tag = getArgs().getString(BUNDLE_KEY);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    @Override
    protected void onDestroyView(@NonNull View view) {
        viewPager.setAdapter(null);
        super.onDestroyView(view);
    }

    @Override
    protected void onChangeStarted(@NonNull ControllerChangeHandler changeHandler, @NonNull ControllerChangeType changeType) {
        setOptionsMenuHidden(!changeType.isEnter);

        if (changeType.isEnter) {
            Log.d("katyagram", "first opening");
        }
    }

}