package ua.rd;


import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import ua.rd.domain.Tweet;
import ua.rd.domain.User;

@Configuration
public class AppConfigRunner {
    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfigRunner.class);

        BeanDefinition beanDefinition = context.getBeanFactory().getBeanDefinition("tweet");
        System.out.println("----------------------------------------------------------");
        System.out.println(beanDefinition);

        System.out.println("----------------------------------------------------------");
        Tweet tweet = (Tweet) context.getBean("tweet");
        System.out.println(tweet);

        System.out.println("----------------------------------------------------------");
        User user = (User) context.getBean("user");
        System.out.println(user);

        System.out.println("----------------------------------------------------------");
        System.out.println(context.getBean(AppConfigRunner.class).getClass());

        System.out.println("----------------------------------------------------------");
    }

    @Bean("tweet")
    @Lazy
    @Scope("singleton")
    public Tweet tweet(){
        System.out.println("Called");
        return new Tweet();
    }


    @Bean
    @Lazy
    public User user(Tweet tweet){
        User user = new User("Andrii");
        user.setTweet(tweet);
        return user;
    }
}
