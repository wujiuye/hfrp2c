# hfrp2c
一个基于http协议、类feign、支持retry和playback的rpc框架，rp2c是rprpc的简写。

重试用于解决网络波动、被限流熔断等场景，而回放则是解决服务端版本更新重启等场景。该框架起初是为定时任务项目而开发，而微服务项目还是使用OpenFeign、Dubbo等框架。