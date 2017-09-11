package ua.rd.ioc;

import java.util.ArrayList;
import java.util.List;

@FunctionalInterface
public interface Config {
    List<BeanDefinition> BEAN_DEFINITIONS = new ArrayList<>();

    List<BeanDefinition> beanDefinitions();
}
