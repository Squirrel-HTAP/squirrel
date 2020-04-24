package org.squirrel.core.service;

import java.io.IOException;
import org.apache.ratis.protocol.RaftClientReply;
import org.junit.jupiter.api.Test;
import org.squirrel.core.Address;
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
    StoreService storeService0 = new StoreService(
        ServerRole.SQU__META_SERVER, Address.fromPort("node1", 9810));
    storeService0.register(new StoreStateMachine());
    storeService0.init();
    storeService0.start();

    StoreService storeService1 = new StoreService(
        ServerRole.SQU__META_SERVER, Address.fromPort("node2", 9811));
    storeService1.register(new StoreStateMachine());
    storeService1.init();
    storeService1.start();

    StoreService storeService2 = new StoreService(
        ServerRole.SQU__META_SERVER, Address.fromPort("node3", 9812));
    storeService2.register(new StoreStateMachine());
    storeService2.init();
    storeService2.start();

    SquirrelClient client = new SquirrelClient(ServerRole.SQU__META_SERVER);
    RaftClientReply reply = client.request();
    System.out.println(reply.getMessage());
    storeService0.close();
    storeService1.close();
    storeService2.close();
  }
}
