package com.transferapp.dto.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Indicates that DTO field value should not be empty
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface NotEmpty {

    String value();

}
