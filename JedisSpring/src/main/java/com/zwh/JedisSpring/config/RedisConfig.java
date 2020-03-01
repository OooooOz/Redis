package com.zwh.JedisSpring.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
@ComponentScan("com.zwh.JedisSpring")
@PropertySource(value={"redis-config.properties"})
public class RedisConfig {
	
	@Value("${redis.pool.maxIdle}")
	private int maxIdle;

	@Value("${redis.pool.maxTotal}")
	private int maxTotal;
	
	@Value("${redis.pool.maxWaitMillis}")
	private int maxWaitMillis;
	
	@Value("${redis.hostName}")
	private String hostName;
	
	@Value("${redis.port}")
	private int port;
	
	@Value("${redis.pool.timeout}")
	private int timeout;
	
	/*	集群配置变量BEG*/
	@Value("${redis.cluster.nodes}")
    private String nodes;
	
	@Value("${redis.cluster.password}")
    private String password;
	
	@Value("${redis.cluster.maxRedirects}")
    private int maxRedirects;
	/*	集群配置变量END*/
	
	@Bean
	public JedisPoolConfig jedisPoolConfig(){
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(maxIdle);
		jedisPoolConfig.setMaxTotal(maxTotal);
		jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
		return jedisPoolConfig;
		
	}
	@Bean
	public JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig jedisPoolConfig){
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
		jedisConnectionFactory.setHostName(hostName);
		jedisConnectionFactory.setPort(port);
		jedisConnectionFactory.setTimeout(timeout);
		jedisConnectionFactory.setPoolConfig(jedisPoolConfig);
		return jedisConnectionFactory;
	}

	/*	集群配置连接BEG*/
//	@Bean
//	public RedisClusterConfiguration redisClusterConfiguration(){
//        List<String> asList = Arrays.asList(nodes.split(","));
//		RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(asList);
//		redisClusterConfiguration.setMaxRedirects(maxRedirects);
//		return redisClusterConfiguration;
//	}
//
//	@Bean
//	public JedisConnectionFactory jedisConnectionFactory(RedisClusterConfiguration redisClusterConfiguration,JedisPoolConfig jedisPoolConfig){
//		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisClusterConfiguration, jedisPoolConfig);
//		return jedisConnectionFactory;
//	}
	/*	集群配置连接END*/
	
	@Bean
	public StringRedisTemplate redisTemplate(JedisConnectionFactory jedisConnectionFactory){
		StringRedisTemplate redisTemplate = new StringRedisTemplate(jedisConnectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
//		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		return redisTemplate;
		
	}
}
