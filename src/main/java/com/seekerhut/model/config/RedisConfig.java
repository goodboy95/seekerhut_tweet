package com.seekerhut.model.config;

import java.util.HashSet;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.redis")
public class RedisConfig {
    private String host;
    private Integer port;
    private String password;
    private Integer database;
    private String prefix;
    private Sentinel sentinel;
    private Jedis jedis;
    private Integer timeout;

    public RedisConfig() {
        host = "127.0.0.1";
        port = 6379;
        password = "";
        database = 0;
        prefix = "";
        timeout = 10000;
        jedis = new Jedis();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getDatabase() {
        return database;
    }

    public void setDatabase(Integer database) {
        this.database = database;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Sentinel getSentinel() {
        return sentinel;
    }

    public void setSentinel(Sentinel sentinel) {
        this.sentinel = sentinel;
    }

    public Jedis getJedis() {
        return jedis;
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public static class Sentinel {
        private String master;
        private HashSet<String> nodes;

        public String getMaster() {
            return master;
        }

        public void setMaster(String master) {
            this.master = master;
        }

        public HashSet<String> getNodes() {
            return nodes;
        }

        public void setNodes(HashSet<String> nodes) {
            this.nodes = nodes;
        }
    }

    public class Jedis {
        private Pool pool;

        public Jedis() {
            pool = new Pool();
        }

        public Pool getPool() {
            return pool;
        }

        public void setPool(Pool pool) {
            this.pool = pool;
        }

        public class Pool {
            private Integer maxActive;
            private Integer maxIdle;
            private Integer maxWait;
            private Integer minIdle;

            public Pool() {
                maxActive = 100;
                maxIdle = 10;
                maxWait = 1000;
                minIdle = 1;
            }
    
            public Integer getMaxActive() {
                return maxActive;
            }
    
            public void setMaxActive(Integer maxActive) {
                this.maxActive = maxActive;
            }
    
            public Integer getMaxIdle() {
                return maxIdle;
            }
    
            public void setMaxIdle(Integer maxIdle) {
                this.maxIdle = maxIdle;
            }
    
            public Integer getMaxWait() {
                return maxWait;
            }
    
            public void setMaxWait(Integer maxWait) {
                this.maxWait = maxWait;
            }
    
            public Integer getMinIdle() {
                return minIdle;
            }
    
            public void setMinIdle(Integer minIdle) {
                this.minIdle = minIdle;
            }
        }
    }
}


