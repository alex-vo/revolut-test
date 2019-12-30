package com.transferapp.service;

import com.transferapp.dto.AccountStateDTO;
import com.transferapp.entity.Account;
import com.transferapp.util.DTOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import spark.HaltException;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class AccountServiceTest {

    EntityManager em = mock(EntityManager.class);
    TransactionService transactionService = mock(TransactionService.class);
    AccountService accountService = new AccountService(em, transactionService);

    @BeforeEach
    public void setup() {
        doAnswer((Answer<Void>) invocation -> {
            invocation.getArgument(0, Runnable.class).run();
            return null;
        }).when(transactionService).executeInTransaction(Mockito.any(Runnable.class));
    }

    @Test
    public void testProcessTransfer_insufficientFunds() {
        prepareAccount(1L, BigDecimal.ZERO);
        prepareAccount(2L, BigDecimal.ZERO);

        try {
            accountService.processTransfer(DTOUtils.prepareTransferDTO(1L, 2L, BigDecimal.ONE));
            fail();
        } catch (HaltException e) {
            assertThat(e.statusCode(), is(400));
            assertThat(e.body(), is("insufficient funds"));
        }
    }

    @Test
    public void testProcessTransfer_happyPath() {
        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        prepareAccount(1L, BigDecimal.valueOf(5));
        prepareAccount(2L, BigDecimal.valueOf(2));

        accountService.processTransfer(DTOUtils.prepareTransferDTO(1L, 2L, BigDecimal.ONE));

        verify(em, times(2)).merge(accountCaptor.capture());
        assertThat(accountCaptor.getAllValues(), contains(
                allOf(hasProperty("balance", equalTo(BigDecimal.valueOf(4)))),
                allOf(hasProperty("balance", equalTo(BigDecimal.valueOf(3))))
        ));
    }

    private Account prepareAccount(Long id, BigDecimal balance) {
        Account account = new Account();
        account.setId(id);
        account.setBalance(balance);

        when(em.find(Account.class, id)).thenReturn(account);

        return account;
    }

    @Test
    public void testGetAccountState_accountNotFound() {
        try {
            accountService.getAccountState(0L);
            fail();
        } catch (HaltException e) {
            assertThat(e.statusCode(), is(404));
            assertThat(e.body(), is("account not found"));
        }
    }

    @Test
    public void testGetAccountState_happyPath() {
        Account account = prepareAccount(1L, BigDecimal.ZERO);
        when(em.find(Account.class, 1L)).thenReturn(account);

        AccountStateDTO accountState = accountService.getAccountState(1L);

        assertThat(accountState.getId(), is(1L));
        assertThat(accountState.getBalance(), is(BigDecimal.ZERO));
    }

}
