package org.squirrel.core;

import java.net.InetAddress;
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

  private String host;
  private int port;

  public static Address fromPort(int port) throws UnknownHostException {
    InetAddress address = InetAddress.getLocalHost();
    return new Address(address.getHostName(), port);
  }

  public static Address from(String address) {
    int lastColon = address.lastIndexOf(':');
    String host = StringUtils.substring(address, 0, lastColon);
    String port = StringUtils.substring(address, lastColon);
    if (StringUtils.isNumeric(port)) {
      return new Address(host, Integer.parseInt(port));
    }
    throw new IllegalArgumentException("wrong node address " + address);
  }

  @Override
  public String toString() {
    return host + ":" + port;
  }
}
