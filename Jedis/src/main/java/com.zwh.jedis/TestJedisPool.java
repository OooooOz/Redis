package com.zwh.jedis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class TestJedisPool {

	public static void main(String[] args) {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		poolConfig.setMaxIdle(10);
		poolConfig.setMaxTotal(10);
		poolConfig.setMaxWaitMillis(1000);
		
		JedisPool jedisPool = new JedisPool(poolConfig, "127.0.0.1",6379);
		for (int i = 0; i <10; i++) {
			Jedis jedis = null;
			try {
				jedis= jedisPool.getResource();
				jedis.set("t"+i, "666");
				System.out.println("t"+i+":"+jedis.get("t"+i));
			} catch (Exception e) {
				// TODO: handle exception
			}finally {
				jedis.close();
			}
		}
//		jedisPool.getResource().ping();// 连接不够时：Could not get a resource from the pool
	}

}
