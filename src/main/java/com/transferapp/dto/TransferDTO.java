package com.transferapp.dto;

import com.transferapp.dto.validation.NotEmpty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferDTO {
    @NotEmpty("from account cannot be empty")
    private Long from;
    @NotEmpty("to account cannot be empty")
    private Long to;
    @NotEmpty("amount cannot be empty")
    private BigDecimal amount;
}
