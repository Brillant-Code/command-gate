package com.brillantcode.gate.handler;

import com.brillantcode.gate.Command;

/**
 * Command handler registry is responsible for keeping track
 * of all available command handlers and providing selected
 * handler for a specific command class.
 */
public interface CommandHandlerRegistry {

  /**
   * Get a {@link CommandHandler} for the command of type {@link CommandT}.
   *
   * @param command    The {@link Command} to get the {@link CommandHandler} for.
   * @param <CommandT> The type of the {@link Command}.
   * @return The {@link CommandHandler} for the given {@link Command}.
   * @throws NoHandlerFoundException If no handler is found for the given {@link Command}.
   */
  <CommandT extends Command> CommandHandler<CommandT> getCommandHandler(
      CommandT command
  );

}
