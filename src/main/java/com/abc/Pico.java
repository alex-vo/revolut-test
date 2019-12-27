package com.abc;

import com.abc.config.JSONResponseTransformer;
import com.abc.dto.validation.TransferDTOValidator;
import com.abc.service.AccountService;
import com.abc.service.TransactionService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.util.Properties;

public class Pico {

    public static JSONResponseTransformer getJSONResponseTransformer() {
        return new JSONResponseTransformer();
    }

    private static EntityManager em;

    public static EntityManager getEntityManager() {
        if (em == null) {
            try {
                Properties hibernateProperties = new Properties();
                hibernateProperties.load(MainClass.class.getClassLoader().getResourceAsStream("hibernate.properties"));
                EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-unit1", hibernateProperties);
                em = emf.createEntityManager();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return em;
    }

    public static TransferDTOValidator getTransferDTOValidator() {
        return new TransferDTOValidator();
    }

    public static AccountService getAccountService() {
        return new AccountService();
    }


    private static TransactionService ts;

    public static TransactionService getTransactionService() {
        if (ts == null) {
            ts = new TransactionService();
        }

        return ts;
    }

}
