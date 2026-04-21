package db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class ConnectDb {

    private static EntityManagerFactory factory;

    static {
        try {
            factory = Persistence.createEntityManagerFactory("TamThanhPharmacyJPA");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static EntityManager getEntityManager() {
        return factory.createEntityManager();
    }

    public static void shutdown() {
        if (factory != null && factory.isOpen()) factory.close();
    }
}
