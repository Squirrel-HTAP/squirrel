package org.squirrel.core.util;

import java.util.stream.Stream;
import org.apache.ratis.protocol.RaftGroup;
import org.apache.ratis.protocol.RaftGroupId;
import org.apache.ratis.protocol.RaftPeer;
import org.apache.ratis.protocol.RaftPeerId;
import org.squirrel.common.SquirrelConfigs;
import org.squirrel.core.ServerRole;

/**
 * @author meijie
 */
public class RaftUtils {

  private static final RaftGroupId groupId = RaftGroupId.randomId();

  public static RaftGroup buildRaftGroup(ServerRole serverRole) {
    if (ServerRole.META_SERVER.equals(serverRole)) {
      return RaftGroup.valueOf(groupId,
          parseRaftPeers(SquirrelConfigs.META_NODES.getString()));
    } else if (ServerRole.STORE_SERVER.equals(serverRole)) {
      // TODO
      return null;
    } else {
      throw new IllegalArgumentException("Unknown Server Role " + serverRole.name());
    }
  }

  private static RaftPeer[] parseRaftPeers(String raftPeers) {
    return Stream.of(raftPeers.split(",")).map(address -> {
      String[] addressParts = address.split(":");
      return new RaftPeer(RaftPeerId.valueOf(addressParts[0]),
          addressParts[1] + ":" + addressParts[2]);
    }).toArray(RaftPeer[]::new);
  }
}
