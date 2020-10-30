package com.yyc;

import com.yyc.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.UUID;

//测试类这两句话相当于启动springboot应用
@SpringBootTest(classes = RedisSpringbootApplication.class)
@RunWith(SpringRunner.class)
public class TestBoundApi {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //spring data 为了方便对redis进行更友好的操作，因此提供了bound api，简化了操作
    @Test
    public void testBound(){

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        //redisTemplate  stringRedisTemplate    将一个key的多次操作进行绑定     对key绑定
//        stringRedisTemplate.opsForValue().set("name","哇哈哈");
//        stringRedisTemplate.opsForValue().append("name","你好");
//        String s = stringRedisTemplate.opsForValue().get("name");
//        System.out.println(s);

        //对字符串类型key进行绑定  后续所有操作都是基于这个key的操作
        BoundValueOperations<String, String> valueOperations = stringRedisTemplate.boundValueOps("name");
        valueOperations.set("哇哈哈");
        valueOperations.append("你好");
        String s1 = valueOperations.get();
        System.out.println(s1);

        //对list set zset hash
        BoundListOperations<String, String> listOperations = stringRedisTemplate.boundListOps("lists");
        listOperations.leftPushAll("张三","lisi","小陈");
        List<String> lists = listOperations.range(0, -1);
        lists.forEach(list-> System.out.println("list = " + list));

//        stringRedisTemplate.boundSetOps();
//        stringRedisTemplate.boundZSetOps();
//        stringRedisTemplate.boundHashOps();

//        stringRedisTemplate.boundValueOps()
//        redisTemplate.boundListOps()
//        redisTemplate.boundSetOps();
//        redisTemplate.boundZSetOps();
//        redisTemplate.boundHashOps();


        /**
         * 1、针对于处理key value 都是 String 使用 stringRedisTemplate
         * 2、针对于处理的key value 存在对象 使用 stringRedisTemplate
         * 3、针对于同一个key的多次操作可以使用boundXxxOps() value list set zset hash的api 简化书写
         *
         */

    }


}
