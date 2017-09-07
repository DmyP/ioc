package ua.rd.ioc;

import ua.rd.exceprions.NoSuchBeanException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ApplicationContext implements Context {
    private BeanDefinition[] beanDefinitions;

    public ApplicationContext(Config config) {
        beanDefinitions = config.beanDefinitions();
    }

    public ApplicationContext() {
        beanDefinitions = Config.EMPTY_BEANDEFINITION; //new BeanDefinition[0];
    }


    @Override
    public <T> T getBean(String beanName) {
        Optional<BeanDefinition> bean = Arrays.stream(beanDefinitions)
                .filter(bd -> bd.getBeanName().equals(beanName))
                .findAny();

        return (T)bean
                .map(BeanDefinition::getBeanType)
                .map(this::newInstance)
                .orElse(null);
    }


    private <T> T newInstance(Class<T> cl){
        try {
            return cl.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String[] getBeanDefinitionNames(){


        return Arrays.stream(beanDefinitions)
                .map(BeanDefinition::getBeanName).toArray(String[]::new);

    }

}
