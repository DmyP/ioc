package ua.rd.services;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import ua.rd.domain.Tweet;
import ua.rd.ioc.Context;
import ua.rd.repository.TweetRepository;

class PrototypeTweetServiceProxy implements TweetService{

    private Context context;
    private TweetService tweetService;

    public PrototypeTweetServiceProxy(TweetService tweetService, Context context) {
        this.context = context;
    }

    @Override
    public Iterable<Tweet> allTweets() {
        return tweetService.allTweets();
    }

    @Override
    public TweetRepository getRepository() {
        return tweetService.getRepository();
    }

    @Override
    public Tweet newTweet() {
        return (Tweet) context.getBean("tweet");
    }


}

public class SimpleTweetService implements TweetService,
        InitializingBean, ApplicationContextAware, DisposableBean{
    private ApplicationContext applicationContext;
    private final TweetRepository tweetRepository;
    private Tweet tweet;

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
    }

    public SimpleTweetService(TweetRepository tweetRepository) {
        this.tweetRepository = tweetRepository;
    }

    @Override
    public Iterable<Tweet> allTweets() {
        return tweetRepository.allTweets();
    }

    @Override
    public TweetRepository getRepository() {
        return tweetRepository;
    }

    @Override
    public Tweet newTweet() {
        return (Tweet) applicationContext.getBean("tweet");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("destroy method");
    }
}