package service;

import entity.DonViTinh;
import repository.intf.DonViTinhRepository;
import repository.impl.DonViTinhRepositoryImpl;

import java.util.List;

public class DonViTinhService {

    private final DonViTinhRepository donViTinhRepo = new DonViTinhRepositoryImpl();

    // Tìm mã đơn vị tính theo tên (Trả về String giống DAO cũ)
    public String timMaDVTTheoTen(String tenDVT) {
        if (tenDVT == null || tenDVT.trim().isEmpty()) {
            return null;
        }
        DonViTinh dvt = donViTinhRepo.timTheoTen(tenDVT.trim());
        return (dvt != null) ? dvt.getMaDVT() : null;
    }

    // Lấy danh sách đơn vị đang hoạt động (trangThai = true)
    public List<DonViTinh> layListDVT() {
        return donViTinhRepo.layDanhSachTheoTrangThai(true);
    }

    // Lấy danh sách đơn vị đã xóa (trangThai = false)
    public List<DonViTinh> layDanhSachDaXoa() {
        return donViTinhRepo.layDanhSachTheoTrangThai(false);
    }

    // Xóa đơn vị tính (chuyển trạng thái sang false)
    public boolean xoaDVT(String maDVT) {
        if (maDVT == null || maDVT.trim().isEmpty()) {
            return false;
        }
        return donViTinhRepo.capNhatTrangThai(maDVT.trim(), false);
    }

    // Khôi phục đơn vị tính (chuyển trạng thái sang true)
    public boolean khoiPhucDVT(String maDVT) {
        if (maDVT == null || maDVT.trim().isEmpty()) {
            return false;
        }
        return donViTinhRepo.capNhatTrangThai(maDVT.trim(), true);
    }

    // Thêm đơn vị tính mới
    public boolean themDVT(DonViTinh dvt) {
        if (dvt == null || dvt.getMaDVT() == null || dvt.getTenDVT() == null || dvt.getTenDVT().trim().isEmpty()) {
            return false;
        }
        return donViTinhRepo.themDVT(dvt);
    }

    // Tìm đơn vị tính theo tên (Trả về Object DonViTinh)
    public DonViTinh timTheoTen(String tenDVT) {
        if (tenDVT == null || tenDVT.trim().isEmpty()) {
            return null;
        }
        return donViTinhRepo.timTheoTen(tenDVT.trim());
    }
}