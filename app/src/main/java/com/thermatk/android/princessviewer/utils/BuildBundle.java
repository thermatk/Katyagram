package com.thermatk.android.princessviewer.utils;

import android.os.Bundle;

public class BuildBundle {
    public static Bundle createBundleWithString(String key, String string) {
        Bundle b = new Bundle();
        b.putString(key, string);
        return b;
    }
}
