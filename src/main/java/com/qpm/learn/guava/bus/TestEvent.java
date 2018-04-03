package com.qpm.learn.guava.bus;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author kangqiang.w
 * @Date 2018/2/9
 **/
@Data
@AllArgsConstructor
public class TestEvent {

    private int i;
    private String desc;

}
