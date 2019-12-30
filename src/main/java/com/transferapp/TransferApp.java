package com.transferapp;

import com.transferapp.dto.transformer.JSONResponseTransformer;
import com.transferapp.dto.AccountStateDTO;
import com.transferapp.dto.TransferDTO;
import com.transferapp.dto.validation.AbstractDTOValidator;
import com.transferapp.dto.validation.TransferDTOValidator;
import com.transferapp.entity.Account;
import com.transferapp.route.GetByIdRoute;
import com.transferapp.route.PostRoute;
import com.transferapp.service.AccountService;
import com.transferapp.service.TransactionService;
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
