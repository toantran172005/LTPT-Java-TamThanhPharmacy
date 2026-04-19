package repository.impl;

import entity.KeThuoc;
import entity.Thuoc;
import repository.GenericJpa;
import repository.intf.KeThuocRepository;

import java.util.List;

public class KeThuocRepositoryImpl extends GenericJpa implements KeThuocRepository {

    @Override
    public boolean themKeThuoc(KeThuoc kt) {
        try {
            inTransaction(em -> em.persist(kt));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean capNhatKeThuoc(KeThuoc kt) {
        try {
            inTransaction(em -> em.merge(kt));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean xoaKeThuoc(String maKe) {
        try {
            inTransaction(em -> {
                KeThuoc kt = em.find(KeThuoc.class, maKe);
                if (kt != null) {
                    kt.setTrangThai(false);
                    em.merge(kt);
                }
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean khoiPhucKeThuoc(String maKe) {
        try {
            inTransaction(em -> {
                KeThuoc kt = em.find(KeThuoc.class, maKe);
                if (kt != null) {
                    kt.setTrangThai(true);
                    em.merge(kt);
                }
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<KeThuoc> layListKeThuoc() {
        return doInTransaction(em -> {
            String jpql = "SELECT k FROM KeThuoc k";
            return em.createQuery(jpql, KeThuoc.class).getResultList();
        });
    }

    @Override
    public List<String> layTatCaTenKe() {
        return doInTransaction(em -> {
            String jpql = "SELECT k.loaiKe FROM KeThuoc k";
            return em.createQuery(jpql, String.class).getResultList();
        });
    }

    @Override
    public List<Thuoc> layListThuocTrongKe(String maKe) {
        return doInTransaction(em -> {
            String jpql = "SELECT t FROM Thuoc t WHERE t.keThuoc.maKe = :maKe";
            return em.createQuery(jpql, Thuoc.class)
                    .setParameter("maKe", maKe)
                    .getResultList();
        });
    }

    @Override
    public KeThuoc timTheoTen(String tenKe) {
        return doInTransaction(em -> {
            String jpql = "SELECT k FROM KeThuoc k WHERE k.loaiKe = :tenKe";
            List<KeThuoc> result = em.createQuery(jpql, KeThuoc.class)
                    .setParameter("tenKe", tenKe)
                    .getResultList();
            return result.isEmpty() ? null : result.get(0);
        });
    }
}
