package com.abc.dto;

import com.abc.dto.validation.NotEmpty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferDTO {
    @NotEmpty("from account be empty")
    private Long from;
    @NotEmpty("to account be empty")
    private Long to;
    @NotEmpty("amount cannot be empty")
    private BigDecimal amount;
}
