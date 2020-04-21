import com.zwh.JedisDistributed.RedisLock;
import redis.clients.jedis.Jedis;

import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Test {
	private static Integer POOL_SIZE=1001;
	private static long AUTO_EXPIRE_TIME = 3000;
	private static int NUM =1000;
    private static long reTryTime = 50000;
	private static LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue();
	static RedisLock redisLock = new RedisLock(POOL_SIZE,AUTO_EXPIRE_TIME,reTryTime);

    public static void getDeamon() {
        Thread deamonThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try (Jedis jedis = redisLock.jedisPool.getResource()) {
                    while (true){
                        Thread.sleep(2000);
                        System.out.println("ttl:"+jedis.ttl(redisLock.LOCK_KEY));
                        jedis.expire(redisLock.LOCK_KEY, 3);
//                        System.out.println("----------续期了----------"+Thread.currentThread().getName());
                    }
                } catch (Exception e) {
                    System.out.println("守护线程异常：" + e.toString());
                }
            }
        });
        deamonThread.setDaemon(true);
        deamonThread.start();
    }

    /**
     *  业务处理超过自动过期时间，守护线程为获取锁续期。
     * */
    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(POOL_SIZE, POOL_SIZE, 10L, TimeUnit.SECONDS, linkedBlockingQueue);
        for (int i = 0; i <50; i++) {
            final int finalI = i;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    String uuid =UUID.randomUUID().toString();
                    String lockResult = redisLock.lock(uuid);   //重试获取锁失败时，请求丢失
                    if ("OK".equals(lockResult)){
                        getDeamon();                            //启动守护线程,每2s续期3s
                        try {
                            Thread.sleep(2000);  //业务逻辑处理耗时
                            NUM--;
                            System.out.println("-------库存NUM："+NUM);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        boolean unlock = redisLock.unlock(uuid);
//                        System.out.println("************unlock:"+unlock+"******"+Thread.currentThread().getName());
                    }else {
                        System.out.println("重试超时："+lockResult);
                    }
                }
            });
        }
        executor.shutdown();
    }
/**
 *  业务处理超过自动过期时间，不做处理
 * */
    public static void main1(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(POOL_SIZE, POOL_SIZE, 10L, TimeUnit.SECONDS, linkedBlockingQueue);
        for (int i = 0; i <100; i++) {
//            final int finalI = i;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    String uuid =UUID.randomUUID().toString();
                    String lockResult = redisLock.lock(uuid);//重试获取锁失败时，请求丢失
                    if ("OK".equals(lockResult)){
                        long start = System.currentTimeMillis();
                        try {
                            Thread.sleep(100);  //业务逻辑处理耗时
                            NUM--;
                            System.out.println("-------库存NUM："+NUM);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        long end = System.currentTimeMillis();

                        if ((end-start)<=AUTO_EXPIRE_TIME){
                            boolean unlock = redisLock.unlock(uuid);    //成功获得了锁并且业务处理为超时
                        }else{
                            System.out.println("------------------业务超时---------------------");
                        }
                    }else {
                        System.out.println("重试超时："+lockResult);
                    }
                }
            });
        }
        executor.shutdown();
    }
}
