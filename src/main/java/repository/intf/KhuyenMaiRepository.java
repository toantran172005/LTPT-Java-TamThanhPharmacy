package repository.intf;

import entity.KhuyenMai;
import java.util.List;

public interface KhuyenMaiRepository {
    KhuyenMai layKhuyenMaiTheoMa(String maKM);
    void capNhatTrangThaiHetHan();
    boolean capNhatKhuyenMai(KhuyenMai km, List<String> danhSachMaThuoc);
    List<KhuyenMai> layDanhSachKM();
    List<KhuyenMai> layDanhSachDaXoa();
    boolean xoaKM(String maKM);
    boolean khoiPhucKM(String maKM);
    boolean themThuocVaoChiTietKM(String maThuoc, String maKM);
    List<Object[]> layDanhSachChiTiet(String maKM);
    boolean themKM(KhuyenMai km, List<Object[]> listChon);
}