package com.seekerhut.mySqlMapper;

import com.seekerhut.model.mysqlModel.Reply;
import org.springframework.stereotype.Repository;

/**
 * ReplyDAO继承基类
 */
@Repository
public interface ReplyDAO extends MyBatisBaseDao<Reply, Long> {
}