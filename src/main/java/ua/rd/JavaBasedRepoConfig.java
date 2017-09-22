package ua.rd;


import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import ua.rd.domain.Tweet;
import ua.rd.domain.User;
import ua.rd.repository.InMemTweetRepository;
import ua.rd.repository.TweetRepository;
import ua.rd.services.SimpleTweetService;
import ua.rd.services.TweetService;

@Configuration
public class JavaBasedRepoConfig {


    @Bean
    public TweetRepository tweetRepository(){
        System.out.println("new tweetRepository");
        return new InMemTweetRepository();
    }
}
