package com.brillantcode.gate


import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

class MethodDescriptorSpec extends Specification {

  @Subject
  MethodDescriptor methodDescriptor

  @Shared
  def handlerMethod = TestMethodHandler.class.getDeclaredMethod(
      TestMethodHandler.HANDLER_METHOD_NAME, TestMethodHandler.HANDLER_COMMAND_TYPE
  )

  def setup() {
    methodDescriptor = new MethodDescriptor(handlerMethod)
  }

  def "methodReference should return a text representation of the method containing class and method names"() {
    when:
    def methodReference = methodDescriptor.methodReference()

    then:
    assert methodReference.contains(handlerMethod.getDeclaringClass().getName())
    assert methodReference.contains(handlerMethod.getName())
  }

  def "parameter should return the parameter at given index"() {
    when:
    def parameter = methodDescriptor.parameter(0)

    then:
    assert parameter.getType() == TestMethodHandler.HANDLER_COMMAND_TYPE
  }

  def "parameter should throw when the provided index is #indexDesc"() {
    when:
    methodDescriptor.parameter(index)

    then:
    thrown(exception)

    where:
    index | indexDesc                     | exception
    -1    | "negative"                    | IllegalArgumentException
    1     | "larger than parameter count" | IllegalArgumentException
  }

}
