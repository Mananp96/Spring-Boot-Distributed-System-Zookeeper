package com.springdistributed.zkservice;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final class ZKUtil {

    public static final String ELECTION_NODE = "/election";

    private static String ipPort = null;

    private ZKUtil() {}

    public static String getHostPortOfServer() {
        if (ipPort != null) {
            return ipPort;
        }
        String ip;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException("failed to fetch Ip!", e);
        }
        int port = Integer.parseInt(System.getProperty("server.port"));
        ipPort = ip.concat(":").concat(String.valueOf(port));
        return ipPort;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

}


