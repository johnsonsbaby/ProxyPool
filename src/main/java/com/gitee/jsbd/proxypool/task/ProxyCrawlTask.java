package com.gitee.jsbd.proxypool.task;

import com.gitee.jsbd.proxypool.dao.ProxyDAO;
import com.gitee.jsbd.proxypool.spider.Ip66Spider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class ProxyCrawlTask {

    private static Logger LOGGER = LoggerFactory.getLogger(ProxyCrawlTask.class);

    @Autowired
    private ProxyDAO proxyDAO;
    @Autowired
    private Ip66Spider ip66Spider;

    /**
     * 定时抓取免费代理
     */
    @Scheduled(initialDelay = 5 * 1000, fixedDelay = 2 * 60 * 60 * 1000)
    public void crawlProxyScheduale() {

        try {
            long start = System.currentTimeMillis();
            LOGGER.info("=====>>>>>开始抓取代理");
            List<String> ip66List = ip66Spider.crawl();
            if (!CollectionUtils.isEmpty(ip66List)) {
                for (String proxy : ip66List) {
                    this.proxyDAO.save(proxy);
                }
            }
            LOGGER.info("<<<<<=====抓取代理结束，共耗时[{}]ms.", System.currentTimeMillis() - start);
        } catch (Exception e) {
            LOGGER.error("Ooooooops!!!!抓取代理定时调度任务发生异常", e);
        }

    }


}
