package ua.rd.ioc;

import ua.rd.annotations.Benchmark;
import ua.rd.annotations.MyPostConstruct;
import ua.rd.exceptions.NoSuchBeanException;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Stream;

public class ApplicationContext implements Context {
    private List<BeanDefinition> beanDefinitions;
    private Map<String, Object> beans;

    public ApplicationContext(Config config) {
        beanDefinitions = Arrays.asList(config.beanDefinitions());
        beans = new HashMap<>();
        initContext(beanDefinitions);
    }

    private void initContext(List<BeanDefinition> beanDefinitions) {
        beanDefinitions.stream().forEach(bd -> getBean(bd.getBeanName()));
    }

    public ApplicationContext() {
        beanDefinitions = Arrays.asList(Config.EMPTY_BEAN_DEfINITION);
        beans = new HashMap<>();
    }

    @Override
    public Object getBean(String beanName) {
        Object bean = beans.get(beanName);
        if (bean == null) {
            BeanDefinition beanDefinition = getBeanDefinitionByName(beanName);
            bean = createNewBean(beanDefinition);
            if (!beanDefinition.isPrototype()) {
                beans.put(beanName, bean);
            }
        }
        return bean;
    }


    private Object createNewBean(BeanDefinition beanDefinition) {
        BeanBuilder beanBuilder = new BeanBuilder(beanDefinition);
        beanBuilder.createNewBeanInstance();
        beanBuilder.callPostConstructAnnotatedMethod();
        beanBuilder.callInitMethod();
        beanBuilder.createBenchmarkProxyForAnnotatedBeans();

        Object bean = beanBuilder.build();

        return bean;
    }

    class BeanBuilder {

        private BeanDefinition beanDefinition;
        private Object bean;

        private BeanBuilder(BeanDefinition beanDefinition) {
            this.beanDefinition = beanDefinition;
        }

        private void createNewBeanInstance() {
            Class<?> type = beanDefinition.getBeanType();
            if (type.getDeclaredConstructors()[0].getParameterCount() == 0) {
                bean = createBeanWithDefaultConstructor(type);
            } else {
                bean = createBeanWithParams(type);
            }
        }

        private void callPostConstructAnnotatedMethod() {
            Class<?> beanType = bean.getClass();
            Stream.of(beanType.getDeclaredMethods())
                    .filter(m -> m.isAnnotationPresent(MyPostConstruct.class))
                    .forEach(m -> {
                        try {
                            m.invoke(bean);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }

        private void callInitMethod() {
            Class<?> beanType = bean.getClass();
            try {
                Method intiMethod = beanType.getMethod("init");
                intiMethod.invoke(bean);
            } catch (NoSuchMethodException e) {
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        private void createBenchmarkProxyForAnnotatedBeans() {
            Class<?> beanClass = bean.getClass();
            Arrays.stream(beanClass.getDeclaredMethods())
                    .filter(m -> m.isAnnotationPresent(Benchmark.class) && m.getAnnotation(Benchmark.class).enabled())
                    .forEach(m -> bean = Proxy.newProxyInstance(
                            beanClass.getClassLoader(),
                            beanClass.getInterfaces(),
                            new MyInvocationHandler(bean)));
        }

        class MyInvocationHandler implements InvocationHandler {
            private Object proxyBean;

            public MyInvocationHandler(Object proxyBean) {
                this.proxyBean = proxyBean;
            }

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                long start = System.nanoTime();
                Object object = method.invoke(proxyBean, args);
                long result = System.nanoTime() - start;
                System.out.println(result);
                return object;
            }
        }

        private Object build() {
            return bean;
        }
    }

    private BeanDefinition getBeanDefinitionByName(String beanName) {
        return beanDefinitions.stream().filter((b) -> beanName.equals(b.getBeanName()))
                .findAny().orElseThrow(NoSuchBeanException::new);
    }

    private Object createBeanWithDefaultConstructor(Class<?> type) {
        try {
            return type.newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    private Object createBeanWithParams(Class<?> type) {
        Constructor<?> constructor = type.getDeclaredConstructors()[0];
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] params = Stream.of(parameterTypes)
                .map(Class::getSimpleName)
                .map(s -> Character.toLowerCase(s.charAt(0)) + s.substring(1))
                .map(this::getBean).toArray();
        try {
            return constructor.newInstance(params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String[] getBeanDefinitionNames() {
        return beanDefinitions.stream().map(BeanDefinition::getBeanName).toArray(String[]::new);
    }
}