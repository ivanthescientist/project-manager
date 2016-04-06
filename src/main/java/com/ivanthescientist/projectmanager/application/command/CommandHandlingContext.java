package com.ivanthescientist.projectmanager.application.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommandHandlingContext implements ApplicationContextAware {
    private final Logger logger = LoggerFactory.getLogger(CommandHandlingContext.class);
    private Map<Class<? extends Command>, Object> commandHandlers = new HashMap<>();
    private Map<Class<? extends Command>, Method> commandHandlerMethods = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> commandHandlerBeans = applicationContext.getBeansWithAnnotation(CommandHandler.class);

        for(Object commandHandler : commandHandlerBeans.values()) {
            List<Method> methods = Arrays.asList(commandHandler.getClass().getMethods());
            for(Method method : methods) {
                if(method.isAnnotationPresent(CommandHandler.class)) {
                    CommandHandler methodAnnotation = method.getAnnotation(CommandHandler.class);
                    Class commandClass = methodAnnotation.value();

                    logger.info(String.format("Registering command handler: %s to %s:%s",
                            commandClass.getSimpleName(), commandHandler.getClass().getSimpleName(), method.getName()));

                    commandHandlers.put(commandClass, commandHandler);
                    commandHandlerMethods.put(commandClass, method);
                }
            }
        }
    }

    public Object handleCommand(Command command) throws Exception {
        Object commandHandler = commandHandlers.get(command.getClass());
        Method commandHandlingMethod = commandHandlerMethods.get(command.getClass());
        try {
            return commandHandlingMethod.invoke(commandHandler, command);
        } catch (IllegalAccessException ignored) {
            return null;
        } catch (InvocationTargetException exception) {
            throw (Exception) exception.getTargetException();
        }
    }
}
