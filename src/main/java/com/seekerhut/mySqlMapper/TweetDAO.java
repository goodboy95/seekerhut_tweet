package com.seekerhut.mySqlMapper;

import com.seekerhut.model.mysqlModel.Tweet;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TweetDAO继承基类
 */
@Repository
public interface TweetDAO extends MyBatisBaseDao<Tweet, Long> {
    public List<Tweet> getTweetByPerson(@Param("tweetAuthor") Long tweetAuthor, @Param("skip") int skip, @Param("take") int take);

    public List<Tweet> getTweetByIds(@Param("tweetIds") List<Long> tweetIds);

    public Long getPersonTweetNum(@Param("tweetAuthor") Long tweetAuthor);
}