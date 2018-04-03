package com.qpm.learn.thread.activeobject;

/**
 * @author kangqiang.w
 * @Date 2018/3/11
 **/
public class RealResult<T> extends Result<T> {

    private final T resultValue;

    public RealResult(T resultValue) {
        this.resultValue = resultValue;
    }

    @Override
    public T getResultValue() {
        return resultValue;
    }
}
