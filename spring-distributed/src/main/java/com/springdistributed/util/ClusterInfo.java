package com.springdistributed.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class ClusterInfo {

    private String master;

    private static ClusterInfo clusterInfo = new ClusterInfo();

    public static ClusterInfo getClusterInfo() {
        return clusterInfo;
    }

}
