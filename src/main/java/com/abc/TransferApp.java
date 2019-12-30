package com.abc;

import com.abc.dto.transformer.JSONResponseTransformer;
import com.abc.dto.AccountStateDTO;
import com.abc.dto.TransferDTO;
import com.abc.dto.validation.AbstractDTOValidator;
import com.abc.dto.validation.TransferDTOValidator;
import com.abc.entity.Account;
import com.abc.route.GetByIdRoute;
import com.abc.route.PostRoute;
import com.abc.service.AccountService;
import com.abc.service.TransactionService;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.log4j.BasicConfigurator;
import spark.Spark;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

public class TransferApp {

    static {
        BasicConfigurator.configure();
    }

    static Injector injector = Guice.createInjector(new TransferAppModule());

    public static void main(String[] args) {
        prepareData();

        final AccountService accountService = injector.getInstance(AccountService.class);
        Spark.post(
                "/transfer",
                new PostRoute<TransferDTO>(TransferDTO.class) {
                    @Override
                    protected AbstractDTOValidator<TransferDTO> getDTOValidator() {
                        return injector.getInstance(TransferDTOValidator.class);
                    }

                    @Override
                    protected void processBody(TransferDTO dto) {
                        accountService.processTransfer(dto);
                    }
                },
                injector.getInstance(JSONResponseTransformer.class)
        );

        Spark.get(
                "/account/:id",
                new GetByIdRoute<AccountStateDTO>() {
                    @Override
                    protected AccountStateDTO processGetRequest(Long id) {
                        return accountService.getAccountState(id);
                    }
                },
                injector.getInstance(JSONResponseTransformer.class)
        );

        Spark.get("/healthcheck", (request, response) -> "ok");
    }

    private static void prepareData() {
        EntityManager em = injector.getInstance(EntityManager.class);
        injector.getInstance(TransactionService.class).executeInTransaction(() -> {
            Account a1 = new Account();
            a1.setBalance(BigDecimal.valueOf(4.5));
            em.persist(a1);

            Account a2 = new Account();
            a2.setBalance(BigDecimal.valueOf(3.5));
            em.persist(a2);
        });
    }
}
