package com.seekerhut.model.mysqlModel;

import java.io.Serializable;

/**
 * tweet
 * @author 
 */
public class Tweet implements Serializable {
    private Long tweetId;

    private String tweetContent;

    private Long tweetAuthor;

    private Long tweetTime;

    private Boolean tweetIsdeleted;

    private Boolean tweetIshidden;

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

    public Boolean getTweetIshidden() {
        return tweetIshidden;
    }

    public void setTweetIshidden(Boolean tweetIshidden) {
        this.tweetIshidden = tweetIshidden;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Tweet other = (Tweet) that;
        return (this.getTweetId() == null ? other.getTweetId() == null : this.getTweetId().equals(other.getTweetId()))
            && (this.getTweetContent() == null ? other.getTweetContent() == null : this.getTweetContent().equals(other.getTweetContent()))
            && (this.getTweetAuthor() == null ? other.getTweetAuthor() == null : this.getTweetAuthor().equals(other.getTweetAuthor()))
            && (this.getTweetTime() == null ? other.getTweetTime() == null : this.getTweetTime().equals(other.getTweetTime()))
            && (this.getTweetIsdeleted() == null ? other.getTweetIsdeleted() == null : this.getTweetIsdeleted().equals(other.getTweetIsdeleted()))
            && (this.getTweetIshidden() == null ? other.getTweetIshidden() == null : this.getTweetIshidden().equals(other.getTweetIshidden()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getTweetId() == null) ? 0 : getTweetId().hashCode());
        result = prime * result + ((getTweetContent() == null) ? 0 : getTweetContent().hashCode());
        result = prime * result + ((getTweetAuthor() == null) ? 0 : getTweetAuthor().hashCode());
        result = prime * result + ((getTweetTime() == null) ? 0 : getTweetTime().hashCode());
        result = prime * result + ((getTweetIsdeleted() == null) ? 0 : getTweetIsdeleted().hashCode());
        result = prime * result + ((getTweetIshidden() == null) ? 0 : getTweetIshidden().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", tweetId=").append(tweetId);
        sb.append(", tweetContent=").append(tweetContent);
        sb.append(", tweetAuthor=").append(tweetAuthor);
        sb.append(", tweetTime=").append(tweetTime);
        sb.append(", tweetIsdeleted=").append(tweetIsdeleted);
        sb.append(", tweetIshidden=").append(tweetIshidden);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}