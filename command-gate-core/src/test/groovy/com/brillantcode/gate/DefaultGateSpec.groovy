package com.brillantcode.gate

import com.brillantcode.gate.handler.CommandHandler
import com.brillantcode.gate.handler.CommandHandlerRegistry
import com.brillantcode.gate.scheduler.CommandRunner
import com.brillantcode.gate.scheduler.CommandScheduler
import spock.lang.Specification
import spock.lang.Subject

class DefaultGateSpec extends Specification {

  @Subject
  DefaultGate gate

  CommandHandlerRegistry commandHandlerRegistry = Mock(CommandHandlerRegistry)

  CommandScheduler commandScheduler = Mock(CommandScheduler)

  def commandHandler = Mock(CommandHandler)

  def setup() {
    commandHandlerRegistry.getCommandHandler(_ as TestCommands.SimpleCommand) >> commandHandler
    gate = new DefaultGate(commandHandlerRegistry, commandScheduler)
  }

  def "Dispatch should call a handler for the command"() {
    given: "a command"
    def cmd = new TestCommands.SimpleCommand("arg")

    when: "it is dispatched with a gate"
    gate.dispatch(cmd)

    then: "command handler for that command is called"
    1 * commandHandler.handle(cmd)
  }

  def "Schedule should call the scheduler with a command runner as an argument"() {
    given: "a command"
    def cmd = new TestCommands.SimpleCommand("arg")

    when: "it is scheduled with a gate"
    gate.schedule(cmd)

    then: "command scheduler is called"
    1 * commandScheduler.schedule(_ as CommandRunner)
  }

  def "Command runner passed to schedule should call the handler when invoked"() {
    given: "a command"
    def cmd = new TestCommands.SimpleCommand("arg")

    and: "command scheduler which immediately calls command runner's run method"
    commandScheduler.schedule(_ as CommandRunner) >> {
      CommandRunner runner -> runner.run()
    }

    when: "command is scheduled with a gate"
    gate.schedule(cmd)

    then: "command handler for that command is called"
    1 * commandHandler.handle(cmd)
  }

}
