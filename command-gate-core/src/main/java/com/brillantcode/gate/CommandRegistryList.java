package com.brillantcode.gate;

import com.google.common.base.Preconditions;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A {@link CommandRegistry} implementation that is itself
 * a list of {@link CommandRegistry}, combining them.
 */
public class CommandRegistryList implements CommandRegistry {

  private final List<CommandRegistry> registries;

  public CommandRegistryList(List<CommandRegistry> registries) {
    Preconditions.checkNotNull(registries);
    this.registries = registries;
  }

  @Override
  public Set<Class<? extends Command>> getCommandTypes() {
    return this.registries.stream()
        .map(CommandRegistry::getCommandTypes)
        .flatMap(Set::stream)
        .collect(Collectors.toUnmodifiableSet());
  }

}


