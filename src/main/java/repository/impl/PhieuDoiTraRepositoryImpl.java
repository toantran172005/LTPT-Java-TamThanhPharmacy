package repository.impl;

import entity.PhieuDoiTra;
import repository.GenericJpa;
import repository.intf.PhieuDoiTraRepository;

import java.util.List;

public class PhieuDoiTraRepositoryImpl extends GenericJpa implements PhieuDoiTraRepository {
    @Override
    public boolean kiemTraHoaDonDaDoiTra(String maHD) {
        return doInTransaction(em -> {
            String jpql = "SELECT COUNT(p) FROM PhieuDoiTra p WHERE p.hoaDon.maHD = :maHD";
            Long count = em.createQuery(jpql, Long.class)
                    .setParameter("maHD", maHD)
                    .getSingleResult();
            return count > 0;
        });
    }

    @Override
    public List<PhieuDoiTra> layListPDT() {
        return doInTransaction(em -> {
            String jpql = "SELECT p FROM PhieuDoiTra p " +
                    "JOIN FETCH p.nhanVien " +
                    "JOIN FETCH p.hoaDon hd " +
                    "JOIN FETCH hd.khachHang " +
                    "ORDER BY CAST(SUBSTRING(p.maPhieuDT, 6) AS int) DESC";
            return em.createQuery(jpql, PhieuDoiTra.class).getResultList();
        });
    }

    @Override
    public boolean themPDT(PhieuDoiTra pdt) {
        try {
            inTransaction(em -> em.persist(pdt));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean themChiTietPDT(Object chiTietPDT) {
        // Lưu ý: Đối tượng chiTietPDT phải là Entity (ví dụ: ChiTietPhieuDoiTra)
        try {
            inTransaction(em -> em.persist(chiTietPDT));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String layMaPDTMoiNhat() {
        return doInTransaction(em -> {
            String jpql = "SELECT p.maPhieuDT FROM PhieuDoiTra p " +
                    "ORDER BY CAST(SUBSTRING(p.maPhieuDT, 6) AS int) DESC";
            List<String> results = em.createQuery(jpql, String.class)
                    .setMaxResults(1)
                    .getResultList();
            return results.isEmpty() ? null : results.get(0);
        });
    }

    @Override
    public List<Object[]> layDanhSachThuocTheoPhieuDT(String maPhieuDT) {
        return doInTransaction(em -> {
            // Giả định bạn có Entity ChiTietPhieuDoiTra được map đúng với Thuoc và DonViTinh
            String jpql = "SELECT ct.phieuDoiTra.maPhieuDT, ct.thuoc.maThuoc, t.tenThuoc, " +
                    "ct.soLuong, ct.donViTinh.maDVT, dvt.tenDVT, ct.mucHoan, ct.tienHoan, ct.ghiChu " +
                    "FROM CTPhieuDoiTra ct " +
                    "JOIN ct.thuoc t " +
                    "JOIN ct.donViTinh dvt " +
                    "WHERE ct.phieuDoiTra.maPhieuDT = :maPhieuDT";
            return em.createQuery(jpql, Object[].class)
                    .setParameter("maPhieuDT", maPhieuDT)
                    .getResultList();
        });
    }

    @Override
    public double tinhTongTienHoanTheoPhieuDT(String maPhieuDT) {
        return doInTransaction(em -> {
            String jpql = "SELECT SUM(ct.tienHoan) FROM CTPhieuDoiTra ct WHERE ct.phieuDoiTra.maPhieuDT = :maPhieuDT";
            Double tong = em.createQuery(jpql, Double.class)
                    .setParameter("maPhieuDT", maPhieuDT)
                    .getSingleResult();
            return tong != null ? tong : 0.0;
        });
    }

    @Override
    public PhieuDoiTra timPhieuDoiTraTheoMa(String maPhieuDT) {
        return doInTransaction(em -> {
            String jpql = """
                SELECT p FROM PhieuDoiTra p 
                JOIN FETCH p.nhanVien 
                JOIN FETCH p.hoaDon hd 
                JOIN FETCH hd.khachHang 
                WHERE p.maPhieuDT = :maPDT
            """;

            return em.createQuery(jpql, PhieuDoiTra.class)
                    .setParameter("maPDT", maPhieuDT)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        });
    }

    @Override
    public int tongSoLuongDaDoiTra(String maHD, String maThuoc, String maDVT) {
        return doInTransaction(em -> {
            String jpql = "SELECT COALESCE(SUM(ct.soLuong), 0) FROM CTPhieuDoiTra ct " +
                    "WHERE ct.phieuDoiTra.hoaDon.maHD = :maHD " +
                    "AND ct.thuoc.maThuoc = :maThuoc " +
                    "AND ct.donViTinh.maDVT = :maDVT";
            Long tong = em.createQuery(jpql, Long.class)
                    .setParameter("maHD", maHD)
                    .setParameter("maThuoc", maThuoc)
                    .setParameter("maDVT", maDVT)
                    .getSingleResult();
            return tong.intValue();
        });
    }
}
