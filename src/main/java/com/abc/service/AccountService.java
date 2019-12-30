package com.abc.service;

import com.abc.Pico;
import com.abc.dto.AccountStateDTO;
import com.abc.dto.TransferDTO;
import com.abc.entity.Account;
import com.abc.mapper.AccountStateDTOMapper;
import spark.Spark;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Optional;

public class AccountService {

    private EntityManager em = Pico.getEntityManager();

    public void processTransfer(TransferDTO dto) {
        Pico.getTransactionService().executeInTransaction(em, () -> {
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
    }

    public AccountStateDTO getAccountState(Long id) {
        Account account = Optional.ofNullable(em.find(Account.class, id))
                .orElseThrow(() -> Spark.halt(404, "account not found"));
        return AccountStateDTOMapper.map(account);
    }

}
