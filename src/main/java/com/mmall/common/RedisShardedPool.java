package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.*;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;

//redis分片连接池
public class RedisShardedPool {
    //为了保证tomcat在启动的时候就加载出来, shardedjedis连接池
    private static ShardedJedisPool pool;
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total", "20")); //jedis连接池和redis server的最大连接数
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle", "10"));//最多有多少个空闲jedis连接实例
    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle", "2")); //最少有多少个空闲jedis连接实例
    //程序从jedispool里拿一个实例的时候，是否要test，验证一下，若设置为true则保证拿到的实例都是可用的
    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow", "true"));
    //程序还实例的时候，是否要test，验证一下，若设置为true则保证还回的实例都是可用的
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return", "true"));

    private static String ip1 = PropertiesUtil.getProperty("redis1.ip");
    private static Integer port1 = Integer.parseInt(PropertiesUtil.getProperty("redis1.port"));
    private static String ip2 = PropertiesUtil.getProperty("redis2.ip");
    private static Integer port2 = Integer.parseInt(PropertiesUtil.getProperty("redis2.port"));

    private static void initPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        config.setBlockWhenExhausted(true);//连接耗尽的时候是否阻塞，false会抛出异常，true阻塞直到超时，默认为true
        JedisShardInfo info1 = new JedisShardInfo(ip1, port1, 1000 * 2);
//        info1.setPassword();
        JedisShardInfo info2 = new JedisShardInfo(ip2, port2, 1000 * 2);

        List<JedisShardInfo> jedisShardInfoList = new ArrayList<>(2);
        jedisShardInfoList.add(info1);
        jedisShardInfoList.add(info2);

        pool = new ShardedJedisPool(config, jedisShardInfoList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
    }

    static {
        initPool();
    }

    public static ShardedJedis getJedis() {
        return pool.getResource();
    }

    public static void returnBrokenResource(ShardedJedis jedis) {

        pool.returnBrokenResource(jedis);

    }


    public static void returnResource(ShardedJedis jedis) {

        pool.returnResource(jedis);

    }

//        public static void main(String[] args) {
//        ShardedJedis jedis = pool.getResource();
//
//        for (int i =0 ;i<10 ;i++){
//            jedis.set("key" + i, "value" + i);
//        }
//
//        returnResource(jedis);
////        pool.destroy(); ////临时调用，销毁连接池中的所有连接
//        System.out.println("program is end");
//    }
}
