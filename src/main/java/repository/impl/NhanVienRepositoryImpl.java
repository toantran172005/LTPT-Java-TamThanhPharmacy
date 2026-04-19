package repository.impl;

import entity.NhanVien;
import entity.TaiKhoan;
import repository.GenericJpa;
import repository.intf.NhanVienRepository;

import java.util.List;

// Giả định bạn đã import GenericJpa từ package tương ứng
// import config.GenericJpa;

public class NhanVienRepositoryImpl extends GenericJpa implements NhanVienRepository {

    @Override
    public NhanVien timNhanVienTheoMa(String maNV) {
        return doInTransaction(em -> em.find(NhanVien.class, maNV));
    }

    @Override
    public List<NhanVien> layListNhanVien() {
        return doInTransaction(em -> {
            String jpql = "SELECT nv FROM NhanVien nv JOIN FETCH nv.thue ORDER BY CAST(SUBSTRING(nv.maNV, 5) AS int)";
            return em.createQuery(jpql, NhanVien.class).getResultList();
        });
    }

    @Override
    public List<NhanVien> layNhanVienDangLam() {
        return doInTransaction(em -> {
            String jpql = "SELECT nv FROM NhanVien nv JOIN FETCH nv.thue WHERE nv.trangThai = true ORDER BY CAST(SUBSTRING(nv.maNV, 5) AS int)";
            return em.createQuery(jpql, NhanVien.class).getResultList();
        });
    }

    @Override
    public List<NhanVien> layNhanVienNghiLam() {
        return doInTransaction(em -> {
            String jpql = "SELECT nv FROM NhanVien nv JOIN FETCH nv.thue WHERE nv.trangThai = false ORDER BY CAST(SUBSTRING(nv.maNV, 5) AS int)";
            return em.createQuery(jpql, NhanVien.class).getResultList();
        });
    }

    @Override
    public boolean xoaNhanVien(String maNV) {
        try {
            inTransaction(em -> {
                NhanVien nv = em.find(NhanVien.class, maNV);
                if (nv != null) {
                    nv.setTrangThai(false);
                    em.merge(nv); // Cập nhật trạng thái thông qua merge
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean khoiPhucNhanVien(String maNV) {
        try {
            inTransaction(em -> {
                NhanVien nv = em.find(NhanVien.class, maNV);
                if (nv != null) {
                    nv.setTrangThai(true);
                    em.merge(nv);
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean capNhatNhanVien(NhanVien nv) {
        try {
            inTransaction(em -> em.merge(nv));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean themNhanVien(NhanVien nv) {
        try {
            inTransaction(em -> em.persist(nv));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean themTaiKhoan(TaiKhoan tk) {
        try {
            inTransaction(em -> em.persist(tk));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String layEmailNV(String maNV) {
        return doInTransaction(em -> {
            String jpql = "SELECT tk.email FROM TaiKhoan tk WHERE tk.nhanVien.maNV = :maNV";
            List<String> emails = em.createQuery(jpql, String.class)
                    .setParameter("maNV", maNV)
                    .getResultList();
            return emails.isEmpty() ? "" : emails.get(0);
        });
    }

    @Override
    public String layAnhNV(String maNV) {
        return doInTransaction(em -> {
            String jpql = "SELECT nv.anh FROM NhanVien nv WHERE nv.maNV = :maNV";
            List<String> anhs = em.createQuery(jpql, String.class)
                    .setParameter("maNV", maNV)
                    .getResultList();
            return anhs.isEmpty() ? null : anhs.get(0);
        });
    }
}