package com.brillantcode.gate.handler;

import com.brillantcode.gate.Command;
import com.brillantcode.gate.GateException;

/**
 * Indicates that no {@link CommandHandler}
 * was found for a {@link Command} type.
 */
public class NoHandlerFoundException extends GateException {

  private static final long serialVersionUID = 1L;

  public NoHandlerFoundException(Class<? extends Command> command) {
    super("No handler found for command class " + command.getName());
  }

}
