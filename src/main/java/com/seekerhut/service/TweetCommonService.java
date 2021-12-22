package com.seekerhut.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.seekerhut.model.mysqlModel.Tweet;
import com.seekerhut.mySqlMapper.TweetDAO;

import com.seekerhut.utils.CommonFunctions;
import com.seekerhut.utils.ConstValues;
import com.seekerhut.utils.JedisHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException.NotImplemented;

@Service("tweetCommonService")
public class TweetCommonService {
    @Resource
    private TweetDAO tweetMapper;

    public List<Tweet> getTimeline(long userId, long prevTimeLabel) {
        var tweetIdList = new ArrayList<Long>();
        List<Long> friends = JedisHelper.smember("following:" + userId).stream().map(idStr -> Long.parseLong(idStr)).collect(Collectors.toList());
        
        //tweetIdList.add(1l);
        List<Tweet> redisResult = JedisHelper.hmget("tweet_cache", tweetIdList, Tweet.class);
        tweetIdList.removeAll(redisResult.stream().map(t -> t.getTweetId()).collect(Collectors.toList()));
        List<Tweet> mysqlResult = tweetMapper.getTweetByIds(tweetIdList);
        redisResult.addAll(mysqlResult);
        return redisResult;
    }

    public List<Tweet> getTweetByPerson(long userId, long targetUserId, int pageIndex, int pageSize) {
        return null;
    }

    public boolean sendTweet(long userId, String content) {
        var masterId = CommonFunctions.generatePrimaryKeyId("tweet");
        var tweet = new Tweet() {
            {
                setTweetId(masterId);
                setTweetContent(content);
                setTweetAuthor(userId);
                setTweetTime(new Date().getTime());
                setTweetIsdeleted(false);
                setTweetIshidden(false);
            }
        };
        var ser = JSONObject.toJSONString(tweet);
        JedisHelper.hset("tweet_cache", masterId, ser);
        JedisHelper.rpush("tweet_outbox", masterId);
        return true;
    }

    public boolean likeOrDislikeTweet(long userId, long tweetId, boolean isLike) {
        if (isLike && JedisHelper.sadd(ConstValues.RedisKeys.likedTweetPrefix + userId, tweetId)) {
            JedisHelper.incr(ConstValues.RedisKeys.tweetLikesPrefix + tweetId);
            return true;
        } else if (!isLike && JedisHelper.srem(ConstValues.RedisKeys.likedTweetPrefix + userId, tweetId)) {
            JedisHelper.decr(ConstValues.RedisKeys.tweetLikesPrefix + tweetId);
            return true;
        } else {
            return false;
        }
    }

    public int tweetLikes(long tweetId) {
        return 0;
    }

    public List<Integer> tweetLikeList(long tweetId, int pageSize, int pageIndex) {
        return new ArrayList<>();
    }

    public boolean follow(long userId, long targetUserId) {
        return false;
    }

    public boolean unfollow(long userId, long targetUserId) {
        return false;
    }

    // 关注的人数
    public int followingNum(long userId) {
        return 0;
    }

    // 粉丝人数
    public int fansNum(long userId) {
        return 0;
    }

    public List<Integer> followingList(long userId, int pageSize, int pageIndex) {
        return new ArrayList<>();
    }

    public List<Integer> fansList(long userId, int pageSize, int pageIndex) {
        return new ArrayList<>();
    }
}
