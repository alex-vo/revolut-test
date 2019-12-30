package com.abc;

import com.abc.dto.AccountStateDTO;
import com.abc.dto.TransferDTO;
import com.abc.dto.validation.AbstractDTOValidator;
import com.abc.entity.Account;
import com.abc.route.GetByIdRoute;
import com.abc.route.PostRoute;
import spark.Spark;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

public class MainClass {

    public static void main(String[] args) {
        prepareData();

        Spark.post(
                "/transfer",
                new PostRoute<TransferDTO>(TransferDTO.class) {
                    @Override
                    protected AbstractDTOValidator<TransferDTO> getDTOValidator() {
                        return Pico.getTransferDTOValidator();
                    }

                    @Override
                    protected void processBody(TransferDTO dto) {
                        Pico.getAccountService().processTransfer(dto);
                    }
                },
                Pico.getJSONResponseTransformer()
        );

        Spark.get(
                "/account/:id",
                new GetByIdRoute<AccountStateDTO>() {
                    @Override
                    protected AccountStateDTO processGetRequest(Long id) {
                        return Pico.getAccountService().getAccountState(id);
                    }
                },
                Pico.getJSONResponseTransformer()
        );
    }

    private static void prepareData() {
        Pico.getTransactionService().executeInTransaction(Pico.getEntityManager(), () -> {
            EntityManager em = Pico.getEntityManager();
            Account a1 = new Account();
            a1.setBalance(BigDecimal.valueOf(4.5));
            em.persist(a1);

            Account a2 = new Account();
            a2.setBalance(BigDecimal.valueOf(3.5));
            em.persist(a2);
        });
    }
}
