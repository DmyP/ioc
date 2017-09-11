package ua.rd.ioc;

public interface Context {
    <T> Object getBean(String beanName);

    String[] getBeanDefinitionNames();
}
