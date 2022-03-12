package com.brillantcode.gate.handler;

import com.brillantcode.gate.Command;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Discoverer implementation that maintains a list
 * of {@link CommandHandlerDiscoverer} and combines the results.
 *
 * <p>When searching for a handler for a given command type,
 * it calls all the discoverers and expects only one of them
 * to return a result.
 *
 * <p>If more than one result is returned
 * per command type, {@link MultipleHandlersFoundException}
 * is thrown.
 *
 * <p>If no result is returned for a command type, null is returned.
 */
public class CompositeCommandHandlerDiscoverer implements CommandHandlerDiscoverer {

  private final List<CommandHandlerDiscoverer> discoverers;

  public CompositeCommandHandlerDiscoverer(List<CommandHandlerDiscoverer> discoverers) {
    Preconditions.checkNotNull(discoverers);
    this.discoverers = discoverers.stream().collect(Collectors.toUnmodifiableList());
  }

  @Override
  public <CommandT extends Command> @Nullable CommandHandler<CommandT> getHandlerFor(
      Class<CommandT> commandType
  ) {
    Preconditions.checkNotNull(commandType);

    List<CommandHandler<CommandT>> commandHandlers = new ArrayList<>();
    for (CommandHandlerDiscoverer commandHandlerDiscoverer : discoverers) {
      CommandHandler<CommandT> handler = commandHandlerDiscoverer.getHandlerFor(commandType);
      if (handler != null) {
        commandHandlers.add(handler);
      }
    }

    if (commandHandlers.size() == 0) {
      return null;
    } else if (commandHandlers.size() > 1) {
      throw new MultipleHandlersFoundException(commandType);
    }

    return commandHandlers.get(0);
  }

}
