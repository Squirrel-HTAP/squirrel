package org.squirrel.server;

import java.net.UnknownHostException;
import org.squirrel.common.AbstractComposeService;
import org.squirrel.core.ServerRole;
import org.squirrel.core.service.StoreService;

/**
 * @author meijie
 */
public class MetaServer extends AbstractComposeService {

  private StoreService storeService;

  public MetaServer(String name) throws UnknownHostException {
    super(name);
    storeService = new StoreService(ServerRole.META_SERVER);
    registryService(storeService);
  }
}
