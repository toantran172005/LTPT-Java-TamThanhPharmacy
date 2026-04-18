package service;

import entity.KeThuoc;
import entity.Thuoc;
import repository.impl.KeThuocRepositoryImpl;
import repository.intf.KeThuocRepository;

import java.util.List;

public class KeThuocService {

    private final KeThuocRepository keThuocRepo = new KeThuocRepositoryImpl();

    public List<KeThuoc> layListKeThuoc() {
        return keThuocRepo.layListKeThuoc();
    }

    public List<String> layTatCaTenKe() {
        return keThuocRepo.layTatCaTenKe();
    }

    public List<Thuoc> layListThuocTrongKe(String maKe) {
        if (maKe == null || maKe.trim().isEmpty()) {
            return null;
        }
        return keThuocRepo.layListThuocTrongKe(maKe);
    }

    public boolean xoaKeThuoc(String maKe) {
        if (maKe == null || maKe.trim().isEmpty()) {
            return false;
        }
        return keThuocRepo.xoaKeThuoc(maKe);
    }

    public boolean khoiPhucKeThuoc(String maKe) {
        if (maKe == null || maKe.trim().isEmpty()) {
            return false;
        }
        return keThuocRepo.khoiPhucKeThuoc(maKe);
    }

    public boolean capNhatKeThuoc(KeThuoc kt) {
        if (kt == null || kt.getMaKe() == null || kt.getMaKe().trim().isEmpty()) {
            return false;
        }
        return keThuocRepo.capNhatKeThuoc(kt);
    }
}