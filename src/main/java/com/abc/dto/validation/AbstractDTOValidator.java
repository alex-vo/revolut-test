package com.abc.dto.validation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDTOValidator {

    public List<String> validate(Object dto) {
        List<String> result = new ArrayList<>();
        for (Field field : dto.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(NotEmpty.class)) {
                boolean accessible = field.isAccessible();
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

        return result;
    }

}
