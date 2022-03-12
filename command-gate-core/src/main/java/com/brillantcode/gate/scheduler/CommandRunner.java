package com.brillantcode.gate.scheduler;

/**
 * Command runner interface implementation should
 * commence an execution of a {@link com.brillantcode.gate.Command}.
 *
 * <p>It is up to the implementation to decide what it exactly means
 * to commence a command execution and how it's done.
 *
 * @see CommandScheduler
 */
@FunctionalInterface
public interface CommandRunner {

  /**
   * Called by the {@link CommandScheduler} implementations
   * when the command is supposed to be executed.
   */
  void run();

}
