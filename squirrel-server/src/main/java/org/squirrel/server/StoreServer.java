package org.squirrel.server;

import java.net.UnknownHostException;
import org.squirrel.common.AbstractComposeService;
import org.squirrel.core.ServerRole;
import org.squirrel.core.service.HeartService;
import org.squirrel.core.service.StoreService;

/**
 * @author meijie
 */
public class StoreServer extends AbstractComposeService {

  private StoreService storeService;
  private HeartService heartService;

  public StoreServer(String name) throws UnknownHostException {
    super(StoreServer.class.getName());
    storeService = new StoreService(ServerRole.STORE_SERVER);
    registryService(storeService);
  }

}
