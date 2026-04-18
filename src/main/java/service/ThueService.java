package service;

import entity.Thue;
import repository.impl.ThueRepositoryImpl;
import repository.intf.ThueRepository;

import java.util.ArrayList;
import java.util.List;

public class ThueService {

    private final ThueRepository thueRepo;

    public ThueService() {
        this.thueRepo = new ThueRepositoryImpl();
    }

    public ArrayList<Thue> layListThue() {
        List<Thue> list = thueRepo.layListThue();
        return list != null ? new ArrayList<>(list) : new ArrayList<>();
    }

    public boolean themThue(Thue t) {
        if (t == null || t.getMaThue() == null || t.getMaThue().trim().isEmpty()) return false;
        // Bổ sung set trạng thái mặc định nếu cần
        t.setTrangThai(true);
        return thueRepo.themThue(t);
    }

    public boolean capNhatThue(Thue t) {
        if (t == null || t.getMaThue() == null || t.getMaThue().trim().isEmpty()) return false;
        return thueRepo.capNhatThue(t);
    }

    public boolean xoaThue(String maThue) {
        if (maThue == null || maThue.trim().isEmpty()) return false;
        return thueRepo.xoaThue(maThue.trim());
    }

    public ArrayList<Thue> timKiem(String tuKhoa) {
        if (tuKhoa == null) tuKhoa = "";
        List<Thue> list = thueRepo.timKiem(tuKhoa.trim());
        return list != null ? new ArrayList<>(list) : new ArrayList<>();
    }
}