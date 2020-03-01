import com.zwh.JedisSpring.Pojo.Person;
import com.zwh.JedisSpring.Util.RedisTest;
import com.zwh.JedisSpring.config.SpringConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {
	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-redis-context.xml");
//		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);//Spring的注解方式
		RedisTest redisTest = (RedisTest) ctx.getBean("redisTest");
		
		redisTest.addStr("s1", "张三");
		redisTest.getStr("s1");
		
		Person person = new Person();
		person.setAge(18);
		person.setName("猫猫");
		person.setSex("0");
		
		redisTest.addModel("猫猫", person);
		redisTest.getModel("猫猫");
		
		/*redisTest.addModel("狗狗", person);
		redisTest.getModel("狗狗");*/
		
	/*	redisTest.addLink("list", "l1");
		redisTest.addLink("list", "l2");
		redisTest.addLink("list", "l3");
		redisTest.getLink("list");
		redisTest.getLink("list");
		redisTest.getLink("list");
		
		redisTest.addHash("hash", "name", "张三");
		redisTest.addHash("hash", "age", "25");
		redisTest.getHash("hash", "name");*/

	}
	
}
