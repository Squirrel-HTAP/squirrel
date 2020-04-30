package org.squirrel.core;

import com.google.common.base.Preconditions;
import java.net.InetAddress;
import java.net.UnknownHostException;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author meijie
 */
@Builder
@Getter
public class Address {

  private String host;
  private int port;

  public static Address defaultAddress() throws UnknownHostException {
    return fromPort(9120);
  }

  public static Address fromPort(int port) throws UnknownHostException {
    InetAddress address = InetAddress.getLocalHost();
    return from(address.getHostAddress(), port);
  }


  public static Address from(String host, int port) {
    Preconditions.checkNotNull(host);
    Preconditions.checkNotNull(port);
    return Address.builder()
        .host(host)
        .port(port)
        .build();
  }

  public static void main(String[] args) throws UnknownHostException {
    Address address = fromPort(3306);
    System.out.println(address.toString());
  }

  public static Address from(String nodeId, String address) {
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
