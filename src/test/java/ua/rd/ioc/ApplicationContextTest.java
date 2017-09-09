package ua.rd.ioc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.rd.exceprions.NoSuchBeanException;

import java.util.*;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
        beanDescriptions.put(beanName, TestBean.class);
        Config config = new JavaMapConfig(beanDescriptions);
        Context context = new ApplicationContext(config);

        String[] actual = context.getBeanDefinitionNames();

        String[] expected = {beanName};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void getBeanDefinitionNamesWithEmptyBeanDefinition() throws Exception {
        Config config = new JavaMapConfig(beanDescriptions);
        Context context = new ApplicationContext(config);

        String[] actual = context.getBeanDefinitionNames();

        String[] expected = {};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void getBeanDefinitionNamesWithSeveralBeanDefinition() throws Exception {
        String beanName1 = "First bean";
        String beanName2 = "Second bean";
        beanDescriptions.put(beanName1, TestBean.class);
        beanDescriptions.put(beanName2, TestBean.class);
        Config config = new JavaMapConfig(beanDescriptions);
        Context context = new ApplicationContext(config);

        String[] actual = context.getBeanDefinitionNames();

        String[] expected = {beanName2, beanName1};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void getBeanWithOneBeanDefinitionIsNotNull() throws Exception {
        String beanName = "First bean";
        beanDescriptions.put(beanName, TestBean.class);
        Config config = new JavaMapConfig(beanDescriptions);
        Context context = new ApplicationContext(config);

        Object bean = context.getBean(beanName);

        assertNotNull(bean);
    }

    @Test
    public void getBeanWithOneBeanDefinition() throws Exception {
        String beanName = "First bean";
        Class<TestBean> beanType = TestBean.class;
        Map<String, Class<?>> beanDescriptions = new HashMap<String, Class<?>>(){{
            put(beanName, beanType);
        }};
        Config config = new JavaMapConfig(beanDescriptions);
        Context context = new ApplicationContext(config);

        TestBean bean = context.getBean(beanName);

        assertEquals(beanDescriptions.get(beanName).getSimpleName(), bean.getClass().getSimpleName());
    }

    static class TestBean {
    }
}