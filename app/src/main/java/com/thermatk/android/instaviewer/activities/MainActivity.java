package com.thermatk.android.instaviewer.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bluelinelabs.conductor.Conductor;
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
    @BindView(R.id.controller_container) ViewGroup container;
    @BindView(R.id.fab) FloatingActionButton fab;

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

    }

    @OnClick(R.id.fab)
    public void fabClicked() {
        Toast.makeText(this,"Clicked FAB, test", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed();
        }
    }
}
