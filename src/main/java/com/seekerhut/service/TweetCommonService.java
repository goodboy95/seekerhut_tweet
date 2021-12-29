package com.seekerhut.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.seekerhut.model.mysqlModel.Tweet;
import com.seekerhut.mySqlMapper.TweetDAO;
import com.seekerhut.utils.CommonFunctions;
import com.seekerhut.utils.ConstValues.RedisKeys;
import com.seekerhut.utils.JedisHelper;

import org.springframework.stereotype.Service;

@Service("tweetCommonService")
public class TweetCommonService {
    @Resource
    private TweetDAO tweetMapper;

    /**
     * 拉取时间线
     * @param userId 用户id
     * @param timelineId 时间线id（传0代表重新生成时间线）
     * @param prevTimeLabel 当前时间线，浏览过的最后一条文章的时间标签
     * @param size 每次拉取的数量
     * @return
     */
    public List<Tweet> getTimeline(long userId, long timelineId, long prevTimeLabel, int size) {
        List<String> friendOutBoxKeys = JedisHelper.smember(RedisKeys.followingSetPrefix + userId)
            .stream().map(idStr -> RedisKeys.outBoxPrefix + idStr).collect(Collectors.toList());
        var allTweetIds = JedisHelper.bulk_smember(friendOutBoxKeys).stream().map(idStr -> Long.parseLong(idStr))
            .sorted().collect(Collectors.toList());
        var displayTweetIds = allTweetIds.stream().filter(id -> id < prevTimeLabel).limit(size).collect(Collectors.toList());

        List<Tweet> redisResult = JedisHelper.hmget(RedisKeys.tweetCache, displayTweetIds, Tweet.class);
        displayTweetIds.removeAll(redisResult.stream().map(t -> t.getTweetId()).collect(Collectors.toList()));
        List<Tweet> mysqlResult = tweetMapper.getTweetByIds(displayTweetIds);
        redisResult.addAll(mysqlResult);
        return redisResult;
    }

    public List<Tweet> getTweetByPerson(long userId, long targetUserId, int pageIndex, int pageSize) {
        return null;
    }

    public boolean sendTweet(long userId, String content, String tagIds) {
        var masterId = CommonFunctions.generatePrimaryKeyId("tweet");
        var curTime = LocalDateTime.now();
        var tweet = new Tweet() {
            {
                setTweetId(masterId);
                setTweetContent(content);
                setTweetAuthor(userId);
                setTweetTime(curTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli());
                setTweetIsdeleted(false);
                setTweetIshidden(false);
            }
        };
        var ser = JSONObject.toJSONString(tweet);
        JedisHelper.hset(RedisKeys.tweetCache, masterId, ser);
        var outboxKey = RedisKeys.outBoxPrefix + curTime.getMonthValue() + ":" + userId;
        var outboxKeyExists = JedisHelper.exists(outboxKey);
        JedisHelper.rpush(outboxKey, masterId);
        if (!outboxKeyExists)
        {
            JedisHelper.expire(outboxKey, 2 * 31 * 86400);
        }
        return true;
    }

    public boolean likeOrUnlikeTweet(long userId, long tweetId, boolean isLike) {
        if (isLike && JedisHelper.sadd(RedisKeys.likedTweetPrefix + userId, tweetId)) {
            JedisHelper.incr(RedisKeys.tweetLikesPrefix + tweetId);
            return true;
        } else if (!isLike && JedisHelper.srem(RedisKeys.likedTweetPrefix + userId, tweetId)) {
            JedisHelper.decr(RedisKeys.tweetLikesPrefix + tweetId);
            return true;
        } else {
            return false;
        }
    }

    public int tweetLikes(long tweetId) {
        Integer res = JedisHelper.get(RedisKeys.tweetLikesPrefix + tweetId, Integer.class);
        return res == null ? 0 : res;
    }

    public List<Integer> tweetLikeList(long tweetId, int pageSize, int pageIndex) {
        return new ArrayList<>();
    }

    public boolean followOrUnfollow(long userId, long targetUserId, boolean isFollow) {
        if (isFollow && JedisHelper.sadd(RedisKeys.followingSetPrefix + userId, targetUserId)) {
            JedisHelper.incr(RedisKeys.followerNumPrefix + targetUserId);
            // MQ msg insert into MySQL
            return true;
        } else if (!isFollow && JedisHelper.srem(RedisKeys.followingSetPrefix + userId, targetUserId)) {
            JedisHelper.decr(RedisKeys.followerNumPrefix + targetUserId);
            // MQ msg remove from MySQL
            return true;
        } else {
            return false;
        }
    }

    // 关注的人数
    public long followingNum(long userId) {
        return JedisHelper.scard(RedisKeys.followingSetPrefix + userId);
    }

    // 粉丝人数
    public long fansNum(long userId) {
        return JedisHelper.get(RedisKeys.followerNumPrefix + userId, Long.class);
    }

    public List<Integer> followingList(long userId, int pageSize, int pageIndex) {
        return new ArrayList<>();
    }

    public List<Integer> fansList(long userId, int pageSize, int pageIndex) {
        // query from MySQL
        return new ArrayList<>();
    }
}
