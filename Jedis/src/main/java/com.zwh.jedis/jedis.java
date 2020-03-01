package com.zwh.jedis;

import redis.clients.jedis.Jedis;

public class jedis {
    public static void main(String[] args) {
        // Jedis ip为连接的虚拟机ip（ifconfig）
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        jedis.set("s", "222");				// 通过redis set赋值
        String result = jedis.get("s");	    // 通过redis get取值
        System.out.println(result);
        jedis.close();						// 关闭jedis

    }
}
