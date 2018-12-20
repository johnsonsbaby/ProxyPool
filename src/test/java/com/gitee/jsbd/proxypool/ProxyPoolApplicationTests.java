package com.gitee.jsbd.proxypool;

import cn.hutool.core.lang.Console;
import com.gitee.jsbd.proxypool.manager.RedisManager;
import com.gitee.jsbd.proxypool.spider.Ip66Spider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProxyPoolApplicationTests {

    @Autowired
    private RedisManager redisManager;
    @Autowired
    private Ip66Spider ip66Spider;

    @Test
    public void testRedisDB() {
        System.out.println(this.redisManager.keys());

        String proxy = "111.198.77.169:9999";

        this.redisManager.save(proxy);
        this.redisManager.validOk(proxy);

        Console.log(this.redisManager.exists(proxy));
        Console.log(this.redisManager.count());
        Console.log(this.redisManager.all());
        Console.log(this.redisManager.batchQuery(100, 110));
    }

    @Test
    public void test66ipSpider() {
        List<String> results = this.ip66Spider.crawl();
        if (!CollectionUtils.isEmpty(results)) {
            for (String proxy : results) {
                Console.log("proxy -> " + proxy);
            }
        }
    }

}

