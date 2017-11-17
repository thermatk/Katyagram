package com.thermatk.android.instaviewer.persist;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

public class PreferenceHelper {
    private static final String FOLLOW_USERS_PREF = "followUsers";

    public static FollowUser[] readFollowUsers(Context appContext) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(appContext);
        String pref = sharedPrefs.getString(FOLLOW_USERS_PREF, null);
        FollowUser[] users;
        if (pref != null) {
            Gson gson = new Gson();
            users = gson.fromJson(pref, FollowUser[].class);
        } else {
            users = null;
        }
        return users;
    }

    public static void writeFollowUsers(Context appContext, FollowUser[] users) {
        if (users != null) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(appContext);
            SharedPreferences.Editor editor = sharedPrefs.edit();
            Gson gson = new Gson();
            String pref = gson.toJson(users);
            editor.putString(FOLLOW_USERS_PREF,pref);
            editor.apply();
        }
    }
}
