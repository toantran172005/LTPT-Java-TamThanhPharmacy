package repository.impl;

import entity.DonViTinh;
import repository.GenericJpa;
import repository.intf.DonViTinhRepository;

import java.util.List;

public class DonViTinhRepositoryImpl extends GenericJpa implements DonViTinhRepository {

    @Override
    public List<DonViTinh> layDanhSachTheoTrangThai(boolean trangThai) {
        return doInTransaction(em -> {
            String jpql = "SELECT d FROM DonViTinh d WHERE d.trangThai = :trangThai";
            return em.createQuery(jpql, DonViTinh.class)
                    .setParameter("trangThai", trangThai)
                    .getResultList();
        });
    }

    @Override
    public DonViTinh timTheoTen(String tenDVT) {
        return doInTransaction(em -> {
            String jpql = "SELECT d FROM DonViTinh d WHERE d.tenDVT = :tenDVT";
            List<DonViTinh> result = em.createQuery(jpql, DonViTinh.class)
                    .setParameter("tenDVT", tenDVT)
                    .getResultList();
            return result.isEmpty() ? null : result.get(0);
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
                DonViTinh dvt = em.find(DonViTinh.class, maDVT);
                if (dvt != null) {
                    dvt.setTrangThai(trangThai);
                    em.merge(dvt);
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<DonViTinh> layListDVT() {
        return doInTransaction(em -> {
            // Chỉ dùng JPQL thuần túy, sạch sẽ để lấy các đơn vị đang hoạt động (true)
            String jpql = "SELECT d FROM DonViTinh d WHERE d.trangThai = true";
            return em.createQuery(jpql, DonViTinh.class).getResultList();
        });
    }
}