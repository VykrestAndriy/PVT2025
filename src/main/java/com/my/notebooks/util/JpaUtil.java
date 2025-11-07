package com.my.notebooks.util;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaUtil {
    private static final String PERSISTENCE_UNIT_NAME = "notebooks_pu";
    private static EntityManagerFactory factory = null;

    static {
        try {
            factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        } catch (Exception e) {
            System.err.println("ПОМИЛКА ІНІЦІАЛІЗАЦІЇ JPA: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return factory;
    }

    public static void shutdown() {
        if (factory != null && factory.isOpen()) {
            factory.close();
        }
    }
}