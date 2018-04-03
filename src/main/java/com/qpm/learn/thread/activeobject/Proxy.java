package com.qpm.learn.thread.activeobject;

/**
 * @author kangqiang.w
 * @Date 2018/3/11
 **/
class Proxy implements ActivityObject {

    private final SchedulerThread scheduler;
    private final Servant servant;

    public Proxy(SchedulerThread scheduler, Servant servant) {
        this.scheduler = scheduler;
        this.servant = servant;
    }

    @Override
    public Result<String> makeString(int count, char fillchar) {
        FutureResult<String> future = new FutureResult<>();
        scheduler.invoke(new MakeStringRequest(servant, future, count, fillchar));
        return future;
    }

    @Override
    public void displayString(String string) {
        scheduler.invoke(new DisplayStringRequest(servant, string));
    }
}
