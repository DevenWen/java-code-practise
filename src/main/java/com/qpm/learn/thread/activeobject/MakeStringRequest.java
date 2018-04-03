package com.qpm.learn.thread.activeobject;

/**
 * @author kangqiang.w
 * @Date 2018/3/11
 **/
public class MakeStringRequest extends MethodRequest<String> {

    private final int count;

    private final char fillchar;

    public MakeStringRequest(Servant servant, FutureResult<String> future, int count, char fillchar) {
        super(servant, future);
        this.count = count;
        this.fillchar = fillchar;
    }

    @Override
    public void execute() {
        Result<String> result = servant.makeString(count, fillchar);
        future.setResult(result);
    }
}
