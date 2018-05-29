# JAVA NIO Selector

Selector 是 Java NIO 中的一个组件，它可以检查一个或多个 NIO Channel，并确定哪儿些通道已经
准备好，例如，读或者写。这样一个线程就可以管理多个 Channel，从而管理多个网络链接。

### 为什么要用 Selector

使用单个线程处理多个线程的优势就是你可以使用更少的线程处理多个 Channel，事实上，你可以仅仅
使用一个线程处理所有的 Channel，对操作系统来说，线程之间的切换是非常昂贵的，并且每个线程也
占用操作系统的资源（如内存）。因此，使用的线程越少越好。

记住，现代操作系统和 CPU 在多任务处理方面变得越来越好，所以随着时间的推移多线程的开销变得越
来越小。事实上，如果一颗 CPU 有多个核心，你可能因为没有多任务而浪费 CPU 的功率。无论如何，
该设计的讨论不属于本文所讨论的内容，这里说一下就足够了。你可以使用 Selector 用一个线程就可
以处理多个 Channel。

如图所示，使用一个 Selector 处理 3 个 Channel：   
![OverviewSelectors](./images/overview-selectors.png)

### 创建 Selector
你可以通过调用 Selector.open() 方法创建一个 Selector，如下：
```
Selector selector = Selector.open();
```

### 注册 Channel 到 Selector
为了让 Selector 使用 Channel，你必须将 Channel 注册到 Selector 上，这是使用 
`SelectableChannel.register()` 方法来实现的，代码如下：   

```
channel.configureBlocking(false);

SelectionKey key = channel.register(selector, SelectionKey.OP_READ);
```
Channel 必须在非阻塞的模式下才能与 Selector 一起使用，这意味着 FileChannel 不能与 Selector
一起使用，因为 FileChannel 不能切换到非阻塞模式，而 SocketChannel 可以正常工作。

注意 register() 方法的第二个参数，这是一个“兴趣集合”，意思是 Selector 对 Channel 的哪儿
些事件感兴趣，Selector 可以监听如下四种不同的事件：  
1. Connect
2. Accept
3. Read
4. Write

一个 Channel 触发一个事件，被认为针对该事件已准备就绪。所以，成功链接到另一台服务器的 Channel，
就是“链接就绪”，接受传入链接的服务器 socket channel，是“接收”就绪，准备好读取数据的 Channel
就是“读取”就绪，准备好你向里写数据的 Channel 就是“写入”就绪。

这四种事件，你可以用 SelectionKey 的四个常量来表示

> SelectionKey.OP_CONNECT  
> SelectionKey.OP_ACCEPT  
> SelectionKey.OP_READ  
> SelectionKey.OP_WRITE  

如果你对不止一种事件感兴趣，那么可以用“位或”操作符将常量连接起来，如下：

```
int interestSet = SelectionKey.OP_READ | SelectionKey.OP_WRITE;    
```

下文还会继续提到interest集合。

### SelectionKey's 





