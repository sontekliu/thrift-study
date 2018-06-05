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
        ByteBuffer buffer = ByteBuffer.allocate(36);

        int len = fileChannel.read(buffer);
        String str = "";
        int LF = 10; // 回车
        int CR = 13; // 换行
        byte[] temp = new byte[0];
        byte[] lineByte = new byte[0];
        while(-1 != len){
            buffer.rewind();
            int size = buffer.remaining();
            byte[] bytes = new byte[size];
            buffer.get(bytes);
            buffer.clear();

            int lineStartIndex = 0;
            boolean hasLF = false;
            for (int i=0;i<size;i++){
                byte b = buffer.get(i);
                if(LF == b){
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
            //buffer.flip();
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
