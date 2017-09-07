package ua.rd.ioc;

import java.util.List;
import java.util.stream.Stream;

public class JavaMapConfig implements Config {
    private List<String> beanDescriptions;

    public JavaMapConfig(List<String> beanDescriptions) {
        this.beanDescriptions = beanDescriptions;
    }

    @Override
    public BeanDefinition[] beanDefinitions() {
        BeanDefinition[] beanDefinitions =
                beanDescriptions.stream()
                        .map(this::getBeanDefinition)
                        .toArray(BeanDefinition[]::new);
        return beanDefinitions;
    }

    private BeanDefinition getBeanDefinition(String name) {
        return () -> name;
    }

}

