package com.brillantcode.gate.handler

import com.brillantcode.gate.MethodDescriptor
import com.brillantcode.gate.TestCommands.NoParamsCommand
import com.brillantcode.gate.TestCommands.SimpleCommand
import spock.lang.Specification

class ReflectiveMethodCommandHandlerSpec extends Specification {

  HandlerHolder handlerHolder

  def setup() {
    handlerHolder = new HandlerHolder()
  }

  def "Handler method must not return a value"() {
    given:
    def method = new MethodDescriptor(
        HandlerHolder.getDeclaredMethod("handlerReturningValue", SimpleCommand)
    )

    when:
    new ReflectiveMethodCommandHandler<>(handlerHolder, method, SimpleCommand)

    then:
    thrown(IllegalArgumentException)
  }

  def "Handler method must have exactly one parameter"() {
    given:
    def method = new MethodDescriptor(
        HandlerHolder.getDeclaredMethod("handlerWithNoParam")
    )

    when:
    new ReflectiveMethodCommandHandler(handlerHolder, method, SimpleCommand)

    then:
    thrown(IllegalArgumentException)
  }

  def "Handler method parameter type must match the command type"() {
    given:
    def method = new MethodDescriptor(
        HandlerHolder.getDeclaredMethod("handlerWithWrongParamType", NoParamsCommand)
    )

    when:
    new ReflectiveMethodCommandHandler<>(handlerHolder, method, SimpleCommand)

    then:
    thrown(IllegalArgumentException)
  }

  static class HandlerHolder {

    Integer handlerReturningValue(SimpleCommand cmd) {
      42
    }

    void handlerWithNoParam() {

    }

    void handlerWithWrongParamType(NoParamsCommand cmd) {

    }

  }

}
