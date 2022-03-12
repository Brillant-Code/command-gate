package com.brillantcode.gate.handler;

import com.brillantcode.gate.Command;
import com.brillantcode.gate.GateException;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.core.ResolvableType;

/**
 * Implementation of {@link CommandHandlerDiscoverer} that is looking for
 * a beans of type {@link CommandHandler} in a Spring
 * {@link org.springframework.beans.factory.BeanFactory}.
 */
public class BeanCommandHandlerDiscoverer implements CommandHandlerDiscoverer {

  private final ListableBeanFactory beanFactory;

  public BeanCommandHandlerDiscoverer(ListableBeanFactory beanFactory) {
    Preconditions.checkNotNull(beanFactory);
    this.beanFactory = beanFactory;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public <CommandT extends Command> @Nullable CommandHandler<CommandT> getHandlerFor(
      Class<CommandT> commandType
  ) {
    Preconditions.checkNotNull(commandType);

    List<CommandHandler<CommandT>> targetCommandHandlers = new ArrayList<>();

    Map<String, CommandHandler> commandHandlerBeans =
        this.beanFactory.getBeansOfType(CommandHandler.class);

    for (CommandHandler<?> commandHandler : commandHandlerBeans.values()) {
      Class<?> handlerCommandType = ResolvableType.forInstance(commandHandler)
          .as(CommandHandler.class)
          .getGeneric(0).resolve();
      if (handlerCommandType == null) {
        throw new IllegalStateException(String.format(
            "Could not find Command type on CommandHandler of class %s",
            commandHandler.getClass().getName()
        ));
      }

      if (handlerCommandType.equals(commandType)) {
        try {
          targetCommandHandlers.add((CommandHandler<CommandT>) commandHandler);
        } catch (ClassCastException e) {
          throw new GateException(
              "Unable to cast CommandHandler to command type for command class "
                  + commandType.getName()
          );
        }
      }
    }

    if (targetCommandHandlers.size() > 1) {
      throw new MultipleHandlersFoundException(commandType);
    } else if (targetCommandHandlers.size() == 0) {
      return null;
    }

    return targetCommandHandlers.get(0);
  }

}
