package ua.rd.ioc;

import java.util.Map;

public class JavaMapConfig implements Config {
    private Map<String, Class<?>> beanDescriptions;

    public JavaMapConfig(Map<String, Class<?>> beanDescriptions) {
        this.beanDescriptions = beanDescriptions;
    }

    @Override
    public BeanDefinition[] beanDefinitions() {
        BeanDefinition[] beanDefinitions =
                beanDescriptions.entrySet().stream()
                        .map(bean -> getBeanDefinition(bean.getKey(), bean.getValue()))
                        .toArray(BeanDefinition[]::new);
        return beanDefinitions;
    }

    private BeanDefinition getBeanDefinition(String name, Class<?> type) {
        return new BeanDefinition() {
            @Override
            public String getBeanName() {
                return name;
            }

            @Override
            public Class<?> getBeanType() {
                return type;
            }
        };
    }
}


