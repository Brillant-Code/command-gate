package com.brillantcode.gate.handler;

import com.brillantcode.gate.CommandRegistry;
import org.springframework.context.SmartLifecycle;

/**
 * Implementation of {@link AbstractMapCommandHandlerRegistry}
 * that is hooking up to the Spring's bean lifecycle management
 * by implementing {@link SmartLifecycle} interface.
 */
public class SpringMapCommandHandlerRegistry extends AbstractMapCommandHandlerRegistry
    implements SmartLifecycle {

  private final Object lifecycleMonitor = new Object();

  private volatile boolean started = false;

  public SpringMapCommandHandlerRegistry(CommandRegistry commandRegistry,
                                         CommandHandlerDiscoverer handlerDiscoverer) {
    super(commandRegistry, handlerDiscoverer);
  }

  @Override
  public void start() {
    synchronized (this.lifecycleMonitor) {
      if (!this.started) {
        doRegister();
        this.started = true;
      }
    }
  }

  @Override
  public void stop() {
    synchronized (this.lifecycleMonitor) {
      this.started = false;
    }
  }

  @Override
  public boolean isRunning() {
    synchronized (this.lifecycleMonitor) {
      return started;
    }
  }

  @Override
  protected boolean isInitialized() {
    return isRunning();
  }

}
