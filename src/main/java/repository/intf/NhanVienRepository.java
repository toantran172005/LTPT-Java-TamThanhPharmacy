package repository.intf;

import entity.NhanVien;
import entity.TaiKhoan;
import java.util.List;

public interface NhanVienRepository {
    NhanVien timNhanVienTheoMa(String maNV);
    List<NhanVien> layListNhanVien();
    List<NhanVien> layNhanVienDangLam();
    List<NhanVien> layNhanVienNghiLam();
    boolean xoaNhanVien(String maNV);
    boolean khoiPhucNhanVien(String maNV);
    boolean capNhatNhanVien(NhanVien nv);
    boolean themNhanVien(NhanVien nv);
    boolean themTaiKhoan(TaiKhoan tk);
    String layEmailNV(String maNV);
    String layAnhNV(String maNV);
}