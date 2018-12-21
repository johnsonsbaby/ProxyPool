package com.gitee.jsbd.proxypool.common;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * selenium 浏览器驱动工具
 */
public class WebDriverUtil {

    public static WebDriver phantomJS() {
        // 设置必要参数
        DesiredCapabilities dcaps = new DesiredCapabilities();
        // ssl证书支持
        dcaps.setCapability("acceptSslCerts", true);
        // 截屏支持
        dcaps.setCapability("takesScreenshot", true);
        // css搜索支持
        dcaps.setCapability("cssSelectorsEnabled", true);
        // js支持
        dcaps.setJavascriptEnabled(true);
        // 驱动支持
//        dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, YourDriverPath);
        // 创建无界面浏览器对象
        PhantomJSDriver driver = new PhantomJSDriver(dcaps);
        return driver;
    }

}
