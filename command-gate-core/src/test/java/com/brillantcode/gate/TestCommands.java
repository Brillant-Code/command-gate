package com.brillantcode.gate;

import lombok.Value;

public class TestCommands {

  public static class NoParamsCommand implements Command {

  }

  @Value
  public static class SimpleCommand implements Command {
    String parameter;
  }

  public static class NoHandlerCommand implements Command {

  }

}
