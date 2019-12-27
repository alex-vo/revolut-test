package com.abc.dto.validation;

import com.abc.dto.TransferDTO;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

public class TransferDTOValidator extends AbstractDTOValidator {

    public List<String> validate(TransferDTO dto) {
        List<String> result = super.validate(dto);

        if (CollectionUtils.isNotEmpty(result)) {
            return result;
        }

        if (dto.getFrom().equals(dto.getTo())) {
            result.add("Cannot transfer to self");
        }

        if (BigDecimal.ZERO.compareTo(dto.getAmount()) > 0) {
            result.add("cannot transfer negative amount");
        }
        return result;
    }

}
