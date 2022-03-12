package com.brillantcode.gate.scheduler;

import com.google.common.base.Preconditions;
import java.util.concurrent.Executor;

/**
 * Implementation of {@link CommandScheduler} that uses
 * a Java {@link Executor} provided to schedule
 * {@link com.brillantcode.gate.Command} execution.
 *
 * <p>Clients are responsible for managing
 * and shutting down the {@link Executor}.
 */
public class JavaExecutorCommandScheduler implements CommandScheduler {

  private final Executor executor;

  public JavaExecutorCommandScheduler(Executor executor) {
    Preconditions.checkNotNull(executor);
    this.executor = executor;
  }

  @Override
  public void schedule(CommandRunner runner) {
    executor.execute(runner::run);
  }

}
