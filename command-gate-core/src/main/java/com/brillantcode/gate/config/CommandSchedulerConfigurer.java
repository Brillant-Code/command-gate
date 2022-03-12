package com.brillantcode.gate.config;

import com.brillantcode.gate.scheduler.CommandScheduler;
import org.apache.commons.lang3.NotImplementedException;

/**
 * Exposes configuration options and building of a {@link CommandScheduler}.
 *
 * @param <C> Type of the {@link GateConfigurer} returned by
 *            {@link ComponentConfigurer#and()} for chaining.
 */
public class CommandSchedulerConfigurer<C extends GateConfigurer>
    extends ComponentConfigurer<C, CommandScheduler> {

  public CommandSchedulerConfigurer(C baseConfigurer) {
    super(baseConfigurer);
  }

  @Override
  protected CommandScheduler configure() {
    return runner -> {
      throw new NotImplementedException(
          "Command scheduling configurer is not yet implemented. "
              + "To use command scheduling manually create an instance of a Gate."
      );
    };
  }

}
