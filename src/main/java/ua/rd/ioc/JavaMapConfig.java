package ua.rd.ioc;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JavaMapConfig implements Config {
    private  Map<String, Map<String,Object>> beanDescriptions;

    public JavaMapConfig(Map<String, Map<String,Object>> beanDescriptions) {
        this.beanDescriptions = beanDescriptions;
    }

    @Override
    public List<BeanDefinition> beanDefinitions() {
        return beanDescriptions.entrySet().stream()
                        .map(this::getBeanDefinition)
                        .collect(Collectors.toList());
    }

    private BeanDefinition getBeanDefinition(Map.Entry<String, Map<String,Object>> descriptionEntry) {
        return new SimpleBeanDefinition(descriptionEntry.getKey(),
                (Class<?>) descriptionEntry.getValue().get("type"),
                (boolean) descriptionEntry.getValue().getOrDefault("isPrototype", false));

    }
}


