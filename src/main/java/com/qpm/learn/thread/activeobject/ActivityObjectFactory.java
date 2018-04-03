package com.qpm.learn.thread.activeobject;

/**
 * @author kangqiang.w
 * @Date 2018/3/11
 **/
public class ActivityObjectFactory {

    public static ActivityObject createActivityObject() {
        Servant servant = new Servant();
        ActivationQueue queue = new ActivationQueue();
        SchedulerThread thread = new SchedulerThread(queue);
        Proxy proxy = new Proxy(thread, servant);
        thread.start();
        return proxy;
    }

}
