package ua.rd.services;

import ua.rd.domain.Tweet;
import ua.rd.repository.TweetRepository;
////
class LazyTweetProxy extends Tweet{
    String bean;

    public LazyTweetProxy(String bean) {
        this.bean = bean;
    }

    Tweet getInstance(){
        appContext.getBean(bean);
    }
    }


public class SimpleTweetService implements TweetService {
    private final TweetRepository tweetRepository;
    private Tweet tweet ; // = new LazyTweetProxy("tweet").getInstance(); создать прокси SimpleTweetService по имени метода

    public SimpleTweetService(TweetRepository tweetRepository, Tweet tweet) {
        this.tweetRepository = tweetRepository;
        this.tweet = tweet;
    //TODO if tweet prototype in newTweet return new tweet
        //if prototype inject proxy or class
    }

//    public SimpleTweetService(TweetRepository tweetRepository) {
//        this.tweetRepository = tweetRepository;
//    }

    @Override
    public Iterable<Tweet> allTweets() {
        return tweetRepository.allTweets();
    }

    @Override
    public TweetRepository getTweetRepository() {
        return tweetRepository;
    }

    @Override
    public Tweet newTweet() {
        return tweet;
    }

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
    }

}
