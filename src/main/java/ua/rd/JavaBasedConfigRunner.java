package ua.rd;


import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import ua.rd.domain.Tweet;
import ua.rd.domain.User;
import ua.rd.repository.TweetRepository;
import ua.rd.services.TweetService;

@Configuration
public class JavaBasedConfigRunner {
    public static void main(String[] args) {
       /* ConfigurableApplicationContext context =
                new AnnotationConfigApplicationContext(JavaBasedRepoConfig.class, JavaBasedServiceConfig.class);

        context.getEnvironment().setActiveProfiles("dev", "test");
        context.refresh();*/

        AnnotationConfigApplicationContext repoContext = new AnnotationConfigApplicationContext(JavaBasedRepoConfig.class);
        AnnotationConfigApplicationContext serviceContext = new AnnotationConfigApplicationContext();
        serviceContext.register(JavaBasedServiceConfig.class);
        serviceContext.setParent(repoContext);
        serviceContext.getEnvironment().setActiveProfiles("dev", "test");
        serviceContext.refresh();

        BeanDefinition beanDefinition = serviceContext.getBeanFactory().getBeanDefinition("tweet");
        System.out.println("----------------------------------------------------------");
        System.out.println(beanDefinition);

        System.out.println("----------------------------------------------------------");
        Tweet tweet = (Tweet) serviceContext.getBean("tweet");
        System.out.println(tweet);

        System.out.println("----------------------------------------------------------");
        TweetRepository tweetRepository = (TweetRepository) serviceContext.getBean("tweetRepository");
        System.out.println(tweetRepository);

        System.out.println("----------------------------------------------------------");
        TweetService tweetService = (TweetService) serviceContext.getBean("tweetService");
        System.out.println(tweetService);

        System.out.println("----------------------------------------------------------");
        User user = (User) serviceContext.getBean("user");
        System.out.println(user);

        System.out.println("----------------------------------------------------------");
        System.out.println(serviceContext.getBean(JavaBasedRepoConfig.class).getClass());
        System.out.println(serviceContext.getBean(JavaBasedServiceConfig.class).getClass());

        System.out.println("----------------------------------------------------------");

        System.out.println(tweetService.newTweet() == tweetService.newTweet());
        System.out.println(tweetService.newTweet());
    }

}
