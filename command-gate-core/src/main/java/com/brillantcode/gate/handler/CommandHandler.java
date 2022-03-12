package com.brillantcode.gate.handler;

import com.brillantcode.gate.Command;

/**
 * Command handler is responsible for actually executing
 * the command handling logic for a given {@link CommandT} command type.
 *
 * @param <CommandT> Type of the {@link Command} that this handler handles.
 */
public interface CommandHandler<CommandT extends Command> {

  /**
   * Execute the handler for a {@link Command} instance.
   *
   * @param command The {@link Command} to handle.
   */
  void handle(CommandT command);

}
