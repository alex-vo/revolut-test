package com.transferapp.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    EntityManager em = mock(EntityManager.class);
    Transaction tx = mock(Transaction.class);
    TransactionService transactionService = new TransactionService(em);

    @BeforeEach
    public void setup() {
        Session session = mock(Session.class);
        when(session.beginTransaction()).thenReturn(tx);
        when(em.unwrap(Session.class)).thenReturn(session);
    }

    @Test
    public void testExecuteInTransaction_happyPath() {
        Runnable runnable = mock(Runnable.class);

        transactionService.executeInTransaction(runnable);

        verify(em).flush();
        verify(runnable).run();
        verify(tx).commit();
        verify(tx, never()).rollback();
    }

    @Test
    public void testExecuteInTransaction_rollbackDueToException() {
        Runnable runnable = mock(Runnable.class);
        doThrow(new RuntimeException("abc")).when(runnable).run();

        try {
            transactionService.executeInTransaction(runnable);
            fail();
        } catch (RuntimeException e) {
            assertThat(e.getMessage(), is("abc"));
        }


        verify(em, never()).flush();
        verify(runnable).run();
        verify(tx, never()).commit();
        verify(tx).rollback();
    }

}
