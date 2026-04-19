package repository.intf;

import entity.KhachHang;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface KhachHangRepository {
    KhachHang timKhachHangTheoSDT(String sdt);
    KhachHang timKhachHangTheoMa(String maKH);
    boolean themKhachHang(KhachHang kh);
    boolean capNhatKhachHang(KhachHang kh);
    boolean xoaKhachHang(String maKH);
    boolean khoiPhucKhachHang(String maKH);
    List<KhachHang> layListKhachHang();
    List<KhachHang> layListKHThongKe(LocalDate ngayBD, LocalDate ngayKT);
    Map<String, Integer> layTongDonHangTheoNgay(LocalDate ngayBD, LocalDate ngayKT);
    Map<String, Double> layTongTienTheoNgay(LocalDate ngayBD, LocalDate ngayKT);
    Map<String, Integer> layTatCaTongDonHang();
    Map<String, Double> layTatCaTongTien();
}