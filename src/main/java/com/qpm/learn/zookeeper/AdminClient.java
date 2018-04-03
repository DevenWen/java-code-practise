package com.qpm.learn.zookeeper;

import lombok.extern.log4j.Log4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.Date;

/**
 * @author kangqiang.w
 * @Date 2018/4/2
 **/
@Log4j
public class AdminClient implements Watcher {

    ZooKeeper zk;
    String hostport;

    AdminClient(String hostport) {
        this.hostport= hostport;
    }

    void start() throws Exception {
        zk = new ZooKeeper(hostport, 15000, this);
    }

    void listState() throws KeeperException, InterruptedException {
        try {
            Stat stat = new Stat();
            byte[] masterData = zk.getData("/master", false, stat);
            Date startDate = new Date(stat.getCtime());
            log.info("Master: " + new String(masterData) + " since " + startDate);
        } catch (KeeperException.NoNodeException e) {
            log.error("No Master");
        }

        log.info("Workers:");
        for (String w : zk.getChildren("/workers", false)) {
            byte data[] = zk.getData("/workers/" + w, false, null);
            String state = new String(data);
            log.info("\t" + w + ":" + state);
        }

        log.info("Tasks");
        for (String t : zk.getChildren("/tasks", false)) {
            log.info("\t" + t);
        }


    }

    @Override
    public void process(WatchedEvent event) {
        log.info(event.toString());
    }

    public static void main(String[] args) throws Exception{
        AdminClient c = new AdminClient(args[0]);
        c.start();
        c.listState();
    }
}
