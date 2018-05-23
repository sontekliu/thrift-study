# JAVA NIO Scatter And Gather

从 Java NIO 开始内置了对 Scatter/Gather 的支持，Scatter/Gather 是用于描述从 Channel 中
读取数据和写入数据到 Channel 的操作。

Scatter (分散)， 这是一个从 Channel 中读取的操作，即将从 Channel 中读取的数据分散到不同的 Buffer
中，因此，一个 Channel 可以将从 Channel 中读取的数据分散到多个 Buffer 中。

Gather (收集)，这是将数据写入 Channel 的写操作，即将多个 Buffer 中的数据写入到一个 Channel 中
，因此，Channel 可以收集多个 Buffer 的数据并将其写入到单个 Channel 中。

Scatter/Gather 在需要将传输数据分成多个部分处理的场合非常有用。例如，一个消息由消息头和消息体两部分组成，
你可能将消息头和消息体分散到不同的 Buffer 中，这样一来，你可以很方便的单独处理消息头和消息体的数据。

### Scatter Reads

Scatter read 是指从一个 Channel 中读取的数据分散到多个 Buffer 中。如图所示：   
![Scatter read](./images/scatter.png)   

代码示例如下：
```
ByteBuffer header = ByteBuffer.allocate(128);
ByteBuffer body   = ByteBuffer.allocate(1024);
ByteBuffer[] bufferArray = { header, body };
channel.read(bufferArray);
```

注意，首先将多个 Buffer 插入到数组中，然后把数组当作参数传递给 channel.read() 方法。read() 
方法将按照 Buffer 在数组中的顺序将 Channel 中的数据读到 Buffer 中，一旦一个 Buffer 被写满，
Channel 则继续向下一个 Buffer 中写。

实际上 Scatter read 必须先写满当前 Buffer 才会转向下一个 Buffer，这也意味着对于动态消息（消
息内容大小不固定）是不适合的。换句话说，如果传输数据分为消息头和消息体，消息头的大小是固定的(例如
128字节)，Scatter read 才能正常工作。

### Gather Writes

Gather write 是指将多个 Buffer 中的数据写入到一个 Channel 中，如图所示：   
![Gather write](./images/gather.png)   

代码示例如下：  
```
ByteBuffer header = ByteBuffer.allocate(128);
ByteBuffer body   = ByteBuffer.allocate(1024);

//write data into buffers

ByteBuffer[] bufferArray = { header, body };
channel.write(bufferArray);
```

Buffer 数组当作参数传递给 write 方法，write() 方法将按照 Buffer 在数组中的顺序将数据写入到 
Channel 中，注意，只有 position 到 limit 之间的数据才能被写入到 Channel 中。因此，如果一个
Buffer 的容量是 128 个字节，但是仅仅包含 58 个字节的数据，那么只有这 58 个字节的数据被写入到
Channel 中，因此与 Scatter read 相反，Gather write 可以较好的处理动态消息。


