package com.brillantcode.gate.handler;

import com.brillantcode.gate.CommandRegistry;

/**
 * Basic implementation of {@link AbstractMapCommandHandlerRegistry}
 * that calls {@link AbstractMapCommandHandlerRegistry#doRegister()}
 * in the constructor.
 *
 * <p>{@link CommandRegistry} and {@link CommandHandlerDiscoverer} instances
 * passed to the constructor must be fully initialized and working
 * during instantiating this class for this implementation to work.
 */
public final class AutoInitializingMapCommandHandlerRegistry
    extends AbstractMapCommandHandlerRegistry {

  public AutoInitializingMapCommandHandlerRegistry(CommandRegistry commandRegistry,
                                                   CommandHandlerDiscoverer handlerDiscoverer) {
    super(commandRegistry, handlerDiscoverer);
    this.doRegister();
  }

  @Override
  protected boolean isInitialized() {
    return true;
  }

}
