package org.squirrel.common;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author meijie
 */
@Slf4j
public abstract class AbstractService implements Service {

  private ServiceStatus status;
  private final String name;

  public AbstractService(String name) {
    this.name = name;
    this.status = ServiceStatus.CREATED;
  }

  @Override
  public ServiceStatus getStatus() {
    return status;
  }

  protected abstract void serviceInit() throws IOException;

  @Override
  public void init() {
    try {
      serviceInit();
      this.status = ServiceStatus.INITED;
    } catch (Throwable throwable) {
      status = ServiceStatus.FAILED;
      log.error("{} failed when do serviceInit", name, throwable);
    }
  }

  protected abstract void serviceStart() throws IOException;

  @Override
  public void start() {
    try {
      serviceStart();
      this.status = ServiceStatus.RUNNING;
    } catch (Throwable throwable) {
      status = ServiceStatus.FAILED;
      log.error("{} failed when do serviceStart", name, throwable);
    }
  }

  protected abstract void serviceStop() throws IOException;

  @Override
  public void stop() {
    try {
      serviceStop();
      this.status = ServiceStatus.STOPPED;
    } catch (Throwable throwable) {
      status = ServiceStatus.FAILED;
      log.error("{} failed when do serviceStop", name, throwable);
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
