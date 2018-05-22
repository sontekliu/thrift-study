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

### Buffer Type

Java NIO 提供了如下 Buffer 类型：

* ByteBuffer
* MappedByteBuffer
* CharBuffer
* DoubleBuffer
* FloatBuffer
* IntBuffer
* LongBuffer
* ShortBuffer

如上所示，这些 Buffer 类型代表了不同的数据类型，换句话说，它们允许你使用 char, short, int, long,
float 或 double 来处理缓冲区的字节。  
MappedByteBuffer有点特别，并且将在其自己的文本中进行介绍。

### 分配缓冲区
要获得一个 Buffer 对象，你必须先分配它，每个 Buffer 类都有一个 allocate() 方法来分配 Buffer
对象
如下示例展示了分配一个拥有 48 字节的 ByteBuffer
```
    ByteBuffer buf = ByteBuffer.allocate(48);
```
如下示例展示了分配一个 1024 个字符的 CharBuffer
```
    CharBuffer buf = CharBuffer.allocate(1024);
```

### 向 Buffer 中写入数据
你可以有两种方式向 Buffer 中写入数据：
1. 使用 Channel 向 Buffer 中写入数据
2. 通过 Buffer 的 put() 方法向 Buffer 中写入数据  

使用 Channel 写入数据的示例：
```
int bytesRead = inChannel.read(buf); //read into buffer.
```
通过 put() 方法写入数据的示例
```
buf.put(127);    
```
有很多重载的 put() 方法，你可以通过很多不同的方式向 Buffer 中写入数据。例如，向指定的位置
写入数据，将一个字节数组写入 Buffer 等。可以查看 JavaDoc 文档查看更多细节。

### flip 方法
flip 方法可以使 Buffer 由写模式切换到读模式，调用 flip 方法，将 position 的值设置为 0，将
limit 的值设置为 position 原来的值。源码如下：
```
public final Buffer flip() {
    limit = position;
    position = 0;
    mark = -1;
    return this;
}
```
换句话说，position 标记了读取位置，limit 标记了 Buffer 中被写入了多少个字节或字符等 ——现在能读取多少个字节或字符等

### 从 Buffer 中读取数据
有两种方式可以实现从 Buffer 中读取数据：
1. 将 Buffer 中的数据读入到 Channel 中
2. 使用 Buffer 的 get 方法读取数据

如下示例展示了如何将数据读入到 Channel 中
```
//read from buffer into channel.
int bytesWritten = inChannel.write(buf);
```
使用 get() 方法读取数据的例子
```
byte aByte = buf.get();  
```
有很多重载的 get() 方法，你可以使用不同的方式从 Buffer 中读取数据，例如，从指定位置读取数据，从 Buffer 中读取一个
字节数组等。可以查看 JavaDoc 文档查看更多详细信息。

### 回放(rewind)
Buffer 的 rewind() 方法，设置 position 的值为0，你可以重新读取 Buffer 中的数据，limit 的值不变，仍然标记了
可以从 Buffer 中读取多少个元素(字节或字符等)。源码如下：
```
public final Buffer rewind() {
    position = 0;
    mark = -1;
    return this;
}
```

### clear() 和 compact()
一旦你读完 Buffer 中的数据，你需让 Buffer 准备好再次被写入，你可以通过调用 clear() 或者 compact() 来完成。

如果你调用 clear() 方法，position 的值被设置成 0，limit 的值设置为 capacity 的值，换句话说，Buffer 被清
空了。但是 Buffer 中的数据没有被清除，这些标记仅仅告诉我们可以从哪里开始写入数据。

如果 Buffer 中还有未读的数据，调用了 clear() 方法，那些数据将被“遗忘”，这就是意味着没有任何标记告诉你，哪儿些
数据已被读过，哪儿些还未被读过。

如果 Buffer 中仍有未读的数据，并且后续还需要读取，但是此时想写入数据，那么调用 compact() 方法而不是 clear() 

compact() 方法将未读的数据拷贝到 Buffer 中的开始位置，然后将 position 的的值设置为右边最后一个未读元素的位置
limit 的值仍然与 capacity 的值一样，就像 clear() 方法那样。现在可以继续向 Buffer 中写入数据了，但是之前未读
数据不会被覆盖掉。

### mark() 和 reset()
你可以通过调用 mark() 方法标记一个给定的 position 的位置，之后可以调用 buffer.reset() 方法恢复到之前标记的
position 位置。例如：
```
buffer.mark();
//call buffer.get() a couple of times, e.g. during parsing.
buffer.reset();  //set position back to mark.    
```

### equals() and compareTo()
可以使用 equals() 和 compareTo() 方法比较两个 Buffer。  
##### equals()
对于equals() 方法来说，如果满足如下条件，则表示两个 Buffer 相等。
> 它们两个拥有相同的类型(字节，字符，整型等)
> 它们所剩余相同数量的字节，字符等
> 所有剩余的字节，字符均相等

如你所见，equals 仅仅比较了 Buffer 中的部分元素，并不是 buffer 中的每一个元素。事实上，仅仅比较的是 Buffer 中
剩余的元素。

##### compareTo()
compareTo() 方法比较的是两个 Buffer 中剩余的元素(字节，字符等)。如果满足如下条件，则认为一个 Buffer 小于 另
一个：
1. 第一个不相等的元素小于另一个 Buffer 中对应的元素。
2. 所有的元素都相等，但是第一个 Buffer 比第二个 Buffer 先耗尽(第一个元素个数比第二个少)




