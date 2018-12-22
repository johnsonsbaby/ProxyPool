package com.gitee.jsbd.proxypool.api;

import cn.hutool.core.lang.Console;
import com.gitee.jsbd.proxypool.common.CodeEnum;
import com.gitee.jsbd.proxypool.common.PageInfo;
import com.gitee.jsbd.proxypool.common.R;
import com.gitee.jsbd.proxypool.dao.ProxyDAO;
import com.gitee.jsbd.proxypool.domain.Proxy;
import com.gitee.jsbd.proxypool.domain.ProxyStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/proxy")
public class ProxyController {

    @Autowired
    private ProxyDAO proxyDAO;

    public static void main(String[] args) {
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageNum(1);
        pageInfo.setPageSize(20);
        pageInfo.setTotal(1000);

        Console.log(pageInfo);
        Console.log(pageInfo.getStart());
        Console.log(0 % 20);
    }

    /**
     * 统计当前代理池中的代理数和稳定代理数
     *
     * @return
     */
    @RequestMapping("/stats")
    public R stats() {
        long all = proxyDAO.count();
        long high = proxyDAO.countHigh();
        ProxyStats stats = new ProxyStats();
        stats.setAll(all);
        stats.setHigh(high);
        return R.ok().data(stats);
    }

    /**
     * 随机获取一个代理，可能包含不稳定的代理
     *
     * @return
     */
    @RequestMapping("/random")
    public R random() {

        String proxy = this.proxyDAO.random();
        if (StringUtils.isEmpty(proxy)) {
            return R.error(CodeEnum.NOT_FOUND);
        }
        return R.ok().data(proxy);
    }

    /**
     * 随机获取一个稳定的代理
     *
     * @return
     */
    @RequestMapping("/random/high")
    public R randomHigh() {

        String proxy = this.proxyDAO.randomGetHighAvailableProxy();
        if (StringUtils.isEmpty(proxy)) {
            return R.error(CodeEnum.NOT_FOUND);
        }
        return R.ok().data(proxy);
    }

    /**
     * 保存手工录入的代理到代理池
     *
     * @return
     */
    @RequestMapping("/save")
    public R save(@RequestParam(name = "ip") String ip, @RequestParam(name = "port") String port) {
        String proxy = ip + ":" + port;
        if (StringUtils.isEmpty(proxy)) {
            return R.error(CodeEnum.PARAMS_ERROR);
        }
        if (!this.proxyDAO.save(proxy)) {
            return R.error(CodeEnum.SAVE_PROXY_ERROR);
        }
        return R.ok();
    }

    /**
     * 分页获取当前代理池的所有代理
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/list")
    public R list(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                  @RequestParam(name = "pageSize", defaultValue = "20") int pageSize) {
        PageInfo pageInfo = new PageInfo();
        int count = (int) this.proxyDAO.count();
        if (count > 0) {
            pageInfo.setPageNum(pageNum);
            pageInfo.setPageSize(pageSize);
            pageInfo.setTotal(count);

            List<Proxy> list = this.proxyDAO.batchQueryWithScore(pageInfo.getStart(), pageInfo.getEnd());
            pageInfo.setList(list);
        }
        return R.ok().data(pageInfo);
    }
}