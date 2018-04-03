package com.qpm.learn.thread.guardedsuspension;

import lombok.Getter;
import lombok.ToString;

/**
 * Created by qpm on 2017/10/21.
 */
@ToString
public class Request {

    @Getter
    private final String name;

    public Request(String name) {
        this.name = name;
    }

}
