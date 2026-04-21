package repository.intf;

import entity.PhieuDoiTra;

import java.util.List;

public interface PhieuDoiTraRepository {
    boolean kiemTraHoaDonDaDoiTra(String maHD);
    List<PhieuDoiTra> layListPDT();
    boolean themPDT(PhieuDoiTra pdt);
    boolean themChiTietPDT(Object chiTietPDT); // Ép kiểu về Entity ChiTietPhieuDoiTra tương ứng
    String layMaPDTMoiNhat();
    List<Object[]> layDanhSachThuocTheoPhieuDT(String maPhieuDT);
    double tinhTongTienHoanTheoPhieuDT(String maPhieuDT);
    PhieuDoiTra timPhieuDoiTraTheoMa(String maPDT);
    int tongSoLuongDaDoiTra(String maHD, String maThuoc, String maDVT);
}
