package com.gitee.jsbd.proxypool.spider;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * 抓取快代理IP(https://www.kuaidaili.com/free/inha/)
 */
@Service
public class KuaidailiSpider implements ISpider {
    private static Logger LOGGER = LoggerFactory.getLogger(KuaidailiSpider.class);

    @Override
    public List<String> crawl() {
        int page = 1;
        List<String> results = new LinkedList<>();
        try {
            while (page <= 100) {
                StringBuilder url = new StringBuilder("https://www.kuaidaili.com/free/inha/");
                url.append(page).append("/");
                LOGGER.info("start crawl [{}]", url.toString());
                HttpResponse response = HttpRequest.get(url.toString()).timeout(5000).executeAsync();

                String html = response.body();
                Thread.sleep(1000);//请求慢一点，这个网站请求快了会返回503

                Document document = Jsoup.parse(html);
                Elements elements = document.select("#list .table tbody tr");

                if (elements != null && elements.size() > 0) {
                    for (Element element : elements) {
                        String ip = element.select("td:nth-child(1)").text();
                        String port = element.select("td:nth-child(2)").text();
                        String proxy = ip + ":" + port;
                        LOGGER.info("Get proxy[{}] from 快代理.", proxy);
                        results.add(proxy);
                    }
                }
                page++;
            }
        } catch (Exception e) {
            LOGGER.error("抓取快代理IP发生异常", e);
        } finally {
        }
        LOGGER.info("size=" + results.size());
        return results;
    }

}
