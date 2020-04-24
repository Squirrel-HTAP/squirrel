package org.squirrel.core.client;

import java.io.IOException;
import org.apache.ratis.RaftConfigKeys;
import org.apache.ratis.client.RaftClient;
import org.apache.ratis.conf.RaftProperties;
import org.apache.ratis.protocol.Message;
import org.apache.ratis.protocol.RaftClientReply;
import org.apache.ratis.protocol.RaftGroup;
import org.apache.ratis.rpc.SupportedRpcType;
import org.squirrel.core.RaftBase;
import org.squirrel.core.ServerRole;
import org.squirrel.core.util.RaftUtils;

/**
 * @author meijie
 */
public class SquirrelClient extends RaftBase implements AutoCloseable {

  private RaftClient raftClient;

  public SquirrelClient(ServerRole serverRole) {

    RaftProperties raftProperties = new RaftProperties();
    RaftConfigKeys.Rpc.setType(raftProperties, SupportedRpcType.NETTY);
    final RaftGroup raftGroup = RaftUtils.buildRaftGroup(serverRole);

    // TODO client id should not be default random id.
    RaftClient.Builder builder = RaftClient.newBuilder()
        .setProperties(raftProperties)
        .setRaftGroup(raftGroup);
    raftClient = builder.build();
  }

  public RaftClientReply request() throws IOException {
    return raftClient.send(Message.valueOf("test"));
  }

  @Override
  public void close() throws Exception {
    raftClient.close();
  }
}
