package com.seekerhut.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.seekerhut.model.config.RedisConfig;

import org.springframework.boot.context.properties.EnableConfigurationProperties;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolAbstract;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.Response;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.params.SetParams;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@EnableConfigurationProperties(RedisConfig.class)
public class JedisHelper {
    private static JedisPoolAbstract jedisPool;
    private static JedisPoolConfig jedisPoolConfig;
    private static String prefix;

    public static void init(RedisConfig redisConfig) {
        // jedis pool configuration
        jedisPoolConfig = new JedisPoolConfig();
        var jedisConfigData = redisConfig.getJedis().getPool();
        int maxIdle = jedisConfigData.getMaxIdle();
        int minIdle = jedisConfigData.getMinIdle();
        int maxTotal = jedisConfigData.getMaxActive();
        int maxWaitTime = jedisConfigData.getMaxWait();
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitTime);

        // redis common-data configuration
        String password = redisConfig.getPassword();
        int database = redisConfig.getDatabase();
        int timeout = redisConfig.getTimeout();
        prefix = redisConfig.getPrefix() + ":";

        // redis single-node or cluster(with sentinels) configuration
        var sentinelData = redisConfig.getSentinel();
        if (sentinelData == null) {
            String host = redisConfig.getHost();
            int port = redisConfig.getPort();
            jedisPool = password.equals("")
                ? new JedisPool(jedisPoolConfig, host, port, timeout, null, database)
                : new JedisPool(jedisPoolConfig, host, port, timeout, password, database);
        } else {
            var masterName = redisConfig.getSentinel().getMaster();
            var sentinelNodes = redisConfig.getSentinel().getNodes();
            jedisPool = password.equals("")
                ? new JedisSentinelPool(masterName, sentinelNodes, jedisPoolConfig, timeout, password, database)
                : new JedisSentinelPool(masterName, sentinelNodes, jedisPoolConfig, timeout, password, database);
        }
    }

    public static void set(String key, Object val) {
        set(key, val, 0);
    }

    public static void set(String key, Object val, int seconds) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        SetParams params = SetParams.setParams().ex(seconds);
        jedis.set(key, val.toString(), params);
        jedis.close();
    }

    public static void setnx(String key, Object val) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        jedis.setnx(key, val.toString());
        jedis.close();
    }

    public static void expire(String key, int seconds) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        jedis.expire(key, seconds);
        jedis.close();
    }

    public static <T> T get(String key, Class<T> clazz) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        String result = jedis.get(key);
        jedis.close();
        return result == null ? null : JSONObject.parseObject(result, clazz);
    }

    public static boolean exists(String key) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        boolean result = jedis.exists(key);
        jedis.close();
        return result;
    }

    public static void hset(String key, Object hashKey, Object val) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        jedis.hset(key, hashKey.toString(), val.toString());
        jedis.close();
    }

    public static void hset(String key, Object hashKey, Object val, int seconds) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        jedis.hset(key, hashKey.toString(), val.toString());
        jedis.close();
    }

    public static <T> T hget(String key, Object hashKey, Class<T> clazz) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        String result = jedis.hget(key, hashKey.toString());
        jedis.close();
        return JSONObject.parseObject(result, clazz);
    }

    public static <T, U extends Serializable> List<U> hmget(String key, List<T> hashKey, Class<U> clazz) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        String[] hashKeyStr = hashKey.toArray(new String[0]);
        List<U> result = jedis.hmget(key, hashKeyStr).stream().map(d -> JSONObject.parseObject(d, clazz)).collect(Collectors.toList());
        jedis.close();
        return result;
    }

    public static boolean hexists(String key, String hashKey) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        boolean result = jedis.hexists(key, hashKey);
        jedis.close();
        return result;
    }

    public static long incr(String key) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        long result = jedis.incr(key);
        jedis.close();
        return result;
    }

    public static long incrBy(String key, int num) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        long result = jedis.incrBy(key, num);
        jedis.close();
        return result;
    }

    public static long decr(String key) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        long result = jedis.decr(key);
        jedis.close();
        return result;
    }

    public static long decrBy(String key, int num) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        long result = jedis.decrBy(key, num);
        jedis.close();
        return result;
    }

    public static void rpush(String key, Object data) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        jedis.rpush(key, data.toString());
        jedis.close();
    }

    public static boolean sadd(String key, Object data) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        var result = jedis.sadd(key, data.toString());
        jedis.close();
        return result == 1;
    }

    public static boolean srem(String key, Object data) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        var result = jedis.srem(key, data.toString());
        jedis.close();
        return result == 1;
    }

    public static Set<String> smember(String key) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        var result = jedis.smembers(key);
        jedis.close();
        return result;
    }

    public static long scard(String key) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        var result = jedis.scard(key);
        jedis.close();
        return result;
    }

    public static Set<String> bulk_smember(List<?> keys) {
        Jedis jedis = jedisPool.getResource();
        var ppl = jedis.pipelined();
        for (Object key : keys) {
            ppl.smembers(key.toString());
        }
        var rawResult = ppl.syncAndReturnAll();
        var result = rawResult.stream().flatMap(obj -> obj == null ? new HashSet<String>().stream() : ((HashSet<String>)obj).stream())
            .collect(Collectors.toSet());
        return result;
    }

    // public static void zadd(String key, Object data) {
    //     key = prefix + key;
    //     Jedis jedis = jedisPool.getResource();
    //     jedis.zadd(key,  data.toString());
    //     jedis.close();
    // }
}
