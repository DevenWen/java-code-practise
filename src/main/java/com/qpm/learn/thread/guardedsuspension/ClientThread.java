package com.qpm.learn.thread.guardedsuspension;/**
 * Created by qpm on 2017/10/21.
 */

import org.omg.PortableServer.THREAD_POLICY_ID;

import java.util.Random;

/**
 * @author qpm
 * @Date 2017/10/21
 **/
public class ClientThread extends Thread {

    public static final int COUNT = 10000;

    private final Random random;
    private final RequestQueue requestQueue;

    public ClientThread(RequestQueue requestQueue, String name, long seed) {
        super(name);
        this.requestQueue = requestQueue;
        this.random = new Random(seed);
    }

    @Override
    public void run() {
        for (int i = 0; i < COUNT; i++) {
            Request request = new Request("No." + i);
            System.out.println(Thread.currentThread().getName() + " requests " + request);
            requestQueue.putRequest(request);
            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {

            }
        }
    }
}
