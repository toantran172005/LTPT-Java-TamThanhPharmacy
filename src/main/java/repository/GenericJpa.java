package repository;

import db.ConnectDb;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.function.Consumer;
import java.util.function.Function;

public abstract class GenericJpa {

    protected void inTransaction(Consumer<EntityManager> action) {
        EntityManager em = ConnectDb.getEntityManager();
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

    protected <R> R doInTransaction(Function<EntityManager, R> action) {
        EntityManager em = ConnectDb.getEntityManager();
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

}
