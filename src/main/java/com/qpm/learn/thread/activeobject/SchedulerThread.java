package com.qpm.learn.thread.activeobject;

/**
 * @author kangqiang.w
 * @Date 2018/3/11
 **/
public class SchedulerThread  extends Thread{
    private final ActivationQueue queue;

    public SchedulerThread(ActivationQueue queue) {
        this.queue = queue;
    }

    public void invoke(MethodRequest request) {
        queue.putRequest(request);
    }

    @Override
    public void run() {
        while (true) {
            MethodRequest request = queue.takeRequest();
            request.execute();
        }
    }
}
