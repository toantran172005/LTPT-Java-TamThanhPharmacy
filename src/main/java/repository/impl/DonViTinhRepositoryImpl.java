package repository.impl;

import entity.DonViTinh;
import jakarta.persistence.NoResultException;
import repository.GenericJpa;
import repository.intf.DonViTinhRepository;

import java.util.List;

public class DonViTinhRepositoryImpl extends GenericJpa implements DonViTinhRepository {

    @Override
    public List<DonViTinh> layDanhSachTheoTrangThai(boolean trangThai) {
        return doInTransaction(em -> {
            // Trong JPQL, ta có thể dùng substring để cắt chuỗi thay cho REPLACE trong SQL Server
            String jpql = "SELECT d FROM DonViTinh d WHERE d.trangThai = :trangThai ORDER BY d.maDVT";
            return em.createQuery(jpql, DonViTinh.class)
                    .setParameter("trangThai", trangThai)
                    .getResultList();
        });
    }

    @Override
    public DonViTinh timTheoTen(String tenDVT) {
        return doInTransaction(em -> {
            try {
                String jpql = "SELECT d FROM DonViTinh d WHERE d.tenDVT = :ten";
                return em.createQuery(jpql, DonViTinh.class)
                        .setParameter("ten", tenDVT)
                        .setMaxResults(1)
                        .getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        });
    }

    @Override
    public DonViTinh timTheoMa(String maDVT) {
        return doInTransaction(em -> em.find(DonViTinh.class, maDVT));
    }

    @Override
    public boolean themDVT(DonViTinh dvt) {
        try {
            inTransaction(em -> em.persist(dvt));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean capNhatTrangThai(String maDVT, boolean trangThai) {
        try {
            inTransaction(em -> {
                DonViTinh d = em.find(DonViTinh.class, maDVT);
                if (d != null) {
                    d.setTrangThai(trangThai);
                    em.merge(d);
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}