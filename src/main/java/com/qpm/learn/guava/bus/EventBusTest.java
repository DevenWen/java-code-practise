package com.qpm.learn.guava.bus;

import com.google.common.eventbus.EventBus;

/**
 * @author kangqiang.w
 * @Date 2018/2/9
 **/
public class EventBusTest {

    public static void main(String[] args) {
        EventBus bus = new EventBus();
        bus.register(new EventBusChangeRecorder());
        bus.post(new TestEvent(1, "测试时间"));
        System.out.println("发送完毕");
    }

}
