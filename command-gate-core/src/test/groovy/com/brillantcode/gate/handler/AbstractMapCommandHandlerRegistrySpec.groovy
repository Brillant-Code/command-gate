package com.brillantcode.gate.handler

import com.brillantcode.gate.CommandCollectionRegistry
import com.brillantcode.gate.CommandRegistry
import com.brillantcode.gate.TestClassHandler
import com.brillantcode.gate.TestCommands
import spock.lang.Specification
import spock.lang.Subject

class AbstractMapCommandHandlerRegistrySpec extends Specification {

  @Subject
  AbstractMapCommandHandlerRegistry registry

  CommandHandlerDiscoverer handlerDiscoverer = Mock(CommandHandlerDiscoverer)

  CommandRegistry commandRegistry

  def setup() {
    commandRegistry = new CommandCollectionRegistry(
        [TestCommands.SimpleCommand]
    )
    registry = new TestingMapCommandHandlerRegistryTest(commandRegistry, handlerDiscoverer)
  }

  def "GetCommandHandler should throw if registry is not initialized"() {
    given: "a command is created"
    def command = new TestCommands.SimpleCommand("Hello")

    when: "getCommandHandler for the command is called"
    registry.getCommandHandler(command)

    then: "exception is thrown"
    thrown(IllegalStateException)
  }

  def "GetCommandHandler should return the handler if it exists and registry is initialized"() {
    given: "a registration of the handler is made"
    def expectedHandler = new TestClassHandler()
    handlerDiscoverer.getHandlerFor(TestCommands.SimpleCommand) >> expectedHandler
    registry.doRegister()

    and: "a command is created"
    def command = new TestCommands.SimpleCommand("Hello")

    when: "getCommandHandler for the command is called"
    def handler = registry.getCommandHandler(command)

    then: "command returned should be the one that is registered"
    assert handler == expectedHandler
  }

  def "GetCommandHandler should throw if handler not found and registry is initialized"() {
    given: "handler for the command is found and registration is done"
    handlerDiscoverer.getHandlerFor(TestCommands.SimpleCommand) >> new TestClassHandler()
    registry.doRegister()

    and: "a command of an unknown type is created"
    def command = new TestCommands.NoParamsCommand()

    when: "getCommandHandler for the unknown command type is called"
    registry.getCommandHandler(command)

    then: "exception is thrown"
    thrown(NoHandlerFoundException)
  }

  def "DoRegister should throw if no handler found for a command"() {
    given: "discoverer returns null for the command"
    handlerDiscoverer.getHandlerFor(TestCommands.SimpleCommand) >> null

    when: "doRegister is called"
    registry.doRegister()

    then: "exception is thrown"
    thrown(NoHandlerFoundException)
  }

  def "Before calling doRegister isInitialized returns false"() {
    expect:
    assert !registry.isInitialized()
  }

  def "After calling doRegister isInitialized returns true"() {
    given: "discoverer returns the handler for the command"
    handlerDiscoverer.getHandlerFor(TestCommands.SimpleCommand) >> new TestClassHandler()

    when: "doRegister is called"
    registry.doRegister()

    then: "isInitialized returns true"
    assert registry.isInitialized()
  }

  static class TestingMapCommandHandlerRegistryTest
      extends AbstractMapCommandHandlerRegistry {

    private boolean initialized = false

    TestingMapCommandHandlerRegistryTest(CommandRegistry commandRegistry,
                                         CommandHandlerDiscoverer handlerDiscoverer) {
      super(commandRegistry, handlerDiscoverer)
    }

    @Override
    protected boolean isInitialized() {
      return this.initialized
    }

    @Override
    protected void doRegister() {
      super.doRegister()
      this.initialized = true
    }

  }

}
