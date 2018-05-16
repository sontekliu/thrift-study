# JAVA NIO Overview 

Java NIO 有如下的核心组件组成：
> Channels  
> Buffers  
> Selectors  

Java NIO 除此之外还有很多类和组件，但是 `Channel`、`Buffer` 和 `Selector` 组成了 Java NIO
的核心 API。在我看来，其他剩余的组件，像 `Pipe`、 `FileLock` 仅仅是用来链接三个核心组件的工具类而
已，因此本小节主要集中在这三大组件，其他组件会在单独的章节中讲解。

### 通道和缓冲区 (Channels and Buffers)

一般情况下，所有的 IO 在 NIO 中都是从 Channel 开始的，Channel 有点像流。Channel 中的数据可以读到
Buffer 中，也可以将数据从 Buffer 写到 Channel 中，如图所示：
![Channel and Buffer](images/overview-channels-buffers.png)

在 Java NIO 中有很多 Channel 和 Buffer 的类型，如下列举了一些主要的 Channel 实现类
> FileChannel  
> DatagramChannel  
> SocketChannel  
> ServerSocketChannel  

正如你所看到的那样，这些通道涵盖了 UPD 和 TCP 的网络 IO 以及文件 IO。

和这些类一起的还有一些其他有趣的接口，但是为了简单起见，本小节我尽量不提及它们，在本教程的其他章节与它们相关的地方
我会进行解释。

以下是 Java NIO 关键的 Buffer 实现类：
> ByteBuffer  
> CharBuffer  
> DoubleBuffer  
> FloatBuffer  
> IntBuffer  
> LongBuffer  
> ShortBuffer

这些 Buffer 涵盖了你通过 IO 发送的基本数据类型：byte、short、int、long、float、double、characters。

Java NIO 还有一个 `MappedByteBuffer`，它和内存映射文件一起使用，我将这个缓冲区从本章节中去除了。

### 选择器(Selectors)

Selector 允许一个线程处理多个通道。如果你的应用有很多链接，但是每个链接的通讯量很低，使用 Selector 就非常适合，
例如，一个聊天服务器。如图展示了一个线程处理多个 Channel：
![selector-three-channels](images/overview-selectors.png)

要使用 Selector，得向 Selector 注册 Channel，然后调用 select() 方法，这个方法会一直阻塞，直到注册其上的
某个 channel 有事件就绪。一旦该方法返回，线程就可以处理这些事件。channel 的事件例子有，如链接到来，接收到数据等。

