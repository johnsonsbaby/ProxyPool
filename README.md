### ProxyPool

<p align="left">
    <a>
    	<img src="https://img.shields.io/badge/JDK-1.8+-brightgreen.svg" >
    	<img src="https://img.shields.io/badge/SpringBoot-2.x-green.svg" >
    </a>
	<a href="https://gitee.com/jsbd/ProxyPool/repository/archive/master.zip">
		<img src="https://img.shields.io/badge/version-1.0.0-green.svg" >
	</a>
	<a href="https://gitee.com/jsbd/ProxyPool/tree/master/LICENSE">
		<img src="http://img.shields.io/:license-MIT-blue.svg" >
	</a>
</p>


-------------------------------------------------------------------------------

#### 系统简介

`ProxyPool`是基于Spring-Boot + Redis实现的稳定高可用的代理池系统

做过爬虫项目的应该都了解，单个IP频繁请求某些网站的时候会被限制IP并要求输入验证码，这时你有两种方式去解决，一是花钱买别人提供的代理池接口，二是自己维护一个稳定高可用的代理池；本系统正是针对第二种解决方案而产生的

系统通过抓取网上免费的代理信息，存储到Redis数据库的有序集合中，并定时去验证这些抓来的代理信息的可用性对代理按照分值大小过滤来获取高可用代理信息，最后通过API接口提供给爬虫项目使用，系统整个架构请参考下面的架构图



-------------------------------------------------------------------------------

#### 已完成

1. 代理池基本框架

2. 存储代理到Redis功能

3. 66ip免费代理抓取

4. 代理验证和代理抓取的定时调度器

5. 添加访问代理的API

6. 添加手工录入代理的功能




#### TODO LIST

1. 添加更多的免费代理爬虫

2. 整个高大上的项目首页

3. 完善文档




#### 系统架构

// TODO 架构图待制作




#### 项目演示

// TODO 准备录制个视频




#### 如何运行？

1. fork本项目或者[点击这里](https://gitee.com/jsbd/ProxyPool/repository/archive/master.zip)下载本项目到自己的电脑 

2. 进入项目根目录，修改`src/main/resources/application.properties`文件中的`Redis`配置为你自己的配置信息，项目默认使用`127.0.0.1:6379`

3. 从终端Terminal进入项目根目录执行`mvn spring-boot:run`即可运行

启动成功后从浏览器访问[http://127.0.0.1:9999](http://127.0.0.1:9999)

4. 如果按照上述已经搭建好了基础环境并顺利的运行起来了ProxyPool代理池系统，进入首页就能一窥ProxyPool的全貌了





#### 开发及运行环境搭建

1. JDK 1.8+、Maven 3.3+、Redis 5.0.2 项目使用了Redis中的有序集合数据结构，不一定非要安装这个版本，只因为作者的电脑上安装的是这个版本而已

2. PhantomJS无头浏览器的安装及PATH环境变量配置（由于要抓取的网站有WAF反爬虫机制，为了简单起见项目使用selenium来驱动PhantomJS浏览器来绕过目标站点对爬虫的限制）




#### 参与开发

1. 在`gitee.com`上`Fork`本仓库到自己的repo

2. 把`Fork`过去的项目也就是你的项目`clone`到你的本地

3. 修改代码，添砖加瓦

4. `git commit`后 `push` 到自己的库

5. 登录`gitee.com`，然后新建一个`pull request`，填写一些必要的信息后提交

6. 等待作者合并
 


#### 开源交流

1. 有问题请在本项目上直接提`ISSUE`

2. 即时沟通请加QQ群： 822297027（加群备注：`ProxyPool`）



#### 特别感谢

感谢以下各个伟大的开源项目，是你们让世界更加的美好，感谢！

1. [Spring-Boot](https://spring.io/projects/spring-boot)
2. [Hutool](https://gitee.com/loolly/hutool)
3. [Redis](https://redis.io/)
4. [Selenium](https://github.com/SeleniumHQ/selenium)
5. [PhantomJS](http://phantomjs.org/api/)
6. [jsoup](https://jsoup.org/)



#### 捐赠

如果你觉得ProxyPool不错，可以捐赠请作者吃个烧饼，在此表示感谢哦^_^
