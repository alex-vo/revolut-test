package com.transferapp.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AccountStateDTO {
    private Long id;
    private BigDecimal balance;
}
