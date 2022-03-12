package com.brillantcode.gate.handler;

import com.brillantcode.gate.Command;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Command handler discoverer is responsible for finding
 * {@link CommandHandler} classes.
 *
 * <p>Implementations are free to choose how the {@link CommandHandler}
 * classes are discovered.
 *
 * <p>If {@link CompositeCommandHandlerDiscoverer} is at use,
 * all {@link CommandHandlerDiscoverer} implementations
 * that are configured at the same time, must guarantee that
 * the handler sets discovered by each are disjoint. That means
 * that there must be no two discoverers in use that may find
 * the same command handler, as that would cause
 * a {@link MultipleHandlersFoundException} exception to be thrown.
 */
public interface CommandHandlerDiscoverer {

  /**
   * Search for the {@link CommandHandler} for the {@link Command}
   * of the given class.
   *
   * @param commandType Class of the {@link Command} to search the handler for.
   * @param <CommandT>  The type of the {@link Command} to search the handler for.
   * @return The {@link CommandHandler} for the given command type, null if not found.
   * @throws MultipleHandlersFoundException If more than one handler is found for the command type.
   */
  <CommandT extends Command> @Nullable CommandHandler<CommandT> getHandlerFor(
      Class<CommandT> commandType
  );

}
