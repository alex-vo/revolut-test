package com.transferapp.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.transferapp.dto.AccountStateDTO;
import com.transferapp.dto.TransferDTO;
import com.transferapp.dto.mapper.AccountStateDTOMapper;
import com.transferapp.entity.Account;
import lombok.extern.slf4j.Slf4j;
import spark.Spark;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Optional;

@Singleton
@Slf4j
public class AccountService {

    private final EntityManager em;
    private final TransactionService transactionService;

    @Inject
    public AccountService(EntityManager em, TransactionService transactionService) {
        this.em = em;
        this.transactionService = transactionService;
    }

    public void processTransfer(TransferDTO dto) {
        log.info(String.format("Processing transfer %s", dto));
        transactionService.executeInTransaction(() -> {
            Account from = Optional.ofNullable(em.find(Account.class, dto.getFrom()))
                    .orElseThrow(() -> Spark.halt(404, String.format("account %d not found", dto.getFrom())));
            Account to = Optional.ofNullable(em.find(Account.class, dto.getTo()))
                    .orElseThrow(() -> Spark.halt(404, String.format("account %d not found", dto.getTo())));
            BigDecimal newBalance = from.getBalance().subtract(dto.getAmount());
            if (BigDecimal.ZERO.compareTo(newBalance) > 0) {
                throw Spark.halt(400, "insufficient funds");
            }
            from.setBalance(newBalance);
            to.setBalance(to.getBalance().add(dto.getAmount()));
            em.merge(from);
            em.merge(to);
        });
        log.info(String.format("Finished processing transfer %s", dto));
    }

    public AccountStateDTO getAccountState(Long id) {
        log.info(String.format("Finding account by id %d", id));
        Account account = Optional.ofNullable(em.find(Account.class, id))
                .orElseThrow(() -> Spark.halt(404, "account not found"));
        return AccountStateDTOMapper.map(account);
    }

}
