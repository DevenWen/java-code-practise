package com.qpm.learn.zookeeper;

import jdk.internal.dynalink.beans.StaticClass;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.KeeperException.*;
import org.apache.zookeeper.AsyncCallback.*;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.Random;

/**
 * @author kangqiang.w
 * @Date 2018/4/2
 **/
@Log4j
public class Master implements Watcher{

    enum MasterStates {RUNNING, ELECTED, NOTELECTED};

    ZooKeeper zk;
    String hostPort;


    @Getter
    MasterStates state;

    ChildrenCache workerCache;

    String serverId = Long.toString(new Random().nextLong());
    boolean isLeader = false;



    void checkMaster(){
        zk.getData("/master", false, masterCheckCallback, null);
    }

    void runForMaster(){
        zk.create("/master", serverId.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, masterCreateCallback, null);
    }

    void startZK() throws Exception{
        zk = new ZooKeeper(hostPort, 15000, this);
    }

    void stopZK() throws InterruptedException{
        zk.close();
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println(watchedEvent);
    }

    public Master(String hostPort) {
        this.hostPort = hostPort;
    }

    public void bootstrap() {
        createParent("/workers", new byte[]{});
        createParent("/assign", new byte[]{});
        createParent("/tasks", new byte[]{});
        createParent("/status", new byte[]{});
    }

    void createParent(String path, byte[] data) {
        zk.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, createParentCallBack, data);
    }

    StringCallback masterCreateCallback = new StringCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    /*
                    连接失败后，继续连接
                     */
                    checkMaster();
                    return;
                case OK:
                    state = MasterStates.ELECTED;
                    takeLeadership();
                    break;
                case NODEEXISTS:
                    state = MasterStates.NOTELECTED;
                    masterExisters();
                    break;
                default:
                    state = MasterStates.NOTELECTED;
                    log.error("Something went wrong when running for master.", KeeperException.create(Code.get(rc), path));
            }
            log.info("I'm " + (state == MasterStates.ELECTED ? "" : "not ") + "the leader." );
        }
    };

    private void takeLeadership() {
        bootstrap();
        getWorkers();
    }

    StringCallback createParentCallBack = new StringCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    createParent(path, (byte[]) ctx);
                    break;
                case OK:
                    log.info("Parent create");
                    break;
                case NODEEXISTS:
                    log.warn("Parent already registered: " + path);
                    break;
                default:
                    log.error("Something went wrong", KeeperException.create(KeeperException.Code.get(rc), path));
            }
        }
    };

    DataCallback masterCheckCallback = new DataCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
            switch (Code.get(rc)) {
                case CONNECTIONLOSS:
                    checkMaster();
                    return;
                case NONODE:
                    runForMaster();
                    return;
                default:
                    log.error("Something went wrong", KeeperException.create(KeeperException.Code.get(rc), path));
            }
        }
    };


    void masterExisters() {
        zk.exists("/master",
                masterExistsWatcher,
                masterExistsCallback, null);
    }

    /**
     * 监控主节点变化的 watcher
     */
    Watcher masterExistsWatcher = new Watcher() {
        @Override
        public void process(WatchedEvent event) {
            if (event.getType() == Event.EventType.NodeDeleted) {
                assert "/master".equals(event.getPath());
                runForMaster();
            }
        }
    };

    StatCallback masterExistsCallback = new StatCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, Stat stat) {
            switch (Code.get(rc)) {
                case CONNECTIONLOSS:
                    /*
                    连接丢失，重新请求
                     */
                    masterExisters();
                    break;
                case OK:
                    if (stat == null) {
                        runForMaster();
                    }
                    break;
                default:
                    checkMaster();
                    break;
            }
        }
    };

    Watcher workersChangeWatcher = new Watcher() {
        @Override
        public void process(WatchedEvent event) {
            if (event.getType() == Event.EventType.NodeChildrenChanged) {
                assert "/workers".equals(event.getPath());
                getWorkers();
            }
        }
    };

    ChildrenCallback workersGetChildrenCallback = new ChildrenCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, List<String> children) {
            switch (Code.get(rc)) {
                case CONNECTIONLOSS:
                    getWorkersList();
                    break;
                case OK:
                    log.info("Succesfully got a list of workers: " + children.size() + " workers");
                    reassignAndSet(children);
                    break;
                default:
                    log.error("Something went wrong", KeeperException.create(KeeperException.Code.get(rc), path));
            }
        }
    };

    private void reassignAndSet(List<String> children) {
        List<String> toProcess;
        if (workerCache == null) {
            workerCache = new ChildrenCache(children);
            toProcess = null;
        } else {
            log.info("Removing and setting");
            toProcess = workerCache.removedAndSet(children);
        }
        if (toProcess != null) {
            for (String worker : toProcess) {
                getAbsentWorkerTasks(worker);
            }
        }
    }

    private void getAbsentWorkerTasks(String worker) {
    }

    private void getWorkersList() {
    }

    /**
     * 获取子节点
     */
    private void getWorkers() {
        zk.getChildren("/workers", workersChangeWatcher, workersGetChildrenCallback, null);
    }

    public static void main(String[] args) throws Exception{
        Master m = new Master(args[0]);
        m.startZK();
        m.runForMaster();


        Thread.sleep(60000);
        m.stopZK();
    }
}
