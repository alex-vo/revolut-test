package com.abc.service;

import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class TransactionService {

    public void executeInTransaction(EntityManager em, Runnable runnable) {
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
