package com.thermatk.android.princessviewer.controllers;

import android.content.Context;
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
import com.bluelinelabs.conductor.support.ControllerPagerAdapter;
import com.thermatk.android.princessviewer.R;

import static com.thermatk.android.princessviewer.utils.BuildBundle.createBundleWithString;

public class HashTagController extends Controller {
    private String tag;
    private final static String BUNDLE_KEY = "tag";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private final ControllerPagerAdapter pagerAdapter;

    public HashTagController(@Nullable Bundle args) {
        super(args);
        pagerAdapter = new ControllerPagerAdapter(this, false) {
            @Override
            public Controller getItem(int position) {
                return new HashTagListController(tag);
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return "Page " + position;
            }
        };
    }

    public HashTagController(String tag) {
        this(createBundleWithString(BUNDLE_KEY,tag));
    }

    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = inflater.inflate(R.layout.controller_hashtag_pager, container, false);
        Context ctx = view.getContext();
        tag = getArgs().getString(BUNDLE_KEY);

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
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