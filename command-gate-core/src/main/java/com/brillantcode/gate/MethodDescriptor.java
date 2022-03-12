package com.brillantcode.gate;

import com.google.common.base.Preconditions;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Provides convenience methods for a Java {@link Method}.
 */
public class MethodDescriptor {

  private final Method javaMethod;

  public MethodDescriptor(Method javaMethod) {
    Preconditions.checkNotNull(javaMethod);
    this.javaMethod = javaMethod;
  }

  public Method javaMethod() {
    return javaMethod;
  }

  public String methodReference() {
    return javaMethod.getDeclaringClass().getName() + "#" + javaMethod.getName();
  }

  /**
   * Returns a {@link Parameter} representing the methods'
   * parameter at the given index.
   *
   * @param index The index of the parameter to return.
   * @return The {@link Parameter} at the given index.
   * @throws IllegalArgumentException If index is negative or
   *                                  larger or equal to the number of parameters.
   */
  public Parameter parameter(int index) {
    Preconditions.checkArgument(index >= 0,
        "Parameter index cannot be negative");
    Preconditions.checkArgument(
        javaMethod.getParameterCount() > index,
        "No parameter for index %s", index
    );
    return javaMethod.getParameters()[index];
  }

}
