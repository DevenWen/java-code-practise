package com.qpm.learn.jvm.clazz;

/**
 *
 * 单分派 多分派
 *
 * @author kangqiang.w
 * @Date 2018/2/27
 **/
public class Dispatch {

    static class QQ {}

    static class _360 {}

    public static class Father {
        public void hardChoice(QQ arg ) {
            System.out.println("father choice QQ");
        }

        public void hardChoice(_360 arg) {
            System.out.printf("father choice _360");
        }
    }

    public static class Son extends Father {
        @Override
        public void hardChoice(QQ arg) {
            System.out.printf("son choice QQ");
        }

        @Override
        public void hardChoice(_360 arg) {
            System.out.printf("son choice _360");
        }
    }

    public static void main(String[] args) {
        Father father = new Father();
        Son son = new Son();

        father.hardChoice(new _360());
        son.hardChoice(new QQ());
    }


}
