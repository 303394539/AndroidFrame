package com.baic.net.api.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by baic on 16/4/20.
 */
@Target(ElementType.METHOD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface PortAnnotation {
    String action() default "";
    String openid() default "";
    String passport() default "";
    String wxappid() default "";
    String vericode() default "";
    int offset() default 0;
    int size() default 0;
}
