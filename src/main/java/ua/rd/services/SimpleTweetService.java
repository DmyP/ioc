package ua.rd.services;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import ua.rd.annotations.Benchmark;
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

@Service("tweetService")
public class SimpleTweetService implements TweetService,
        InitializingBean, ApplicationContextAware, DisposableBean{
    private ApplicationContext applicationContext;

    private TweetRepository tweetRepository;
    private Tweet tweet;

    @Required
    @Autowired
    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
        System.out.println("setTweet");
    }

    public SimpleTweetService() {
    }

    @Autowired
    public SimpleTweetService(TweetRepository tweetRepository) {
        System.out.println("constructor with param " + tweetRepository);
        this.tweetRepository = tweetRepository;
    }

    public void fillRepo(TweetRepository tweetRepository){
        this.tweetRepository = tweetRepository;
        System.out.println("fillRepo");
    }

    public void fillTweet(Tweet tweet){
        this.tweet = tweet;
        System.out.println("fillTweet");
    }

    public Tweet getTweet() {
        return tweet;
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
    @Lookup
    @Benchmark
    //TODO make benchmark work using BFPP
    public Tweet newTweet() {
        return tweet;
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