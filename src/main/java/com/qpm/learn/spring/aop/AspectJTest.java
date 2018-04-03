package com.qpm.learn.spring.aop;

import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author kangqiang.w
 * @Date 2018/3/25
 **/
@Aspect
public class AspectJTest {

    @Pointcut("execution(* *.test(..))")
    public void test(){}

    @Before("test()")
    public void beforeTest() {
        System.out.println("beforeTest");
    }

    @After("test()")
    public void afterTest() {
        System.out.println("afterTest");
    }
}
