# 用户中心 DAY1

**目的**： 完整了解做项目的思路，接触一些企业级开发的技术。

## 企业做项目流程

​	需求分析=> 设计（概要设计、详细设计）=>技术选型=>初始化/引入需要的技术=>写Demo=>写代码(实现业务逻辑)=>测试(单元测试)=>代码提交/代码评审=>部署=>发布

### 一、需求分析

1. 登录/注册
2. 用户管理（仅管理员可见）
3. 用户校验（仅VIP用户）

### 二、技术选型

前端：三件套+React+组件库Ant Design+Umi+Ant Design Pro(现成的管理系统)

后端：Java + springMVC + mybatis + mybatis-plus + spring boot + mysql

- java（语言基础）
- spring（依赖注入框架，帮助你管理Java对象，集成一些其他内容）
- springMVC（web框架，提供接口访问、restful接口等能力）
- mybatis（Java操作数据库的框架，持久层框架，对jdbc的封装）
- mybatis-plus（对mybatis的增强，不用写sql也能实现增删改查）
- spring boot（快速启动/快速集成项目，不用自己管理spring配置、整合各种框架）

部署：服务器/容器（平台）

### 三、计划

1. 初始化项目

   1. 前端初始化✔
      1. 初始化项目✔
      2. 引入一些组件之类✔
      3. 框架介绍/瘦身❌
   2. 后端初始化 
      1. 准备环境（MySQL等）进行测试，来看是否连接成功✔
      2. 引入框架（整合框架）✔

2. 数据库设计

   ~~~
   CREATE TABLE `users` (
     `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
     `username` varchar(256) DEFAULT NULL COMMENT '用户昵称',
     `userAccount` varchar(256) DEFAULT NULL COMMENT '账号',
     `avatarUrl` varchar(1024) DEFAULT NULL COMMENT '用户头像',
     `gender` tinyint DEFAULT NULL COMMENT '性别',
     `userPassword` varchar(256) DEFAULT NULL COMMENT '用户密码',
     `phone` varchar(128) DEFAULT NULL COMMENT '电话',
     `email` varchar(256) DEFAULT NULL COMMENT '邮箱',
     `userStatus` int NOT NULL DEFAULT '0' COMMENT '用户状态',
     `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `updateTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
     `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
     PRIMARY KEY (`id`)
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
   
   
   ~~~

3. 登录和注册

   1. 后端
      1. 规整项目目录
      2. 实现基本数据库操作（操作user表）
         - 模型user对象=》和数据库字段的关联，自动生成
      3. 写登录逻辑
         - 
   2. 前端

4. 用户管理（仅管理员可见）对用户的查询或修改   20min

   1. 前端
   2. 后端

   

# 用户中心 DAY2

1. 用户库表设计
2. 完成登录界面的前后端开发
   1. 开发完成后端登录功能（单机登录=》后续优化为（分布式/第三方登录）
   2. 开发后端用户的管理接口（用户的查询/状态更改）
   3. 开发前端用户登录注册功能


### 自动生成器的使用

MyBatis X插件，自动根据数据库生成domain实体对象、mapper（操作数据库的对象）、mapper.xml(定义了mapper对象和数据库的关联，可以在里面自己写SQL)、service（包含常用的增删改查的操作）、serviceImpl（具体实现service）

### 注册逻辑

1. 用户在前端输入账户和密码、以及校验码（手机验证码、公众号等）
2. 校验用户的账户、密码是否符合要求
   1. 输入字段非空
   2. 账户长度**不少于**4位
   3. 密码长度**不小于**8位
   4. 账户不能重复
   5. 账户不包含特殊字符
   6. 密码和校验密码相同
3. 对密码进行加密（密码千万不能明文存储在数据库中❗）
4. 向数据库插入用户数据

## 登录功能

### 登录接口

1. 接收参数：用户账户、密码

2. 请求类型：POST

   ~~~
   请求参数很长时，不建议使用GET（会根据不同的浏览器有不同的最大参数长度）
   ~~~

3. 请求体：JSON格式的数据

4. 返回值：用户信息（**脱敏**）

### 登录逻辑

1. 检验用户账户和密码是否合法

   1. 非空
   2. 账户长度不小于4位
   3. 密码长度不小于8位
   4. 账户不包含特殊字符

2. 检验密码是否输入正确，要和数据库中的密文密码进行对比

   1. 使用Lombok中的Slf4j来添加日志，不正确的日志：
      ~~~java
      if (user == null) {
                  log.info("user login failed,userAccount cannot match userPassword");
                  return null;
              }
      ~~~

      

3. 记录用户的登录态（session），将其存到服务器上（用后端Spring Boot框架封装的服务器tomcat去记录）cookie

4. 返回用户信息（脱敏）





### 用户管理接口

1. 查询用户
   1. 允许根据用户名查询
2. 删除用户
   1. 将以上的两个用户管理接口，都加上管理员才可查看的功能
