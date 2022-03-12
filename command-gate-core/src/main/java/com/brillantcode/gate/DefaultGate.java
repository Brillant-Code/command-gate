package com.brillantcode.gate;

import com.brillantcode.gate.handler.CommandHandler;
import com.brillantcode.gate.handler.CommandHandlerRegistry;
import com.brillantcode.gate.scheduler.CommandRunner;
import com.brillantcode.gate.scheduler.CommandScheduler;
import com.google.common.base.Preconditions;

/**
 * Default {@link Gate} implementation.
 *
 * <p>Uses a {@link CommandHandlerRegistry} for retrieving
 * {@link CommandHandler} for a given command.
 *
 * <p>Uses a {@link CommandScheduler} for scheduling
 * asynchronous executions.
 */
public class DefaultGate implements Gate {

  private final CommandHandlerRegistry commandHandlerRegistry;

  private final CommandScheduler commandScheduler;

  /**
   * Creates the gate with the given handler registry and scheduler.
   *
   * @param commandHandlerRegistry The {@link CommandHandlerRegistry} to use.
   * @param commandScheduler       The {@link CommandScheduler} to use.
   * @throws NullPointerException If any of the arguments are null.
   */
  public DefaultGate(CommandHandlerRegistry commandHandlerRegistry,
                     CommandScheduler commandScheduler) {
    Preconditions.checkNotNull(commandHandlerRegistry);
    Preconditions.checkNotNull(commandScheduler);
    this.commandHandlerRegistry = commandHandlerRegistry;
    this.commandScheduler = commandScheduler;
  }

  @Override
  public void dispatch(Command cmd) {
    execute(cmd);
  }

  @Override
  public void schedule(Command cmd) {
    commandScheduler.schedule(new ExecutorCommandRunner(cmd));
  }

  protected void execute(Command cmd) {
    CommandHandler<Command> commandHandler =
        this.commandHandlerRegistry.getCommandHandler(cmd);

    commandHandler.handle(cmd);
  }

  private final class ExecutorCommandRunner implements CommandRunner {

    private final Command command;

    public ExecutorCommandRunner(Command command) {
      Preconditions.checkNotNull(command);
      this.command = command;
    }

    @Override
    public void run() {
      execute(command);
    }

  }

}
