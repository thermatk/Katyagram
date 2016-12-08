package com.thermatk.android.princessviewer.data;

public class InstagramPhoto {
    public String username;
    public String caption;
    public String createdTime;
    public String imageUrl;
    public String profileUrl;
    public String comment1;
    public String user1;
    public String comment2;
    public String user2;
    public String id;
    public int imageHeight;
    public int likesCount;
    public int commentsCount;

    public String getRelativeTime() {
        long ct = Long.parseLong(createdTime);
        long now = System.currentTimeMillis() / 1000;
        long elapsedSeconds = now - ct;

        if (elapsedSeconds < 60) { // less than a minute
            return String.format("%.0fс", elapsedSeconds);
        } else if (elapsedSeconds < 3600) { // less than an hour
            return String.format("%.0fм", Math.floor(elapsedSeconds / 60));
        } else if (elapsedSeconds < 86400) { // less than a day
            return String.format("%.0fч", Math.floor(elapsedSeconds / 3600));
        } else {
            return String.format("%.0fд", Math.floor(elapsedSeconds / 86400));
        }
    }
}
