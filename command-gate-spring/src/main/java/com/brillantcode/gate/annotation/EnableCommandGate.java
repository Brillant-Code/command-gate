package com.brillantcode.gate.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

/**
 * Enables the configuration of the command gate.
 *
 * @see CommandGateConfigurationSelector
 * @see CommandRegistryConfiguration
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(CommandGateConfigurationSelector.class)
public @interface EnableCommandGate {

  /**
   * Java packages which should be scanned for {@link com.brillantcode.gate.Command} types.
   *
   * @return An array of Java package names.
   */
  String[] basePackages() default {};

}
