package com.gitee.jsbd.proxypool.spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

/**
 * 抓取66ip的免费代理地址
 */
@Service
public class Ip66Spider implements ISpider {
    private static Logger LOGGER = LoggerFactory.getLogger(Ip66Spider.class);

    @Override
    public List<String> crawl() {
        int page = 100;
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        List<String> results = new LinkedList<>();
        try {
            while (page > 0) {
                StringBuilder url = new StringBuilder("http://www.66ip.cn/");
                url.append(page).append(".html");

                driver.get(url.toString());
                wait.until(presenceOfElementLocated(By.cssSelector(".containerbox table")));

                String html = driver.getPageSource();
                Document document = Jsoup.parse(html);
                Elements elements = document.select(".containerbox table tr:gt(0)");

                if (elements != null && elements.size() > 0) {
                    for (Element element : elements) {
                        String ip = element.select("td:nth-child(1)").text();
                        String port = element.select("td:nth-child(2)").text();
                        String proxy = ip + ":" + port;
                        LOGGER.info("Get proxy[{}] from 66ip.", proxy);
                        results.add(proxy);
                    }
                }

                page--;
            }
        } catch (Exception e) {
            LOGGER.error("抓取66ip代理发生异常", e);
        } finally {
            driver.quit();
        }

        return results;
    }

}
