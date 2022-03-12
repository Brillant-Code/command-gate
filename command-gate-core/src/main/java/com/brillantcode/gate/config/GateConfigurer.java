package com.brillantcode.gate.config;

import com.brillantcode.gate.CommandRegistry;
import com.brillantcode.gate.DefaultGate;
import com.brillantcode.gate.Gate;
import com.brillantcode.gate.handler.AutoInitializingMapCommandHandlerRegistry;
import com.brillantcode.gate.handler.CommandHandlerDiscoverer;
import com.brillantcode.gate.handler.CommandHandlerRegistry;
import com.brillantcode.gate.handler.CompositeCommandHandlerDiscoverer;
import com.brillantcode.gate.scheduler.CommandScheduler;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Allows building a custom-configured instance of a {@link Gate}.
 *
 * <pre>
 *   GateConfigurer.create()
 *     .commandRegistry()
 *       .
 * </pre>
 */
public class GateConfigurer {

  private final AtomicBoolean instantiated = new AtomicBoolean(false);

  private final List<CommandHandlerDiscoverer> commandHandlerDiscoverers = new ArrayList<>();

  private CommandHandlerRegistryFactory commandHandlerRegistryFactory =
      AutoInitializingMapCommandHandlerRegistry::new;

  private @Nullable CommandRegistryConfigurer<GateConfigurer> commandRegistryConfigurer;

  private @Nullable CommandSchedulerConfigurer<GateConfigurer> commandSchedulerConfigurer;

  private GateConfigurer() {
  }

  public static GateConfigurer create() {
    return new GateConfigurer();
  }


  /**
   * Sets the {@link CommandRegistryConfigurer} used to create
   * the {@link CommandRegistry}.
   *
   * <p>Overrides previous command registry configuration.
   *
   * @param commandRegistryConfigurer The configurer to use.
   * @return This configurer instance.
   */
  public GateConfigurer commandRegistry(
      CommandRegistryConfigurer<GateConfigurer> commandRegistryConfigurer) {
    Preconditions.checkNotNull(commandRegistryConfigurer);
    this.commandRegistryConfigurer = commandRegistryConfigurer;
    return this;
  }

  /**
   * Returns current {@link CommandRegistryConfigurer} or creates a default one
   * if non exists.
   *
   * @return The command registry configurer instance.
   * @see CommandRegistryConfigurer
   */
  public CommandRegistryConfigurer<GateConfigurer> commandRegistry() {
    if (this.commandRegistryConfigurer == null) {
      this.commandRegistryConfigurer = new CommandRegistryConfigurer<>(this);
    }
    return commandRegistryConfigurer;
  }


  /**
   * Sets the {@link CommandHandlerRegistryFactory} that should be used
   * for instantiating the {@link CommandHandlerRegistry}.
   *
   * @param commandHandlerRegistryFactory The factory to use for instantiating the registry.
   * @return This configurer instance.
   */
  public GateConfigurer commandHandlerRegistry(
      CommandHandlerRegistryFactory commandHandlerRegistryFactory) {
    Preconditions.checkNotNull(commandHandlerRegistryFactory);
    this.commandHandlerRegistryFactory = commandHandlerRegistryFactory;
    return this;
  }


  /**
   * Inserts the specified {@link CommandHandlerDiscoverer} at the end
   * of the list.
   *
   * @param handlerDiscoverer The handler discoverer to insert.
   * @return This configurer instance.
   */
  public GateConfigurer addHandlerDiscoverer(CommandHandlerDiscoverer handlerDiscoverer) {
    Preconditions.checkNotNull(handlerDiscoverer);
    this.commandHandlerDiscoverers.add(handlerDiscoverer);
    return this;
  }

  /**
   * Inserts the specified {@link CommandHandlerDiscoverer} at the specified
   * position in the list.
   *
   * @param position          index at which the specified discoverer is to be inserted.
   * @param handlerDiscoverer The handler discoverer to insert.
   * @return This configurer instance.
   */
  public GateConfigurer addHandlerDiscoverer(int position,
                                             CommandHandlerDiscoverer handlerDiscoverer) {
    Preconditions.checkNotNull(handlerDiscoverer);
    Preconditions.checkArgument(position >= 0,
        "Handler discoverer position cannot be negative");
    Preconditions.checkArgument(position <= commandHandlerDiscoverers.size(),
        "Handler discoverer position cannot be greater than discoverer list size");
    this.commandHandlerDiscoverers.add(position, handlerDiscoverer);
    return this;
  }


  /**
   * Sets the {@link CommandSchedulerConfigurer} used to create
   * the {@link CommandScheduler}.
   *
   * <p>Overrides previous command scheduler configuration.
   *
   * @param commandSchedulerConfigurer The configurer to use.
   * @return This configurer instance.
   */
  public GateConfigurer commandScheduler(
      CommandSchedulerConfigurer<GateConfigurer> commandSchedulerConfigurer) {
    Preconditions.checkNotNull(commandSchedulerConfigurer);
    this.commandSchedulerConfigurer = commandSchedulerConfigurer;
    return this;
  }

  /**
   * Returns current {@link CommandSchedulerConfigurer} or creates a default one
   * if non exists.
   *
   * @return The command scheduler configurer instance.
   * @see CommandSchedulerConfigurer
   */
  public CommandSchedulerConfigurer<GateConfigurer> commandScheduler() {
    if (this.commandSchedulerConfigurer == null) {
      this.commandSchedulerConfigurer = new CommandSchedulerConfigurer<>(this);
    }
    return this.commandSchedulerConfigurer;
  }


  /**
   * Creates new instance of a {@link Gate} according to the current configuration
   * of this instance of {@link GateConfigurer}.
   *
   * @return The configured instance of {@link Gate}.
   * @throws IllegalStateException If a gate instance was already created by this configurer.
   */
  public Gate configure() {

    if (this.instantiated.get()) {
      throw new IllegalStateException("Command Gate object was already instantiated "
          + " by this configurer instance.");
    }

    CommandRegistry commandRegistry = commandRegistry().configure();

    CommandHandlerDiscoverer commandHandlerDiscoverer =
        new CompositeCommandHandlerDiscoverer(commandHandlerDiscoverers);

    CommandHandlerRegistry handlerRegistry = commandHandlerRegistryFactory.instantiate(
        commandRegistry, commandHandlerDiscoverer
    );

    CommandScheduler commandScheduler = commandScheduler().configure();

    return new DefaultGate(
        handlerRegistry, commandScheduler
    );
  }

}
