package org.squirrel.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Dispatch Request to Target Dispatcher.
 *
 * @author meijie
 */
public class RequestDispatcher implements Dispatcher<Request> {

  private static final Map<RequestType, Dispatcher<Request>> dispatchers = new HashMap<>();

  private static final Object lock = new Object();

  private void registry(RequestType type, Dispatcher<Request> dispatcher) {
    synchronized (lock) {
      dispatchers.putIfAbsent(type, dispatcher);
    }
  }

  private void unRegistry(RequestType type) {
    synchronized (lock) {
      dispatchers.remove(type);
    }
  }

  @Override
  public void dispatch(Request request) {
    dispatchers.get(request).dispatch(request);
  }

}
