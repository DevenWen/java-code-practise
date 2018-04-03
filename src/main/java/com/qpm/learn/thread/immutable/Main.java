package com.qpm.learn.thread.immutable;

/**
 * Created by qpm on 2017/10/21.
 */
public class Main {

    public static void main(String[] args) {
        MutablePerson mutalbe = new MutablePerson("start", "start");
        new CrackerThread(mutalbe).start();
        new CrackerThread(mutalbe).start();
        new CrackerThread(mutalbe).start();

        for (int i = 0; true; i++) {
            mutalbe.setPersion("" + i, "" + i);
        }

    }

}
