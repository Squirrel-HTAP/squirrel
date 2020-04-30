package org.squirrel.core.service;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Properties;
import org.apache.ratis.protocol.RaftClientReply;
import org.junit.jupiter.api.Test;
import org.squirrel.common.SquirrelConfigs;
import org.squirrel.core.Address;
import org.squirrel.core.Node;
import org.squirrel.core.ServerRole;
import org.squirrel.core.client.SquirrelClient;
import org.squirrel.core.statemachine.StoreStateMachine;

/**
 * Store Server Test Case
 *
 * @author meijie
 */
public class StoreServiceTest {

  @Test
  public void baseTest() throws IOException {
    Properties properties = new Properties();
    properties.setProperty(SquirrelConfigs.META_STORE_FOLDER.getKey(), "~/log/node1");
    SquirrelConfigs.setProperties(properties);
    StoreService storeService0 = new StoreService(
        ServerRole.META_SERVER, buildLocalNode("node1", 9810));
    storeService0.register(new StoreStateMachine());
    storeService0.init();
    storeService0.start();

    properties.setProperty(SquirrelConfigs.META_STORE_FOLDER.getKey(), "~/log/node2");
    StoreService storeService1 = new StoreService(
        ServerRole.META_SERVER, buildLocalNode("node2", 9811));
    storeService1.register(new StoreStateMachine());
    storeService1.init();
    storeService1.start();

    properties.setProperty(SquirrelConfigs.META_STORE_FOLDER.getKey(), "~/log/node3");
    StoreService storeService2 = new StoreService(
        ServerRole.META_SERVER, buildLocalNode("node3", 9812));
    storeService2.register(new StoreStateMachine());
    storeService2.init();
    storeService2.start();

    SquirrelClient client = new SquirrelClient(ServerRole.META_SERVER);
    RaftClientReply reply = client.request();
    System.out.println(reply.getMessage());
    storeService0.close();
    storeService1.close();
    storeService2.close();
  }

  private Node buildLocalNode(String nodeId, int port) throws UnknownHostException {
    return Node.builder().nodeId(nodeId)
        .address(Address.fromPort(port))
        .build();
  }
}
