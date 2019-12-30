package com.transferapp.dto.validation;

import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Performs validation using general validation logic applicable for all DTOs
 *
 * @param <T> type of the DTO
 */
public abstract class AbstractDTOValidator<T> {

    /**
     * Checks @NotEmpty annotated field of provided object and returns error messages if those are null.
     *
     * @param dto Object to be validated
     * @return list of error messages
     */
    public List<String> validate(T dto) {
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

        if (CollectionUtils.isNotEmpty(result)) {
            return result;
        }

        return validateSpecific(dto);
    }

    protected abstract List<String> validateSpecific(T dto);

}
