package com.transferapp.dto.validation;

import com.transferapp.dto.TransferDTO;
import com.google.inject.Singleton;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class TransferDTOValidator extends AbstractDTOValidator<TransferDTO> {

    @Override
    public List<String> validateSpecific(TransferDTO dto) {
        List<String> result = new ArrayList<>();
        if (dto.getFrom().equals(dto.getTo())) {
            result.add("Cannot transfer to self");
        }

        if (BigDecimal.ZERO.compareTo(dto.getAmount()) >= 0) {
            result.add("transfer amount has to be positive");
        }
        return result;
    }
}