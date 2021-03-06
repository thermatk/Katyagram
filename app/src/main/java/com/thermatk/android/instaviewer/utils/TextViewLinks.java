package com.thermatk.android.instaviewer.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.klinker.android.link_builder.Link;
import com.thermatk.android.instaviewer.R;

import java.util.regex.Pattern;

public class TextViewLinks {

    public static Link setupLinkAuthor(String name, Context ctx, Link.OnClickListener clk) {
        return new Link(name)
                .setTextColor(ContextCompat.getColor(ctx, R.color.instagram_bold_font))                  // optional, defaults to holo blue
                .setHighlightAlpha(.4f)                                     // optional, defaults to .15f
                .setUnderlined(false)                                       // optional, defaults to true
                .setBold(true)                                              // optional, defaults to false
                .setOnClickListener(clk);
    }

    public static Link setupLinkHashtags(Context ctx, Link.OnClickListener clk) {
        // return new Link(Pattern.compile("#([^#\\s@]*[^.\\s])+"))
        return new Link(Pattern.compile("(#\\w+)"))
                .setTextColor(ContextCompat.getColor(ctx, R.color.instagram_bold_font))
                .setUnderlined(false)
                .setBold(false)
                .setOnClickListener(clk);
    }
    // http://blog.jstassen.com/2016/03/code-regex-for-instagram-username-and-hashtags/
    public static Link setupLinkMentions(Context ctx, Link.OnClickListener clk) {
        return new Link(Pattern.compile("(?:@)([A-Za-z0-9_](?:(?:[A-Za-z0-9_]|(?:\\.(?!\\.))){0,28}(?:[A-Za-z0-9_]))?)"))
                .setTextColor(ContextCompat.getColor(ctx, R.color.instagram_bold_font))
                .setUnderlined(false)
                .setBold(false)
                .setOnClickListener(clk);
    }
}
