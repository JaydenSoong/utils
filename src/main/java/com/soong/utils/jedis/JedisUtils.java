package com.soong.utils.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

/**
 * Jedis 工具类，提供一个获取连接的方法和一个归还连接的方法，
 * 使用该工具类，需要提供一个配置文件名为 jedis.properties 的配置文件,
 * 在该配置文件中指定要连接的主机地址、端口号和其它的一些配置信息。
 */
public class JedisUtils {
    private static JedisPool jedisPool;

    static {
        InputStream is = JedisUtils.class.getClassLoader().getResourceAsStream("jedis.properties");
        Properties prop = new Properties();
        try {
            assert is != null;
            prop.load(is);
            Set<Object> keys = prop.keySet();
            JedisPoolConfig config = new JedisPoolConfig();
            // 检查主机地址与端口号信息
            if (!keys.contains("host")) {
                throw new RuntimeException("配置文件中没有指定主机地址。");
            }
            if (!keys.contains("port")) {
                throw new RuntimeException("配置文件中没有指定端口号。");
            }
            // 设置配置文件中的数据
            if (keys.contains("maxTotal")) {
                config.setMaxTotal(Integer.parseInt(prop.getProperty("maxTotal")));
            }
            if (keys.contains("maxIdle")) {
                config.setMaxIdle(Integer.parseInt(prop.getProperty("maxIdle")));
            }
            if (keys.contains("minIdle")) {
                config.setMinIdle(Integer.parseInt(prop.getProperty("minIdle")));
            }
            if (keys.contains("maxWaitMillis")) {
                config.setMaxWaitMillis(Integer.parseInt(prop.getProperty("maxWaitMillis")));
            }
            if (keys.contains("testOnBorrow")) {
                config.setTestOnBorrow(Boolean.parseBoolean(prop.getProperty("testOnBorrow")));
            }
            if (keys.contains("testOnReturn")) {
                config.setTestOnReturn(Boolean.parseBoolean(prop.getProperty("testOnReturn")));
            }
            if (keys.contains("timeBetweenEvictionRunsMillis")) {
                config.setTimeBetweenEvictionRunsMillis(Integer.parseInt(prop.getProperty("timeBetweenEvictionRunsMillis")));
            }
            if (keys.contains("testWhileIdle")) {
                config.setTestWhileIdle(Boolean.parseBoolean(prop.getProperty("testWhileIdle")));
            }
            if (keys.contains("numTestsPerEvictionRun")) {
                config.setNumTestsPerEvictionRun(Integer.parseInt(prop.getProperty("numTestsPerEvictionRun")));
            }

            // 根据配置对象创建连接池
            jedisPool = new JedisPool(config, prop.getProperty("host"), Integer.parseInt(prop.getProperty("port")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用 JedisUtils 工具类获取 jedis 连接的方法
     * @return jedis 对象
     */
    public static Jedis getJedis() {
        return jedisPool.getResource();
    }

    /**
     * 归还 jedis 对象
     * @param jedis，需要归还的 jedis
     */
    public static void close(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }
}


