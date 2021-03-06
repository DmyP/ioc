package ua.rd.domain;

import org.springframework.beans.factory.annotation.Value;
import ua.rd.annotations.MyTweet;

//@MyTweet(value = "abc")
public class Tweet {
    private Long tweetId;
    private String txt;
    private User user;

    public Tweet() {
    }

    public Tweet(String txt) {
        this.txt = txt;
    }

    public Tweet(String txt, User user) {
        this.txt = txt;
        this.user = user;
    }

    public Tweet(Long tweetId, String txt, User user) {
        this.tweetId = tweetId;
        this.txt = txt;
        this.user = user;
    }

    public Long getTweetId() {
        return tweetId;
    }

    public void setTweetId(Long tweetId) {
        this.tweetId = tweetId;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public User getUser() {
        return user;
    }

   //@Value("user")
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "tweetId=" + tweetId +
                ", txt='" + txt + '\'' +
                ", user=" + user +
                '}';
    }

    private void init() {
        user.setTweet(this);
    }
}
