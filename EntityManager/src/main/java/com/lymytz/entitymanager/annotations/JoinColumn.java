package com.lymytz.entitymanager.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JoinColumn  {
    public  String referencedColumnName() default "";
    public  String name();
    public boolean insertable() default false;
    public boolean updatable() default false;
}
