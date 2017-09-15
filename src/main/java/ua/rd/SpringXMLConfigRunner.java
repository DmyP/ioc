package ua.rd;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.rd.services.TweetService;

import java.util.Arrays;

public class SpringXMLConfigRunner {
    public static void main(String[] args) {
        ConfigurableApplicationContext repoContext =
                new ClassPathXmlApplicationContext("repoContext.xml");
        ConfigurableApplicationContext serviceContext =
                new ClassPathXmlApplicationContext(new String[]{"serviceContext.xml"}, repoContext);
        System.out.println(Arrays.toString(repoContext.getBeanDefinitionNames()));
        System.out.println(Arrays.toString(serviceContext.getBeanDefinitionNames()));



        System.out.println(serviceContext.getBean("tweet"));
        //TweetService tweetService = (TweetService) serviceContext.getBean("tweetService");
        //System.out.println(tweetService.allTweets());

        serviceContext.close();
        repoContext.close();

    }
}