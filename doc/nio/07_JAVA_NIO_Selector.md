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

在上一小节中，当你通过 register() 方法将 Channel 注册到 Selector 上，register() 返回一个 SelectionKey 对象，
SelectionKey 对象包含以下有趣的属性：   
> interest 集合  
> ready 集合   
> Channel  
> Selector  
> 附加对象(可选)  

下面会对这些属性进行简单的描述。

##### interest 集合

就像“注册 Channel 到 Selector” 章节中描述的那样，interest 集合是你选择的感兴趣事件的集合，
你可以通过 SelectionKey 读写 interest 集合，就像这样：  
```
int interestSet = selectionKey.interestOps();

boolean isInterestedInAccept  = (interestSet & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT;
boolean isInterestedInConnect = (interestSet & SelectionKey.OP_CONNECT) == SelectionKey.OP_CONNECT;
boolean isInterestedInRead    = (interestSet & SelectionKey.OP_READ) == SelectionKey.OP_READ;
boolean isInterestedInWrite   = (interestSet & SelectionKey.OP_WRITE) == SelectionKey.OP_WRITE;    
```
你可以看到，你可以用“与”操作 interest 集合和给定的 SelectionKey 常量，来确定某个事件是否在 interest 集合中。

##### ready 集合
ready 集合是通道已经准备就绪的操作的集合。在一次选择之后，你会首先访问 ready 集合，Selection 将在下一节讲解，你
可以通过如下方式访问 ready 集合：

```
int readySet = selectionKey.readyOps();
```
你可以用像检测 interest 集合那样的方法来检测 Channel 中什么事件或者操作准备就绪。但是，你也可以使用如下四个
方法，他们都返回 boolean 值：  
```
selectionKey.isAcceptable();
selectionKey.isConnectable();
selectionKey.isReadable();
selectionKey.isWritable();
```

##### Channel + Selector
从 SelectionKey 访问 channel 和 selector 是很简单的，如下：
```
Channel  channel  = selectionKey.channel();
Selector selector = selectionKey.selector();    
```

##### 附加对象
你可以将对象附加到 SelectionKey 上，这是识别给定的 Channel 或者将更多的信息附加到 Channel 的便捷方式。例如，
你可以附加与通道一起使用的 Buffer ，或者是包含聚集数据的某个对象。下面就是如何附加对象：
```
selectionKey.attach(theObject);

Object attachedObj = selectionKey.attachment();
```
你也可以在用 register 方法向 Selector 注册 Channel 的时候附加一个对象，代码如下：
```
SelectionKey key = channel.register(selector, SelectionKey.OP_READ, theObject);
```

### 通过 Selector 选择 Channel
一旦你向 Selector 注册了一个或者多个 Channel，你就可以调用几个重载 select() 方法。这些方法将返回你感兴趣的事件
（连接，接受，读或写）已准备就绪的通道。换句话说，如果你对读就绪的 Channel 感兴趣，那么 select() 方法将返回读就绪
的那些 Channel。下面是 select() 方法：  
> int select()  
> int select(long timeout)  
> int selectNow()  

select() 会一直阻塞到至少有一个 Channel 在你注册的事件上准备就绪了。  
select(long timeout) 和 select() 方法一样，除了最长会阻塞 timeout 毫秒（参数）。  
selectNow() 不会被阻塞，自从前一次选择操作之后，没有通道变成可选择的，则直接返回 0.

select() 返回的 int 值则表示有多少个 Channel 已经就绪，也就是，自从上次调用 select() 方法之后，又有多少
Channel 变成就绪状态。当你调用 select() 方法返回 1， 说明只有一个 Channel 准备就绪，你再次调用 select()
方法，如果另一个通道准备就绪了，将再次返回 1。如果第一个准备就绪的 Channel 没有做任何操作，现在就有 2 个就绪
的通道，但是在每次调用 select() 之间只有一个 Channel 就绪。

###### Selection Keys
一旦你调用了 select() 方法，它的返回值则表明了有一个或者多个 channel 准备就绪，然后你可以通过调用 selector 
的 selectedKeys() 方法访问"已选择健集"中准备就绪的通道，代码如下：
```
Set<SelectionKey> selectedKeys = selector.selectedKeys();    
```
当你向 Selector 注册一个 Channel 时，Channel.register() 方法返回一个 SelectionKey 对象。这个对象
代表了注册到该 selector 的 Channel。可以通过 selectedKeySet() 方法访问这些对象。

你可以迭代已选择的健集合来访问那些已准备就绪的 Channel。代码如下：
```
Set<SelectionKey> selectedKeys = selector.selectedKeys();
Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
while(keyIterator.hasNext()) {
    SelectionKey key = keyIterator.next();
    if(key.isAcceptable()) {
        // a connection was accepted by a ServerSocketChannel.
    } else if (key.isConnectable()) {
        // a connection was established with a remote server.
    } else if (key.isReadable()) {
        // a channel is ready for reading
    } else if (key.isWritable()) {
        // a channel is ready for writing
    }
    keyIterator.remove();
}
```
这个循环遍历已选择健集合中的健，并检测每一个健对应的 Channel 准备就绪。

注意，在每次迭代末尾 keyIterator.remove() 方法的调用，Selector 不会自己从已选择的健集合中移除
SelectionKey 实例。必须在处理完 Channel 时自己移除。当下次 Channel 变成 "ready" 状态时，Selector
将会再将其加入到已选择健集合中。
SelectionKey.channel() 返回的 channel 对象可以被转化成你需要处理的类型，如 ServerSocketChannel，
SocketChannel 等。

###### wakeUp()
调用了 select() 方法并且已阻塞的线程，即使没有 Channel 准备就绪，也可以使其从 select() 方法返回。只要
有其他线程调用第一个线程调用 select() 方法的那个对象上调用 wakeup() 方法即可。阻塞在 select() 方法的线
程会立刻返回。

如果其他线程调用了 wakeup() 方法，并且当前没有线程阻塞在 select() 方法上，那么下一个调用 select() 方法
的线程会立刻"醒来"。

####### close()
当你用完 Selector 并且调用其 close() 方法，这将会关闭 Selector，并且注册到该 Selector 上所有的 
SelectionKey 实例无效，但是 Channel 本身不会关闭。

### 完整的 Selector 示例
这里有一个完整的示例，打开一个 Selector，注册一个 Channel （Channel 的初始化过程忽略），持续监听这个
Selector 上的四个事件（接受，连接，读，写）是否准备就绪。

```
Selector selector = Selector.open();
channel.configureBlocking(false);
SelectionKey key = channel.register(selector, SelectionKey.OP_READ);

while(true) {
  int readyChannels = selector.select();
  if(readyChannels == 0) continue;

  Set<SelectionKey> selectedKeys = selector.selectedKeys();
  Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
  
  while(keyIterator.hasNext()) {
    SelectionKey key = keyIterator.next();
    if(key.isAcceptable()) {
        // a connection was accepted by a ServerSocketChannel.
    } else if (key.isConnectable()) {
        // a connection was established with a remote server.
    } else if (key.isReadable()) {
        // a channel is ready for reading
    } else if (key.isWritable()) {
        // a channel is ready for writing
    }
    keyIterator.remove();
  }
}
```






































