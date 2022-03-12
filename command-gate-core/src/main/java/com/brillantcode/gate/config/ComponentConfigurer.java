package com.brillantcode.gate.config;

import com.google.common.base.Preconditions;

abstract class ComponentConfigurer<C extends GateConfigurer, T> {

  private final C baseConfigurer;

  public ComponentConfigurer(C baseConfigurer) {
    Preconditions.checkNotNull(baseConfigurer);
    this.baseConfigurer = baseConfigurer;
  }

  public C and() {
    return this.baseConfigurer;
  }

  protected abstract T configure();

}
