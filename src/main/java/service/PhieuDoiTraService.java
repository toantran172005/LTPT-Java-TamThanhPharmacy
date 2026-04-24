package service;

import entity.PhieuDoiTra;
import repository.impl.PhieuDoiTraRepositoryImpl;
import repository.intf.PhieuDoiTraRepository;

import java.util.List;

public class PhieuDoiTraService {
    private final PhieuDoiTraRepository pdtRepo;

    public PhieuDoiTraService() {
        this.pdtRepo = new PhieuDoiTraRepositoryImpl();
    }

    public boolean kiemTraHoaDonDaDoiTra(String maHD) {
        if (maHD == null || maHD.trim().isEmpty()) return false;
        return pdtRepo.kiemTraHoaDonDaDoiTra(maHD);
    }

    public List<PhieuDoiTra> layListPDT() {
        return pdtRepo.layListPDT();
    }

    public boolean themPDT(PhieuDoiTra pdt) {
        if (pdt == null || pdt.getMaPhieuDT() == null || pdt.getMaPhieuDT().isEmpty()) {
            return false;
        }
        return pdtRepo.themPDT(pdt);
    }

    public boolean themChiTietPDT(Object chiTietPDT) {
        if (chiTietPDT == null) return false;
        return pdtRepo.themChiTietPDT(chiTietPDT);
    }

    public String layMaPDTMoiNhat() {
        return pdtRepo.layMaPDTMoiNhat();
    }

    public List<Object[]> layDanhSachThuocTheoPhieuDT(String maPhieuDT) {
        if (maPhieuDT == null || maPhieuDT.trim().isEmpty()) return null;
        return pdtRepo.layDanhSachThuocTheoPhieuDT(maPhieuDT);
    }

    public double tinhTongTienHoanTheoPhieuDT(String maPhieuDT) {
        if (maPhieuDT == null || maPhieuDT.trim().isEmpty()) return 0.0;
        return pdtRepo.tinhTongTienHoanTheoPhieuDT(maPhieuDT);
    }

    public PhieuDoiTra timPhieuDoiTraTheoMa(String maPhieuDT) {
        if (maPhieuDT == null || maPhieuDT.trim().isEmpty()) {
            return null;
        }
        return pdtRepo.timPhieuDoiTraTheoMa(maPhieuDT);
    }

    public int tongSoLuongDaDoiTra(String maHD, String maThuoc, String maDVT) {
        if (maHD == null || maThuoc == null || maDVT == null) return 0;
        return pdtRepo.tongSoLuongDaDoiTra(maHD, maThuoc, maDVT);
    }
}
