package com.qpm.learn.thread.guardedsuspension;

import com.sun.org.apache.regexp.internal.RE;

/**
 * Created by qpm on 2017/10/21.
 */
public class Main {

    public static void main(String[] args) {
        RequestQueue requestQueue = new RequestQueue();
        new ClientThread(requestQueue, "Alice", 3142592L).start();
        new ServerThread(requestQueue, "Bobby", 6571349L).start();
    }
}
