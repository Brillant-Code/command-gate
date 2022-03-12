package com.brillantcode.gate;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.util.ClassUtils;

/**
 * A {@link CommandRegistry} implementation that is using Spring's
 * {@link ClassPathScanningCandidateComponentProvider} to search for
 * class names of classes implementing {@link Command} interface,
 * that are located in or up the hierarchy of one of the given base packages.
 */
public class ClassPathScannerCommandRegistry implements CommandRegistry, InitializingBean {

  private final String[] basePackages;

  @Nullable
  private CommandRegistry commandRegistry;

  /**
   * Instantiates the scanner for the given base packages.
   *
   * @param basePackages Java packages to start the scanning from.
   */
  public ClassPathScannerCommandRegistry(String[] basePackages) {
    this.basePackages = basePackages;
  }

  @Override
  public void afterPropertiesSet() {
    Preconditions.checkNotNull(basePackages);
    Preconditions.checkState(basePackages.length > 0,
        "Must provide at least on package");

    ClassPathScanningCandidateComponentProvider scanner =
        new ClassPathScanningCandidateComponentProvider(false);
    scanner.addIncludeFilter(new AssignableTypeFilter(Command.class));

    List<Class<? extends Command>> commandTypes = new ArrayList<>();
    for (String basePackage : basePackages) {
      commandTypes.addAll(
          extractCommandTypes(scanner.findCandidateComponents(basePackage))
      );
    }

    this.commandRegistry = new CommandCollectionRegistry(commandTypes);
  }

  protected List<Class<? extends Command>> extractCommandTypes(
      Set<BeanDefinition> beanDefinitions) {

    // TODO Consider using BeanDefinition#getResolvableType instead of loading by class name
    return beanDefinitions.stream()
        .map(this::getCommandClassName)
        .map(this::loadCommandClass)
        .collect(Collectors.toList());
  }

  protected String getCommandClassName(BeanDefinition beanDefinition) {

    String beanClassName = beanDefinition.getBeanClassName();
    if (beanClassName == null) {
      throw new GateException(
          "Found a Command candidate but could not resolve class name"
      );
    }

    return beanClassName;
  }

  @SuppressWarnings("unchecked")
  protected Class<? extends Command> loadCommandClass(String className) {
    ClassLoader defaultClassLoader = ClassUtils.getDefaultClassLoader();
    try {
      return (Class<? extends Command>)
          Preconditions.checkNotNull(defaultClassLoader).loadClass(className);
    } catch (ClassNotFoundException e) {
      throw new IllegalArgumentException(String.format(
          "Could not load class with name %s", className
      ), e);
    } catch (ClassCastException e) {
      throw new IllegalArgumentException(String.format(
          "Could not cast class %s to %s class", className, Command.class.getName()
      ), e);
    }
  }

  @Override
  public Set<Class<? extends Command>> getCommandTypes() {
    return Preconditions.checkNotNull(this.commandRegistry)
        .getCommandTypes();
  }

}
