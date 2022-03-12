package com.brillantcode.gate.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that carries information used for discovering
 * a {@link com.brillantcode.gate.handler.CommandHandler}
 * for a {@link com.brillantcode.gate.Command}.
 *
 * <p>What this annotation does depends on its target.
 *
 * <p>If it is put on a Java type assignable to {@link com.brillantcode.gate.Command}
 * it is used for specifying the {@link com.brillantcode.gate.handler.CommandHandler}
 * to use for the command.
 *
 * <p>If it is put on a Java type assignable to {@link com.brillantcode.gate.handler.CommandHandler}
 * it serves as an additional markup for a command handler. It has no functional effect.
 *
 * <p>If it is put on a Java method, it declares the method
 * as a {@link com.brillantcode.gate.handler.CommandHandler}
 * for the command type equal to the methods' first parameter type.
 *
 * <p>The method must declare a single parameter that reflects the command type
 * that the method handles.
 */
@Target({
    ElementType.TYPE,
    ElementType.METHOD
})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CommandHandler {

  /**
   * Specifies the type of the {@link com.brillantcode.gate.handler.CommandHandler}
   * to be used for the annotated {@link com.brillantcode.gate.Command}.
   *
   * <p>Applicable only if the annotation target is assignable
   * to {@link com.brillantcode.gate.Command}.
   *
   * <p>In order to be recognized as a valid command handler
   * the type must be a non-abstract Java class.
   *
   * <p>TODO Add documentation about command handler instantiation.
   *
   * @return Type of the command handler.
   */
  Class<? extends com.brillantcode.gate.handler.CommandHandler> handlerType()
      default com.brillantcode.gate.handler.CommandHandler.class;

}
