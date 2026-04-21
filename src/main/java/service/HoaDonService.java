package service;

import entity.CTHoaDon;
import entity.HoaDon;
import entity.KhachHang;
import repository.intf.HoaDonRepository;
import repository.impl.HoaDonRepositoryImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class HoaDonService {
    private HoaDonRepository hdRepo;

    public HoaDonService() {
        this.hdRepo = new HoaDonRepositoryImpl();
    }

    public List<HoaDon> layListHoaDon() {
        return hdRepo.layListHoaDon();
    }

    public List<HoaDon> layListHDDaXoa() {
        return hdRepo.layListHDDaXoa();
    }

    public Map<LocalDate, Double> layDoanhThuTheoNgay(LocalDate ngayBD, LocalDate ngayKT) {
        if(ngayBD == null || ngayKT == null) throw new IllegalArgumentException("Ngày không hợp lệ!");
        return hdRepo.layDoanhThuTheoNgay(ngayBD, ngayKT);
    }

    public List<KhachHang> layListKHThongKe(LocalDate ngayBD, LocalDate ngayKT) {
        return hdRepo.layListKHThongKe(ngayBD, ngayKT);
    }

    public HoaDon timHoaDonTheoMa(String maHD) {
        if (maHD == null || maHD.trim().isEmpty()) return null;
        return hdRepo.timHoaDonTheoMa(maHD);
    }

    public boolean themHoaDon(HoaDon hd) {
        if (hd == null || hd.getMaHD() == null) return false;
        return hdRepo.themHoaDon(hd);
    }

    public boolean themChiTietHoaDon(CTHoaDon cthd) {
        if(cthd == null || cthd.getHoaDon() == null || cthd.getThuoc() == null) return false;
        if(cthd.getSoLuong() <= 0) return false;
        return hdRepo.themChiTietHoaDon(cthd);
    }

    public boolean xoaHD(String maHD) {
        if (maHD == null || maHD.trim().isEmpty()) return false;
        return hdRepo.xoaHD(maHD);
    }

    public boolean khoiPhucHD(String maHD) {
        if (maHD == null || maHD.trim().isEmpty()) return false;
        return hdRepo.khoiPhucHD(maHD);
    }

    public int layTongDonHang(String maKH) {
        if (maKH == null || maKH.isEmpty()) return 0;
        return hdRepo.layTongDonHang(maKH);
    }

    public double layTongTien(String maKH) {
        if (maKH == null || maKH.isEmpty()) return 0;
        return hdRepo.layTongTien(maKH);
    }

    public double layTongTienTheoSanPham(String maHD, String maSP) {
        return hdRepo.layTongTienTheoSanPham(maHD, maSP);
    }

    public double tinhTongTienTheoHoaDon(String maHD) {
        if (maHD == null || maHD.trim().isEmpty()) return 0.0;
        return hdRepo.tinhTongTienTheoHoaDon(maHD);
    }

    public List<Object[]> layChiTietHoaDon(String maHD) {
        return hdRepo.layChiTietHoaDon(maHD);
    }

    public String layMaHoaDonMoiNhat() {
        return hdRepo.layMaHoaDonMoiNhat();
    }
}