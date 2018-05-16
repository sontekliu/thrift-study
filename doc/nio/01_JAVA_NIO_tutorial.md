# JAVA NIO 

Java NIO (New IO) 从 JDK 1.4 开始提供了一种可选择的 IO API。可选择的含义是针对 Java 标准 IO 和 Java
网络 IO 而言的。Java NIO 提供了一种相比于标准 IO 不同的处理 IO 的方式。

### Java NIO: 通道和缓冲 (Channels and Buffers)

传统的 IO API 针对的是字节流和字符流的，而 NIO 针对的是 channels 和 buffers。数据的读操作是
从 channel 到 buffer，写操作是从 buffer 到 channel。

### Java NIO: 非阻塞 IO (Non-blocking IO)

Java NIO 提供了非阻塞 IO 的功能。例如，一个线程可以请求一个 channel 读取数据到 buffer
在 channel 读取数据到 buffer 的过程中，这个线程可以做一些其他事情。一旦数据被读入到 buffer
中，线程转而可以继续处理 buffer 中的数据。数据的写入操作和上面的数据读取操作相同。

### Java NIO: 选择器 (Selectors)

Java NIO 包含了“选择器”的概念。选择器是一个可以监视多个事件（如：打开链接，数据到达等）通道
的对象，因此，一个线程可以监视多个数据通道。

