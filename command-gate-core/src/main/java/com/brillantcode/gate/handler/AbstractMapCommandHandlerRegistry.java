package com.brillantcode.gate.handler;

import com.brillantcode.gate.Command;
import com.brillantcode.gate.CommandRegistry;
import com.brillantcode.gate.GateException;
import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract implementation of {@link CommandHandlerRegistry}
 * that stores known {@link CommandHandler} objects in a {@link HashMap}.
 */
public abstract class AbstractMapCommandHandlerRegistry implements CommandHandlerRegistry {

  private final CommandRegistry commandRegistry;

  private final CommandHandlerDiscoverer handlerDiscoverer;

  private final Map<Class<? extends Command>, CommandHandler<?>> commandHandlers =
      new HashMap<>();

  /**
   * Creates a {@link CommandHandlerRegistry} instance
   * that will try to find handlers for commands provided
   * by the given {@link CommandRegistry} using
   * given {@link CommandHandlerDiscoverer}.
   *
   * @param commandRegistry   The {@link CommandRegistry} to provide the known types.
   * @param handlerDiscoverer The {@link CommandHandlerDiscoverer} to use.
   */
  public AbstractMapCommandHandlerRegistry(CommandRegistry commandRegistry,
                                           CommandHandlerDiscoverer handlerDiscoverer) {
    Preconditions.checkNotNull(commandRegistry);
    Preconditions.checkNotNull(handlerDiscoverer);
    this.commandRegistry = commandRegistry;
    this.handlerDiscoverer = handlerDiscoverer;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <CommandT extends Command> CommandHandler<CommandT> getCommandHandler(
      CommandT command) {
    Preconditions.checkNotNull(command);
    Preconditions.checkState(isInitialized(), "Registry is not initialized");

    CommandHandler<?> commandHandler = this.commandHandlers.get(command.getClass());

    if (commandHandler == null) {
      throw new NoHandlerFoundException(command.getClass());
    }

    try {
      return (CommandHandler<CommandT>) commandHandler;
    } catch (ClassCastException e) {
      throw new GateException(
          "Unable to cast CommandHandler to command type for command class "
              + command.getClass().getName()
      );
    }
  }

  protected void doRegister() {
    for (Class<? extends Command> commandType : commandRegistry.getCommandTypes()) {
      CommandHandler<?> handlerForCommand = handlerDiscoverer.getHandlerFor(commandType);
      if (handlerForCommand == null) {
        throw new NoHandlerFoundException(commandType);
      }
      this.commandHandlers.put(commandType, handlerForCommand);
    }
  }

  protected abstract boolean isInitialized();

}
