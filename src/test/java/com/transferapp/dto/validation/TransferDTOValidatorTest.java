package com.transferapp.dto.validation;

import com.transferapp.dto.TransferDTO;
import com.transferapp.util.DTOUtils;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;

public class TransferDTOValidatorTest {

    TransferDTOValidator transferDTOValidator = new TransferDTOValidator();

    @Test
    public void testValidate_noErrors() {
        TransferDTO dto = DTOUtils.prepareTransferDTO(1L, 2L, BigDecimal.valueOf(10));

        List<String> result = transferDTOValidator.validate(dto);

        assertThat(result, hasSize(0));
    }

    @Test
    public void testValidate_emptyFields() {
        TransferDTO dto = new TransferDTO();

        List<String> result = transferDTOValidator.validate(dto);

        assertThat(result, contains("source account cannot be empty", "destination account cannot be empty", "amount cannot be empty"));
    }

    @Test
    public void testValidate_nonPositive() {
        TransferDTO dto = DTOUtils.prepareTransferDTO(1L, 2L, BigDecimal.ZERO);

        List<String> result = transferDTOValidator.validate(dto);

        assertThat(result, contains("transfer amount has to be positive"));
    }

    @Test
    public void testValidate_transferToSelf() {
        TransferDTO dto = DTOUtils.prepareTransferDTO(1L, 1L, BigDecimal.ONE);

        List<String> result = transferDTOValidator.validate(dto);

        assertThat(result, contains("Cannot transfer to self"));
    }

}
