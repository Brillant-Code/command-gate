package com.brillantcode.gate.config


import com.brillantcode.gate.TestCommands
import com.brillantcode.gate.handler.CommandHandler
import com.brillantcode.gate.handler.CommandHandlerDiscoverer
import spock.lang.Specification

class GateConfigurerSpec extends Specification {

  CommandHandlerDiscoverer handlerDiscoverer = Stub(CommandHandlerDiscoverer)

  CommandHandler simpleCommandHandler = Mock(CommandHandler)

  def setup() {
    handlerDiscoverer.getHandlerFor(TestCommands.SimpleCommand) >> simpleCommandHandler
  }

  def "Configures a simple command gate"() {
    given: "a command gate configurer instance"
    def configurer = new GateConfigurer()

    and: "a simple command type is registered"
    configurer.commandRegistry().addTypes([TestCommands.SimpleCommand])

    and: "command handler discoverer that returns simple command handler is added"
    configurer.addHandlerDiscoverer(handlerDiscoverer)

    and: "a simple command"
    def cmd = new TestCommands.SimpleCommand("Hello")

    when: "command gate is configured"
    def gate = configurer.configure()

    and: "SimpleCommand is dispatched to the newly created gate"
    gate.dispatch(cmd)

    then: "command handler is invoked"
    1 * simpleCommandHandler.handle(cmd)
  }

}
