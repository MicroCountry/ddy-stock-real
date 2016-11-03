package com.ddy.stock.real.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public final class JedisUtil {
    
    //可用连接实例的最大数目，默认值为8；
    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    private static int MAX_ACTIVE = 1024;
    
    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    private static int MAX_IDLE = 200;
    
    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    private static int MAX_WAIT = 10000;
    
    private static int TIMEOUT = 10000;
    
    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private static boolean TEST_ON_BORROW = true;
    
    private static JedisPool jedisPool = null;
    static Logger logger = LoggerFactory.getLogger(JedisUtil.class);
    /**
     * 初始化Redis连接池
     */
    static {
        try {
            FileInputStream in = new FileInputStream(new File(System.getenv("DDY_RESOURCES_DIR")+"/jedis.properties"));
        	Properties prop = new Properties();
        	
        	prop.load(in);
        	String ADDR = prop.getProperty("jedis.host").trim();
        	int PORT = Integer.parseInt(prop.getProperty("jedis.port").trim());
        	String AUTH = prop.getProperty("jedis.auth").trim();
        	
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(MAX_ACTIVE);
            config.setMaxIdle(MAX_IDLE);
            config.setMaxWaitMillis(MAX_WAIT);
            config.setTestOnBorrow(TEST_ON_BORROW);
            //logger.info("ADDR:"+ADDR+",PORT:"+PORT+",AUTH:"+AUTH);
            if (StringUtils.isBlank(AUTH))
            	jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT);
            else
            	jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT, AUTH);
            logger.debug("Redis连接池初始化成功，Host" + ADDR + ",PORT:" + PORT);
        } catch (Exception e) {
            logger.error("Redis 连接配置文件 jedis.properties 加载失败！");
        }
    }
    
    /**
     * 获取Jedis实例
     * @return Jedis实例
     */
    public synchronized static Jedis getJedis() {
        try {
            if (jedisPool != null) {
                Jedis resource = jedisPool.getResource();
                return resource;
            } else {
                return null;
            }
        } catch (Exception e) {
        	logger.error(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 释放jedis资源
     * @param jedis 需要释放的jedis实例
     */
    public static void returnResource(final Jedis jedis) {
        if (jedis != null) {
            jedisPool.returnResourceObject(jedis);
        }
    }
    
    public static void removeKey(String key){
        Jedis jedis = getJedis();
        if(jedis!=null){
            try{
                @SuppressWarnings("unused")
				long ret = jedis.del(key);
                //logger.info("removeKey key:"+key+",ret:"+ret);
            }finally{
                JedisUtil.returnResource(jedis);
            }
        }
    }
    
    
    public static void set(String key,String value){
        Jedis jedis = getJedis();
        if(jedis!=null){
            try{
                @SuppressWarnings("unused")
				String ret = jedis.set(key, value);
                //logger.info("hset key:"+key+",value:"+value+",ret:"+ret);
            }finally{
                JedisUtil.returnResource(jedis);
            }
        }
    }
    public static String get(String key){
        Jedis jedis = getJedis();
        if(jedis!=null){
            try{
                return jedis.get(key);
            }finally{
                JedisUtil.returnResource(jedis);
            }
        }
        return null;
    }
    
    public static void hset(String key,String field,String value){
        Jedis jedis = getJedis();
        if(jedis!=null){
            try{
                @SuppressWarnings("unused")
				long ret = jedis.hset(key, field, value);
                //logger.info("hset key:"+key+"field:"+field+",value:"+value+",ret:"+ret);
            }finally{
                JedisUtil.returnResource(jedis);
            }
        }
    }
    public static String hget(String key,String field){
        Jedis jedis = getJedis();
        if(jedis!=null){
            try{
                return jedis.hget(key, field);
            }finally{
                JedisUtil.returnResource(jedis);
            }
        }
        return null;
    }
    
    
    public static void main(String[] args) {
		
	}
}
