package ua.opu.cafe.speedtests;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Rollback(false)
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@RepeatedTest(100)
public @interface WithinTransaction100 {

  @AliasFor(annotation = RepeatedTest.class, attribute = "value")
  int value() default 100;
}
