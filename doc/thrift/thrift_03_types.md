# Thrift 的数据类型

`Thrift` 类型包括一些预定义的基本数据类型，自定义的结构体，容器类型，异常和服务类型等。

#### 基本数据类型

- bool  boolean 类型，只有 `true` 和 `false` 两个值，占用一个字节
- byte  单字节
- i16   有符号 16 位整型，相当于 `java` 的 `Short`
- i32   有符号 32 位整型，相当于 `java` 的 `Integer`
- i64   有符号 64 位整型，相当于 `java` 的 `Long`
- double 64 位的浮点数
- string 未知编码的文本或二进制字符串

注意，`Thrift` 不支持无符号整型，因为有的语言不支持无符号整型，`Thrift` 或者说 `RPC` 框架支持
的语言越多，对应的基本数据类型越少，因为不同的语言所支持的数据类型不一致，`RPC` 框架要兼容其交集。

#### 容器类型

`Thrift` 的容器是强类型的容器，它映射到流行语言中最常用的容器。它们使用 `Java` 泛型风格进行表示，
有如下三种类型的容器：
- list\<t1\>    包含 `t1` 类型元素的**有序**列表，`t1` 可重复
- set\<t1\>     包含 `t1` 类型元素的**无序**集合，`t1` 不可重复
- map\<t1, t2\> 键值对映射，`t1` 表示 `key`，`t2` 表示 `value`

容器中的类型可以是基本数据类型，结构体(struct)，异常(exceptions)，但是不包括服务(services)

#### 结构体和异常

`Thrift` 的结构体与 `C` 语言的结构体从概念上很相似，都是将一些相关的元素集合到一块。在面向对象
的语言中，结构体被翻译成类(classes)。

`Exceptions` 在语法构成与功能上等同于 `struct`，只不过此处是使用 `exception` 关键字。
`exception` 与 `struct` 从语义上的不同在于，当定义一个 `RPC Services` 的时候，开发
者可能会对接口方法声明抛出一个异常(exception)而不是 `struct`。

#### 服务(services)

`Service` 定义等同于面向对象语言中的接口(interface)或者是一个纯粹的抽象类（即没有方法实现），
`Thrift` 编译器根据这个 `interface` 生成拥有完整功能的客户端和服务端。

#### 类型定义

`Thrift` 支持 `C/C++` 风格的类型定义，例如：
```
typedef i32 int         // 结尾可以没有分号
typedef Tweet  ReTweet  // 结构体(Struct)也可以用于类型定义
```

#### 枚举

当你在定义一个结构体类型，里面的某个字段值，是几个提前预定义好的值的话，此时就适合使用枚举类型。例如：

```
enum TweetType {
    TWEET,       # 枚举被指定为 C 语言风格，编译器指定默认值是从 0 开始
    RETWEET = 2, # 当然也可以指定一个整型常量
    DM = 0xa,    # 十六进制也是可以的
    REPLY
}

struct Tweet {
    1: required i32 userId;
    2: required string userName;
    3: required string text;
    4: optional Location loc;
    5: optional TweetType tweetType = TweetType.TWEET # 指定默认值时，需要使用枚举的全名
    ...
    16: optional string language = "english"
}
```
注意：与 `Protocol Buffers` 不同的是，`Thrift` 不支持嵌套的枚举，在这方面，`struct` 也不支持嵌套。
枚举常量值必须在32位整数的范围以内

#### 注释

`Thrift` 支持 `Shell`、`C` 语言风格的多行注释和 `Java`，`C++` 风格的单行注释。如：

```
# This is a valid comment.

/*
 * This is a multi-line comment.
 * Just like in C.
 */
 
// C++/Java style single-line comments work just as well.
```

