package ua.rd.annotations;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Component
@Scope("prototype")
public @interface MyTweet {
    String value() default "";
    String scope() default "singleton";
}
