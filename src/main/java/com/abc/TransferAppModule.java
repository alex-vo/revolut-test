package com.abc;

import com.google.inject.AbstractModule;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
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
        Properties hibernateProperties = new Properties();
        hibernateProperties.load(TransferApp.class.getClassLoader().getResourceAsStream("hibernate.properties"));
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-unit1", hibernateProperties);
        return emf.createEntityManager();
    }
}
