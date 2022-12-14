##

> neo-reward
>> 原神签到、京豆领取、其他登陆奖励
>>> 1. 脱离Spring架构的一次尝试，netty + 反射、注解实现注入 + jdk动态代理
>>>2. 基于netty实现的http服务请求
>>>3. 基于动态代理实现的异步失败重试
>>>4. docker容器化，基于jdk17 + debian打包，使用jlink制作自定义jre，减少镜像体积。支持多平台打包amd64和arm64

> neo-gateway
>> 简单网关实现
>>> 1. 基于servlet3新特性，异步处理请求
>>>2. 基于redis + lua的简单分布式限流，redis stream实现的简单消息队列
>>>3. 参考Tomcat线程池实现压榨CPU性能的线程池
>>>4. 快慢线程池，实现慢请求隔离，防止慢请求过多导致的系统响应延迟