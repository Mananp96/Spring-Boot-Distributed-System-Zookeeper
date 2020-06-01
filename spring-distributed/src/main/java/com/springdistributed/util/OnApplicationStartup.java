package com.springdistributed.util;

import com.springdistributed.model.Note;
import com.springdistributed.zkservice.ZKService;
import com.springdistributed.zkservice.ZKUtil;
import com.springdistributed.zkwatchers.ConnectStateChangeListener;
import com.springdistributed.zkwatchers.MasterChangeListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class OnApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private ZKService zkService;

    @Autowired
    private MasterChangeListener masterChangeListener;

    @Autowired
    private ConnectStateChangeListener connectStateChangeListener;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            // create /election parent znode
            zkService.createAllParentNodes();

            // create ephemeral sequential znode in /election
            zkService.createNodeInElectionZnode(ZKUtil.getHostPortOfServer());

            // get leader node from ZK and set it as Master in ClusterInfo
            ClusterInfo.getClusterInfo().setMaster(zkService.getLeaderNodeData());

            // sync notes data from master
            syncDataFromMaster();

            // register watchers for leader change, and zk session state change
            zkService.registerChildrenChangeWatcher(ZKUtil.ELECTION_NODE, masterChangeListener);
            zkService.registerZkSessionStateListener(connectStateChangeListener);
        } catch (Exception e) {
            throw new RuntimeException("Startup failed!!", e);
        }
    }

    private void syncDataFromMaster() {
        // TODO need try catch here for session not found
        if (ZKUtil.getHostPortOfServer().equals(ClusterInfo.getClusterInfo().getMaster())) {
            return;
        }
        String requestUrl;
        requestUrl = "http://".concat(ClusterInfo.getClusterInfo().getMaster().concat("/persons"));
        List<Note> notes = restTemplate.getForObject(requestUrl, List.class);
        NotesData.getAllNotesFromData().addAll(notes);
    }

}
