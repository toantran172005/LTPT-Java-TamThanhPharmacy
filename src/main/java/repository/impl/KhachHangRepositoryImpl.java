package repository.impl;

import entity.KhachHang;
import repository.GenericJpa;
import repository.intf.KhachHangRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KhachHangRepositoryImpl extends GenericJpa implements KhachHangRepository {

    @Override
    public KhachHang timKhachHangTheoSDT(String sdt) {
        return doInTransaction(em -> {
            String jpql = "SELECT k FROM KhachHang k WHERE k.sdt = :sdt";
            List<KhachHang> result = em.createQuery(jpql, KhachHang.class)
                    .setParameter("sdt", sdt)
                    .getResultList();
            return result.isEmpty() ? null : result.get(0);
        });
    }

    @Override
    public KhachHang timKhachHangTheoMa(String maKH) {
        return doInTransaction(em -> em.find(KhachHang.class, maKH));
    }

    @Override
    public boolean themKhachHang(KhachHang kh) {
        try {
            inTransaction(em -> em.persist(kh));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean capNhatKhachHang(KhachHang kh) {
        try {
            inTransaction(em -> em.merge(kh));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean xoaKhachHang(String maKH) {
        try {
            inTransaction(em -> {
                KhachHang kh = em.find(KhachHang.class, maKH);
                if (kh != null) {
                    kh.setTrangThai(false); // Xóa mềm
                    em.merge(kh);
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean khoiPhucKhachHang(String maKH) {
        try {
            inTransaction(em -> {
                KhachHang kh = em.find(KhachHang.class, maKH);
                if (kh != null) {
                    kh.setTrangThai(true); // Khôi phục
                    em.merge(kh);
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<KhachHang> layListKhachHang() {
        return doInTransaction(em -> {
            String jpql = "SELECT k FROM KhachHang k ORDER BY CAST(SUBSTRING(k.maKH, 5) AS Integer)";
            return em.createQuery(jpql, KhachHang.class).getResultList();
        });
    }

    @Override
    public Map<String, Integer> layTongDonHangTheoNgay(LocalDate ngayBD, LocalDate ngayKT) {
        return doInTransaction(em -> {
            String jpql = "SELECT h.khachHang.maKH, COUNT(DISTINCT h.maHD) " +
                    "FROM HoaDon h WHERE h.ngayLap BETWEEN :ngayBD AND :ngayKT AND h.trangThai = true " +
                    "GROUP BY h.khachHang.maKH";
            List<Object[]> results = em.createQuery(jpql, Object[].class)
                    .setParameter("ngayBD", ngayBD)
                    .setParameter("ngayKT", ngayKT)
                    .getResultList();
            Map<String, Integer> map = new HashMap<>();
            for (Object[] row : results) {
                map.put((String) row[0], ((Number) row[1]).intValue());
            }
            return map;
        });
    }

    @Override
    public Map<String, Double> layTongTienTheoNgay(LocalDate ngayBD, LocalDate ngayKT) {
        return doInTransaction(em -> {
            String jpql = "SELECT h.khachHang.maKH, SUM(" +
                    "CASE WHEN km.loaiKM = 'Giảm giá' THEN ct.soLuong * ct.donGia * (1 - km.mucKM / 100.0) " +
                    "WHEN km.loaiKM = 'Mua tặng' THEN (ct.soLuong - MOD(ct.soLuong, (km.soLuongMua + km.soLuongTang)) * km.soLuongTang) * ct.donGia " +
                    "ELSE ct.soLuong * ct.donGia END) " +
                    "FROM CTHoaDon ct " +
                    "JOIN ct.hoaDon h " +
                    "JOIN ct.thuoc t " +
                    "LEFT JOIN t.khuyenMai km " +
                    "WHERE h.ngayLap BETWEEN :ngayBD AND :ngayKT AND h.trangThai = true " +
                    "GROUP BY h.khachHang.maKH";

            List<Object[]> results = em.createQuery(jpql, Object[].class)
                    .setParameter("ngayBD", ngayBD)
                    .setParameter("ngayKT", ngayKT)
                    .getResultList();

            Map<String, Double> map = new HashMap<>();
            for (Object[] row : results) {
                map.put((String) row[0], ((Number) row[1]).doubleValue());
            }
            return map;
        });
    }

    @Override
    public Map<String, Double> layTatCaTongTien() {
        return doInTransaction(em -> {
            String jpql = "SELECT h.khachHang.maKH, SUM(" +
                    "CASE WHEN km.loaiKM = 'Giảm giá' THEN ct.soLuong * ct.donGia * (1 - km.mucKM / 100.0) " +
                    "WHEN km.loaiKM = 'Mua tặng' THEN (ct.soLuong - MOD(ct.soLuong, (km.soLuongMua + km.soLuongTang)) * km.soLuongTang) * ct.donGia " +
                    "ELSE ct.soLuong * ct.donGia END) " +
                    "FROM CTHoaDon ct " +
                    "JOIN ct.hoaDon h " +
                    "JOIN ct.thuoc t " +
                    "LEFT JOIN t.khuyenMai km " +
                    "GROUP BY h.khachHang.maKH";

            List<Object[]> results = em.createQuery(jpql, Object[].class).getResultList();

            Map<String, Double> map = new HashMap<>();
            for (Object[] row : results) {
                map.put((String) row[0], ((Number) row[1]).doubleValue());
            }
            return map;
        });
    }

    @Override
    public Map<String, Integer> layTatCaTongDonHang() {
        return doInTransaction(em -> {
            String jpql = "SELECT h.khachHang.maKH, COUNT(DISTINCT h.maHD) FROM HoaDon h GROUP BY h.khachHang.maKH";
            List<Object[]> results = em.createQuery(jpql, Object[].class).getResultList();
            Map<String, Integer> map = new HashMap<>();
            for (Object[] row : results) {
                map.put((String) row[0], ((Number) row[1]).intValue());
            }
            return map;
        });
    }

    @Override
    public List<KhachHang> layListKHThongKe(LocalDate ngayBD, LocalDate ngayKT) {
        return doInTransaction(em -> {
            String jpql = "SELECT DISTINCT k " +
                    "FROM HoaDon h JOIN h.khachHang k " +
                    "WHERE h.ngayLap BETWEEN :ngayBD AND :ngayKT";

            List<KhachHang> listKhachHang = em.createQuery(jpql, KhachHang.class)
                    .setParameter("ngayBD", ngayBD)
                    .setParameter("ngayKT", ngayKT)
                    .getResultList();

            listKhachHang.sort((k1, k2) -> {
                try {
                    int so1 = Integer.parseInt(k1.getMaKH().replaceAll("[^0-9]", ""));
                    int so2 = Integer.parseInt(k2.getMaKH().replaceAll("[^0-9]", ""));
                    return Integer.compare(so1, so2);
                } catch (Exception e) {
                    return 0;
                }
            });

            return listKhachHang;
        });
    }
}