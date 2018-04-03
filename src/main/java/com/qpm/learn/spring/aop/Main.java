package com.qpm.learn.spring.aop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author kangqiang.w
 * @Date 2018/3/25
 **/
public class Main {


    public static void main(String[] args) {
        ApplicationContext bf = new ClassPathXmlApplicationContext("aspectTest.xml");
        TestBean tb = bf.getBean(TestBean.class);

        tb.test();
    }

}
