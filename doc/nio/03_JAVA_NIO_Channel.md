# JAVA NIO Channel 

Java NIO Channels 和 streams 很像，但是又有稍微的差别：

* 既可以从通道中读取数据，又能将数据写入通道，而流的读写通常是单向的
* 通道的读写操作是异步的
* 通道中的数据总是先读到一个 Buffer，或者从一个 Buffer 中写入

如上所述，你可以从通道中读取数据到缓冲区，也可也将数据从缓冲区写入到通道，如图所示：

![Channel and Buffer](images/overview-channels-buffers.png)

### 通道实现类
下面是 Java NIO 中几个重要的通道实现类：
> FileChannel  
> DatagramChannel  
> SocketChannel  
> ServerSocketChannel  

FileChannel 从文件中读写数据。  
DatagramChannel 通过 UDP 协议读写网络数据。
SocketChannel 通过 TCP 协议读取网络中的数据。  
ServerSocketChannel 可以监听新进来的 TCP 网络链接，就像网络服务器一样，对每个进来的链接都会创建一个 SocketChannel

### 基本的通道示例

下面是一个通过 FileChannel 将数据读取到 Buffer 的示例：
```
    RandomAccessFile aFile = new RandomAccessFile("data/nio-data.txt", "rw");
    FileChannel inChannel = aFile.getChannel();

    ByteBuffer buf = ByteBuffer.allocate(48);

    int bytesRead = inChannel.read(buf);
    while (bytesRead != -1) {

      System.out.println("Read " + bytesRead);
      buf.flip();

      while(buf.hasRemaining()){
          System.out.print((char) buf.get());
      }

      buf.clear();
      bytesRead = inChannel.read(buf);
    }
    aFile.close();
```
注意 buf.flip() 的调用，首先将数据读入到 Buffer，然后反转 Buffer，接着再从 Buffer 中将数据读取出来，
在下一节将会深入讲解 Buffer 的细节
