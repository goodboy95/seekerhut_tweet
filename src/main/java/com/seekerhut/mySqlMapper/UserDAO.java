package com.seekerhut.mySqlMapper;

import com.seekerhut.model.mysqlModel.User;
import org.springframework.stereotype.Repository;

/**
 * UserDAO继承基类
 */
@Repository
public interface UserDAO extends MyBatisBaseDao<User, Long> {
}