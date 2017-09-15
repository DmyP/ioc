package ua.rd;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.rd.services.TweetService;

import java.util.Arrays;

public class SpringXMLConfigRunner {
    public static void main(String[] args) {
        ConfigurableApplicationContext appContext =
                new ClassPathXmlApplicationContext("appContext.xml");
        System.out.println(Arrays.toString(appContext.getBeanDefinitionNames()));
        BeanDefinition bd = appContext.getBeanFactory().getBeanDefinition("tweetService");

        TweetService tweetService = (TweetService) appContext.getBean("tweetService");
        appContext.refresh();
        System.out.println(tweetService.allTweets());

    }
}