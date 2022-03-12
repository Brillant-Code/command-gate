package com.brillantcode.gate.config;

import com.brillantcode.gate.CommandRegistry;
import com.brillantcode.gate.handler.CommandHandlerDiscoverer;
import com.brillantcode.gate.handler.CommandHandlerRegistry;

/**
 * Factory for creating instances of {@link CommandHandlerRegistry}.
 */
@FunctionalInterface
public interface CommandHandlerRegistryFactory {

  /**
   * Instantiates a new object of type assignable to {@link CommandHandlerRegistry}.
   *
   * @param commandRegistry   Registry of commands that the handler registry
   *                          should have the handlers for.
   * @param handlerDiscoverer Handler discoverer for finding instances of command handlers
   *                          given a command type.
   * @return Newly created instance of command handler registry implementation.
   */
  CommandHandlerRegistry instantiate(CommandRegistry commandRegistry,
                                     CommandHandlerDiscoverer handlerDiscoverer);

}
