## HC小区管理系统安装教程（开发环境）

1、下载代码 <br/>
git clone https://gitee.com/wuxw7/MicroCommunity.git <br/>
执行 mvn clean package  和 mvn clean install<br/><br/>
2、添加hosts<br/>
127.0.0.1 dev.db.java110.com <br/>
127.0.0.1 dev.zk.java110.com <br/>
127.0.0.1 dev.kafka.java110.com <br/>
127.0.0.1 dev.redis.java110.com <br/>
127.0.0.1 api.java110.com <br/>
127.0.0.1 dev.java110.com <br/>
3、安装mysql 导入TT.sql （文件在docs/db下）<br/>
4、安装redis 指定redis密码<br/>
5、修改模块service-开头的服务下的 application-dev.yml 和 dataSource.yml<br/>
application-dev.yml 中主要修改redis密码

```yaml
jedis:
  pool:
    config:
      maxTotal: 100
      maxIdle: 20
      maxWaitMillis: 20000
    host: dev.redis.java110.com
    port: 6379
    timeout: 3000
    password: hc
```
将 password: hc 中的hc 修改为redis 指定的密码
```yaml
spring:
  profiles:
    active: share
  http:
    encoding:
      charset: UTF-8
      enabled: true
      force: true
  application:
    name: community-service
  redis:
    database: 8
    host: dev.redis.java110.com
    port: 6379
    password: hc
    pool:
      max-active: 300
      max-wait: 10000
      max-idle: 100
      min-idle: 0
      timeout: 0
```
将 password: hc 中的hc 修改为redis 指定的密码<br/>
```yaml
dataSources:
  ds0: !!com.alibaba.druid.pool.DruidDataSource
    driverClassName: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://dev.db.java110.com:3306/TT_EN?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8
    username: TT_EN
    password: TT@12345678
    minIdle: 5
    validationQuery: SELECT 1 FROM DUAL
    initialSize: 5
    maxWait: 60000
    filters: stat,wall,log4j
    poolPreparedStatements: true
```
dataSource.yml 中修改password密码<br/>
注意 比如 service-api服务中是没有dataSource.yml 文件<br/>
6、启动模块service-开头服务下的*ServiceApplicationStart.java文件中的main方法<br/>
注意启动是没有顺序的，但是我们优先推荐您启动service-eureka<br/>
7、下载前端代码 <br/>
git clone https://gitee.com/wuxw7/MicroCommunity.git <br/>
前端代码位于仓库的 pc-web 目录下，克隆完成后进入该目录：<br/>
cd MicroCommunity/pc-web <br/>
8、修改vue.config.js 中的ip和端口信息<br/>
```shell script
    proxy: {
      '/app': {
        target: 'http://127.0.0.1:8008/app', // 后端API地址
        changeOrigin: true,
        pathRewrite: {
          '^/app': ''
        }
      },
      '/callComponent': {
        target: 'http://127.0.0.1:8008/callComponent', // 后端API地址
        changeOrigin: true,
        pathRewrite: {
          '^/callComponent': ''
        }
      }
```
将127.0.0.1 修改为你后端的ip<br/>
9、启动并且访问<br/>
npm run dev
```shell script
npm run dev
```
表示启动成功<br/>
访问地址：http://localhost:3000