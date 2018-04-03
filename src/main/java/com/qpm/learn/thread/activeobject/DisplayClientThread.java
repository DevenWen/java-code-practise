package com.qpm.learn.thread.activeobject;

/**
 * @author kangqiang.w
 * @Date 2018/3/11
 **/
public class DisplayClientThread extends Thread {
    private final ActivityObject activityObject;

    public DisplayClientThread(String name, ActivityObject activityObject) {
        super(name);
        this.activityObject = activityObject;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; true; i++) {
                String string = Thread.currentThread().getName() + " " + i;
                activityObject.displayString(string);
                Thread.sleep(200);
            }
        } catch (InterruptedException e) {
        }
    }
}
