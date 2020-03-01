package com.zwh.JedisSpring.config;

import com.zwh.JedisSpring.Util.RedisTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.zwh.JedisSpring")

public class SpringConfig {

	@Bean
	public RedisTest getRedisTest(){
		
		return new RedisTest();
	}
}
