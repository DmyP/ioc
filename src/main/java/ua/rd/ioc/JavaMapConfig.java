package ua.rd.ioc;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class JavaMapConfig implements Config {
    private Map<String, Class<?>> beanDescriptions;

    public JavaMapConfig(Map<String, Class<?>> beanDescriptions) {
        this.beanDescriptions = beanDescriptions;
    }

    @Override
    public BeanDefinition[] beanDefinitions() {
        BeanDefinition[] beanDefinitions =
                beanDescriptions.entrySet().stream()
                        .map(Map.Entry::getValue)
                        .toArray(BeanDefinition[]::new);
        return beanDefinitions;
    }
}

