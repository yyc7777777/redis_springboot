package com.yyc;

import com.yyc.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.UUID;

//测试类这两句话相当于启动springboot应用
@SpringBootTest(classes = RedisSpringbootApplication.class)
@RunWith(SpringRunner.class)
public class TestRedisTemplate {

    //注入RedisTemplate       key object  value object  ===> 对象序列化 name new User() ===>  name序列化结果  对象序列化结果
    @Autowired
    private RedisTemplate redisTemplate;

    //opsForxxx     Value list set zset hash

    @Test
    public void testRedisTemplate(){

        /**
         * RedisTemplate对象中 key和value的序列化都是     JdkSerializationRedisSerializer
         *      key:String
         *      value:object
         *      修改key默认序列化方案：StringRedisSerializer
         */

        //修改key的序列化方案：String类型序列化
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //修改hash的序列化方案
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        User user = new User();
        user.setId(UUID.randomUUID().toString()).setName("小陈").setAge(88).setBir(new Date());
        redisTemplate.opsForValue().set("user", user);  //对redis进行设置 对象需要经过序列化

        User user1 = (User) redisTemplate.opsForValue().get("user");
        System.out.println("user1 = " + user1);

        redisTemplate.opsForList().leftPush("list",user);

        redisTemplate.opsForSet().add("set",user);

        redisTemplate.opsForZSet().add("zset",user,10);

        redisTemplate.opsForHash().put("map","name",user);

    }


}
