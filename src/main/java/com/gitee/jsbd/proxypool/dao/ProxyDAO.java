package com.gitee.jsbd.proxypool.dao;

import cn.hutool.core.util.RandomUtil;
import com.gitee.jsbd.proxypool.domain.Proxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class ProxyDAO {

    private static final String PROXY_RULE = "\\d+\\.\\d+\\.\\d+\\.\\d+\\:\\d+";
    private static Logger LOGGER = LoggerFactory.getLogger(ProxyDAO.class);
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
            if (!exists(proxy)) {
                LOGGER.info("===>>>保存代理:[{}]", proxy);
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
     * 获取所有稳定的代理数
     *
     * @return
     */
    public long countHigh() {
        return this.redisTemplate.opsForZSet().rangeByScore(redisKey, maxScore, maxScore).size();
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
     * 批量获取带分值的代理列表
     *
     * @param start
     * @param end
     * @return
     */
    public List<Proxy> batchQueryWithScore(long start, long end) {
        Set<ZSetOperations.TypedTuple<Object>> rangeWithScores = this.redisTemplate.opsForZSet().reverseRangeWithScores(redisKey, start, end);

        if (!CollectionUtils.isEmpty(rangeWithScores)) {
            List<Proxy> results = new ArrayList<>(rangeWithScores.size());
            Iterator<ZSetOperations.TypedTuple<Object>> iterator = rangeWithScores.iterator();
            while (iterator.hasNext()) {
                ZSetOperations.TypedTuple<Object> next = iterator.next();
                String[] proxyArr = next.getValue().toString().split(":");
                Proxy bean = new Proxy();
                bean.setIp(proxyArr[0]);
                bean.setPort(proxyArr[1]);
                bean.setScore(next.getScore());
                results.add(bean);
            }
            return results;
        }
        return null;
    }

    /**
     * 将可用的代理设置为最大score
     *
     * @param proxy
     * @return
     */
    public boolean validOk(String proxy) {
        LOGGER.info("===>>>代理[{}]可用，设置为[{}]", proxy, maxScore);
        return this.redisTemplate.opsForZSet().add(redisKey, proxy, maxScore);
    }

    /**
     * 从Redis中获取可用的代理
     * 优先获取最大分值的代理，如果没有找到最大分值代理，
     * 则最小最大区间获取可用代理，然后随机挑选一个代理返回给用户
     *
     * @return
     */
    public String random() {

        Set<Object> proxies = this.redisTemplate.opsForZSet().rangeByScore(redisKey, maxScore, maxScore);
        if (CollectionUtils.isEmpty(proxies)) {
            proxies = this.redisTemplate.opsForZSet().rangeByScore(redisKey, minScore, maxScore);
        }

        if (CollectionUtils.isEmpty(proxies)) {
            return null;
        }

        return getRandomProxy(proxies).toString();
    }

    /**
     * 获取高可用的代理
     *
     * @return
     */
    public String randomGetHighAvailableProxy() {
        Set<Object> proxies = this.redisTemplate.opsForZSet().rangeByScore(redisKey, maxScore, maxScore);
        if (CollectionUtils.isEmpty(proxies)) {
            return null;
        }
        return getRandomProxy(proxies).toString();
    }

    /**
     * 将当前代理的分值做-1或删除操作
     *
     * @param proxy
     * @return
     */
    public void decrementOrRemove(String proxy) {
        Double score = this.redisTemplate.opsForZSet().score(redisKey, proxy);
        if (score != null && score - 1 > minScore) {
            LOGGER.warn("===>>>将代理[{}]的分值-1", proxy);
            this.redisTemplate.opsForZSet().incrementScore(redisKey, proxy, -1);
        } else {
            LOGGER.warn("===>>>将代理[{}]从Redis当中删除", proxy);
            this.redisTemplate.opsForZSet().remove(redisKey, proxy);
        }
    }

    /**
     * 获取当前代理的分值，如果代理不存在返回null
     *
     * @param proxy
     * @return
     */
    public Double score(String proxy) {
        return this.redisTemplate.opsForZSet().score(redisKey, proxy);
    }


    /**
     * 清理失效的代理
     */
    public void cleanup() {
        Set<Object> invalidSet = this.redisTemplate.opsForZSet().rangeByScore(redisKey, minScore, minScore);
        if (!CollectionUtils.isEmpty(invalidSet)) {
            for (Object proxy : invalidSet) {
                this.decrementOrRemove(proxy.toString());
            }
        }
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
