package com.lymytz.entitymanager.annotations;

import com.lymytz.entitymanager.enums.GenerationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GeneratedValue {
   public String generator();

    public GenerationType strategy();
}
