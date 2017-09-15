package ua.rd.ioc;

import java.util.ArrayList;
import java.util.List;

@FunctionalInterface
public interface Config {

    BeanDefinition[] EMPTY_BEAN_DEfINITION = new BeanDefinition[0];

    BeanDefinition[] beanDefinitions();
}
