package com.turchenkov.parsing.parsingmethods.timer;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class MeasuringTimeBeanPostProcessor implements BeanPostProcessor {
    private Map<String, Class> stringClassMap = new HashMap<>();
    private MeasuringTimeController timeController = new MeasuringTimeController();

    public MeasuringTimeBeanPostProcessor() {
        MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
        try {
            beanServer.registerMBean(timeController, new ObjectName("measuringTime", "name", "timeController"));
        } catch (InstanceAlreadyExistsException | MalformedObjectNameException | NotCompliantMBeanException | MBeanRegistrationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(MeasuringTime.class)){
            stringClassMap.put(beanName, beanClass);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class beanClass = stringClassMap.get(beanName);
        if (beanClass != null){
            return Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    if (timeController.isFlag()) {
                        long startTime = System.currentTimeMillis();
                        Object invocation = method.invoke(bean, args);
                        long endTime = System.currentTimeMillis();
                        System.out.println(endTime-startTime);
                        return invocation;
                    }else {
                        return method.invoke(bean, args);
                    }
                }
            });
        }
        return bean;
    }
}
