package com.lymytz.entitymanager.annotations;

import com.lymytz.entitymanager.enums.FetchType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface OneToMany {
    public String mappedBy();

    public boolean orphanRemoval() default false;

    public FetchType fetch() default FetchType.EAGER;
}
