package org.squirrel.common;

import java.util.Properties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author meijie
 */
@Slf4j
@AllArgsConstructor
public enum SquirrelConfigs {

  // ==========================================================
  // == Configuration for Meta Server
  // ==========================================================
  META_NODES("meta.nodes", String.class, "meta server nodes.",
      "node1:localhost:9810,node2:localhost:9811,node3:localhost:9812"),
  META_PORT("meta.port", Integer.class, "meta server port", "9810"),

  // ==========================================================
  // == Configuration For Raft.
  // ==========================================================

  STORE_FOLDER("store.folder", String.class,
      "the folder which store raft's log", "/home/meijie/log"),
  STORE_PORT("store.port", Integer.class,
      "the port which used by store service", "9710");

  private static Properties properties;
  private String key;
  private Class type;
  private String description;
  private String defaultValue;

  public String getString() {
    if (properties == null) {
      return defaultValue;
    }
    return properties.getProperty(key, defaultValue);
  }

  public int getInt() {
    String intValue = getString();
    return Integer.parseInt(intValue);
  }

  public void setProperties(final Properties properties) {
    this.properties = properties;
  }

  public static void logConfigs() {
    log.info("configuration for squirrel:");
    for (SquirrelConfigs configItem : SquirrelConfigs.values()) {
      log.info("{}={}, type:{}, default:{}", configItem.key,
          configItem.getString(), configItem.type, configItem.defaultValue);
    }
  }
}
