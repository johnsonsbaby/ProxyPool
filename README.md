<p align="center">
	<h1>ProxyPool</h1>
	<h2>Java代理池系统</h2>
</p>

-- QQ交流群： 822297027 --

-------------------------------------------------------------------------------

#### 软件简介
ProxyPool是基于Spring-Boot 2.x + Redis实现的代理池系统，做过爬虫项目的应该都了解，维护一个稳定的代理池对爬虫项目是多么重要。

-------------------------------------------------------------------------------


#### 基础环境

1. Java 1.8+、Maven 3.3+、Redis 5.0.2、Chrome、 ChromeDriver(添加到系统的环境变量PATH中)由于要抓取的网站有WAF反爬虫机制，项目依赖selenium来驱动Chrome浏览器绕过爬虫的限制，所以需要你的电脑中已经安装了Chrome浏览器和ChromeDriver驱动，具体方式可以去搜索引擎寻找答案，网上这方面的文章很多
2. fork本项目或者[点击这里](https://gitee.com/jsbd/ProxyPool/repository/archive/master.zip)下载本项目到自己的电脑
3. 从Terminal进入项目根目录执行`mvn spring-boot:run`即可运行本项目，从浏览器访问[http://127.0.0.1:9999](http://127.0.0.1:9999)


#### 已完成功能

1. 代理池基本框架
2. 存储代理到Redis功能


#### 待办事项

1. 代理验证定时调度器
2. 添加更多的免费代理爬虫
3. 添加访问代理的API
4. 完善文档


#### 参与贡献

1. 在Gitee上Fork 本仓库到自己的repo
2. 把fork过去的项目也就是你的项目clone到你的本地
3. 修改代码，添砖加瓦
4. commit后push到自己的库
5. 登录Gitee，然后新建一个pull request，提交时填写一些必要的信息
6. 等待作者合并


#### 特别鸣谢

1. [Spring-Boot](https://spring.io/projects/spring-boot)
2. [Hutool](https://gitee.com/loolly/hutool)
3. [Redis](https://redis.io/)
4. [selenium](https://github.com/SeleniumHQ/selenium)


#### 捐赠

如果你觉得ProxyPool不错，可以捐赠请作者吃个烧饼，在此表示感谢^_^。

点击以下链接，将页面拉到最下方点击“捐赠”即可。

[前往捐赠](https://gitee.com/jsbd/ProxyPool)