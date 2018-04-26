# Thrift 安装

`Thrift` 作为 `RPC` 的通讯框架，有很多的优势，但是唯一不足的是，`Thrift` 官网并没有提供
良好的文档，本文作为一个指南性教程，一步一步教如何快速掌握 `Thrift` 的使用以及其优良的特性。

`Thrift` 安装比较复杂，本文仅仅以 `Linux` 的发行版 `CentOS 6` 为示例，演示安装过程。

使用 `Thrift` 肯定是以某种语言为依托的，本文使用的语言是 `Java` (1.7 以及以上版本)、
`Python` (2.6及以上)，在后续的示例中会以`Python` 和 `Java` 为示例，分别作为服务端
或客户端，完成跨语言的调用。所以在安装 `Thrift` 之前，必须先安装 `Python` 和 `JDK`。
`Python` 操作系统默认安装，此处不再多说，至于 `Java` 的依赖包 `JDK` 网上有很多安装教程，
请自行百度或者Google。此处重点关注 `Thrift` 的安装。

1. 首先，安装一些必要的工具和包：
```
    # yum -y install ant  // java 需要
    
    # yum -y update
    
    # yum install -y wget
    
    # yum install libboost-dev libboost-test-dev libboost-program-options-dev libboost-filesystem-dev libboost-thread-dev libevent-devautomake libtool flex bison pkg-config g++ libssl-dev

    
    
```

2. Thrift 安装

```
    # tar -zxvf thrift-0.11.0.tar.gz
```

