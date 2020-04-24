package org.squirrel.core.service;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.ratis.RaftConfigKeys;
import org.apache.ratis.conf.RaftProperties;
import org.apache.ratis.netty.NettyConfigKeys;
import org.apache.ratis.protocol.RaftGroup;
import org.apache.ratis.protocol.RaftPeerId;
import org.apache.ratis.rpc.SupportedRpcType;
import org.apache.ratis.server.RaftServer;
import org.apache.ratis.server.RaftServerConfigKeys;
import org.apache.ratis.statemachine.impl.BaseStateMachine;
import org.squirrel.common.AbstractService;
import org.squirrel.common.SquirrelConfigs;
import org.squirrel.core.Address;
import org.squirrel.core.ServerRole;
import org.squirrel.core.util.RaftUtils;

/**
 * Store Service for Meta Server and Store Server.
 *
 * @author meijie
 */
@Slf4j
public class StoreService extends AbstractService {

  private final ServerRole serverRole;
  private Address address;
  private RaftServer raftServer;
  private BaseStateMachine storeStateMachine;

  public StoreService(ServerRole serverRole) throws UnknownHostException {
    super("Store Service");
    this.serverRole = serverRole;
  }

  public StoreService(ServerRole serverRole, Address address) {
    super("Store Service");
    this.serverRole = serverRole;
    this.address = address;
  }

  public void register(BaseStateMachine storeStateMachine) {
    this.storeStateMachine = storeStateMachine;
  }

  @Override
  protected void serviceInit() throws IOException {
    RaftGroup raftGroup = RaftUtils.buildRaftGroup(this.serverRole);
    RaftProperties properties = buildRaftProperties();
    if (this.address == null) {
      this.address = buildAddress();
    }

    raftServer = RaftServer.newBuilder()
        .setServerId(RaftPeerId.valueOf(address.getNodeId()))
        .setGroup(raftGroup)
        .setProperties(properties)
        .setStateMachine(storeStateMachine)
        .build();
  }

  private RaftProperties buildRaftProperties() {
    RaftProperties properties = new RaftProperties();
    List<File> storeFileList = Lists.newArrayList(
        new File(SquirrelConfigs.STORE_FOLDER.getString() + "/" + address.getPort()));
    RaftServerConfigKeys.setStorageDirs(properties, storeFileList);
    RaftConfigKeys.Rpc.setType(properties, SupportedRpcType.NETTY);
    NettyConfigKeys.Server.setPort(properties, address.getPort());
    return properties;
  }

  private Address buildAddress() throws UnknownHostException {
    if (ServerRole.STORE_SERVER.equals(serverRole)) {
      return Address.fromPort(SquirrelConfigs.META_PORT.getInt());
    } else if (ServerRole.SQU__META_SERVER.equals(serverRole)) {
      return Address.fromPort(SquirrelConfigs.STORE_PORT.getInt());
    } else {
      throw new IllegalArgumentException("Unknown Server Role " + serverRole.name());
    }
  }

  @Override
  protected void serviceStart() throws IOException {
    raftServer.start();
  }

  @Override
  protected void serviceStop() throws IOException {
    if (raftServer != null) {
      raftServer.close();
    }
  }
}
