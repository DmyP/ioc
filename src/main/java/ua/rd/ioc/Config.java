package ua.rd.ioc;

@FunctionalInterface
public interface Config {
    BeanDefinition[] BEAN_DEFINITIONS = new BeanDefinition[0];

    BeanDefinition[] beanDefinitions();
}
