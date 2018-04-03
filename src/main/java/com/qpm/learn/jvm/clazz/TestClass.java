package com.qpm.learn.jvm.clazz;

/**
 * @author kangqiang.w
 * @Date 2018/2/7
 **/
public class TestClass {

    private int m;

    public int inc() {
        int x;
        try {
            x = 10;
            return x;
        } catch (Exception e) {
            x = 20;
            return x;
        } finally {
            x = 30;
        }
    }

}
