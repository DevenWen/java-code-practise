package com.qpm.learn.thread.activeobject;

/**
 * @author kangqiang.w
 * @Date 2018/3/11
 **/
public class DisplayStringRequest extends MethodRequest<Object> {
    private final String string;

    public DisplayStringRequest(Servant servant, String string) {
        super(servant, null);
        this.string = string;
    }

    @Override
    public void execute() {
        servant.displayString(string);
    }
}
