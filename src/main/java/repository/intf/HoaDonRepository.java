package repository.intf;

import entity.CTHoaDon;
import entity.HoaDon;
import entity.KhachHang;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface HoaDonRepository {
    List<HoaDon> layListHoaDon();
    List<HoaDon> layListHDDaXoa();
    Map<LocalDate, Double> layDoanhThuTheoNgay(LocalDate ngayBD, LocalDate ngayKT);
    List<KhachHang> layListKHThongKe(LocalDate ngayBD, LocalDate ngayKT);
    HoaDon timHoaDonTheoMa(String maHD);

    boolean themHoaDon(HoaDon hd);
    // Gom các tham số (maHD, maThuoc, soLuong, maDVT, donGia) thành Entity CT_HoaDon
    boolean themChiTietHoaDon(CTHoaDon cthd);

    boolean xoaHD(String maHD);
    boolean khoiPhucHD(String maHD);

    int layTongDonHang(String maKH);
    double layTongTien(String maKH);
    double layTongTienTheoSanPham(String maHD, String maSP);
    double tinhTongTienTheoHoaDon(String maHD);
    List<Object[]> layChiTietHoaDon(String maHD);
    String layMaHoaDonMoiNhat();
}