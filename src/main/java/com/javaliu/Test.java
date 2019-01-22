package com.javaliu;

/**
 * <br> 类名：Test
 * <br> 描述：
 * <br> 作者：sontek
 * <br> 创建：2018年05月31日
 * <br> 版本：V1.0.0
 */
public class Test {

    public static void main(String[] args) {
        System.out.println(-1>>>0);
        // 1000 0000 0000 0001
        // 0011 1111 1111 1111
        // 1100 0000 0000 0001
        // 0011 1111 1111 1111 1111 1111 1111 1111
        System.out.println(-1>>>2);
        System.out.println(1<<3);
        System.out.println(1<<4);
        System.out.println(4 & 5);
    }
}
