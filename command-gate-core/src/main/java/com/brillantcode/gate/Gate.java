package com.brillantcode.gate;

/**
 * Provides common interface for starting execution
 * of {@link Command} instances.
 */
public interface Gate {

  /**
   * Dispatches the {@link Command} synchronously.
   *
   * <p>Returns when processing of the command is finished.
   *
   * @param cmd The {@link Command} to process.
   */
  void dispatch(Command cmd);

  /**
   * Schedules the {@link Command} for asynchronous processing.
   *
   * <p>Returns immediately after scheduling the command.
   *
   * <p>How and when the scheduled command is executed,
   * depends on the configuration of the asynchronous execution channel.
   *
   * @param cmd The {@link Command} to schedule for processing.
   */
  void schedule(Command cmd);

}
