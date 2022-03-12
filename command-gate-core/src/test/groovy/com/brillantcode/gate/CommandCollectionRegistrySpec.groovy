package com.brillantcode.gate

import spock.lang.Specification

class CommandCollectionRegistrySpec extends Specification {

  def "Command collection registry returns the correct collection of command types"() {
    given: "list of command types"
    def commandTypes = [
        TestCommands.NoParamsCommand,
        TestCommands.SimpleCommand
    ]

    and: "a command-collection registry with those types"
    def registry = new CommandCollectionRegistry(commandTypes)

    when: "command types are retrieved from the registry"
    def results = registry.getCommandTypes()

    then: "they are the same types that were passed to the registry"
    assert results == commandTypes as Set
  }

}
