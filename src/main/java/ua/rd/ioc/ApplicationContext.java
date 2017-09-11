package ua.rd.ioc;

import ua.rd.exceprions.NoSuchBeanException;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ApplicationContext implements Context {
    private List<BeanDefinition> beanDefinitions;
    private Map<String, Object> beans = new HashMap<>();

    public ApplicationContext(Config config) {
        beanDefinitions = config.beanDefinitions();
    }

    public ApplicationContext() {
        beanDefinitions = Config.BEAN_DEFINITIONS;
    }

    @Override
    public Object getBean(String beanName) {
        BeanDefinition beanDefinition = getBeanDefinitionByName(beanName);
        Object bean = beans.get(beanName);
        if (bean == null) {
            bean = createNewBean(beanDefinition);
            if (!beanDefinition.isPrototype()) {
                beans.put(beanName, bean);
            }
        }
        return bean;
    }

    private Object createNewBean(BeanDefinition beanDefinition) {
        Object bean = createNewBeanInstance(beanDefinition);
        return bean;
    }

    private BeanDefinition getBeanDefinitionByName(String beanName) {
        return beanDefinitions.stream()
                .filter(e -> e.getBeanName().equals(beanName))
                .findAny()
                .orElseThrow(NoSuchBeanException::new);
    }

    private Object createNewBeanInstance(BeanDefinition bd) {
        Class<?> type = bd.getBeanType();
        Constructor<?> constructor = type.getDeclaredConstructors()[0];
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object newBean = null;
        if (constructor.getParameterCount() == 0) {
            newBean = createBeanWithDefaultConstructor(type);
        } else {
            newBean = createBeanWithConstructorWithParams(type);
        }
        return newBean;
    }

    private Object createBeanWithConstructorWithParams(Class<?> type) {
        Constructor<?> constructor = type.getDeclaredConstructors()[0];
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        //TODO
        //получить масив параметров берем тип,  тип преобразовываем в масив строк, переобр первую букву в маленькую
        return null;
    }

    public Object createBeanWithDefaultConstructor(Class<?> type){
       Object newBean;
       try {
           newBean = type.newInstance();
       } catch (InstantiationException | IllegalAccessException e) {
           throw new RuntimeException(e);
       }
       return newBean;
    }

    public String[] getBeanDefinitionNames(){
        return beanDefinitions.stream()
                .map(BeanDefinition::getBeanName).toArray(String[]::new);
    }

}
