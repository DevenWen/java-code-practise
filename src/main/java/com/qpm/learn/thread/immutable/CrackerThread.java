package com.qpm.learn.thread.immutable;

/**
 * Created by qpm on 2017/10/21.
 */
public class CrackerThread extends Thread {

    private final MutablePerson mutable;

    public CrackerThread(MutablePerson m) {
        this.mutable = m;
    }

    @Override
    public void run() {
        while (true) {
            ImmutablePerson immutable = new ImmutablePerson(mutable);
            if (!immutable.getName().equals(immutable.getAddress())) {
                System.out.println(currentThread().getName() + " ***BROKEN*** " + immutable);
                return;
            }
        }
    }
}
