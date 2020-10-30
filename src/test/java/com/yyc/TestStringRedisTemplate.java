package com.yyc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.concurrent.TimeUnit;

//测试类这两句话相当于启动springboot应用
@SpringBootTest(classes = RedisSpringbootApplication.class)
@RunWith(SpringRunner.class)
public class TestStringRedisTemplate {

    //注入StringRedisTemplate
    @Autowired
    private StringRedisTemplate stringRedisTemplate;    //key value 都是字符串

    //操作redis中的key
    @Test
    public void TestKey(){

//        stringRedisTemplate.delete("name");//删除一个key
        Boolean hasKey = stringRedisTemplate.hasKey("name");//判断key是否存在
        System.out.println("hasKey = " + hasKey);
        DataType name = stringRedisTemplate.type("name");//判断key所对应值的类型
        System.out.println("name = " + name);
        Set<String> keys = stringRedisTemplate.keys("*");//获取redis中的所有key
        keys.forEach(key-> System.out.println("key = " + key));

    }

    //操作redis中的字符串  opsForValue 实际操作的就是redis中的String类型
    @Test
    public void TestString(){

        stringRedisTemplate.opsForValue().set("name","小醋"); //set 用来设置一个key value
        String name = stringRedisTemplate.opsForValue().get("name");//用来获取一个key对应的value

        stringRedisTemplate.opsForValue().set("code","2333",120, TimeUnit.SECONDS);//设置一个key的超时时间

        stringRedisTemplate.opsForValue().append("name","你好");//追加

    }

    //操作redis中的list类型  opsForList
    @Test
    public void TestList(){
//        stringRedisTemplate.opsForList().leftPush("names","小陈");//创建一个列表，并放入一个元素
//        stringRedisTemplate.opsForList().leftPushAll("names","张三","李四","王五");//创建一个列表，放入多个元素
//        List<String> names = new ArrayList<>();
//        names.add("哈哈");
//        names.add("呵呵");
//        stringRedisTemplate.opsForList().leftPushAll("names",names);//创建一个列表，放入多个元素

        List<String> stringList = stringRedisTemplate.opsForList().range("names", 0, -1);//遍历list
        stringList.forEach(value-> System.out.println("value = " + value));

        stringRedisTemplate.opsForList().trim("names",1,4);//截取指定区间的list

    }

    //操作redis中的set类型  opsForSet
    @Test
    public void TestSet(){
        stringRedisTemplate.opsForSet().add("sets","张三","张三","小米","xiaoming");//创建set 并放入多个元素
        Set<String> sets = stringRedisTemplate.opsForSet().members("sets");//查看set中的元素
        sets.forEach(value-> System.out.println("value = " + value));
    }

    //操作redis中的zset类型  opsForZset
    @Test
    public void TestZset(){
        stringRedisTemplate.opsForZSet().add("zsets","小黑",56);//创建并放入元素
        Set<String> zsets = stringRedisTemplate.opsForZSet().range("zsets", 0, -1);//指定范围查询
        zsets.forEach(value-> System.out.println("value = " + value));

        Set<ZSetOperations.TypedTuple<String>> zsets1 = stringRedisTemplate.opsForZSet().rangeByScoreWithScores("zsets", 0, 100);//获取指定元素以及分数
        zsets1.forEach(typedTuple->{
            System.out.println(typedTuple.getValue());
            System.out.println(typedTuple.getScore());
        } );

    }

    //操作redis中的hash类型  opsForHash
    @Test
    public void TestHash(){

        stringRedisTemplate.opsForHash().put("maps","name","张三");//创建一个hash类型 并放入key value

        Map<String,String> map = new HashMap<>();
        map.put("age","19");
        map.put("bir","2020-11-11");
        stringRedisTemplate.opsForHash().putAll("maps",map);//放入多个key value

        List<Object> values = stringRedisTemplate.opsForHash().multiGet("maps", Arrays.asList("name", "age"));//获取多个key的value
        values.forEach(value-> System.out.println("value = " + value));
        String  value = (String) stringRedisTemplate.opsForHash().get("maps", "name");//获取hash中某个key的值

        List<Object> vals = stringRedisTemplate.opsForHash().values("maps");//获取hash中所有value

        Set<Object> keys = stringRedisTemplate.opsForHash().keys("maps");//获取所有key

    }


}
