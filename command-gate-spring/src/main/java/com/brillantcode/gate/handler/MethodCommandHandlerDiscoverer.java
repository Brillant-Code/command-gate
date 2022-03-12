package com.brillantcode.gate.handler;

import com.brillantcode.gate.Command;
import com.brillantcode.gate.MethodDescriptor;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * Implementation of {@link CommandHandlerDiscoverer} that is looking
 * for methods annotated with {@link com.brillantcode.gate.annotation.CommandHandler}
 * annotation on beans in a Spring {@link org.springframework.beans.factory.BeanFactory}.
 *
 * @see com.brillantcode.gate.annotation.CommandHandler
 */
public class MethodCommandHandlerDiscoverer implements CommandHandlerDiscoverer {

  protected final Log logger;

  private final ListableBeanFactory beanFactory;

  /**
   * Instantiate the discoverer using the given
   * {@link ListableBeanFactory} as the source of beans to scan for
   * {@link com.brillantcode.gate.annotation.CommandHandler} annotated methods.
   *
   * @param beanFactory Spring {@link ListableBeanFactory} containing beans to scan.
   */
  public MethodCommandHandlerDiscoverer(ListableBeanFactory beanFactory) {
    Preconditions.checkNotNull(beanFactory);
    this.logger = LogFactory.getLog(this.getClass());
    this.beanFactory = beanFactory;
  }

  @Override
  public <CommandT extends Command> @Nullable CommandHandler<CommandT> getHandlerFor(
      Class<CommandT> commandType
  ) {
    Preconditions.checkNotNull(commandType);

    List<CommandHandler<CommandT>> commandHandlers = new ArrayList<>();

    String[] beanNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(
        beanFactory, Object.class
    );

    for (String beanName : beanNames) {
      if (!beanName.startsWith("scopedTarget.")) {
        Class<?> beanType = null;
        try {
          beanType = beanFactory.getType(beanName);
        } catch (Throwable ex) {
          // An unresolvable bean type, probably from a lazy bean - let's ignore it.
          if (logger.isDebugEnabled()) {
            logger.debug("Could not resolve target class for bean with name '"
                + beanName + "'", ex);
          }
        }
        if (beanType != null) {
          Set<CommandHandler<CommandT>> handlerMethodsForCommand =
              findHandlerMethodsForCommand(beanType, commandType);
          commandHandlers.addAll(handlerMethodsForCommand);
        }
      }
    }

    if (commandHandlers.size() > 1) {
      throw new MultipleHandlersFoundException(commandType);
    } else if (commandHandlers.size() == 0) {
      return null;
    }

    return commandHandlers.get(0);
  }

  private <CommandT extends Command> Set<CommandHandler<CommandT>> findHandlerMethodsForCommand(
      Class<?> beanType, Class<CommandT> commandType
  ) {
    Map<Method, CommandHandler<CommandT>> methodCommandHandlerMap = MethodIntrospector
        .selectMethods(beanType,
            (MethodIntrospector.MetadataLookup<CommandHandler<CommandT>>) method -> {

              if (AnnotationUtils.findAnnotation(method,
                  com.brillantcode.gate.annotation.CommandHandler.class) == null) {
                return null;
              }

              MethodDescriptor methodDescriptor = new MethodDescriptor(method);
              if (method.getParameterCount() == 1
                  && commandType.equals(methodDescriptor.parameter(0).getType())) {
                return new ReflectiveMethodCommandHandler<>(
                    beanFactory.getBean(beanType), methodDescriptor, commandType
                );
              }
              return null;
            });

    return Sets.newHashSet(methodCommandHandlerMap.values());
  }

}
