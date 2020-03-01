package com.zwh.jedis;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;


public class Test_Cluster {

	/**
     * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Set<HostAndPort> nodes = new HashSet<HostAndPort>();
		nodes.add(new HostAndPort("10.5.5.88", 8001));
		nodes.add(new HostAndPort("10.5.5.88", 8002));
		nodes.add(new HostAndPort("10.5.5.88", 8003));
		nodes.add(new HostAndPort("10.5.5.88", 8004));
		nodes.add(new HostAndPort("10.5.5.88", 8005));
		nodes.add(new HostAndPort("10.5.5.88", 8006));

		JedisCluster jedisCluster = new JedisCluster(nodes);
		jedisCluster.set("s3", "6699");
		String string = jedisCluster.get("s3");
		System.out.println(string);
		jedisCluster.close();

	}

}
