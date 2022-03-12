package com.brillantcode.gate.annotation;

import com.brillantcode.gate.ClassPathScannerCommandRegistry;
import com.brillantcode.gate.CommandRegistry;
import com.google.common.base.Preconditions;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

/**
 * Configuration responsible for instantiating
 * a {@link CommandRegistry} bean, based on configuration
 * defined in the {@link EnableCommandGate} annotation.
 */
public class CommandRegistryConfiguration implements ImportAware {

  @Nullable
  protected AnnotationAttributes enableCommandGate;

  @Nullable
  protected String defaultBasePackage;

  @Override
  public void setImportMetadata(AnnotationMetadata importMetadata) {

    this.enableCommandGate = AnnotationAttributes.fromMap(
        importMetadata.getAnnotationAttributes(
            EnableCommandGate.class.getName(), false
        )
    );

    if (this.enableCommandGate == null) {
      throw new IllegalArgumentException(
          "@EnableCommandGate is not present on importing class " + importMetadata.getClassName());
    }
    this.defaultBasePackage = ClassUtils.getPackageName(importMetadata.getClassName());
  }

  /**
   * Creates an instance of {@link ClassPathScannerCommandRegistry}
   * that will scan packages defined in the {@link EnableCommandGate}
   * annotation.
   *
   * @return A command registry bean instance.
   */
  @Bean
  public CommandRegistry commandRegistry() {
    Preconditions.checkNotNull(this.enableCommandGate,
        "@EnableCommandGate annotation metadata was not injected");

    String[] basePackages = Preconditions.checkNotNull(this.enableCommandGate)
        .getStringArray("basePackages");

    if (basePackages.length > 0) {
      Preconditions.checkNotNull(this.defaultBasePackage,
          "defaultBasePackage was not initialized");
      basePackages = new String[]{Preconditions.checkNotNull(this.defaultBasePackage)};
    }

    return new ClassPathScannerCommandRegistry(basePackages);
  }

}
