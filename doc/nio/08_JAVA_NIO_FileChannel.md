# JAVA NIO FileChannel

Java NIO FileChannel 是链接文件的通道，使用文件 Channel 你可以从文件中读取数据，也可也
将数据写入到文件中，Java NIO FileChannel 是替代标准的 Java IO 读取文件的一种方式。

### Opening a FileChannel
在使用 FileChannel 之前，你必须先打开它，但是你不能直接打开它，需要通过 InputStream,
OutputStream 或者 RandomAccessFile 来获取 FileChannel 的实例，如下是通过RandomAccessFile
来获取 FileChannel 的示例：
```
RandomAccessFile aFile     = new RandomAccessFile("data/nio-data.txt", "rw");
FileChannel      inChannel = aFile.getChannel();
```

### Reading Data from a FileChannel
你可以调用 FileChannel 的 read() 方法从 FileChannel 中读取数据，代码如下：
```
ByteBuffer buf = ByteBuffer.allocate(48);
int bytesRead = inChannel.read(buf);
```
首先创建一个 Buffer，从 FileChannel 读取的数据被读取到 Buffer 中。   
第二，调用 FileChannel.read() 方法，该方法将数据从 FileChannel 读取到 Buffer 中，
read() 方法返回的 int 值则表示有多少字节的数据被写入到 Buffer 中，如果返回值是 -1，则
表示到了文件末尾。

### Writing Data to a FileChannel
将数据写入到 FileChannel 中是通过 FileChannel.write() 方法来完成的，该方法的参数是
一个 Buffer。代码示例如下：
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

注意，FileChannel.write() 方法是在一个 While 循环中调用的，因为无法保证 write() 方法可以
向 FileChannel 中写入多少字节的数据，因此需要重复调用 write 方法，直到没有 Buffer 中没有可
以被写入 FileChannel 的数据。

### Closing a FileChannel
当你用完 FileChannel 时，必须关闭它，关闭代码如下：
```
channel.close();    
```

### FileChannel Position
有时你可能会从文件的一个特殊的位置，进行读取和写入操作，你可以通过调用 FileChannel 的 position
方法来获取到 FileChannel 的当前位置。

你也可以调用 position(long pos) 方法来设定 FileChannel 的位置。

代码示例如下：
```
long pos channel.position();
channel.position(pos +123);
```
如果你设置 position 的位置为文件的末尾之后，然后试图从 FileChannel 中读取数据，读取方法将返回 -1 --
文件结尾的标识。

如果你设置 position 的位置为文件的末尾之后，然后向 FileChannel 中写入数据，文件将撑大到当前位置，并写入
数据，这可能导致“文件空洞”，磁盘上物理文件中写入的数据间有空隙。

### FileChannel Size
FileChannel 对象的 size() 方法将返回通道所关联的文件的大小，代码如下：
```
long fileSize = channel.size();    
```

### FileChannel Truncate
你可以调用 FileChannel.truncate() 方法截取一个文件，截取文件时，文件将指定长度后面的部分删除。
代码示例如下：
```
channel.truncate(1024);
```
该示例是截取文件的前 1024 个字节。

### FileChannel Force
FileChannel.force() 方法将 Channel 中还未写入磁盘的数据强制写到磁盘中，出于性能的考虑，操作系
统可能将数据缓存在内存中，所以在调用 force() 方法之前不能保证之前被写入 Channel 的数据一定会及时
的写入到磁盘中。

force() 方法接收一个 Boolean 类型的参数，指名是否将文件的元数据信息(权限等)也一块写到磁盘。

如下示例将数据与文件元数据一块写入到磁盘：
```
channel.force(true);
```



