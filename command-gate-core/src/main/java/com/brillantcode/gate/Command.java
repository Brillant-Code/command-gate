package com.brillantcode.gate;

import com.google.common.base.Preconditions;

/**
 * Marker interface for command classes.
 */
public interface Command {

  /**
   * Dispatch the command to the specified {@link Gate} instance.
   *
   * @param gate {@link Gate} instance to dispatch the command to.
   */
  default void dispatchTo(Gate gate) {
    Preconditions.checkNotNull(gate);
    gate.dispatch(this);
  }

}
