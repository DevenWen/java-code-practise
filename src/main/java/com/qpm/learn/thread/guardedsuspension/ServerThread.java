package com.qpm.learn.thread.guardedsuspension;/**
 * Created by qpm on 2017/10/21.
 */

import java.util.Random;

/**
 * @author qpm
 * @Date 2017/10/21
 **/
public class ServerThread extends Thread {

    private final Random random;
    private final RequestQueue requestQueue;

    public ServerThread(RequestQueue requestQueue, String name, long seed) {
        super(name);
        this.requestQueue = requestQueue;
        this.random = new Random(seed);
    }

    @Override
    public void run() {
        for (int i = 0; i < ClientThread.COUNT; i++) {
            Request request = requestQueue.getRequest();
            System.out.println(Thread.currentThread().getName() + " handles " + request);
            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {

            }
        }
    }
}
