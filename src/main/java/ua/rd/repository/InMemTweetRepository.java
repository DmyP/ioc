package ua.rd.repository;

import ua.rd.domain.Tweet;

import java.util.Arrays;
import java.util.List;

public class InMemTweetRepository implements TweetRepository {
    private List<Tweet> tweets;

    {
        tweets = Arrays.asList(
          new Tweet(1L, "First Message", null),
          new Tweet(2L, "Second Message", null)
        );
    }

    @Override
    public Iterable<Tweet> allTweets() {
        return tweets;
    }
}
