package repository.impl;

import entity.KhuyenMai;
import repository.GenericJpa;
import repository.intf.KhuyenMaiRepository;

import java.time.LocalDate;
import java.util.List;

public class KhuyenMaiRepositoryImpl extends GenericJpa implements KhuyenMaiRepository {

    @Override
    public KhuyenMai layKhuyenMaiTheoMa(String maKM) {
        return doInTransaction(em -> em.find(KhuyenMai.class, maKM));
    }

    @Override
    public void capNhatTrangThaiHetHan() {
        inTransaction(em -> {
            String jpql = "UPDATE KhuyenMai k SET k.trangThai = false WHERE k.ngayKT < :homNay AND k.trangThai = true";
            em.createQuery(jpql)
                    .setParameter("homNay", LocalDate.now())
                    .executeUpdate();
        });
    }

    @Override
    public boolean capNhatKhuyenMai(KhuyenMai km, List<String> danhSachMaThuoc) {
        try {
            inTransaction(em -> {
                // 1. Cập nhật Khuyến mãi
                em.merge(km);

                // 2. Reset khuyến mãi của thuốc cũ
                em.createQuery("UPDATE Thuoc t SET t.khuyenMai.maKM = null WHERE t.khuyenMai.maKM = :maKM")
                        .setParameter("maKM", km.getMaKM())
                        .executeUpdate();

                // 3. Set khuyến mãi cho thuốc mới
                if (danhSachMaThuoc != null && !danhSachMaThuoc.isEmpty()) {
                    for (String maThuoc : danhSachMaThuoc) {
                        em.createQuery("UPDATE Thuoc t SET t.khuyenMai.maKM = :maKM WHERE t.maThuoc = :maThuoc")
                                .setParameter("maKM", km.getMaKM())
                                .setParameter("maThuoc", maThuoc)
                                .executeUpdate();
                    }
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<KhuyenMai> layDanhSachKM() {
        return doInTransaction(em -> {
            List<KhuyenMai> list = em.createQuery("SELECT k FROM KhuyenMai k", KhuyenMai.class).getResultList();
            // Xử lý sắp xếp theo số trong chuỗi maKM giống ORDER BY TRY_CAST(REPLACE...)
            list.sort((k1, k2) -> {
                int id1 = Integer.parseInt(k1.getMaKM().replaceAll("[^0-9]", ""));
                int id2 = Integer.parseInt(k2.getMaKM().replaceAll("[^0-9]", ""));
                return Integer.compare(id1, id2);
            });
            return list;
        });
    }

    @Override
    public List<KhuyenMai> layDanhSachDaXoa() {
        return doInTransaction(em -> {
            List<KhuyenMai> list = em.createQuery("SELECT k FROM KhuyenMai k WHERE k.trangThai = false", KhuyenMai.class).getResultList();
            list.sort((k1, k2) -> {
                int id1 = Integer.parseInt(k1.getMaKM().replaceAll("[^0-9]", ""));
                int id2 = Integer.parseInt(k2.getMaKM().replaceAll("[^0-9]", ""));
                return Integer.compare(id1, id2);
            });
            return list;
        });
    }

    @Override
    public boolean xoaKM(String maKM) {
        try {
            inTransaction(em -> {
                KhuyenMai km = em.find(KhuyenMai.class, maKM);
                if (km != null) {
                    km.setTrangThai(false);
                    em.merge(km);
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean khoiPhucKM(String maKM) {
        try {
            inTransaction(em -> {
                KhuyenMai km = em.find(KhuyenMai.class, maKM);
                if (km != null) {
                    km.setTrangThai(true);
                    em.merge(km);
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean themThuocVaoChiTietKM(String maThuoc, String maKM) {
        try {
            inTransaction(em -> {
                em.createQuery("UPDATE Thuoc t SET t.khuyenMai.maKM = :maKM WHERE t.maThuoc = :maThuoc")
                        .setParameter("maKM", maKM)
                        .setParameter("maThuoc", maThuoc)
                        .executeUpdate();
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Object[]> layDanhSachChiTiet(String maKM) {
        return doInTransaction(em -> {
            String jpql = "SELECT k.tenKM, k.loaiKM, k.ngayBD, k.ngayKT, t.maThuoc, t.tenThuoc " +
                    "FROM Thuoc t, KhuyenMai k " +
                    "WHERE t.khuyenMai.maKM = k.maKM AND k.maKM LIKE :maKM";
            return em.createQuery(jpql, Object[].class)
                    .setParameter("maKM", maKM)
                    .getResultList();
        });
    }

    @Override
    public boolean themKM(KhuyenMai km, List<Object[]> listChon) {
        try {
            inTransaction(em -> {
                em.persist(km);
                em.flush();

                if (listChon != null && !listChon.isEmpty()) {
                    for (Object[] row : listChon) {
                        String maThuoc = (String) row[0];

                        em.createQuery("""
                        UPDATE Thuoc t 
                        SET t.khuyenMai.maKM = :maKM 
                        WHERE t.maThuoc = :maThuoc
                    """)
                                .setParameter("maKM", km.getMaKM())
                                .setParameter("maThuoc", maThuoc)
                                .executeUpdate();
                    }
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}