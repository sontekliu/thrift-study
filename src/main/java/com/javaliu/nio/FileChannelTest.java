package com.javaliu.nio;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
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
    public static void readData() throws Exception{
        RandomAccessFile randomAccessFile = new RandomAccessFile("text.txt", "r");
        FileChannel fileChannel = randomAccessFile.getChannel();
        // hello World
        ByteBuffer buffer = ByteBuffer.allocate(12);

        int len = fileChannel.read(buffer);
        String str = "";
        int LF = 10;
        byte[] temp = new byte[0];
        while(-1 != len){
            buffer.flip();
            int size = buffer.remaining();
            byte[] bytes = new byte[size];
            boolean hasLF = false;

            int start = 0;
            int index = 0;
            while (buffer.hasRemaining()){
                index++;
                byte b = buffer.get();
                if(b == LF){
                    hasLF = true;
                }
                if(hasLF){
                    temp = new byte[index-start];
                    System.arraycopy(bytes, start, temp, 0, temp.length);
                }
            }
            buffer.flip();
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
