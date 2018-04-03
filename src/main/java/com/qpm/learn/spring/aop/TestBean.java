package com.qpm.learn.spring.aop;

/**
 * @author kangqiang.w
 * @Date 2018/3/25
 **/
public class TestBean {

    private String testStr = "testStr";

    public String getTestStr() {
        return testStr;
    }

    public void setTestStr() {
        this.testStr = testStr;
    }

    public void test() {
        System.out.println("test");
    }

}
