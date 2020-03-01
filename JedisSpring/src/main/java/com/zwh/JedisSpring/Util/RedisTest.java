package com.zwh.JedisSpring.Util;

import javax.annotation.Resource;

import com.zwh.JedisSpring.Pojo.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class RedisTest {
	
		@Autowired
		private StringRedisTemplate redisTemplate;

		@Resource(name="redisTemplate")
		private ValueOperations<String, Person> valueOperations;

		//操作普通键值对
		public void addStr(String key,String value){
			redisTemplate.opsForValue().set(key, value);
		}

		public void getStr(String key){
			System.out.println(redisTemplate.opsForValue().get(key));
		}
	
		//操作链表
		public void addLink(String key,String value){
			redisTemplate.opsForList().leftPush(key, value);
		}
	
		public void getLink(String key){
			System.out.println(redisTemplate.opsForList().leftPop(key));
		}
	
		//操作hash
		public void addHash(String key,String hashKey,String hashValue){
			redisTemplate.opsForHash().put(key, hashKey, hashValue);
//		redisTemplate.opsForHash().putAll(key, map);
		}
	
		public void getHash(String key,String hashKey){
			System.err.println(redisTemplate.opsForHash().get(key, hashKey));
		}
	
		//bound方法操作
		public void boundValue(String key,String value){
			redisTemplate.boundValueOps(key).set(value);
			redisTemplate.boundValueOps(key).get();
		}
		
		//向redis中存放实体类
		public void addModel(String key,Person person){
			valueOperations.set(key, person);
		}
		
		public void getModel(String key){
			System.out.println(key+":"+valueOperations.get(key));
		}


		public ValueOperations<String, Person> getValueOperations() {
			return valueOperations;
		}

		public void setValueOperations(ValueOperations<String, Person> valueOperations) {
			this.valueOperations = valueOperations;
		}

		public StringRedisTemplate getRedisTemplate() {
			return redisTemplate;
		}

		public void setRedisTemplate(StringRedisTemplate redisTemplate) {
			this.redisTemplate = redisTemplate;
		}

		
		
		
}
