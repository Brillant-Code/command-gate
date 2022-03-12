package com.brillantcode.gate.handler;

import com.brillantcode.gate.Command;
import com.brillantcode.gate.MethodDescriptor;
import com.google.common.base.Preconditions;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.checkerframework.checker.initialization.qual.UnderInitialization;

/**
 * {@link CommandHandler} implementation that reflectively
 * calls a Java method.
 *
 * <p>Java method that is responsible for handling a {@link Command}
 * must meet following criteria:
 * <ul>
 *   <li>Return no value</li>
 *   <li>Take only one parameter</li>
 *   <li>Parameter type have to match the type of the handler {@link Command}</li>
 * </ul>
 *
 * @param <CommandT> The type of the {@link Command} that the handler handles.
 */
public class ReflectiveMethodCommandHandler<CommandT extends Command>
    implements CommandHandler<CommandT> {

  private final Object methodOwningBean;

  private final MethodDescriptor method;

  private final Class<CommandT> commandType;

  /**
   * Instantiates new handler for the given command type that will be handled by a Java method
   * described by a {@link MethodDescriptor} and invoked on the given object.
   *
   * @param methodOwningBean Object on which the method will be invoked.
   * @param method           A {@link MethodDescriptor} of the Java method to invoke.
   * @param commandType      The class of the {@link Command} that the handler handles.
   * @throws NullPointerException If any of the arguments are null.
   */
  public ReflectiveMethodCommandHandler(Object methodOwningBean, MethodDescriptor method,
                                        Class<CommandT> commandType) {
    Preconditions.checkNotNull(methodOwningBean);
    Preconditions.checkNotNull(method);
    Preconditions.checkNotNull(commandType);
    this.methodOwningBean = methodOwningBean;
    this.method = method;
    this.commandType = commandType;
    assertHandlerMethodValid(method, commandType);
  }

  @Override
  public void handle(CommandT command) {

    Preconditions.checkArgument(this.commandType.equals(command.getClass()),
        "Wrong command type " + command.getClass().getName()
            + " received by a handler for command type " + this.commandType.getName());

    final Method javaMethod = this.method.javaMethod();
    try {
      javaMethod.invoke(methodOwningBean, command);
    } catch (InvocationTargetException ex) {
      Throwable cause = ex.getCause();
      if (cause instanceof RuntimeException) {
        throw (RuntimeException) cause;
      }
      if (cause instanceof Exception) {
        throw new RuntimeException(cause);
      }
      if (cause instanceof Error) {
        throw (Error) cause;
      }
      throw new RuntimeException(ex);
    } catch (IllegalAccessException ex) {
      throw new IllegalStateException(ex);
    }
  }

  private void assertHandlerMethodValid(
      // @formatter:off
      @UnderInitialization ReflectiveMethodCommandHandler<CommandT> this,
      // @formatter:on
      MethodDescriptor method, Class<CommandT> commandType
  ) {
    Method javaMethod = method.javaMethod();
    Preconditions.checkArgument(
        javaMethod.getReturnType().equals(Void.TYPE),
        "Command handler method %s must not return a value",
        method.methodReference()
    );
    Preconditions.checkArgument(
        javaMethod.getParameterCount() == 1,
        "Command handler method %s should have exactly one parameter",
        method.methodReference()
    );

    Class<?> commandParameterType = method.parameter(0).getType();
    Preconditions.checkArgument(
        commandParameterType.equals(commandType),
        "Parameter of the command handler method %s "
            + "should be of type %s but is of type %s",
        method.methodReference(),
        commandType.getName(),
        commandParameterType.getName()
    );
  }

}
