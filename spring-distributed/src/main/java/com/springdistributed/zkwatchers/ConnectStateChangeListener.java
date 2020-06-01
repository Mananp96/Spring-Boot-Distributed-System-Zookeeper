package com.springdistributed.zkwatchers;

import com.springdistributed.model.Note;
import com.springdistributed.util.ClusterInfo;
import com.springdistributed.util.NotesData;
import com.springdistributed.zkservice.ZKService;
import com.springdistributed.zkservice.ZKUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkStateListener;
import org.apache.zookeeper.Watcher;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Setter
public class ConnectStateChangeListener implements IZkStateListener {

    private RestTemplate restTemplate = new RestTemplate();
    private ZKService zkService;

    @Override
    public void handleStateChanged(Watcher.Event.KeeperState keeperState) throws Exception {
        log.info(keeperState.name()); // 1. disconnected, 2. expired, 3. SyncConnected
    }

    @Override
    public void handleNewSession() throws Exception {
        log.info("connected to zookeeper");
        // sync data from master
        syncDataFromMaster();

        // retry creating znode under /election
        zkService.createNodeInElectionZnode(ZKUtil.getHostPortOfServer());
        ClusterInfo.getClusterInfo().setMaster(zkService.getLeaderNodeData());
    }

    @Override
    public void handleSessionEstablishmentError(Throwable throwable) throws Exception {
        log.info("could not establish session");
    }

    private void syncDataFromMaster() {
        // TODO need try catch here for session not found
        if (ZKUtil.getHostPortOfServer().equals(ClusterInfo.getClusterInfo().getMaster())) {
            return;
        }
        String requestUrl;
        requestUrl = "http://".concat(ClusterInfo.getClusterInfo().getMaster().concat("/persons"));
        List<Note> notes = restTemplate.getForObject(requestUrl, List.class);
        NotesData.getAllNotesFromData().clear();
        NotesData.getAllNotesFromData().addAll(notes);
    }
}
