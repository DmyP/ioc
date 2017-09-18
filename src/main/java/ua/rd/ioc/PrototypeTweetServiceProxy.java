package ua.rd.ioc;

import ua.rd.services.TweetService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class PrototypeTweetServiceProxy {
    private TweetService realTweetService;
    private Context context;

    public PrototypeTweetServiceProxy(TweetService realTweetService, Context context) {
        this.realTweetService = realTweetService;
        this.context = context;
    }

    TweetService createProxy() {
        Object proxyTweetService =
                Proxy.newProxyInstance(
                        ClassLoader.getSystemClassLoader(),
                        new Class[]{TweetService.class},
                        new InvocationHandler() {
                            @Override
                            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                if (method.getName().equals("newTweet")) {
                                    return context.getBean("tweet");
                                } else {
                                    return method.invoke(realTweetService, args);
                                }
                            }
                        });
        return (TweetService) proxyTweetService;
    }
}
