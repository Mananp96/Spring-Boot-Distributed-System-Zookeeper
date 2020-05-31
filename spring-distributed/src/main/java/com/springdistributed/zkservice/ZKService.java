package com.springdistributed.zkservice;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkStateListener;

public interface ZKService {

    void createNodeInElectionZnode(String data);

    String getZNodeData(String path);

    String getLeaderNodeData();

    void createAllParentNodes();

    void registerChildrenChangeWatcher(String path, IZkChildListener iZkChildListener);

    void registerZkSessionStateListener(IZkStateListener iZkStateListener);

}
