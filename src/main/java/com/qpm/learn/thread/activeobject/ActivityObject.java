package com.qpm.learn.thread.activeobject;

/**
 * 主动对象方
 *
 * @author kangqiang.w
 * @Date 2018/3/11
 **/
public interface ActivityObject {

    Result<String> makeString(int count, char fillchar);

    void displayString(String string);

}
