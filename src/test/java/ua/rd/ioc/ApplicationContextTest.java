package ua.rd.ioc;

import org.junit.Test;
import ua.rd.exceprions.NoSuchBeanException;

import java.util.*;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

public class ApplicationContextTest {

    @Test(expected = NoSuchBeanException.class)
    public void getBean() throws Exception {
        Context context = new ApplicationContext();
        context.getBean("abc");
    }

    @Test
    public void getBeanDefinitionNamesWithEmptyContext() throws Exception {
        //Given
        Context context = new ApplicationContext();
        //When
        String[] actual = context.getBeanDefinitionNames();
        //Then
        String[] expected = {};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void getBeanDefinitionNamesWithOneBeanDefinition() throws Exception {
        //Given
        String beanName = "First bean";
        List<String> beanDescriptions = Arrays.asList(beanName);
        Config config = new JavaMapConfig( beanDescriptions);
        Context context = new ApplicationContext(config);
        //When
        String[] actual = context.getBeanDefinitionNames();
        //Then
        String[] expected = {beanName};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void getBeanDefinitionNamesWithEmptyBeanDefinition() throws Exception {
        //Given
        List<String> beanDescriptions = Collections.emptyList();
        Config config = new JavaMapConfig( beanDescriptions);
        Context context = new ApplicationContext(config);
        //When
        String[] actual = context.getBeanDefinitionNames();
        //Then
        String[] expected = {};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void getBeanDefinitionNamesWithSeveralBeanDefinition() throws Exception {
        String beanName1 = "First bean";
        String beanName2 = "Second bean";
        List<String> beanDescriptions = Arrays.asList(beanName1, beanName2);
        Config config = new JavaMapConfig( beanDescriptions);
        Context context = new ApplicationContext(config);

        String[] actual = context.getBeanDefinitionNames();

        String[] expected = {beanName1, beanName2};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void getBeanWithOneBeanDefinitionIsNotNull() throws Exception {
        String beanName = "First bean";
        List<String> beanDescriptions = Arrays.asList(beanName);
        Config config = new JavaMapConfig( beanDescriptions);
        Context context = new ApplicationContext(config);

        Object bean = context.getBean(beanName);

        assertNotNull(bean);
    }

    @Test
    public void getBeanWithOneBeanDefinition() throws Exception {
        String beanName = "First bean";
        Class<TestBean> beanType = TestBean.class;

        //TODO  replace list woth map
        //List<String> beanDescriptions = Arrays.asList(beanName);
        Map<String, Class<?>> beanDescriptions = new HashMap<String, Class<?>>(){{
                put(beanName, beanType);
            }};

        Config config = new JavaMapConfig(beanDescriptions);
        Context context = new ApplicationContext(config);

        TestBean bean = (TestBean) context.getBean(beanName);


        assertNotNull(bean);
    }

}