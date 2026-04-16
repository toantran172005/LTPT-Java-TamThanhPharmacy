package Utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.util.function.Consumer;
import java.util.function.Function;

public class JPAUtils {
    private static EntityManagerFactory factory;

    static {
        try {
            factory = Persistence.createEntityManagerFactory("TamThanhPharmacy");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static EntityManager getEntityManager() {
        return factory.createEntityManager();
    }

    public static void inTransaction(Consumer<EntityManager> action) {
        EntityManager em = getEntityManager();
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            action.accept(em);
            tr.commit();
        } catch (Exception e) {
            if (tr.isActive()) tr.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public static <R> R doInTransaction(Function<EntityManager, R> action) {
        EntityManager em = getEntityManager();
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            R result = action.apply(em);
            tr.commit();
            return result;
        } catch (Exception e) {
            if (tr.isActive()) tr.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public static void shutdown() {
        if (factory != null && factory.isOpen()) factory.close();
    }
}