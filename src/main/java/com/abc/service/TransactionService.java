package com.abc.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

@Singleton
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
            tx = session.beginTransaction();

            runnable.run();

            em.flush();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }

            throw e;
        }
    }

}
