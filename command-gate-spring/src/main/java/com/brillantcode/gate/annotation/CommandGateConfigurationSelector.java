package com.brillantcode.gate.annotation;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Spring configuration selector for {@link EnableCommandGate} annotation.
 */
public class CommandGateConfigurationSelector implements ImportSelector {

  @Override
  public String[] selectImports(AnnotationMetadata importingClassMetadata) {
    return new String[]{CommandRegistryConfiguration.class.getName()};
  }

}
