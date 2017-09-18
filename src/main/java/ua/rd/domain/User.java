package ua.rd.domain;

public class User {
    private Tweet tweet;

    public User(Tweet tweet) {
        this.tweet = tweet;
    }


    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
    }
}
