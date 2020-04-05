package org.squirrel.common;

import java.io.IOException;

/**
 * The base abstract for running service.
 *
 * @author meijie
 * @since 0.1
 */
public interface Service extends AutoCloseable {

  ServiceStatus getStatus();

  void init();

  void start();

  void stop() throws IOException;

  String getName();
}
