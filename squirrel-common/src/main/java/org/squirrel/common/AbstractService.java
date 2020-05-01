package org.squirrel.common;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author meijie
 */
@Slf4j
public abstract class AbstractService implements Service {

  protected ServiceStatus status;
  private final String name;

  public AbstractService(String name) {
    this.status = ServiceStatus.CREATED;
    this.name = name;
  }

  @Override
  public ServiceStatus getStatus() {
    return status;
  }

  protected abstract void serviceInit() throws IOException;

  @Override
  public void init() {
    try {
      this.status = ServiceStatus.INITED;
      serviceInit();
    } catch (Throwable throwable) {
      status = ServiceStatus.FAILED;
      log.error("{} failed when do serviceInit", name, throwable);
      System.exit(-1);
    }
  }

  protected abstract void serviceStart() throws IOException;

  @Override
  public void start() {
    try {
      this.status = ServiceStatus.RUNNING;
      serviceStart();
    } catch (Throwable throwable) {
      status = ServiceStatus.FAILED;
      log.error("{} failed when do serviceStart", name, throwable);
      System.exit(-1);
    }
  }

  protected abstract void serviceStop() throws IOException;

  @Override
  public void stop() {
    try {
      this.status = ServiceStatus.STOPPED;
      serviceStop();
    } catch (Throwable throwable) {
      status = ServiceStatus.FAILED;
      log.error("{} failed when do serviceStop", name, throwable);
      System.exit(-1);
    }
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public void close() {
    stop();
  }
}
