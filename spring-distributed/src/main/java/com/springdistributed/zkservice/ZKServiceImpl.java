package com.springdistributed.zkservice;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;

import java.util.Collections;
import java.util.List;

@Slf4j
public class ZKServiceImpl implements ZKService {

    private ZkClient zkClient;

    private ZKServiceImpl(String hostPort) {
        zkClient = new ZkClient(hostPort, 12000, 3000, new ZKStringSerializer());
    }

    public void closeConnection() {
        zkClient.close();
    }

    @Override
    public void createNodeInElectionZnode(String data) {
        if (!zkClient.exists(ZKUtil.ELECTION_NODE)) {
            zkClient.create(ZKUtil.ELECTION_NODE, "election node", CreateMode.PERSISTENT);
        }
        zkClient.create(ZKUtil.ELECTION_NODE.concat("/node"), data, CreateMode.EPHEMERAL_SEQUENTIAL);
    }

    @Override
    public String getZNodeData(String path) {
        return zkClient.readData(path, null);
    }

    @Override
    public String getLeaderNodeData() {
        if (!zkClient.exists(ZKUtil.ELECTION_NODE)) {
            throw new RuntimeException("Node node /election not exists!");
        }
        List<String> nodesInElection = zkClient.getChildren(ZKUtil.ELECTION_NODE);
        Collections.sort(nodesInElection);
        String leaderZNode = nodesInElection.get(0);
        return getZNodeData(ZKUtil.ELECTION_NODE.concat("/").concat(leaderZNode));
    }

    @Override
    public void createAllParentNodes() {
        if (!zkClient.exists(ZKUtil.ELECTION_NODE)) {
            zkClient.create(ZKUtil.ELECTION_NODE, "election node", CreateMode.PERSISTENT);
        }
    }

    @Override
    public void registerChildrenChangeWatcher(String path, IZkChildListener iZkChildListener) {
        zkClient.subscribeChildChanges(path, iZkChildListener);
    }

    @Override
    public void registerZkSessionStateListener(IZkStateListener iZkStateListener) {
        zkClient.subscribeStateChanges(iZkStateListener);
    }
}
