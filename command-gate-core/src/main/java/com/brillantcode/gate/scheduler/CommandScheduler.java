package com.brillantcode.gate.scheduler;

/**
 * Interface for scheduling {@link com.brillantcode.gate.Command} executions.
 */
public interface CommandScheduler {

  /**
   * Schedule the execution of a {@link com.brillantcode.gate.Command}.
   *
   * <p>Command is to be executed by the {@link CommandRunner} provided,
   * at the discretion of the implementation.
   *
   * @param runner The {@link CommandRunner} to execute
   *               the {@link com.brillantcode.gate.Command}.
   */
  void schedule(CommandRunner runner);

}
