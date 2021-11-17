package com.lymytz.entitymanager.annotations;

import com.lymytz.entitymanager.enums.ActionType;
import com.lymytz.entitymanager.enums.FetchType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ManyToOne {
    public String alias() default "";

    public boolean force() default false;

    public FetchType fetch() default FetchType.LAZY;

    public ActionType delete() default ActionType.NO_ACTION;

    public ActionType update() default ActionType.CASCADE;
}
