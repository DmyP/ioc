package ua.rd.ioc;

import ua.rd.exceprions.NoSuchBeanException;

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
        beanBuilder.createBenchmarkProxyForAnnotatedBeans();
        return beanBuilder.build();
    }

    class BeanBuilder {
        private BeanDefinition bd;
        private Object bean;

        BeanBuilder(BeanDefinition beanDefinition) {
            this.bd = beanDefinition;
        }

        private void createNewBeanInstance() {
            Class<?> type = bd.getBeanType();
            Constructor<?> constructor = type.getDeclaredConstructors()[0];
            bean = constructor.getParameterCount() == 0 ?
                    createBeanWithDefaultConstructor(type) : createBeanWithConstructorWithParams(type);
        }

        private void callPostConstructAnnotatedMethod() {
            Class<?> beanType = bean.getClass();
            Method[] allMethods = beanType.getMethods();

            Arrays.stream(allMethods).filter(method -> method.isAnnotationPresent(MyPostConstruct.class)).forEach(method -> {
                try {
                    method.invoke(bean);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
        }

        private void callInitMethod() {
            Class<?> beanType = bean.getClass();
            try {
                Method initMethod = beanType.getMethod("init");
                initMethod.invoke(bean);
            } catch (NoSuchMethodException ignored) {
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        private void createBenchmarkProxyForAnnotatedBeans() {
            Class<?> beanClass = bean.getClass();
            Method[] beanDeclaredMethods = beanClass.getDeclaredMethods();
            for (Method m : beanDeclaredMethods) {
                if (m.isAnnotationPresent(Benchmark.class) && m.getAnnotation(Benchmark.class).enabled()) {
                    bean = Proxy.newProxyInstance(
                            beanClass.getClassLoader(),
                            beanClass.getInterfaces(),
                            new MyInvocationHandler(bean));
                }
            }
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

            public Object createBeanWithDefaultConstructor(Class<?> type) {
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
        public String[] getBeanDefinitionNames() {
            return beanDefinitions.stream()
                    .map(BeanDefinition::getBeanName).toArray(String[]::new);
        }
    }
