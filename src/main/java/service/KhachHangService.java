package service;

import entity.KhachHang;
import repository.impl.KhachHangRepositoryImpl;
import repository.intf.KhachHangRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KhachHangService {

    private final KhachHangRepository khachHangRepo;

    public KhachHangService() {
        this.khachHangRepo = new KhachHangRepositoryImpl();
    }

    // ================== CÁC HÀM THÊM / CẬP NHẬT / XÓA ==================

    public boolean themKhachHang(KhachHang kh) {
        if (kh == null || kh.getMaKH() == null || kh.getMaKH().trim().isEmpty() ||
                kh.getTenKH() == null || kh.getTenKH().trim().isEmpty()) {
            return false;
        }
        if (kh.getTuoi() <= 0) {
            return false;
        }
        kh.setTrangThai(true); // Đảm bảo trạng thái mặc định là true (hoạt động)
        return khachHangRepo.themKhachHang(kh);
    }

    // Support hàm thêm khách hàng mới với tham số String truyền từ giao diện
    public boolean themKhachHang(String maKH, String tenKH, String sdt, String tuoiStr) {
        if (tuoiStr == null || tuoiStr.trim().isEmpty()) return false;
        try {
            int tuoi = Integer.parseInt(tuoiStr.trim());
            KhachHang kh = new KhachHang(maKH, tenKH, sdt, tuoi, true);
            return themKhachHang(kh);
        } catch (NumberFormatException e) {
            return false; // Tránh lỗi Exception lan lên View
        }
    }

    public boolean capNhatKhachHang(KhachHang kh) {
        if (kh == null || kh.getMaKH() == null || kh.getMaKH().trim().isEmpty()) {
            return false;
        }
        if (kh.getTuoi() <= 0) {
            return false;
        }
        return khachHangRepo.capNhatKhachHang(kh);
    }

    public boolean xoaKhachHang(String maKH) {
        if (maKH == null || maKH.trim().isEmpty()) return false;
        return khachHangRepo.xoaKhachHang(maKH.trim());
    }

    public boolean khoiPhucKhachHang(String maKH) {
        if (maKH == null || maKH.trim().isEmpty()) return false;
        return khachHangRepo.khoiPhucKhachHang(maKH.trim());
    }

    // ================== CÁC HÀM LẤY DANH SÁCH & THỐNG KÊ ==================

    public List<KhachHang> layListKhachHang() {
        return khachHangRepo.layListKhachHang();
    }

    public List<KhachHang> layListKHThongKe(LocalDate ngayBD, LocalDate ngayKT) {
        if (ngayBD == null || ngayKT == null) throw new IllegalArgumentException("Ngày không được để trống.");
        if (ngayBD.isAfter(ngayKT)) throw new IllegalArgumentException("Ngày bắt đầu không được lớn hơn ngày kết thúc.");

        return khachHangRepo.layListKHThongKe(ngayBD, ngayKT);
    }

    public Map<String, Integer> layTongDonHangTheoNgay(LocalDate ngayBD, LocalDate ngayKT) {
        if (ngayBD == null || ngayKT == null) return new HashMap<>();
        if (ngayBD.isAfter(ngayKT)) return new HashMap<>();

        return khachHangRepo.layTongDonHangTheoNgay(ngayBD, ngayKT);
    }

    public Map<String, Double> layTongTienTheoNgay(LocalDate ngayBD, LocalDate ngayKT) {
        if (ngayBD == null || ngayKT == null) return new HashMap<>();
        if (ngayBD.isAfter(ngayKT)) return new HashMap<>();

        return khachHangRepo.layTongTienTheoNgay(ngayBD, ngayKT);
    }

    public Map<String, Integer> layTatCaTongDonHang() {
        return khachHangRepo.layTatCaTongDonHang();
    }

    public Map<String, Double> layTatCaTongTien() {
        return khachHangRepo.layTatCaTongTien();
    }

    // ================== CÁC HÀM TÌM KIẾM ĐƠN LẺ ==================

    public KhachHang timKhachHangTheoSDT(String sdt) {
        if (sdt == null || sdt.trim().isEmpty()) return null;
        return khachHangRepo.timKhachHangTheoSDT(sdt.trim());
    }

    public KhachHang timKhachHangTheoMa(String maKH) {
        if (maKH == null || maKH.trim().isEmpty()) return null;
        return khachHangRepo.timKhachHangTheoMa(maKH.trim());
    }
}