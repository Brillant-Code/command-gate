package com.brillantcode.gate;

import java.util.Set;

/**
 * Stores and provides available {@link Command} types.
 */
public interface CommandRegistry {

  /**
   * Returns a {@link Set} of Java {@link Class}
   * of available {@link Command} types.
   *
   * @return A set of available command types.
   */
  Set<Class<? extends Command>> getCommandTypes();

}
