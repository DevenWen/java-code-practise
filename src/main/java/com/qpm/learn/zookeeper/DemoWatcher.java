package com.qpm.learn.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

/**
 * @author kangqiang.w
 * @Date 2018/3/31
 **/
public class DemoWatcher implements Watcher {

    ZooKeeper zk;
    String hostPort;

    DemoWatcher(String hostPort) {
        this.hostPort = hostPort;
    }

    void startZK() throws Exception{
        zk = new ZooKeeper(hostPort, 15000, this);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println(watchedEvent);
    }

    public static void main(String[] args) throws Exception {

        DemoWatcher m = new DemoWatcher("localhost:2181");

        m.startZK();

        Thread.sleep(60000);
    }
}
