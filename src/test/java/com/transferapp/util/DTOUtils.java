package com.transferapp.util;

import com.transferapp.dto.TransferDTO;

import java.math.BigDecimal;

public class DTOUtils {

    public static TransferDTO prepareTransferDTO(Long from, Long to, BigDecimal amount) {
        TransferDTO dto = new TransferDTO();
        dto.setFrom(from);
        dto.setTo(to);
        dto.setAmount(amount);
        return dto;
    }

}
