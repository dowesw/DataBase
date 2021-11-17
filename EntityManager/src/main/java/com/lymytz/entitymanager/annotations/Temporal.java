package com.lymytz.entitymanager.annotations;

import com.lymytz.entitymanager.enums.TemporalType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Date;

import static com.lymytz.entitymanager.enums.TemporalType.DATE;
import static com.lymytz.entitymanager.enums.TemporalType.TIMESTAMP;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)

public @interface Temporal {
    TemporalType value() default DATE;
}
