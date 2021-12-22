package com.seekerhut.model.mysqlModel;

import java.io.Serializable;
import java.util.Date;

/**
 * tweet_post
 * @author 
 */
public class TweetPost implements Serializable {
    private Long tweetId;

    private String tweetContent;

    private Long tweetAuthor;

    private Long tweetTime;

    private Boolean tweetIsdeleted;

    private Boolean tweetIsHidden;

    private static final long serialVersionUID = 1L;

    public Long getTweetId() {
        return tweetId;
    }

    public void setTweetId(Long tweetId) {
        this.tweetId = tweetId;
    }

    public String getTweetContent() {
        return tweetContent;
    }

    public void setTweetContent(String tweetContent) {
        this.tweetContent = tweetContent;
    }

    public Long getTweetAuthor() {
        return tweetAuthor;
    }

    public void setTweetAuthor(Long tweetAuthor) {
        this.tweetAuthor = tweetAuthor;
    }

    public Long getTweetTime() {
        return tweetTime;
    }

    public void setTweetTime(Long tweetTime) {
        this.tweetTime = tweetTime;
    }

    public Boolean getTweetIsdeleted() {
        return tweetIsdeleted;
    }

    public void setTweetIsdeleted(Boolean tweetIsdeleted) {
        this.tweetIsdeleted = tweetIsdeleted;
    }

    public Boolean getTweetIsHidden() {
        return tweetIsHidden;
    }

    public void setTweetIsHidden(Boolean tweetIsHidden) {
        this.tweetIsHidden = tweetIsHidden;
    }
}