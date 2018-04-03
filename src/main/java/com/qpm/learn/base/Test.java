package com.qpm.learn.base;

import javax.xml.bind.SchemaOutputResolver;


public class Test {
    private static void changeValue(int[] arr) {
        for (int i = 0; i <  arr.length; i++) {
            arr[i] *= 2;
        }
    }
    public static void main(String[] args) {
        int[] arr = {1,2,3,4,5};
        changeValue(arr);

        int x = 2;
        do {
            x += x;
        } while (x < 17);
        System.out.println(x);

    }
}
