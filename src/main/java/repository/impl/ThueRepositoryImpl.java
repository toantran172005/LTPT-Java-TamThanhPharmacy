package repository.impl;

import entity.Thue;
import repository.GenericJpa;
import repository.intf.ThueRepository;

import java.util.List;

// Giả định bạn đã import GenericJpa
// import config.GenericJpa;

public class ThueRepositoryImpl extends GenericJpa implements ThueRepository {

    @Override
    public List<Thue> layListThue() {
        return doInTransaction(em -> {
            // Giả sử Entity cấu hình trangThai là Boolean.
            String jpql = "SELECT t FROM Thue t WHERE t.trangThai = true";
            return em.createQuery(jpql, Thue.class).getResultList();
        });
    }

    @Override
    public boolean themThue(Thue t) {
        try {
            inTransaction(em -> em.persist(t));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean capNhatThue(Thue t) {
        try {
            inTransaction(em -> em.merge(t));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean xoaThue(String maThue) {
        try {
            inTransaction(em -> {
                Thue t = em.find(Thue.class, maThue);
                if (t != null) {
                    t.setTrangThai(false); // Đổi trạng thái thành false (hoặc 0)
                    em.merge(t); // Cập nhật xuống DB
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Thue> timKiem(String tuKhoa) {
        return doInTransaction(em -> {
            String jpql = "SELECT t FROM Thue t WHERE (t.maThue LIKE :tuKhoa OR t.loaiThue LIKE :tuKhoa) AND t.trangThai = true";
            return em.createQuery(jpql, Thue.class)
                    .setParameter("tuKhoa", "%" + tuKhoa + "%")
                    .getResultList();
        });
    }
}