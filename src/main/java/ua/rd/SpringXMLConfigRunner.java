package ua.rd;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.rd.domain.Tweet;
import ua.rd.domain.User;
import ua.rd.services.TweetService;

import java.util.Arrays;

public class SpringXMLConfigRunner {
    public static void main(String[] args) {
        ConfigurableApplicationContext repoContext =
                new ClassPathXmlApplicationContext("repoContext.xml");
        ConfigurableApplicationContext serviceContext =
                new ClassPathXmlApplicationContext(new String[]{"serviceContext.xml"}, repoContext);
        System.out.println("repo context - " + Arrays.toString(repoContext.getBeanDefinitionNames()));
        System.out.println("service context - " + Arrays.toString(serviceContext.getBeanDefinitionNames()));

        TweetService tweetService = (TweetService) serviceContext.getBean("tweetService");
        Tweet tweet = (Tweet) serviceContext.getBean("tweet");
        System.out.println(tweet);
        System.out.println(tweetService.getClass());

        System.out.println(serviceContext.getBean(User.class));

        System.out.println(tweetService.newTweet() == tweetService.newTweet());
        System.out.println(tweetService.getClass());
        System.out.println(tweetService.allTweets());
        serviceContext.close();
        repoContext.close();

    }
}