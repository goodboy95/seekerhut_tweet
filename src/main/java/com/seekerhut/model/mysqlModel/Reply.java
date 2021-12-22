package com.seekerhut.model.mysqlModel;

import java.io.Serializable;

/**
 * reply
 * @author 
 */
public class Reply implements Serializable {
    private Long replyId;

    private Long replyTweetid;

    private Long replyTime;

    private String replyContent;

    private Boolean replyIsdeleted;

    private static final long serialVersionUID = 1L;

    public Long getReplyId() {
        return replyId;
    }

    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }

    public Long getReplyTweetid() {
        return replyTweetid;
    }

    public void setReplyTweetid(Long replyTweetid) {
        this.replyTweetid = replyTweetid;
    }

    public Long getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(Long replyTime) {
        this.replyTime = replyTime;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public Boolean getReplyIsdeleted() {
        return replyIsdeleted;
    }

    public void setReplyIsdeleted(Boolean replyIsdeleted) {
        this.replyIsdeleted = replyIsdeleted;
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
        Reply other = (Reply) that;
        return (this.getReplyId() == null ? other.getReplyId() == null : this.getReplyId().equals(other.getReplyId()))
            && (this.getReplyTweetid() == null ? other.getReplyTweetid() == null : this.getReplyTweetid().equals(other.getReplyTweetid()))
            && (this.getReplyTime() == null ? other.getReplyTime() == null : this.getReplyTime().equals(other.getReplyTime()))
            && (this.getReplyContent() == null ? other.getReplyContent() == null : this.getReplyContent().equals(other.getReplyContent()))
            && (this.getReplyIsdeleted() == null ? other.getReplyIsdeleted() == null : this.getReplyIsdeleted().equals(other.getReplyIsdeleted()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getReplyId() == null) ? 0 : getReplyId().hashCode());
        result = prime * result + ((getReplyTweetid() == null) ? 0 : getReplyTweetid().hashCode());
        result = prime * result + ((getReplyTime() == null) ? 0 : getReplyTime().hashCode());
        result = prime * result + ((getReplyContent() == null) ? 0 : getReplyContent().hashCode());
        result = prime * result + ((getReplyIsdeleted() == null) ? 0 : getReplyIsdeleted().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", replyId=").append(replyId);
        sb.append(", replyTweetid=").append(replyTweetid);
        sb.append(", replyTime=").append(replyTime);
        sb.append(", replyContent=").append(replyContent);
        sb.append(", replyIsdeleted=").append(replyIsdeleted);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}