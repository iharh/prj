package mygwt.common.security.annotation;

import mygwt.common.security.model.ClassName;
import mygwt.common.security.model.PermissionName;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SecureParameter {
    PermissionName permission();
    // Either strategyName or strategyClass is needed
    String strategyName() default "";
    ClassName strategyClass() default ClassName.NONE;
    int num() default 0;
    boolean isRecursiveOnly() default false;
    boolean isNullPermissed() default false;
}
