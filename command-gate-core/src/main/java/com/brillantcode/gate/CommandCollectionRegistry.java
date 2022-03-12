package com.brillantcode.gate;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import java.util.Collection;
import java.util.Set;

/**
 * A {@link CommandRegistry} implementation
 * that holds a static collection of {@link Command} types.
 */
public class CommandCollectionRegistry implements CommandRegistry {

  private final Collection<Class<? extends Command>> commandTypes;

  public CommandCollectionRegistry(Collection<Class<? extends Command>> commandTypes) {
    Preconditions.checkNotNull(commandTypes);
    this.commandTypes = commandTypes;
  }

  @Override
  public Set<Class<? extends Command>> getCommandTypes() {
    return ImmutableSet.copyOf(this.commandTypes);
  }

}
