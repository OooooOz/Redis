package com.zwh.JedisDistributed;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;


public class RedisLock {
    public String LOCK_KEY = "reids_lock";
    private long AUTO_EXPIRE_TIME;          //锁自动释放阈值
    private long reTryTime;                 //重试时间阈值
    private SetParams params ;
    private static GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
    public static JedisPool jedisPool = null;

    public RedisLock(Integer inventory, long AUTO_EXPIRE_TIME,long reTryTime) {
        this.AUTO_EXPIRE_TIME = AUTO_EXPIRE_TIME;
        this.params = SetParams.setParams().nx().px(AUTO_EXPIRE_TIME);
        this.reTryTime=reTryTime;
        poolConfig.setMaxIdle(inventory);
        poolConfig.setMaxTotal(inventory);
        poolConfig.setMaxWaitMillis(2000);//2s后报错
        jedisPool = new JedisPool(poolConfig, "192.168.199.188", 6379,4000);//4s后报连接超时
    }

    /**
     *  尝试获取锁，到了重试时间阈值退出，即可能请求丢失
     * */
    public String lock(String id) {
        Long start = System.currentTimeMillis();
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            for (; ; ) {
                //SET命令返回OK ，则证明获取锁成功
                String lock = jedis.set(LOCK_KEY, id, params);
                if ("OK".equals(lock)) {
                    return "OK";
                }
                //否则循环等待，在timeout时间内仍未获取到锁，则获取失败
                long l = System.currentTimeMillis() - start;
                if (l >= reTryTime) {
                    return "TimeOut";
                }
                Thread.sleep(200);
            }
        } catch (Exception e) {
            System.out.println("******异常*******："+e.toString());
        } finally {
            try {
                if (jedis != null) {
//                    System.out.println(jedisPool.getNumWaiters()+"----链接活跃数:"+jedisPool.getNumActive()+"----空闲连接数:"+jedisPool.getNumIdle());
                    jedis.close();
//                    System.out.println(jedisPool.getNumWaiters()+"----Close后活跃数:"+jedisPool.getNumActive()+"----空闲连接数:"+jedisPool.getNumIdle());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }
/**
 * 释放锁方法
 * */
    public boolean unlock(String id) {
        Jedis jedis = null;
        String script =
                "if redis.call('get',KEYS[1]) == ARGV[1] then" +
                        "   return redis.call('del',KEYS[1]) " +
                        "else" +
                        "   return 0 " +
                        "end";
        try {
            jedis = jedisPool.getResource();
            String result = jedis.eval(script, Collections.singletonList(LOCK_KEY), Collections.singletonList(id)).toString();
            return "1".equals(result) ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                jedis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
