package service;

import entity.KhuyenMai;
import repository.impl.KhuyenMaiRepositoryImpl;
import repository.intf.KhuyenMaiRepository;

import java.util.List;

public class KhuyenMaiService {

    private final KhuyenMaiRepository repo;

    public KhuyenMaiService() {
        this.repo = new KhuyenMaiRepositoryImpl();
    }

    public KhuyenMai layKhuyenMaiTheoMa(String maKM) {
        if (maKM == null || maKM.trim().isEmpty()) return null;
        return repo.layKhuyenMaiTheoMa(maKM);
    }

    public void capNhatTrangThaiHetHan() {
        repo.capNhatTrangThaiHetHan();
    }

    public boolean capNhatKhuyenMai(KhuyenMai km, List<String> danhSachMaThuoc) {
        if (km == null || km.getMaKM() == null) return false;
        return repo.capNhatKhuyenMai(km, danhSachMaThuoc);
    }

    public List<KhuyenMai> layDanhSachKM() {
        return repo.layDanhSachKM();
    }

    public List<KhuyenMai> layDanhSachDaXoa() {
        return repo.layDanhSachDaXoa();
    }

    public boolean xoaKM(String maKM) {
        if (maKM == null || maKM.trim().isEmpty()) return false;
        return repo.xoaKM(maKM);
    }

    public boolean khoiPhucKM(String maKM) {
        if (maKM == null || maKM.trim().isEmpty()) return false;
        return repo.khoiPhucKM(maKM);
    }

    public boolean themThuocVaoChiTietKM(String maThuoc, String maKM) {
        if (maThuoc == null || maKM == null) return false;
        return repo.themThuocVaoChiTietKM(maThuoc, maKM);
    }

    public List<Object[]> layDanhSachChiTiet(String maKM) {
        if (maKM == null || maKM.trim().isEmpty()) return null;
        return repo.layDanhSachChiTiet(maKM);
    }

    public boolean themKM(KhuyenMai km, List<Object[]> listChon) {
        if (km == null) return false;
        return repo.themKM(km, listChon);
    }
}