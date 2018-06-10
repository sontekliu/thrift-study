# JAVA NIO SocketChannel

Java NIO SocketChannel 是链接到 TCP 网络套接字的通道，它等价于 Java 网络编程中的套接字。
有两种方式创建 SocketChannel。

1. 打开一个 SocketChannel 链接到网络上的一台服务器。   
2. 当一个新的链接到达 ServerSocketChannel 的时候，会创建一个 SocketChannel。

### 打开 SocketChannel
如下代码是如何打开一个 SocketChannel：
```
SocketChannel socketChannel = SocketChannel.open();
socketChannel.connect(new InetSocketAddress("http://jenkov.com", 80));
```

### 关闭 SocketChannel
调用 SocketChannel.close() 方法关闭一个 SocketChannel 
```
socketChannel.close();  
```
### 从 SocketChannel 读取数据
要从 SocketChannel 中读取数据，你需要调用其 read() 方法，示例如下：
```
ByteBuffer buf = ByteBuffer.allocate(48);
int bytesRead = socketChannel.read(buf);
```
首先创建一个 Buffer ，将从 SocketChannel 读取的数据被读到 Buffer 中。

然后调用 SocketChannel.read() 方法，这个方法将从 SocketChannel 读取的数据写入到
Buffer 中，read() 方法返回的 int 值标明有多少个字节的数据被写入到 Buffer 中，如果
返回值是 -1， 则表示读到了流的末尾（连接被关闭）。

### 写入 SocketChannel
SocketChannel.write() 方法可以将数据写入到 SocketChannel 中，
SocketChannel.write() 接收一个 Buffer 作为参数，示例如下：
```
String newData = "New String to write to file..." + System.currentTimeMillis();
ByteBuffer buf = ByteBuffer.allocate(48);
buf.clear();
buf.put(newData.getBytes());

buf.flip();

while(buf.hasRemaining()) {
    channel.write(buf);
}
```
注意，SocketChannel.write() 方法是在 while 循环中调用的，因为无法保证有多少字节
的数据通过 write() 方法被写入到 SocketChannel 中，所以需要不断重复调用 write() 方法
直到 Buffer 中没有被写的字节为止。

### 非阻塞模式
你可以将 SocketChannel 设置为非阻塞模式，设置之后，你就可以使用异步的方式调用 connect(), read(), 
write() 方法了。   
##### connect()
如果 SocketChannel 在非阻塞的模式下调用 connect() 方法，此方法可能在连接建立之前就已经返回了，为了确定
连接是否建立，你可以调用 finishConnect() 方法。代码如下：
```
socketChannel.configureBlocking(false);
socketChannel.connect(new InetSocketAddress("http://jenkov.com", 80));

while(! socketChannel.finishConnect() ){
    //wait, or do something else...    
}
```

##### write()
在非阻塞模式下，write() 可能还未写出任何数据就已经返回了，因此，需要在一个循环中调用 write() 方法，
前面已有示例，此处不在赘述。

##### read()
在非阻塞模式下，read() 方法可能还未读取任何数据，就已经返回了，因此，你需要关注返回的 int 值，
它会告诉你读取了多少字节的数据。
 
##### 非阻塞模式与选择器
非阻塞模式与 Selector 配合使用会工作的更好，通过将一个或者多个选择器注册到 Selector 上，可以询问
Selector 哪个 Channel 准备好了读取操作，哪个通道准备好了写入操作。关于 Selector 与 SocketChannel
的搭配使用，在下面的章节会详细介绍。

