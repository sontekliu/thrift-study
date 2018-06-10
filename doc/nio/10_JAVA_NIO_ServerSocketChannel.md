# JAVA NIO ServerSocketChannel

Java NIO ServerSocketChannel 是用来监听 TCP 连接的通道，等同于 Java 网络编程
中的 ServerSocket，ServerSocketChannel 位于 java.nio.channels 包中。代码
示例如下：
```
ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
while(true){
    SocketChannel socketChannel =
            serverSocketChannel.accept();
    //do something with socketChannel...
}
```
### 打开 ServerSocketChannel
通过调用 ServerSocketChannel.open() 方法来打开一个 ServerSocketChannel。代码如下：
``` 
ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
```
### 关闭 ServerSocketChannel
通过调用 ServerSocketChannel.close() 方法来关闭一个 ServerSocketChannel，代码如下：
```
serverSocketChannel.close();
```
### 监听进来的连接
通过调用 ServerSocketChannel.accept() 方法来接收进来的连接，当 accept() 返回时，它包含
一个新进来的连接的 SocketChannel，因此，accept() 方法会一直阻塞到有新的连接到来。

通常不会只监听一个连接，你可以在 while 循环中调用 accept() 方法，代码如下：
``` 
while(true){
    SocketChannel socketChannel =
            serverSocketChannel.accept();
    //do something with socketChannel...
}
```
### 非阻塞模式
ServerSocketChannel 可以被设置为非阻塞模式，在非阻塞模式下，accept() 方法会立刻返回，如果还没有新的连接
到来的话，它可能返回 null，因此，需要检查返回的 SocketChannel 对象是否为 null，代码示例如下：

```
ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
serverSocketChannel.socket().bind(new InetSocketAddress(9999));
serverSocketChannel.configureBlocking(false);

while(true){
    SocketChannel socketChannel =
            serverSocketChannel.accept();
    if(socketChannel != null){
        //do something with socketChannel...
    }
}
```
