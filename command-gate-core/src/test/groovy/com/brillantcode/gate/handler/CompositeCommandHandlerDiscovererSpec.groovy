package com.brillantcode.gate.handler

import com.brillantcode.gate.TestClassHandler
import com.brillantcode.gate.TestCommands
import spock.lang.Specification
import spock.lang.Subject

class CompositeCommandHandlerDiscovererSpec extends Specification {

  @Subject
  CompositeCommandHandlerDiscoverer discoverer

  CommandHandlerDiscoverer discoverer1 = Mock(CommandHandlerDiscoverer)

  CommandHandlerDiscoverer discoverer2 = Mock(CommandHandlerDiscoverer)

  def setup() {
    discoverer = new CompositeCommandHandlerDiscoverer([discoverer1, discoverer2])
  }

  def "GetHandlerFor should call all discoverers"() {
    given: "a command type"
    def cmdType = TestCommands.SimpleCommand

    when: "getHandlerFor is called"
    discoverer.getHandlerFor(cmdType)

    then: "both discoverers were called"
    1 * discoverer1.getHandlerFor(cmdType)
    1 * discoverer2.getHandlerFor(cmdType)
  }

  def "GetHandlerFor returns null if no handler found"() {
    given: "a command type"
    def cmdType = TestCommands.SimpleCommand

    and: "both discoverers return null for the command type"
    discoverer1.getHandlerFor(cmdType) >> null
    discoverer2.getHandlerFor(cmdType) >> null

    when: "getHandlerFor is called"
    def result = discoverer.getHandlerFor(cmdType)

    then: "null is returned"
    assert result == null
  }

  def "GetHandlerFor throws if more than one handler found"() {
    given: "a command type"
    def cmdType = TestCommands.SimpleCommand

    and: "both discoverers return a handler"
    discoverer1.getHandlerFor(cmdType) >> new TestClassHandler()
    discoverer2.getHandlerFor(cmdType) >> new TestClassHandler()

    when: "getHandlerFor is called"
    discoverer.getHandlerFor(cmdType)

    then: "exception is thrown"
    thrown(MultipleHandlersFoundException)
  }

  def "GetHandlerFor returns the handler"() {
    given: "a command type"
    def cmdType = TestCommands.SimpleCommand

    and: "one discoverer returns null for the command type"
    discoverer1.getHandlerFor(cmdType) >> null

    and: "another discoverer returns a handler"

    def handler = new TestClassHandler()
    discoverer2.getHandlerFor(cmdType) >> handler

    when: "getHandlerFor is called"
    def result = discoverer.getHandlerFor(cmdType)

    then: "handler is returned"
    assert result == handler
  }

}
