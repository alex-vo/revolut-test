package com.abc.dto.validation;

import com.abc.dto.TransferDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TransferDTOValidator extends AbstractDTOValidator<TransferDTO> {


    @Override
    public List<String> validateSpecific(TransferDTO dto) {
        List<String> result = new ArrayList<>();
        if (dto.getFrom().equals(dto.getTo())) {
            result.add("Cannot transfer to self");
        }

        if (BigDecimal.ZERO.compareTo(dto.getAmount()) > 0) {
            result.add("cannot transfer negative amount");
        }
        return result;
    }
}
