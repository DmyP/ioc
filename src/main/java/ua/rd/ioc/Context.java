package ua.rd.ioc;

public interface Context {
    <T> T getBean(String beanName);

    String[] getBeanDefinitionNames();
}
