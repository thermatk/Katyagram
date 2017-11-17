package com.thermatk.android.instaviewer.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;

import com.mikepenz.materialdrawer.DrawerBuilder;
import com.thermatk.android.instaviewer.R;
import com.thermatk.android.instaviewer.controllers.EntryController;
import com.thermatk.android.instaviewer.persist.FollowUser;

import static com.thermatk.android.instaviewer.persist.PreferenceHelper.readFollowUsers;


public class MainActivity extends AppCompatActivity {

    private Router router;
    public ViewGroup container;
    public FollowUser[] users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = findViewById(R.id.controller_container);

        router = Conductor.attachRouter(this, container, savedInstanceState);

        users = readFollowUsers(getApplicationContext());
        if(users != null) {
            Log.d("katyagram", "Loaded followusers: " + users.length);
        }


        new DrawerBuilder().withActivity(this).build();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
            }
        });
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(new EntryController("")));
        }

    }

    @Override
    public void onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed();
        }
    }
}
