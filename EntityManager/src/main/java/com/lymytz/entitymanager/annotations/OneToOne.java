package com.lymytz.entitymanager.annotations;

import com.lymytz.entitymanager.enums.CascadeType;
import com.lymytz.entitymanager.enums.FetchType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface OneToOne {
    public String alias() default "";

    public String mappedBy() default "";

    public boolean optional() default true;

    public FetchType fetch() default FetchType.EAGER;

    public CascadeType cascade() default CascadeType.ALL;

}
