package org.squirrel.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author meijie
 */
public abstract class AbstractComposeService extends AbstractService {

  private List<Service> serviceList = new ArrayList<>();

  public void registryService(Service service) {
    synchronized (this) {
      serviceList.add(service);
    }
  }

  public void unRegistryService(Service service) {
    synchronized (this) {
      serviceList.remove(service);
    }
  }

  public AbstractComposeService(String name) {
    super(name);
  }

  @Override
  protected void serviceInit() throws IOException {
    for (Service service : serviceList) {
      service.init();
    }
  }


  @Override
  protected void serviceStart() throws IOException {
    for (Service service : serviceList) {
      service.start();
    }
  }

  @Override
  protected void serviceStop() throws IOException {
    for (Service service : serviceList) {
      service.stop();
    }
  }
}
