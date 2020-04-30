package org.squirrel.core;

import java.net.InetAddress;
import java.net.UnknownHostException;
import lombok.Builder;
import lombok.Getter;

/**
 * Node information
 *
 * @author meijie
 */
@Getter
@Builder
public class Node {

  private String nodeId;
  private Address address;
  private Location location;

  public static Node defaultNode() throws UnknownHostException {
    InetAddress inetAddress = InetAddress.getLocalHost();
    return Node.builder().nodeId(inetAddress.getHostName())
        .address(Address.defaultAddress())
        .build();
  }
}
