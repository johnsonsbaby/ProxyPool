package com.gitee.jsbd.proxypool.task;

import com.gitee.jsbd.proxypool.dao.ProxyDAO;
import com.gitee.jsbd.proxypool.spider.Ip66Spider;
import com.gitee.jsbd.proxypool.spider.KuaidailiSpider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
@ConfigurationProperties
public class ProxyCrawlTask {

    private static Logger LOGGER = LoggerFactory.getLogger(ProxyCrawlTask.class);

    @Autowired
    private ProxyDAO proxyDAO;
    @Autowired
    private Ip66Spider ip66Spider;
    @Autowired
    private KuaidailiSpider kuaidailiSpider;
    @Value("${task.crawl.enabled}")
    private Boolean enabled;

    /**
     * 定时抓取免费代理
     */
    @Scheduled(initialDelay = 2 * 1000, fixedDelay = 1 * 60 * 60 * 1000)
    public void crawlProxyScheduale() {

        if (enabled) {

            try {
                long start = System.currentTimeMillis();
                LOGGER.info("=====>>>>>开始抓取代理");
                List<String> results = ip66Spider.crawl();
                results.addAll(kuaidailiSpider.crawl());

                if (!CollectionUtils.isEmpty(results)) {
                    for (String proxy : results) {
                        this.proxyDAO.save(proxy);
                    }
                }
                LOGGER.info("<<<<<=====抓取代理结束，共耗时[{}]ms.", System.currentTimeMillis() - start);
            } catch (Exception e) {
                LOGGER.error("Ooooooops!!!!抓取代理定时调度任务发生异常", e);
            }
        }

    }


}
