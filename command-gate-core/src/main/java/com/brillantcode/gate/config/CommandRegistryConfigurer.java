package com.brillantcode.gate.config;

import com.brillantcode.gate.Command;
import com.brillantcode.gate.CommandCollectionRegistry;
import com.brillantcode.gate.CommandRegistry;
import com.brillantcode.gate.CommandRegistryList;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;

/**
 * Exposes configuration options and building of a {@link CommandRegistry}.
 *
 * @param <C> Type of the {@link GateConfigurer} returned by
 *            {@link ComponentConfigurer#and()} for chaining.
 */
public class CommandRegistryConfigurer<C extends GateConfigurer>
    extends ComponentConfigurer<C, CommandRegistry> {

  private final Set<Class<? extends Command>> types = new HashSet<>();

  private final List<CommandRegistry> registries = new ArrayList<>();

  public CommandRegistryConfigurer(C baseConfigurer) {
    super(baseConfigurer);
  }

  /**
   * Adds collection of {@link Command} types to the command registry.
   *
   * @param types Java collection of command types to add.
   * @return The current instance of the configurer.
   */
  public CommandRegistryConfigurer<C> addTypes(Collection<Class<? extends Command>> types) {
    Preconditions.checkNotNull(types);
    this.types.addAll(Sets.newHashSet(types));
    return this;
  }

  /**
   * Clears the registry of all the command types added with {@link #addTypes(Collection)}.
   *
   * @return The current instance of the configurer.
   */
  public CommandRegistryConfigurer<C> clearTypes() {
    this.types.clear();
    return this;
  }

  /**
   * Adds the specified {@link CommandRegistry} to the end of the list of registries
   * of the root command registry.
   *
   * @param registry The command registry to be added.
   * @return The current instance of the configurer.
   */
  public CommandRegistryConfigurer<C> addRegistry(CommandRegistry registry) {
    Preconditions.checkNotNull(registry);
    this.registries.add(registry);
    return this;
  }

  /**
   * Adds the specified {@link CommandRegistry} at the specified position of the list
   * of registries of the root command registry.
   *
   * @param position The index at which the specified registry in to be added.
   * @param registry The command registry to be added.
   * @return The current instance of the configurer.
   */
  public CommandRegistryConfigurer<C> addRegistry(int position, CommandRegistry registry) {
    Preconditions.checkNotNull(registry);
    Preconditions.checkArgument(position >= 0,
        "Registry position cannot be negative");
    Preconditions.checkArgument(position <= registries.size(),
        "Registry position cannot be greater than registry list size");
    registries.add(position, registry);
    return this;
  }

  @Override
  protected CommandRegistry configure() {

    if (CollectionUtils.isNotEmpty(this.types)) {
      configureTypesRegistry();
    }

    List<CommandRegistry> collectedRegistries =
        Collections.unmodifiableList(this.registries);

    return new CommandRegistryList(collectedRegistries);
  }

  protected void configureTypesRegistry() {
    registries.add(new CommandCollectionRegistry(this.types));
  }

}
