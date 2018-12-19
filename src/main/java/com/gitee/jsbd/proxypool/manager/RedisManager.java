package com.gitee.jsbd.proxypool.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.regex.Pattern;

@Service
public class RedisManager {
    public static final double INIT_SCORE = 10;
    public static final double MIN_SCORE = 0;
    public static final double MAX_SCORE = 100;
    private static final String PROXY_RULE = "\\d+\\.\\d+\\.\\d+\\.\\d+\\:\\d+";
    private static final String PROXIES_KEY = "proxies";
    private static Logger LOGGER = LoggerFactory.getLogger(RedisManager.class);
    private static Pattern proxyPattern = Pattern.compile(PROXY_RULE);
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 判断当前代理是否已经存在
     *
     * @param proxy
     * @return
     */
    public boolean exists(String proxy) {
        return this.redisTemplate.opsForZSet().score(PROXIES_KEY, proxy) != null;
    }

    /**
     * 保存代理，并初始化代理的分值
     *
     * @param proxy
     * @return
     */
    public boolean save(String proxy) {
        if (proxyPattern.matcher(proxy).matches()) {
            LOGGER.info("保存代理:[{}]", proxy);
            if (!exists(proxy)) {
                return this.redisTemplate.opsForZSet().add(PROXIES_KEY, proxy, INIT_SCORE);
            }
        }
        return false;
    }

    /**
     * 获取所有代理数
     *
     * @return
     */
    public long count() {
        return this.redisTemplate.opsForZSet().zCard(PROXIES_KEY);
    }

    /**
     * 获取全部代理
     *
     * @return
     */
    public Set<Object> all() {
        return this.redisTemplate.opsForZSet().rangeByScore(PROXIES_KEY, MIN_SCORE, MAX_SCORE);
    }

    /**
     * 获取redis中的所有键的集合
     *
     * @return
     */
    public Set<String> keys() {
        return this.redisTemplate.keys("*");
    }

    /**
     * 批量获取代理列表
     *
     * @return
     */
    public Set<Object> batchQuery(long start, long end) {
        return this.redisTemplate.opsForZSet().reverseRange(PROXIES_KEY, start, end);
    }

    /**
     * 将可用的代理设置为最大score
     *
     * @param proxy
     * @return
     */
    public boolean validOk(String proxy) {
        LOGGER.info("代理[{}]可用，设置为[{}]", proxy, MAX_SCORE);
        return this.redisTemplate.opsForZSet().add(PROXIES_KEY, proxy, MAX_SCORE);
    }

}
