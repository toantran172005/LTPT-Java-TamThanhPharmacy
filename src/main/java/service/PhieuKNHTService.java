package service;


import entity.PhieuKhieuNaiHoTroKH;
import repository.impl.PhieuKNHTRepositoryImpl;
import repository.intf.PhieuKNHTRepository;

import java.util.List;

public class PhieuKNHTService {
    private PhieuKNHTRepository repo = new PhieuKNHTRepositoryImpl();

    public List<PhieuKhieuNaiHoTroKH> getAll() {
        return repo.layTatCaPhieu();
    }

    public boolean create(PhieuKhieuNaiHoTroKH phieu) {
        if (phieu == null || phieu.getMaPhieu().isEmpty()) return false;
        return repo.themPhieu(phieu);
    }

    public boolean update(PhieuKhieuNaiHoTroKH phieu) {
        if (phieu == null || phieu.getMaPhieu().isEmpty()) return false;
        return repo.capNhatPhieu(phieu);
    }

    public boolean updateStatus(String maPhieu, String status) {
        if (maPhieu == null || maPhieu.isEmpty()) return false;
        return repo.doiTrangThaiPhieu(maPhieu, status);
    }
}