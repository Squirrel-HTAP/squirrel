package org.squirrel.common;

/**
 * @author meijie
 */
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

  protected abstract void serviceInit(SquirrelConfigs configs);

  @Override
  public void init(SquirrelConfigs configs) {
    serviceInit(configs);
    this.status = ServiceStatus.INITED;
  }

  protected abstract void serviceStart();

  @Override
  public void start() {
    serviceStart();
    this.status = ServiceStatus.RUNNING;
  }

  protected abstract void serviceStop();

  @Override
  public void stop() {
    serviceStop();
    this.status = ServiceStatus.STOPPED;
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
