package com.transferapp.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@Builder
@ToString
public class AccountStateDTO {
    private Long id;
    private BigDecimal balance;
}
