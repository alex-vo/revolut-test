package com.transferapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Standard error response to be returned to API consumer in case of unexpected errors
 */
@Data
@AllArgsConstructor
public class ResponseMessageDTO {
    private String message;
}
