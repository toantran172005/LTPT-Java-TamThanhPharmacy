package service;

import entity.NhanVien;
import entity.TaiKhoan;

import repository.impl.NhanVienRepositoryImpl;
import repository.intf.NhanVienRepository;
import java.util.ArrayList;
import java.util.List;

public class NhanVienService {

    private final NhanVienRepository nhanVienRepo;

    public NhanVienService() {
        this.nhanVienRepo = new NhanVienRepositoryImpl();
    }

    public NhanVien timNhanVienTheoMa(String maNV) {
        if (maNV == null || maNV.trim().isEmpty()) return null;
        return nhanVienRepo.timNhanVienTheoMa(maNV.trim());
    }

    public ArrayList<NhanVien> layListNhanVien() {
        List<NhanVien> list = nhanVienRepo.layListNhanVien();
        return list != null ? new ArrayList<>(list) : new ArrayList<>();
    }

    public ArrayList<NhanVien> layNhanVienDangLam() {
        List<NhanVien> list = nhanVienRepo.layNhanVienDangLam();
        return list != null ? new ArrayList<>(list) : new ArrayList<>();
    }

    public ArrayList<NhanVien> layNhanVienNghiLam() {
        List<NhanVien> list = nhanVienRepo.layNhanVienNghiLam();
        return list != null ? new ArrayList<>(list) : new ArrayList<>();
    }

    public boolean xoaNhanVien(String maNV) {
        if (maNV == null || maNV.trim().isEmpty()) return false;
        return nhanVienRepo.xoaNhanVien(maNV.trim());
    }

    public boolean khoiPhucNhanVien(String maNV) {
        if (maNV == null || maNV.trim().isEmpty()) return false;
        return nhanVienRepo.khoiPhucNhanVien(maNV.trim());
    }

    public boolean capNhatNhanVien(NhanVien nv) {
        if (nv == null || nv.getMaNV() == null || nv.getMaNV().trim().isEmpty()) return false;
        return nhanVienRepo.capNhatNhanVien(nv);
    }

    public boolean themNhanVien(NhanVien nv) {
        if (nv == null || nv.getMaNV() == null || nv.getMaNV().trim().isEmpty()) return false;
        return nhanVienRepo.themNhanVien(nv);
    }

    public boolean themTaiKhoan(TaiKhoan tk) {
        if (tk == null || tk.getMaTK() == null || tk.getMaTK().trim().isEmpty()) return false;
        return nhanVienRepo.themTaiKhoan(tk);
    }

    public String layEmailNV(String maNV) {
        if (maNV == null || maNV.trim().isEmpty()) return "";
        return nhanVienRepo.layEmailNV(maNV.trim());
    }

    public String layAnhNV(String maNV) {
        if (maNV == null || maNV.trim().isEmpty()) return null;
        return nhanVienRepo.layAnhNV(maNV.trim());
    }
}