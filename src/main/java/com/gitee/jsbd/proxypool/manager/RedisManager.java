package com.gitee.jsbd.proxypool.manager;

import cn.hutool.core.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Set;
import java.util.regex.Pattern;

@Service
public class RedisManager {

    private static final String PROXY_RULE = "\\d+\\.\\d+\\.\\d+\\.\\d+\\:\\d+";
    private static Logger LOGGER = LoggerFactory.getLogger(RedisManager.class);
    private static Pattern proxyPattern = Pattern.compile(PROXY_RULE);
    @Value("${proxy.score.init}")
    private double initScore = 5;
    @Value("${proxy.score.min}")
    private double minScore = 0;
    @Value("${proxy.score.max}")
    private double maxScore = 10;
    @Value("${proxy.redis.key}")
    private String redisKey = "proxies";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 判断当前代理是否已经存在
     *
     * @param proxy
     * @return
     */
    public boolean exists(String proxy) {
        return this.redisTemplate.opsForZSet().score(redisKey, proxy) != null;
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
                return this.redisTemplate.opsForZSet().add(redisKey, proxy, initScore);
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
        return this.redisTemplate.opsForZSet().zCard(redisKey);
    }

    /**
     * 获取全部代理
     *
     * @return
     */
    public Set<Object> all() {
        return this.redisTemplate.opsForZSet().rangeByScore(redisKey, minScore, maxScore);
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
        return this.redisTemplate.opsForZSet().reverseRange(redisKey, start, end);
    }

    /**
     * 将可用的代理设置为最大score
     *
     * @param proxy
     * @return
     */
    public boolean validOk(String proxy) {
        LOGGER.info("代理[{}]可用，设置为[{}]", proxy, maxScore);
        return this.redisTemplate.opsForZSet().add(redisKey, proxy, maxScore);
    }

    /**
     * 从Redis中获取可用的代理
     * 优先获取最大分值的代理，如果没有找到最大分值代理，
     * 则最小最大区间获取可用代理，然后随机挑选一个代理返回给用户
     *
     * @return
     * @throws Exception
     */
    public String random() throws Exception {

        Set<Object> proxies = this.redisTemplate.opsForZSet().rangeByScore(redisKey, maxScore, maxScore);
        if (CollectionUtils.isEmpty(proxies)) {
            proxies = this.redisTemplate.opsForZSet().rangeByScore(redisKey, minScore, maxScore);
        }

        if (CollectionUtils.isEmpty(proxies)) {
            throw new Exception("池中已无可用代理");
        }

        return getRandomProxy(proxies).toString();
    }

    /**
     * 随机从集合中获取一个元素
     *
     * @param set
     * @return
     */
    private Object getRandomProxy(Set<Object> set) {
        int rn = RandomUtil.randomInt(set.size());
        int i = 0;
        for (Object object : set) {
            if (i == rn) {
                return object;
            }
            i++;
        }
        return null;
    }


}
