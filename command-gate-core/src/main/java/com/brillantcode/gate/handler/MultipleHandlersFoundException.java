package com.brillantcode.gate.handler;

import com.brillantcode.gate.Command;
import com.brillantcode.gate.GateException;

/**
 * Indicates that more than one {@link CommandHandler}
 * were found for one {@link Command} type.
 */
public class MultipleHandlersFoundException extends GateException {

  private static final long serialVersionUID = 1L;

  public MultipleHandlersFoundException(Class<? extends Command> commandClass) {
    super("More than one handler found for command class " + commandClass.getName());
  }

}
