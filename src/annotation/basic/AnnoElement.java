package annotation.basic;

import util.MyLogger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
//@Target(ElementType.TYPE) //클래스에만 적용
//@Target(ElementType.METHOD) //메소드에만 적용
//@Target(ElementType.FIELD) //필드에만 적용
public @interface AnnoElement {
    //자바 기본 제공 타입만 선언 가능.
    String value();
    int count() default 0;
    String[] tags() default {};

    Class<? extends MyLogger> annoData() default MyLogger.class; //클래스 정보는 가능
}
