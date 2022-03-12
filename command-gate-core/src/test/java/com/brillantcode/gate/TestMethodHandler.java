package com.brillantcode.gate;

public class TestMethodHandler {

  public static final String HANDLER_METHOD_NAME = "handle";

  public static final Class<TestCommands.SimpleCommand> HANDLER_COMMAND_TYPE =
      TestCommands.SimpleCommand.class;

  public void handle(TestCommands.SimpleCommand command) {

  }

}
