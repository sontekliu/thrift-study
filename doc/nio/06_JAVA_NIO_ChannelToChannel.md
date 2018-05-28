# JAVA NIO Channel to Channel

在 Java NIO，如果 Channel 中有一个是 FileChannel，你可以直接从一个 Channel 传输数据到
另一个 Channel。FileChannel 的 transferTo() 和 transferFrom() 可以实现直接 Channel
间的数据传输。

### transferFrom()

FileChannel.transferFrom() 方法，可以将源 Channel 中的数据传输到 FileChannel 中。示例
如下：

```
RandomAccessFile fromFile = new RandomAccessFile("fromFile.txt", "rw");
FileChannel      fromChannel = fromFile.getChannel();

RandomAccessFile toFile = new RandomAccessFile("toFile.txt", "rw");
FileChannel      toChannel = toFile.getChannel();

long position = 0;
long count    = fromChannel.size();

toChannel.transferFrom(fromChannel, position, count);
```
方法的输入参数 position 表示从 position 位置开始向目标文件写入数据，count表示最多传输
的字节数，如果源通道剩余字节数小于 count，则传输的字节数要小于请求的字节数。

除此之外，在 SocketChannel 的实现中，SocketChannel 可能仅仅传输截止到目前为止已经准备
好的数据，即使之后可能还有更多的数据到来，因此，SocketChannel 可能不会将请求的所有数据传
输到 FileChannel 中。

### transferTo()

transferTo() 方法，将从 FileChannel 中的数据传输到其他 Channel 中，示例如下：

```
RandomAccessFile fromFile = new RandomAccessFile("fromFile.txt", "rw");
FileChannel      fromChannel = fromFile.getChannel();

RandomAccessFile toFile = new RandomAccessFile("toFile.txt", "rw");
FileChannel      toChannel = toFile.getChannel();

long position = 0;
long count    = fromChannel.size();

fromChannel.transferTo(position, count, toChannel);
```
注意，此示例与之前的示例是很像的，唯一的不同是 FileChannel 对象调用的方法，其他都是相同的。
上面所说的关于 SocketChannel 的问题在 transferTo() 同样存在，SocketChannel 的实现可能
会一直从 FileChannel 中传输数据到目标 Buffer。直到 Buffer 中被填满，才停止。





