package org.squirrel.core.service;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.ratis.RaftConfigKeys;
import org.apache.ratis.conf.RaftProperties;
import org.apache.ratis.protocol.RaftGroup;
import org.apache.ratis.protocol.RaftGroupId;
import org.apache.ratis.protocol.RaftPeer;
import org.apache.ratis.protocol.RaftPeerId;
import org.apache.ratis.rpc.SupportedRpcType;
import org.apache.ratis.server.RaftServer;
import org.apache.ratis.server.RaftServerConfigKeys;
import org.apache.ratis.thirdparty.com.google.protobuf.ByteString;
import org.squirrel.common.AbstractService;
import org.squirrel.common.SquirrelConfigs;
import org.squirrel.core.Address;
import org.squirrel.core.ServerRole;
import org.squirrel.core.statemachine.StoreStateMachine;

/**
 * Store Service.
 *
 * @author meijie
 */
@Slf4j
public class StoreService extends AbstractService {

  private final ServerRole serverRole;
  private RaftServer raftServer;
  private StoreStateMachine storeStateMachine;

  public StoreService(ServerRole serverRole) {
    super("Store Service");
    this.serverRole = serverRole;
  }

  @Override
  protected void serviceInit() throws IOException {
    RaftGroup raftGroup = buildRaftGroup();
    RaftProperties properties = buildRaftProperties();
    storeStateMachine = StoreStateMachine.builder().build();
    Address address = Address.fromPort(SquirrelConfigs.RAFT_SERVICE_PORT.getInt());

    raftServer = RaftServer.newBuilder()
        .setServerId(RaftPeerId.valueOf(address.toString()))
        .setGroup(raftGroup)
        .setProperties(properties)
        .setStateMachine(storeStateMachine)
        .build();
  }

  private RaftProperties buildRaftProperties() {
    RaftProperties properties = new RaftProperties();
    List<File> storeFileList = Lists.newArrayList(
        new File(SquirrelConfigs.RAFT_STORE_FOLDER.getString()));
    RaftServerConfigKeys.setStorageDirs(properties, storeFileList);
    RaftConfigKeys.Rpc.setType(properties, SupportedRpcType.NETTY);
    return properties;
  }

  private RaftGroup buildRaftGroup() {
    if (ServerRole.META_SERVER.equals(serverRole)) {
      return RaftGroup.valueOf(
          RaftGroupId.valueOf(ByteString.copyFromUtf8(serverRole.name())),
          parseRaftPeers(SquirrelConfigs.META_NODES.getString()));
    } else if (ServerRole.STORE_SERVER.equals(serverRole)) {
      // TODO
      return null;
    } else {
      throw new IllegalArgumentException("Unknown Server Role " + serverRole.name());
    }
  }

  private RaftPeer[] parseRaftPeers(String raftPeers) {
    return Stream.of(raftPeers.split(",")).map(address -> {
      String[] addressParts = address.split(":");
      return new RaftPeer(RaftPeerId.valueOf(
          addressParts[0] + ":" + addressParts[1]),
          addressParts[0] + ":" + addressParts[1]);
    }).toArray(RaftPeer[]::new);
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
