package com.transferapp.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

@Singleton
@Slf4j
public class TransactionService {

    private final EntityManager em;

    @Inject
    public TransactionService(EntityManager em) {
        this.em = em;
    }

    public void executeInTransaction(Runnable runnable) {
        EntityTransaction tx = null;

        try {
            Session session = em.unwrap(Session.class);
            log.debug("Beginning transaction");
            tx = session.beginTransaction();

            runnable.run();

            em.flush();
            tx.commit();
            log.debug("Committed transaction");
        } catch (Exception e) {
            log.debug("Rolling back transaction");
            if (tx != null) {
                tx.rollback();
            }

            throw e;
        }
    }

}
