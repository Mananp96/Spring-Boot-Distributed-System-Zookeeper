package com.springdistributed.zkwatchers;

import com.springdistributed.util.ClusterInfo;
import com.springdistributed.zkservice.ZKService;
import com.springdistributed.zkservice.ZKUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkChildListener;

import java.util.Collections;
import java.util.List;

@Slf4j
@Setter
public class MasterChangeListener implements IZkChildListener {

    private ZKService zkService;

    /**
     * listens for deletion of sequential znode under /election znode and updates the
     * clusterinfo
     *
     * @param parentPath
     * @param children
     */
    @Override
    public void handleChildChange(String parentPath, List<String> children) throws Exception {
        if (children.isEmpty()) {
            throw new RuntimeException("No node to select master!");
        } else {
            // get least sequenced Znode
            Collections.sort(children);
            String masterZnode = children.get(0);

            // once Znode is fetched, fetch the Znode data to get the hostname of new leader
            String masterInfo = zkService.getZNodeData(ZKUtil.ELECTION_NODE.concat("/").concat(masterZnode));
            log.info("new master is: {}", masterInfo);

            //update the cluster info with new leader
            ClusterInfo.getClusterInfo().setMaster(masterInfo);
        }
    }
}
