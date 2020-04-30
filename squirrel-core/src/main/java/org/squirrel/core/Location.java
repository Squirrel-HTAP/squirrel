package org.squirrel.core;

import lombok.Builder;
import lombok.Getter;

/**
 * the location for node.
 *
 * @author meijie
 */
@Builder
@Getter
public class Location {

  private String rack;
  private String zone;
  private String dc;
}
