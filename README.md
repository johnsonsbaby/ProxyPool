### ProxyPool

<p align="left">
    <a>
    	<img src="https://img.shields.io/badge/JDK-1.8+-brightgreen.svg" >
    	<img src="https://img.shields.io/badge/SpringBoot-2.x-green.svg" >
    </a>
	<a href="https://gitee.com/jsbd/ProxyPool/repository/archive/master.zip">
		<img src="https://img.shields.io/badge/version-1.0.0-green.svg" >
	</a>
	<a href="https://gitee.com/jsbd/ProxyPool/LICENSE">
		<img src="http://img.shields.io/:license-MIT-blue.svg" >
	</a>
</p>


-------------------------------------------------------------------------------

#### 软件简介

ProxyPool是基于Spring-Boot 2.x + Redis实现的代理池系统，做过爬虫项目的应该都了解，维护一个稳定的代理池对爬虫项目是多么重要。



-------------------------------------------------------------------------------



#### 环境搭建

1. JDK 1.8+ 安装及PATH环境变量配置
2. Maven 3.3+ 安装及PATH环境变量配置
3. Redis 5.0.2 项目使用了Redis中的有序集合数据结构，不一定非要安装这个版本，只因为作者的电脑上安装的是这个版本而已
4. Chrome浏览器及相应版本的ChromeDriver（由于要抓取的网站有WAF反爬虫机制，为了简单起见项目依赖selenium来驱动Chrome浏览器访问目标站点来绕过反爬虫的限制，所以需要你的电脑中已经安装了Chrome浏览器和ChromeDriver驱动）



#### 运行项目

1. fork本项目或者[点击这里](https://gitee.com/jsbd/ProxyPool/repository/archive/master.zip)下载本项目到自己的电脑 
2. 修改`src/main/resources/application.properties`文件中的相关配置为你自己的配置
3. 从Terminal进入项目根目录执行`mvn spring-boot:run`即可运行本项目，从浏览器访问[http://127.0.0.1:9999](http://127.0.0.1:9999)即可看到系统首页文档



#### 项目文档

如果按照上述已经搭建好了基础环境并顺利的运行起来了ProxyPool代理池系统，进入首页就能看到所提供的所有API接口文档



#### 已完成功能

1. 代理池基本框架
2. 存储代理到Redis功能
3. 66ip免费代理抓取



#### 待办事项

1. 代理验证定时调度器
2. 添加更多的免费代理爬虫
3. 添加访问代理的API
4. 添加手工录入代理的功能
5. 完善文档



#### 参与贡献

1. 在Gitee上Fork本仓库到自己的repo
2. 把Fork过去的项目也就是你的项目clone到你的本地
3. 修改代码，添砖加瓦
4. commit后push到自己的库
5. 登录Gitee，然后新建一个pull request，提交时填写一些必要的信息
6. 等待作者合并
7. 即时交流请加QQ交流群： 822297027（加群备注：ProxyPool） 



#### 特别鸣谢

1. [Spring-Boot](https://spring.io/projects/spring-boot)
2. [Hutool](https://gitee.com/loolly/hutool)
3. [Redis](https://redis.io/)
4. [selenium](https://github.com/SeleniumHQ/selenium)



#### 捐赠

如果你觉得ProxyPool不错，可以捐赠请作者吃个烧饼，在此表示感谢^_^，点击最下方“捐赠”按钮即可。