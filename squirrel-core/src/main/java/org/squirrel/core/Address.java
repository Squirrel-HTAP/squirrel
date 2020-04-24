package org.squirrel.core;

import java.net.UnknownHostException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author meijie
 */
@Getter
@AllArgsConstructor
public class Address {

  private String nodeId;
  private String host;
  private int port;

  public static Address fromPort(String nodeId, int port) throws UnknownHostException {
//    InetAddress address = InetAddress.getLocalHost();
//    return new Address(address.getHostName(), port);
    return new Address(nodeId, "127.0.0.1", port);
  }

  public static Address fromPort(int port) throws UnknownHostException {
//    InetAddress address = InetAddress.getLocalHost();
//    return new Address(address.getHostName(), port);
    return fromPort("localhost", port);
  }

  public static Address from(String nodeId, String address) {
    int lastColon = address.lastIndexOf(':');
    String host = StringUtils.substring(address, 0, lastColon);
    String port = StringUtils.substring(address, lastColon);
    if (StringUtils.isNumeric(port)) {
      return new Address(nodeId, host, Integer.parseInt(port));
    }
    throw new IllegalArgumentException("wrong node address " + address);
  }

  @Override
  public String toString() {
    return host + ":" + port;
  }
}
