package ua.rd.ioc;

import ua.rd.exceprions.NoSuchBeanException;
import ua.rd.ioc.annotations.MyPostConstruct;

import java.lang.reflect.*;
import java.util.*;

public class ApplicationContext implements Context {
    private List<BeanDefinition> beanDefinitions;
    private Map<String, Object> beans = new HashMap<>();

    public ApplicationContext(Config config) {
        beanDefinitions = config.beanDefinitions();
        initContext(beanDefinitions);
    }

    private void initContext(List<BeanDefinition> beanDefinitions) {
        beanDefinitions.forEach(bd -> getBean(bd.getBeanName()));
    }

    public ApplicationContext() {
        beanDefinitions = Config.BEAN_DEFINITIONS;
    }

    @Override
    public Object getBean(String beanName) {
        BeanDefinition beanDefinition = getBeanDefinitionByName(beanName);
        Object bean = beans.get(beanName);
        if (bean != null) {
            return bean;
        }
        bean = createNewBean(beanDefinition);
        if (!beanDefinition.isPrototype()) {
            beans.put(beanName, bean);
        }
        return bean;
    }

    private Object createNewBean(BeanDefinition beanDefinition) {
        BeanBuilder beanBuilder = new BeanBuilder(beanDefinition);
        beanBuilder.createNewBeanInstance();
        beanBuilder.callPostConstructAnnotatedMethod();
        beanBuilder.callInitMethod();
        // TODO beanBuilder.createBenchmarkProxy();
        return beanBuilder.build();
    }

     class BeanBuilder{
        private BeanDefinition bd;
        private Object bean;

        public BeanBuilder(BeanDefinition beanDefinition) {
            this.bd = beanDefinition;
        }

        private void createNewBeanInstance() {
            Class<?> type = bd.getBeanType();
            Constructor<?> constructor = type.getDeclaredConstructors()[0];
            Object newBean;
            if (constructor.getParameterCount() == 0) {
                newBean = createBeanWithDefaultConstructor(type);
            } else {
                newBean = createBeanWithConstructorWithParams(type);
            }
            bean = newBean;
        }

        private void callPostConstructAnnotatedMethod() {
            Class<?> beanType = bean.getClass();
            Method[] allMethods = beanType.getMethods();

            for (final Method method : allMethods) {
                if (method.isAnnotationPresent(MyPostConstruct.class)) {
                    try {
                        method.invoke(bean);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private void callInitMethod() {
            Class<?> beanType = bean.getClass();
            try {
                Method initMethod = beanType.getMethod("init");
                initMethod.invoke(bean);
            } catch (NoSuchMethodException e) {
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        private void createBenchmarkProxy() {
            Class<?> beanClass = bean.getClass();
            bean = Proxy.newProxyInstance(
                    beanClass.getClassLoader(),
                    beanClass.getInterfaces(),
                    new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            //System.out.println(proxy.getClass());
                            System.out.println(method.getName());
                            return method.invoke(bean);
                        }
                    });
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

        private Object createBeanWithConstructorWithParams(Class<?> type) {
            Constructor<?> constructor = type.getDeclaredConstructors()[0];
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            String[] paramsNames = Arrays.stream(parameterTypes)
                    .map(Class::getSimpleName)
                    .map(simpleName -> simpleName.substring(0, 1)
                            .toLowerCase() + simpleName.substring(1))
                    .toArray(String[]::new);
            Object[] paramInstances = Arrays.stream(paramsNames)
                    .map(ApplicationContext.this::getBean)
                    .toArray();
            Constructor<?> currentConstructor = null;
            try {
                currentConstructor = type.getConstructor(parameterTypes);
                return currentConstructor.newInstance(paramInstances);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return currentConstructor;
        }

        private Object build() {
            return bean;
        }
    }
    //Byte Buddy

    private BeanDefinition getBeanDefinitionByName(String beanName) {
        return beanDefinitions.stream()
                .filter(e -> e.getBeanName().equals(beanName))
                .findAny()
                .orElseThrow(NoSuchBeanException::new);
    }

    public String[] getBeanDefinitionNames(){
        return beanDefinitions.stream()
                .map(BeanDefinition::getBeanName).toArray(String[]::new);
    }
}
