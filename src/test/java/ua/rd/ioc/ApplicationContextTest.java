package ua.rd.ioc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.rd.exceptions.NoSuchBeanException;

import java.util.*;

import static org.junit.Assert.*;

public class ApplicationContextTest {
    private Map<String, Class<?>> beanDescriptions;

    @Before
    public void init() {
        beanDescriptions = new HashMap<>();
    }
    @After
    public void cleanUp() {
        beanDescriptions = null;
    }

    @Test(expected = NoSuchBeanException.class)
    public void getBean() throws Exception {
        Context context = new ApplicationContext();
        context.getBean("abc");
    }

    @Test
    public void getBeanDefinitionNamesWithEmptyContext() throws Exception {

        Context context = new ApplicationContext();

        String[] actual = context.getBeanDefinitionNames();

        String[] expected = {};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void getBeanDefinitionNamesWithOneBeanDefinition() throws Exception {
        String beanName = "First bean";
        Class<TestBean> beanType = TestBean.class;
        beanDescriptions.put(beanName, TestBean.class);
        Map<String, Map<String,Object>> beanDescriptions = new HashMap<String, Map<String,Object>>(){{
            put(beanName,
                    new HashMap<String, Object>(){{
                        put("type", beanType);
                    }});
        }};
        Config config = new JavaMapConfig(beanDescriptions);
        Context context = new ApplicationContext(config);

        String[] actual = context.getBeanDefinitionNames();

        String[] expected = {beanName};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void getBeanDefinitionNamesWithEmptyBeanDefinition() throws Exception {
        Map<String, Map<String,Object>> beanDescriptions = new HashMap<>();
        Config config = new JavaMapConfig(beanDescriptions);
        Context context = new ApplicationContext(config);

        String[] actual = context.getBeanDefinitionNames();

        String[] expected = {};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void getBeanDefinitionNamesWithSeveralBeanDefinition() throws Exception {
        String beanName = "First bean";
        String beanName1 = "First bean";
        String beanName2 = "Second bean";
        beanDescriptions.put(beanName1, TestBean.class);
        beanDescriptions.put(beanName2, TestBean.class);
        Class<TestBean> beanType = TestBean.class;
        Map<String, Map<String,Object>> beanDescriptions = new HashMap<String, Map<String,Object>>(){{
            put(beanName1,
                    new HashMap<String, Object>(){{
                        put("type", beanType);
                    }});
            put(beanName2,
                    new HashMap<String, Object>(){{
                        put("type", beanType);
                    }});
        }};
        Config config = new JavaMapConfig(beanDescriptions);
        Context context = new ApplicationContext(config);

        String[] actual = context.getBeanDefinitionNames();

        String[] expected = {beanName2, beanName1};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void getBeanWithOneBeanDefinitionIsNotNull() throws Exception {
        String beanName = "First bean";
       // beanDescriptions.put(beanName, TestBean.class);
        Class<TestBean> beanType = TestBean.class;
        Map<String, Map<String,Object>> beanDescriptions = new HashMap<String, Map<String,Object>>(){{
            put(beanName,
                    new HashMap<String, Object>(){{
                        put("type", beanType);
                    }});
        }};
        Config config = new JavaMapConfig(beanDescriptions);
        Context context = new ApplicationContext(config);

        Object bean = context.getBean(beanName);

        assertNotNull(bean);
    }

    @Test
    public void getBeanWithOneBeanDefinition() throws Exception {
        String beanName = "First bean";
        Class<TestBean> beanType = TestBean.class;
        Map<String, Map<String,Object>> beanDescriptions = new HashMap<String, Map<String,Object>>(){{
            put(beanName,
                    new HashMap<String, Object>(){{
                        put("type", beanType);
            }});
        }};
        Config config = new JavaMapConfig(beanDescriptions);
        Context context = new ApplicationContext(config);

        TestBean bean = (TestBean) context.getBean(beanName);

        assertNotNull(bean);
    }

    @Test
    public void gebBeanIsSingleton() throws Exception {
        String beanName = "First bean";
        Class<TestBean> beanType = TestBean.class;
        Map<String, Map<String,Object>> beanDescriptions = new HashMap<String, Map<String,Object>>(){{
            put(beanName,
                    new HashMap<String, Object>(){{
                        put("type", beanType);
                    }});
        }};
        Config config = new JavaMapConfig(beanDescriptions);
        Context context = new ApplicationContext(config);

        TestBean bean1 = (TestBean) context.getBean(beanName);
        TestBean bean2 = (TestBean) context.getBean(beanName);

        assertSame(bean1, bean2);
    }

    @Test
    public void gebBeanIsPrototype() throws Exception {
        String beanName = "First bean";
        Class<TestBean> beanType = TestBean.class;
        Map<String, Map<String,Object>> beanDescriptions = new HashMap<String, Map<String,Object>>(){{
            put(beanName,
                    new HashMap<String, Object>(){{
                        put("type", beanType);
                        put("isPrototype", true);
                    }});
        }};
        Config config = new JavaMapConfig(beanDescriptions);
        Context context = new ApplicationContext(config);

        TestBean bean1 = (TestBean) context.getBean(beanName);
        TestBean bean2 = (TestBean) context.getBean(beanName);

        assertNotSame(bean1, bean2);
    }

    @Test
    public void gebBeaNotSameInstancesWithSameTypes() throws Exception {
        String beanName1 = "First bean";
        String beanName2 = "Second bean";
        Class<TestBean> beanType = TestBean.class;
        Map<String, Map<String,Object>> beanDescriptions = new HashMap<String, Map<String,Object>>(){{
            put(beanName1,
                    new HashMap<String, Object>(){{
                        put("type", beanType);
                    }});
            put(beanName2,
                    new HashMap<String, Object>(){{
                        put("type", beanType);
                    }});
        }};
        Config config = new JavaMapConfig(beanDescriptions);
        Context context = new ApplicationContext(config);

        TestBean bean1 = (TestBean) context.getBean(beanName1);
        TestBean bean2 = (TestBean) context.getBean(beanName2);

        assertNotSame(bean1, bean2);
    }

    @Test
    public void getBeanWithDependentBeans() throws Exception {
        Map<String, Map<String,Object>> beanDescriptions = new HashMap<String, Map<String,Object>>(){{
            put("testBean",
                    new HashMap<String, Object>(){{
                        put("type", TestBean.class);
                        put("isPrototype", false);
                    }});
            put("testBeanWithConstructor",
                    new HashMap<String, Object>(){{
                        put("type", TestBeanWithConstructor.class);
                        put("isPrototype", false);
                    }});
        }};
        Config config = new JavaMapConfig(beanDescriptions);
        Context context = new ApplicationContext(config);

        TestBean bean = (TestBean) context.getBean("testBean");

        assertNotNull(bean);
    }

    @Test
    public void getBeanWithDependedBeansWithTwoParam() throws Exception {

        Map<String, Map<String, Object>> beanDescriptions =
                new HashMap<String, Map<String, Object>>() {{
                    put("testBean",
                            new HashMap<String, Object>() {{
                                put("type", TestBean.class);
                                put("isPrototype", false);
                            }});
                    put("testBeanWithConstructorWithTwoParams",
                            new HashMap<String, Object>() {{
                                put("type", TestBeanWithConstructorTwoParams.class);
                                put("isPrototype", false);
                            }});
                }};

        Config config = new JavaMapConfig(beanDescriptions);
        Context context = new ApplicationContext(config);

        TestBean bean = (TestBean) context.getBean("testBean");

        assertNotNull(bean);
    }

    @Test
    public void getBeanCallInitMethod() throws Exception {
        Map<String, Map<String,Object>> beanDescriptions = new HashMap<String, Map<String,Object>>(){{
            put("testBean",
                    new HashMap<String, Object>(){{
                        put("type", TestBean.class);
                        put("isPrototype", false);
                    }});
        }};
        Config config = new JavaMapConfig(beanDescriptions);
        Context context = new ApplicationContext(config);

        TestBean bean = (TestBean) context.getBean("testBean");
        assertEquals("initialized", bean.initValue);
    }

    @Test
    public void getBeanCallPostConstructMethod() throws Exception {
        Map<String, Map<String,Object>> beanDescriptions = new HashMap<String, Map<String,Object>>(){{
            put("testBean",
                    new HashMap<String, Object>(){{
                        put("type", TestBean.class);
                        put("isPrototype", false);
                    }});
        }};
        Config config = new JavaMapConfig(beanDescriptions);
        Context context = new ApplicationContext(config);

        TestBean bean = (TestBean) context.getBean("testBean");
        assertEquals("initializedByPostConstruct", bean.postConstructValue);
    }

    @Test
    public void getBeanCallBenchMarkMethod() throws Exception {
        Map<String, Map<String, Object>> beanDescriptions =
                new HashMap<String, Map<String, Object>>() {{
                    put("testBeanBenchmark",
                            new HashMap<String, Object>() {{
                                put("type", TestBeanBenchmark.class);
                                put("isPrototype", true);
                            }}
                    );
                }};

        Config config = new JavaMapConfig(beanDescriptions);
        Context context = new ApplicationContext(config);

        BeanInterface bean = (BeanInterface) context.getBean("testBeanBenchmark");

        assertEquals("ytrewq", bean.benchmarkMethod("qwerty"));
    }

    public interface BeanInterface {
        String benchmarkMethod(String str);
    }

    static class TestBeanBenchmark implements BeanInterface {
        @Override
        @Benchmark
        public String benchmarkMethod(String str) {
            return new StringBuilder(str).reverse().toString();
        }
    }

    static class TestBean{
        String initValue;
        String postConstructValue;

        public void init(){
            initValue = "initialized";
        }

        @MyPostConstruct
        public void postConstruct(){
            initValue = "initializedByPostConstruct";
            postConstructValue = "initializedByPostConstruct";
        }
    }

    static class TestBeanWithConstructor {
        private TestBean testBean;

        TestBeanWithConstructor(TestBean testBean) {
            this.testBean = testBean;
        }
    }

    static class TestBeanWithConstructorTwoParams {
        private TestBean testBean1;
        private TestBean testBean2;

        public TestBeanWithConstructorTwoParams(TestBean testBean1, TestBean testBean2) {
            this.testBean1 = testBean1;
            this.testBean2 = testBean2;
        }
    }
}