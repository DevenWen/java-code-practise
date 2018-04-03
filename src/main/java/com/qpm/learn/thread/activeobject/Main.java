package com.qpm.learn.thread.activeobject;

/**
 * @author kangqiang.w
 * @Date 2018/3/11
 **/
public class Main {

    public static void main(String[] args) {
        ActivityObject activityObject = ActivityObjectFactory.createActivityObject();
        new MakeClientThread("alic", activityObject).start();
        new MakeClientThread("boby", activityObject).start();
        new DisplayClientThread("chris", activityObject).start();
    }

}
