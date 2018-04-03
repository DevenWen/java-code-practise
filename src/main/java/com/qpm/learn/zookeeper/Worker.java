package com.qpm.learn.zookeeper;

import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.apache.zookeeper.*;
import org.apache.zookeeper.AsyncCallback.*;
import org.apache.zookeeper.KeeperException.*;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * @author kangqiang.w
 * @Date 2018/4/2
 **/
@Log4j
public class Worker implements Watcher {

    ZooKeeper zk;
    String status;
    String hostPort;
    String serverId = Long.toString(new Random().nextLong());

    private ThreadPoolExecutor executor;

    public Worker(String hostPort) {
        this.hostPort = hostPort;
        this.executor = new ThreadPoolExecutor(1, 1, 1000L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(200));
    }

    void startZK() throws Exception {
        zk = new ZooKeeper(hostPort, 15000, this);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        log.info(watchedEvent.toString() + ", " + hostPort);
    }

    void register() {
        zk.create("/workers/worker-" + serverId, "Idle".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL,
                createWorkerCallback,
                null);
    }

    StringCallback createWorkerCallback = new StringCallback() {
        @Override
        public void processResult(int rc, String path, Object o, String s1) {
            switch (Code.get(rc)) {
                case CONNECTIONLOSS:
                    register();
                    break;
                case OK:
                    log.info("Registered successfully:" + serverId);
                    break;
                case NODEEXISTS:
                    log.warn("Already registered: " + serverId);
                    break;
                default:
                    log.error("Something went wrong: " + KeeperException.create(Code.get(rc), path));
            }
        }
    };

    StatCallback statusUpdateCallBack = new StatCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, Stat stat) {
            switch (Code.get(rc)) {
                case CONNECTIONLOSS:
                    updateStatus((String) ctx);
                    return;
            }
        }
    };

    private synchronized void updateStatus(String status) {
        if (status == this.status) {
            zk.setData("/workers/" + "worker-" + serverId,
                        status.getBytes(), -1, statusUpdateCallBack, status);
        }
    }

    public void setStatus(String status) {
        this.status = status;
        updateStatus(status);
    }


    Watcher newTaskWatcher = new Watcher() {
        @Override
        public void process(WatchedEvent event) {
            if (event.getType() == Event.EventType.NodeChildrenChanged) {
                assert new String("/assign/worker-" + serverId).equals(event.getPath());
                getTask();
            }
        }
    };

    private void getTask() {
        zk.getChildren("/assign/worker-" + serverId,
                        newTaskWatcher,
                        taskGetChildrenCallback,
                        null);
    }

    ChildrenCache assignedTaskCache = new ChildrenCache();

    ChildrenCallback taskGetChildrenCallback = new ChildrenCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, List<String> children) {
            switch (Code.get(rc)) {
                case CONNECTIONLOSS:
                    getTask();
                    break;
                case OK:
                    if (children != null) {
                        executor.execute(new Runnable() {
                            List<String> children;
                            DataCallback cb;

                            public Runnable init(List<String> children, DataCallback cb) {
                                this.children = children;
                                this.cb = cb;
                                return this;
                            }

                            @Override
                            public void run() {
                                if (children == null) {
                                    return;
                                }
                                log.info("Looping into tasks");
                                setStatus("Working");
                                for (String task : children) {
                                    log.info("New task: {" + task + "}");
                                    zk.getData("/assign/worker-" + serverId + "/" + task, false, cb, task);
                                }
                            }
                        }.init(assignedTaskCache.addedAndSet(children), taskDataCallback));
                    }
                    break;
                default:
                    System.out.println("getChildren failed: " + KeeperException.create(Code.get(rc), path));
            }
        }
    };

    DataCallback taskDataCallback = new DataCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {

        }
    };


    public static void main(String[] args) throws Exception{
        Worker w = new Worker(args[0]);
        w.startZK();
        /*
        注册自己
         */
        w.register();
        /*
        开始监听数据
         */
        w.getTask();

        Thread.sleep(60000);
    }
}
