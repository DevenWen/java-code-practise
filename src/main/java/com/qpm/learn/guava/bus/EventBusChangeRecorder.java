package com.qpm.learn.guava.bus;

import com.google.common.eventbus.Subscribe;

import javax.swing.event.ChangeEvent;

/**
 * @author kangqiang.w
 * @Date 2018/2/9
 **/
public class EventBusChangeRecorder {

    @Subscribe
    public void recordCustomerChange(TestEvent e) {
        System.out.println("get changeEvent!" + e.toString());
    }

}
