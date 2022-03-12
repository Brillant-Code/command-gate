package com.brillantcode.gate.handler

import com.brillantcode.gate.CommandCollectionRegistry
import com.brillantcode.gate.TestClassHandler
import com.brillantcode.gate.TestCommands
import spock.lang.Specification

class AutoInitializingMapCommandHandlerRegistrySpec extends Specification {

  def "Registry is initialized right after calling the constructor"() {
    given: "registry with one command"
    def commandRegistry = new CommandCollectionRegistry([TestCommands.SimpleCommand])

    and: "discoverer that returns a handler for the command"
    def handlerDiscoverer = Stub(CommandHandlerDiscoverer)
    handlerDiscoverer.getHandlerFor(TestCommands.SimpleCommand) >> new TestClassHandler()

    when: "AutoInitializingMapCommandHandlerRegistry is instantiated"
    def registry = new AutoInitializingMapCommandHandlerRegistry(
        commandRegistry, handlerDiscoverer
    )

    then: "registry is initialized"
    assert registry.isInitialized()
  }

}
