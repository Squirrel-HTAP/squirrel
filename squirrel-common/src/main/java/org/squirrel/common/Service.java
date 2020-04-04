package org.squirrel.common;

/**
 * The base abstract for running service.
 *
 * @author meijie
 * @since 0.1
 */
public interface Service extends AutoCloseable {

  ServiceStatus getStatus();

  void init(SquirrelConfigs configs);

  void start();

  void stop();

  String getName();
}
