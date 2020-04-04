package org.squirrel.common;

import com.google.common.base.Preconditions;
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
  // == Configuration For Raft.
  // ==========================================================

  RAFT_STORE_FOLDER("raft.store.folder", String.class,
      "the folder which store raft's log", "");


  private static Properties properties;
  private String key;
  private Class type;
  private String description;
  private String defaultValue;

  public String getString() {
    Preconditions.checkNotNull(properties);
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
