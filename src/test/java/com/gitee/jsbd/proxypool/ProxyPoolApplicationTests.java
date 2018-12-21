package com.gitee.jsbd.proxypool;

import cn.hutool.core.lang.Console;
import com.gitee.jsbd.proxypool.dao.ProxyDAO;
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
    private ProxyDAO proxyDAO;
    @Autowired
    private Ip66Spider ip66Spider;

    @Test
    public void testRedisDB() {
        System.out.println(this.proxyDAO.keys());

        String proxy = "111.198.77.169:9999";

        this.proxyDAO.save(proxy);
        this.proxyDAO.validOk(proxy);

        Console.log(this.proxyDAO.score(proxy));
        this.proxyDAO.decrementOrRemove(proxy);
        Console.log(this.proxyDAO.score(proxy));

        Console.log(this.proxyDAO.exists(proxy));
        Console.log(this.proxyDAO.count());
        Console.log(this.proxyDAO.all());
        Console.log(this.proxyDAO.batchQuery(100, 110));
        this.proxyDAO.batchQueryWithScore(0, 0);
    }

    @Test
    public void testCleanupInvalidProxy() {
        this.proxyDAO.cleanup();
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

