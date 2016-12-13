package com.thermatk.android.princessviewer.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler;
import com.klinker.android.link_builder.Link;
import com.thermatk.android.princessviewer.R;
import com.thermatk.android.princessviewer.controllers.HashTagListController;
import com.thermatk.android.princessviewer.controllers.PhotosListController;

import java.util.regex.Pattern;

public class TextViewLinks {

    public static Link setupLinkAuthor(String name, Context ctx, Link.OnClickListener clk) {
        Link link = new Link(name)
                .setTextColor(ContextCompat.getColor(ctx, R.color.instagram_bold_font))                  // optional, defaults to holo blue
                .setHighlightAlpha(.4f)                                     // optional, defaults to .15f
                .setUnderlined(false)                                       // optional, defaults to true
                .setBold(true)                                              // optional, defaults to false
                .setOnClickListener(clk);
        return link;
    }

    public static Link setupLinkHashtags(Context ctx, Link.OnClickListener clk) {
        Link link = new Link(Pattern.compile("(#\\w+)"))
                .setTextColor(ContextCompat.getColor(ctx, R.color.instagram_bold_font))
                .setUnderlined(false)
                .setBold(false)
                .setOnClickListener(clk);
        return link;
    }

    public static Link setupLinkMentions(Context ctx, Link.OnClickListener clk) {
        Link link = new Link(Pattern.compile("(@\\w+)"))
                .setTextColor(ContextCompat.getColor(ctx, R.color.instagram_bold_font))
                .setUnderlined(false)
                .setBold(false)
                .setOnClickListener(clk);
        return link;
    }
}
