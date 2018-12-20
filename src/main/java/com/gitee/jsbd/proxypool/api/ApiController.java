package com.gitee.jsbd.proxypool.api;

import com.gitee.jsbd.proxypool.common.CodeEnum;
import com.gitee.jsbd.proxypool.common.R;
import com.gitee.jsbd.proxypool.dao.ProxyDAO;
import com.gitee.jsbd.proxypool.domain.ProxyStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class ApiController {

    @Autowired
    private ProxyDAO proxyDAO;

    /**
     * 统计当前代理池中的代理数和稳定代理数
     *
     * @return
     */
    @RequestMapping("/proxy/stats")
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
    @RequestMapping("/proxy/random")
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
    @RequestMapping("/proxy/random/high")
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
    @RequestMapping("/proxy/save")
    public R save(@RequestParam(name = "ip") String ip, @RequestParam(name = "port") String port) {
        String proxy = ip + port;
        if (!StringUtils.isEmpty(proxy)) {
            return R.error(CodeEnum.PARAMS_ERROR);
        }
        if (!this.proxyDAO.save(proxy)) {
            return R.error(CodeEnum.SAVE_PROXY_ERROR);
        }
        return R.ok();
    }

}