package ua.rd;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import ua.rd.domain.Tweet;
import ua.rd.domain.User;
import ua.rd.repository.InMemTweetRepository;
import ua.rd.repository.TweetRepository;
import ua.rd.services.SimpleTweetService;
import ua.rd.services.TweetService;

import java.util.Arrays;

@Configuration
public class JavaBasedServiceConfig {

    @Autowired
    private TweetRepository tweetRepository;


    @Autowired
    private Environment env;

    @Bean
    public TweetService tweetService(){
        System.out.println("new tweetService");
        return new SimpleTweetService(){
            @Override
            public Tweet newTweet() {
                return tweet();
            }
        };
    }
    @Bean("tweet")
    @Scope("prototype")
    @Profile("default")
    public Tweet tweet(){
        Tweet tweet = new Tweet();
        tweet.setTxt("default tweet");


        return tweet;
    }

    @Bean("tweet")
    @Scope("prototype")
    @Profile("dev")
    public Tweet tweetDev(){
        Tweet tweet = new Tweet();
        tweet.setTweetId(1L);
        if (Arrays.asList(env.getActiveProfiles()).contains("test")){
            tweet.setTxt("Dev + test tweet");
        }else {
            tweet.setTxt("dev tweet");
        }
        return tweet;
    }

    @Bean
    public User user(Tweet tweet){
        System.out.println("new user");
        User user = new User("Andrii");
        user.setTweet(tweet);
        return user;
    }
}
