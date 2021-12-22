package com.seekerhut.mySqlMapper;

import com.seekerhut.model.mysqlModel.Relation;
import org.springframework.stereotype.Repository;

/**
 * RelationDAO继承基类
 */
@Repository
public interface RelationDAO extends MyBatisBaseDao<Relation, Long> {
}