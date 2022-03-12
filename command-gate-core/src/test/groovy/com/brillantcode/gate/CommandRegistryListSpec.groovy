package com.brillantcode.gate

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

class CommandRegistryListSpec extends Specification {

  @Subject
  CommandRegistryList registry

  CommandRegistry subRegistry1 = Mock(CommandRegistry)

  CommandRegistry subRegistry2 = Mock(CommandRegistry)

  @Shared
  def registry1Type = TestCommands.NoParamsCommand

  @Shared
  def registry2Type = TestCommands.SimpleCommand

  def setup() {
    subRegistry1.getCommandTypes() >> ([registry1Type] as Set)
    subRegistry2.getCommandTypes() >> ([registry2Type] as Set)
  }

  def "When the registry list is empty it should return an empty set"() {
    given: "a list registry with an empty list of registries"
    registry = new CommandRegistryList([])

    when: "command types are retrieved"
    def results = registry.getCommandTypes()

    then: "empty set is returned"
    assert results.isEmpty()
  }

  def "When one registry is in the list it should return the contents of that registry"() {
    given: "a list registry with one sub registry on the list"
    registry = new CommandRegistryList([subRegistry1])

    when: "command types are retrieved"
    def results = registry.getCommandTypes()

    then: "command types of the only sub registry are returned"
    assert results == [registry1Type] as Set
  }

  def "When two registries are in the list it should return the combined contents of both registries"() {
    given: "a list registry with two sub registries on the list"
    registry = new CommandRegistryList([subRegistry1, subRegistry2])

    when: "command types are retrieved"
    def results = registry.getCommandTypes()

    then: "command types of both sub registries combined are returned"
    assert results == [registry1Type, registry2Type] as Set
  }

}
