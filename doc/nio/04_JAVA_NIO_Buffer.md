# JAVA NIO Buffer 

Java NIO Buffer 是被用来与 Channel 一起工作的，正如你所知道的那样，数据是从 Channel 读到 Buffer，从
Buffer 中写到 Channel。

Buffer 本质上是即可以写入数据，又可以从中读取数据的内存块，这个内存块被 Java NIO Buffer 所包装，它提供了一
系列方法，从而使得对内存块的操作变得很方便。

### Buffer 的基本使用

Buffer 数据的读写一般要有如下 4 步：

> 1. 将数据写入 Buffer
> 2. 调用 buffer.flip() 方法
> 3. 将数据从 Buffer 中读取出来
> 4. 调用 buffer.clear() 或者 buffer.compat()方法

当你将数据写入一个 Buffer 的时候，Buffer 记录了你一共写了多少数据，一旦你需要读取数据，你需要使用 flip() 
将 Buffer 从写入模式切换到读取模式。在读取模式中，你可以读取所有已被写入的数据。

一旦你读完所有的数据，你需要清空 Buffer 以便让其再次写入。你可以有两种方式清空数据：调用 buffer.clear()
方法或者 buffer.compact() 方法。clear() 清空整个 buffer 区域，compact() 方法仅仅清除已读过的数据
还未读取的数据将被移动到 Buffer 的起始位置，将从未读数据的末尾开始写入新的数据。
下面是一个 Buffer 的简单使用示例，注意：write, flip, read 和 clear 方法:

```
    RandomAccessFile aFile = new RandomAccessFile("data/nio-data.txt", "rw");
    FileChannel inChannel = aFile.getChannel();

    //create buffer with capacity of 64 bytes
    ByteBuffer buf = ByteBuffer.allocate(64);

    int bytesRead = inChannel.read(buf); //read into buffer.
    while (bytesRead != -1) {
        buf.flip();  //make buffer ready for read

        while(buf.hasRemaining()){
            System.out.print((char) buf.get()); // read 1 byte at a time
        }
        buf.clear(); //make buffer ready for writing
        bytesRead = inChannel.read(buf);
    }
    aFile.close();
```

### Buffer Position, Limit 和 Capacity

你必须熟悉 Buffer 的三个属性，才能更好的理解 Buffer 如何工作的：

> position  
> limit  
> capacity  

`position` 和 `limit` 根据 buffer 处在读模式或者写模式的不同而表示不同的含义，`capacity` 所表示
的含义是不变的，不因 buffer 模式的不同而不同。

下面是 Buffer 读写模式 `capactity`, `position`, `limit` 的示意图  
![Buffer 读写模式](./images/buffers-modes.png)

### Capacity
作为一个内存区域，Buffer 有一个固定的大小值，也可也称之为 `capacity`。你只能向 buffer 中写入 capacity 
个byte、long、char 等类型，一旦 Buffer 被写满，你必须先清空它(通过读取数据或者调用clear方法) 才能向里面
写入更多的数据

### Position
当你向一个 Buffer 中写入数据时，你必须找到一个确定的位置，position 的初始值是0，当一个 byte、long等类型被写
入 Buffer 时，position 会向前移动到下一个被插入数据的 Buffer 单元。position 的最大值是 capactity - 1

当你从 Buffer 中读取数据时，也是从一个特定的位置读。当你将 Buffer 从写模式转换到读模式时，
position 被重置为0，当你从 Buffer 的 position 处读取数据时，position 向前移动到下一个
可读的位置。

### Limit
在写模式下，Buffer 中的 limit 表示你最多能往 Buffer 里写入数据的数量，在写模式下，limit 等于
Buffer 的 capacity。

当将 Buffer 反转成读模式时，limit 表示你可以读到多少数据，因此，将 Buffer 转换为读模式时，
limit 被设置成为写模式时候的 position 的值，换句话说，你可以读取到之前写入的所有数据（limit 被
设置为写数据的值，这个值在写模式下就是 position）

