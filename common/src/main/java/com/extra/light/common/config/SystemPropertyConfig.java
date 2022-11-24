package com.extra.light.common.config;

/**
 * @author 林树毅
 */
public class SystemPropertyConfig {

    public static void setSystemPropertyConfig(String serverName, String dataId) {
        System.setProperty("serverName", serverName);
        System.setProperty("dataId", dataId);
    }
}
