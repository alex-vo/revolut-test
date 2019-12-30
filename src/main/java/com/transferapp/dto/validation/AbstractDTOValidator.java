package com.transferapp.dto.validation;

import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDTOValidator<T> {

    public List<String> validate(T dto) {
        List<String> result = new ArrayList<>();
        for (Field field : dto.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(NotEmpty.class)) {
                boolean accessible = field.canAccess(dto);
                try {
                    field.setAccessible(true);
                    Object value = field.get(dto);
                    if (value == null) {
                        result.add(field.getAnnotation(NotEmpty.class).value());
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } finally {
                    field.setAccessible(accessible);
                }
            }
        }

        if (CollectionUtils.isNotEmpty(result)) {
            return result;
        }

        return validateSpecific(dto);
    }

    protected abstract List<String> validateSpecific(T dto);

}
