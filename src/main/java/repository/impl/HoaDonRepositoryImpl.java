package repository.impl;

import entity.CTHoaDon;
import entity.HoaDon;
import entity.KhachHang;
import repository.GenericJpa;
import repository.intf.HoaDonRepository;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HoaDonRepositoryImpl extends GenericJpa implements HoaDonRepository {

//    @Override
//    public List<HoaDon> layListHoaDon() {
//        return doInTransaction(em -> {
//            String jpql = "SELECT h FROM HoaDon h WHERE h.trangThai = true " +
//                    "ORDER BY CAST(SUBSTRING(h.maHD, 5, LENGTH(h.maHD)) AS Integer)";
//            return em.createQuery(jpql, HoaDon.class).getResultList();
//        });
//    }

    @Override
    public List<HoaDon> layListHoaDon() {
        return doInTransaction(em -> {
            // SỬ DỤNG LEFT JOIN FETCH ĐỂ TẢI LÊN LUÔN NHÂN VIÊN VÀ KHÁCH HÀNG
            String jpql = "SELECT h FROM HoaDon h " +
                    "LEFT JOIN FETCH h.nhanVien " +
                    "LEFT JOIN FETCH h.khachHang " +
                    "WHERE h.trangThai = true";

            return em.createQuery(jpql, HoaDon.class).getResultList();
        });
    }

//    @Override
//    public List<HoaDon> layListHDDaXoa() {
//        return doInTransaction(em -> {
//            String jpql = "SELECT h FROM HoaDon h WHERE h.trangThai = false " +
//                    "ORDER BY CAST(SUBSTRING(h.maHD, 5, LENGTH(h.maHD)) AS Integer)";
//            return em.createQuery(jpql, HoaDon.class).getResultList();
//        });
//    }

    @Override
    public List<HoaDon> layListHDDaXoa() {
        return doInTransaction(em -> {
            // SỬ DỤNG LEFT JOIN FETCH TƯƠNG TỰ
            String jpql = "SELECT h FROM HoaDon h " +
                    "LEFT JOIN FETCH h.nhanVien " +
                    "LEFT JOIN FETCH h.khachHang " +
                    "WHERE h.trangThai = false";

            return em.createQuery(jpql, HoaDon.class).getResultList();
        });
    }

    @Override
    public Map<LocalDate, Double> layDoanhThuTheoNgay(LocalDate ngayBD, LocalDate ngayKT) {
        return doInTransaction(em -> {
            String jpql = "SELECT hd.ngayLap, SUM(c.soLuong * c.donGia) " +
                    "FROM CTHoaDon c JOIN c.hoaDon hd " +
                    "WHERE hd.ngayLap BETWEEN :ngayBD AND :ngayKT " +
                    "AND hd.trangThai = true " +
                    "GROUP BY hd.ngayLap " +
                    "ORDER BY hd.ngayLap";

            List<Object[]> results = em.createQuery(jpql, Object[].class)
                    .setParameter("ngayBD", ngayBD)
                    .setParameter("ngayKT", ngayKT)
                    .getResultList();

            Map<LocalDate, Double> map = new LinkedHashMap<>();
            for (Object[] row : results) {
                LocalDate date = (LocalDate) row[0];
                Double tongTien = row[1] instanceof Number ? ((Number) row[1]).doubleValue() : 0.0;
                map.put(date, tongTien);
            }
            return map;
        });
    }

    @Override
    public List<KhachHang> layListKHThongKe(LocalDate ngayBD, LocalDate ngayKT) {
        return doInTransaction(em -> {
            String jpql = "SELECT DISTINCT h.khachHang FROM HoaDon h " +
                    "WHERE h.ngayLap BETWEEN :ngayBD AND :ngayKT AND h.trangThai = true " ;
            return em.createQuery(jpql, KhachHang.class)
                    .setParameter("ngayBD", ngayBD)
                    .setParameter("ngayKT", ngayKT)
                    .getResultList();
        });
    }

//    @Override
//    public HoaDon timHoaDonTheoMa(String maHD) {
//        return doInTransaction(em -> em.find(HoaDon.class, maHD));
//    }

    @Override
    public HoaDon timHoaDonTheoMa(String maHD) {
        return doInTransaction(em -> {
            String jpql = "SELECT h FROM HoaDon h " +
                    "LEFT JOIN FETCH h.nhanVien " +
                    "LEFT JOIN FETCH h.khachHang " +
                    "WHERE h.maHD = :maHD";

            return em.createQuery(jpql, HoaDon.class)
                    .setParameter("maHD", maHD)
                    .getResultStream() // Lấy stream để tránh lỗi nếu không tìm thấy
                    .findFirst()
                    .orElse(null);     // Nếu không có trả về null
        });
    }

    @Override
    public boolean themHoaDon(HoaDon hd) {
        try {
            inTransaction(em -> em.persist(hd));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean themChiTietHoaDon(CTHoaDon cthd) {
        try {
            inTransaction(em -> em.persist(cthd));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean xoaHD(String maHD) {
        try {
            inTransaction(em -> {
                HoaDon hd = em.find(HoaDon.class, maHD);
                if (hd != null) {
                    hd.setTrangThai(false);
                    em.merge(hd); // Update trạng thái qua merge
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean khoiPhucHD(String maHD) {
        try {
            inTransaction(em -> {
                HoaDon hd = em.find(HoaDon.class, maHD);
                if (hd != null) {
                    hd.setTrangThai(true);
                    em.merge(hd);
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int layTongDonHang(String maKH) {
        return doInTransaction(em -> {
            String jpql = "SELECT COUNT(h) FROM HoaDon h WHERE h.khachHang.maKH = :maKH";
            Long count = em.createQuery(jpql, Long.class)
                    .setParameter("maKH", maKH)
                    .getSingleResult();
            return count != null ? count.intValue() : 0;
        });
    }

    @Override
    public double layTongTien(String maKH) {
        return doInTransaction(em -> {
            String jpql = "SELECT SUM(c.soLuong * c.donGia) FROM CTHoaDon c " +
                    "WHERE c.hoaDon.khachHang.maKH = :maKH AND c.hoaDon.trangThai = true";
            Double sum = em.createQuery(jpql, Double.class)
                    .setParameter("maKH", maKH)
                    .getSingleResult();
            return sum != null ? sum : 0.0;
        });
    }

    @Override
    public double layTongTienTheoSanPham(String maHD, String maSP) {
        return doInTransaction(em -> {
            String jpql = "SELECT SUM(CASE " +
                    "WHEN km.loaiKM = 'Giảm giá' THEN c.soLuong * c.donGia * (1.0 - km.mucKM / 100.0) " +
                    "WHEN km.loaiKM = 'Mua tặng' THEN (c.soLuong - FLOOR(c.soLuong / (km.soLuongMua + km.soLuongTang)) * km.soLuongTang) * c.donGia " +
                    "ELSE c.soLuong * c.donGia END) " +
                    "FROM CTHoaDon c LEFT JOIN c.thuoc.khuyenMai km " +
                    "WHERE c.hoaDon.maHD = :maHD AND c.thuoc.maThuoc = :maSP";
            Double sum = em.createQuery(jpql, Double.class)
                    .setParameter("maHD", maHD)
                    .setParameter("maSP", maSP)
                    .getSingleResult();
            return sum != null ? sum : 0.0;
        });
    }

    @Override
    public double tinhTongTienTheoHoaDon(String maHD) {
        return doInTransaction(em -> {
            String jpql = "SELECT SUM(CASE " +
                    "WHEN km.loaiKM = 'Giảm giá' THEN c.soLuong * c.donGia * (1.0 - km.mucKM / 100.0) " +
                    "WHEN km.loaiKM = 'Mua tặng' THEN (c.soLuong - FLOOR(c.soLuong / (km.soLuongMua + km.soLuongTang)) * km.soLuongTang) * c.donGia " +
                    "ELSE c.soLuong * c.donGia END) " +
                    "FROM CTHoaDon c LEFT JOIN c.thuoc.khuyenMai km " +
                    "WHERE c.hoaDon.maHD = :maHD";
            Double sum = em.createQuery(jpql, Double.class)
                    .setParameter("maHD", maHD)
                    .getSingleResult();
            return sum != null ? sum : 0.0;
        });
    }

    @Override
    public List<Object[]> layChiTietHoaDon(String maHD) {
        return doInTransaction(em -> {
            String jpql = "SELECT c.hoaDon.maHD, c.thuoc.maThuoc, c.thuoc.tenThuoc, c.soLuong, c.donViTinh.maDVT, c.donViTinh.tenDVT, c.donGia, " +
                    "(CASE WHEN km.loaiKM = 'Giảm giá' THEN c.soLuong * c.donGia * (1.0 - km.mucKM / 100.0) " +
                    "WHEN km.loaiKM = 'Mua tặng' THEN (c.soLuong - FLOOR(c.soLuong / (km.soLuongMua + km.soLuongTang)) * km.soLuongTang) * c.donGia " +
                    "ELSE c.soLuong * c.donGia END) " +
                    "FROM CTHoaDon c LEFT JOIN c.thuoc.khuyenMai km " +
                    "WHERE c.hoaDon.maHD = :maHD";
            return em.createQuery(jpql, Object[].class)
                    .setParameter("maHD", maHD)
                    .getResultList();
        });
    }

    @Override
    public String layMaHoaDonMoiNhat() {
        return doInTransaction(em -> {
            String jpql = "SELECT h.maHD FROM HoaDon h ORDER BY CAST(SUBSTRING(h.maHD, 5, LENGTH(h.maHD)) AS Integer) DESC";
            List<String> result = em.createQuery(jpql, String.class).setMaxResults(1).getResultList();
            return result.isEmpty() ? null : result.get(0);
        });
    }
}