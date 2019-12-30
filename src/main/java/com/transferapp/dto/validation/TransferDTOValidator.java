package com.transferapp.dto.validation;

import com.google.inject.Singleton;
import com.transferapp.dto.TransferDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Performs validation using specific logic applicable com.transferapp.dto.TransferDTO
 */
@Singleton
public class TransferDTOValidator extends AbstractDTOValidator<TransferDTO> {

    /**
     * Checks that:
     * - transferred amount is positive
     * - transfer is being made between different accounts
     *
     * @param dto Object to be validated
     * @return list of error messages
     */
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
