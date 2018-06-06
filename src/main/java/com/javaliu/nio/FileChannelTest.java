package com.javaliu.nio;


import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * <br> 类名：FileChannelTest
 * <br> 描述：
 * <br> 作者：sontek
 * <br> 创建：2018年06月04日
 * <br> 版本：V1.0.0
 */
public class FileChannelTest {

    public static void main(String[] args) throws Exception{

        //writeData();
        readData();
        //System.out.println(new String(new byte[]{49,124,49,50,51,124,-27,-68,-96,-28,-72,-119,13,10}, "UTF-8"));
/*        System.out.println(new String(new byte[]{50,124,49,50,52,124,-27,-114,-122,-27,-113,-78,10}, "UTF-8"));
        System.out.println(new String(new byte[]{51,124,49,50,56,124,-25,-114,-117,-28,-70,-108,10}, "UTF-8"));
        System.out.println(new String(new byte[]{52,124,49,50,53,124,-24,-75,-75,-27,-123,-83,10}, "UTF-8"));
        System.out.println(new String(new byte[]{53,124,49,50,54,124,-23,-87,-84,-28,-72,-119,10}, "UTF-8"));
        System.out.println(new String(new byte[]{54,124,49,50,55,124,-26,-75,-117,-24,-81,-107,10}, "UTF-8"));*/
        //truncateFile();
    }

    // 写数据
    public static void writeData() throws Exception{
        RandomAccessFile randomAccessFile = new RandomAccessFile("text.txt", "rw");

        FileChannel fileChannel = randomAccessFile.getChannel();

        String data = "hello Worldhello Worldhello Worldhello Worldhello Worldhello Worldhello World";
        ByteBuffer buffer = ByteBuffer.allocate(data.length());
        buffer.clear();
        buffer.put(data.getBytes());
        buffer.flip();
        while (buffer.hasRemaining()){
            fileChannel.write(buffer);
        }

        fileChannel.force(true);
        fileChannel.close();
    }

    // 读取数据

    /**
     * 读区文件的示例
     *
     * 1|123|张三
     * 2|124|历史
     * 3|128|王五
     * 4|125|赵六
     * 5|126|马三
     * 6|127|测试
     *
     * @throws Exception
     */
    public static void readData() throws Exception{
        RandomAccessFile randomAccessFile = new RandomAccessFile("text.txt", "r");
        FileChannel fileChannel = randomAccessFile.getChannel();
        // hello World
        ByteBuffer buffer = ByteBuffer.allocate(90);

        int len = fileChannel.read(buffer);
        String str = "";
        int CR = 13; // 回车
        int LF = 10; // 换行
        byte[] temp = new byte[0];
        byte[] lineByte = new byte[0];
        while(-1 != len){
            buffer.flip();                  // 切换模式
            int size = buffer.remaining();  // 读取到的字节数
            byte[] bytes = new byte[size];
            buffer.get(bytes);              // 将buffer 中的数据转移到 bytes 字节数组中
            buffer.clear();                 // 清空 buffer，即 position = 0, limit == capacity

            int lineStartIndex = 0;         // 记录每行的首行索引值
            boolean hasLF = false;
            for (int i=0;i<size;i++){
                byte b = buffer.get(i);
                if(LF == b || CR == b){                // 所读取的字节中是否有 换行
                    hasLF = true;
                    int tempSize = temp.length;
                    int lineNum = i - lineStartIndex;
                    lineByte = new byte[lineNum + tempSize];
                    System.arraycopy(temp, 0, lineByte, 0, temp.length);
                    temp = new byte[0];
                    System.arraycopy(bytes, lineStartIndex, lineByte, tempSize, lineNum);
                    System.out.println(new String(lineByte, "UTF-8"));
                    if(i+1 <size && bytes[i+1] == CR){
                        lineStartIndex = i + 2;
                    }else {
                        lineStartIndex = i + 1;
                    }
                }
            }
            if(hasLF){
                temp = new byte[size-lineStartIndex];
                System.arraycopy(bytes, lineStartIndex, temp, 0, temp.length);
            }else{
                byte[] toTemp = new byte[temp.length + bytes.length];
                System.arraycopy(temp, 0, toTemp, 0, temp.length);
                System.arraycopy(bytes, 0, toTemp, temp.length, bytes.length);
                temp = toTemp;
            }
            len = fileChannel.read(buffer);
        }
        fileChannel.close();
    }

    // 读取数据
    public static void truncateFile() throws Exception{
        RandomAccessFile randomAccessFile = new RandomAccessFile("text.txt", "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();
        FileChannel newFileChannel = fileChannel.truncate(3);
        // hello World
        ByteBuffer buffer = ByteBuffer.allocate(4);

        int len = newFileChannel.read(buffer);
        System.out.println(newFileChannel.position());
        while(-1 != len){
            buffer.flip();
            while (buffer.hasRemaining()){
                System.out.print((char) buffer.get());

            }
            System.out.println("");
            //buffer.clear();
            buffer.flip();
            len = newFileChannel.read(buffer);
            System.out.println(len);
        }
        fileChannel.close();
    }
}
