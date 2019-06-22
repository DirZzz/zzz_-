

## netty 是一个网络框架，提供了统一的api 来处理网络传输中的问题。



基本概念：

1、Channel：通道：是client 与 server建立连接的抽象。

2、ChannelHandler：

3、ChannelHandlerPipeline：连接建立时，会绑定一个HandlerPipeline，既一组Handler，

用于处理业务。

4、BootStrap：用于建立一个节点（server、client）

5、EventLoopGroup：用于处理各种事件通知。

Encoder、Decoder实际就是Handler。

ChannelInBoundHandler接口：所有获取数据的Handler 的抽象

ChannelOuntBoundHandler接口：所有发送数据的Handler 的抽象





### 事件模型：

 	EventLoop  interface :

![img](C:/Users/Sandu/AppData/Local/YNote/data/air0210@163.com/df10df185ece4e5b8d952aef140679cd/clipboard.png)

其实现有：NoiEventLoop 、LocalEventLoop、EpollEventLoop 等。

### NioEventLoop 类图：

![img](C:/Users/Sandu/AppData/Local/YNote/data/air0210@163.com/058542d75fe34c43b7d16c92a216fcd1/clipboard.png)



由图可知，每个EventLoop 都实现了ScheduledExecutorService（定时任务），用于处理定时任务( 用于心跳检测？)。

并且，每个EventLoop 都持有一个线程实例，此线程实例会在构造EventLoop 时（在singleThreadEventExecutor的构造方法中）被初始化。	