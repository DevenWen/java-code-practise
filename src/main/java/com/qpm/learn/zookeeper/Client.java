package com.qpm.learn.zookeeper;

import lombok.extern.log4j.Log4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.AsyncCallback.*;
import org.apache.zookeeper.KeeperException.*;
import org.apache.zookeeper.data.Stat;

/**
 * @author kangqiang.w
 * @Date 2018/4/2
 **/
@Log4j
public class Client implements Watcher {

    ZooKeeper zk;
    String hostPort;

    Client(String hostPort) {
        this.hostPort = hostPort;
    }

    void startZk() throws Exception {
        zk = new ZooKeeper(hostPort, 15000, this);
    }

    String queueCommand(String command) throws KeeperException {
        while (true) {
            try {
                String name = zk.create("/tasks/task-",
                        command.getBytes(),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.PERSISTENT_SEQUENTIAL);
                return name;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (NodeExistsException e) {
            }
        }
    }



    @Override
    public void process(WatchedEvent event) {
        log.info(event.toString());
    }

    public static void main(String[] args) throws Exception{
        Client c = new Client(args[0]);

        c.startZk();

        String name = c.queueCommand(args[1]);
        System.out.printf("Created " + name);

        Thread.sleep(60000);

    }
}
