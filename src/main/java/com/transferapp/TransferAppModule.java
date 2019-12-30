package com.transferapp;

import com.google.inject.AbstractModule;
import com.transferapp.entity.Account;
import org.hibernate.cfg.AvailableSettings;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.util.Collections;
import java.util.Properties;

public class TransferAppModule extends AbstractModule {

    @Override
    protected void configure() {
        EntityManager em;
        try {
            em = createEntityManager();
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize entity manager needed for database access", e);
        }
        bind(EntityManager.class).toInstance(em);
    }

    private EntityManager createEntityManager() throws IOException {
        Properties props = new Properties();
        props.load(TransferApp.class.getClassLoader().getResourceAsStream("hibernate.properties"));
        props.put(AvailableSettings.LOADED_CLASSES, Collections.singletonList(Account.class));
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-unit1", props);
        return emf.createEntityManager();
    }
}
