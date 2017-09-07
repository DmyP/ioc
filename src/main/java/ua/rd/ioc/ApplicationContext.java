package ua.rd.ioc;

import ua.rd.exceprions.NoSuchBeanException;

import java.util.Arrays;
import java.util.List;

public class ApplicationContext implements Context {
    private BeanDefinition[] beanDefinitions;

    public ApplicationContext(Config config) {
        beanDefinitions = config.beanDefinitions();
    }

    public ApplicationContext() {
        beanDefinitions = Config.EMPTY_BEANDEFINITION; //new BeanDefinition[0];
    }

    @Override
    public Object getBean(String beanName) {
        List<BeanDefinition> beanDefinitions =
                Arrays.asList(this.beanDefinitions);
        if (beanDefinitions.stream().map(BeanDefinition::getBeanName).anyMatch(n -> n.equals(beanName))) {

            //TODO найти бин с именем и для него через рефлекшн создать бин с таким имененем
            //return new Object();
            //BeanDefinition beanDefinition;
            return beanDefinition.getBeanType().newInstance();
        } else {
            throw new NoSuchBeanException();
        }
    }

    @Override
    public String[] getBeanDefinitionNames() {
        String[] beanDefinitionNames = Arrays.stream(beanDefinitions)
                .map(BeanDefinition::getBeanName)
                .toArray(String[]::new);
        return beanDefinitionNames;
    }
}
