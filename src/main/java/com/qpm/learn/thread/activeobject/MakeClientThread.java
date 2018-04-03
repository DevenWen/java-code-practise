package com.qpm.learn.thread.activeobject;

/**
 * @author kangqiang.w
 * @Date 2018/3/11
 **/
public class MakeClientThread extends Thread {

    private final ActivityObject activityObject;

    private final char fillchar;

    public MakeClientThread(String name, ActivityObject activityObject) {
        super(name);
        this.activityObject = activityObject;
        this.fillchar = name.charAt(0);
    }

    @Override
    public void run() {
        try {
            for (int i = 0; true; i++) {
                Result<String> result = activityObject.makeString(i, fillchar);
                Thread.sleep(10);
                String value = result.getResultValue();
                System.out.printf(Thread.currentThread().getName() + ": value = " + value);
            }
        } catch (InterruptedException e) {
        }
    }
}
