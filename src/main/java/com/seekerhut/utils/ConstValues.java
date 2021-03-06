package com.seekerhut.utils;

import java.util.HashMap;

public class ConstValues {
    public static final int millisecondsInAnHour = 3600 * 1000;

    public static class RedisKeys {
        public static String PrimaryKeyIdPrefix = "PrimaryKeyId:";
        public static String likedTweetPrefix = "likedTweet:";
        public static String tweetLikesPrefix = "tweetLikes:";
        public static String followerNumPrefix = "followerNum:";
        public static String followingSetPrefix = "followingSet:";
        public static String outBoxPrefix = "outBox:";
        public static String tweetCache = "tweetCache";
    }
}
