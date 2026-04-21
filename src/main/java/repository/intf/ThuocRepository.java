package repository.intf;

import entity.DonViTinh;
import entity.QuocGia;
import entity.Thue;
import entity.Thuoc;

import java.time.LocalDate;
import java.util.List;

public interface ThuocRepository {
    boolean kiemTraTrungTenVaQuocGia(String tenThuoc, String maQG);
    String layMaQuocGiaTheoTen(String tenQG);
    String layMaThuocTheoTenVaQG(String tenThuoc, String tenQG);
    QuocGia layQuocGiaTheoMa(String maQG);
    String layTenDonViTinhTheoMaThuoc(String maThuoc);
    String layMaThuocTheoTen(String tenThuoc);
    boolean capNhatSoLuongTon(String maThuoc, String maDVT, int soLuong, boolean isTang);
    Number layGiaBanTheoDVT(String maThuoc, String maDVT);
    String layHoacTaoThue(Thue thue);
    String layHoacTaoDVT(DonViTinh dvt);
    boolean themThuoc(Thuoc thuoc);
    boolean luuData(String maPNT, String maNCC, String maNV, LocalDate ngayNhap, List<Thuoc> listThuoc);
    List<Object[]> layDanhSachThuocChoKM();
    List<QuocGia> layListQuocGiaTheoThuoc(String tenThuoc);
    String timMaQGTheoTen(String tenQG);
    String timTenQGTheoMaThuoc(String maThuoc);
    List<Thuoc> layListThuocHoanChinh();
    List<Thuoc> layListThuoc();
    Thuoc timThuocTheoMa(String maThuoc);
    String layMaKMTheoMaThuoc(String maThuoc);
    double layDonGiaTheoMaThuoc(String maThuoc);
    boolean capNhatTrangThaiThuoc(String maThuoc, boolean trangThai); // Dùng cho cả xoá và khôi phục
    boolean capNhatThuoc(Thuoc thuoc);
    List<Object[]> layListTHThongKe(LocalDate ngayBD, LocalDate ngayKT);
    Thuoc layThuocDeDat(String maThuoc);
    int laySoLuongTon(String maThuoc);
    List<QuocGia> layListQG();

    //NewInstance (21/04)
    List<Thuoc> layListThuoc(boolean isTrangThai);
    boolean giamSoLuongTon(String maThuoc, String maDVT, int soLuongBan);

}