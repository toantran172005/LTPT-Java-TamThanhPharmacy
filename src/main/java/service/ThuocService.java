package service;

import entity.DonViTinh;
import entity.QuocGia;
import entity.Thuoc;
import entity.Thue;
import repository.impl.ThuocRepositoryImpl;
import repository.intf.ThuocRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ThuocService {

    private final ThuocRepository thuocRepo = new ThuocRepositoryImpl();

    // ================== CÁC HÀM CẬP NHẬT / THÊM MỚI ==================

    public boolean themThuoc(Thuoc thuoc) {
        if (thuoc == null || thuoc.getTenThuoc() == null || thuoc.getTenThuoc().trim().isEmpty()) {
            return false;
        }
        return thuocRepo.themThuoc(thuoc);
    }

    public boolean capNhatThuoc(Thuoc thuoc) {
        if (thuoc == null || thuoc.getMaThuoc() == null) {
            return false;
        }
        return thuocRepo.capNhatThuoc(thuoc);
    }

    public boolean capNhatTrangThaiThuoc(String maThuoc, boolean isKhuyenPhuc) {
        if (maThuoc == null || maThuoc.trim().isEmpty()) {
            return false;
        }
        return thuocRepo.capNhatTrangThaiThuoc(maThuoc.trim(), isKhuyenPhuc);
    }

    public boolean capNhatSoLuongTon(String maThuoc, String maDVT, int soLuong, boolean isTang) {
        if (maThuoc == null || maThuoc.trim().isEmpty() || maDVT == null || maDVT.trim().isEmpty()) {
            return false;
        }
        if (soLuong < 0) return false; // Số lượng cập nhật không được âm
        return thuocRepo.capNhatSoLuongTon(maThuoc, maDVT, soLuong, isTang);
    }

    public boolean luuData(String maPNT, String maNCC, String maNV, LocalDate ngayNhap, List<Thuoc> listThuoc) {
        if (listThuoc == null || listThuoc.isEmpty()) return false;
        return thuocRepo.luuData(maPNT, maNCC, maNV, ngayNhap, listThuoc);
    }

    public String layHoacTaoThue(Thue thue) {
        if (thue == null || thue.getLoaiThue() == null) return null;
        return thuocRepo.layHoacTaoThue(thue);
    }

    public String layHoacTaoDVT(DonViTinh dvt) {
        if (dvt == null || dvt.getTenDVT() == null || dvt.getTenDVT().trim().isEmpty()) return null;
        return thuocRepo.layHoacTaoDVT(dvt);
    }

    // ================== CÁC HÀM LẤY DANH SÁCH ==================

    public List<Thuoc> layListThuocHoanChinh() {
        return thuocRepo.layListThuocHoanChinh();
    }

    public List<Thuoc> layListThuoc(boolean trangThai) {
        return thuocRepo.layListThuoc(trangThai);
    }

    public List<QuocGia> layListQG() {
        return thuocRepo.layListQG();
    }

    public List<Object[]> layDanhSachThuocChoKM() {
        return thuocRepo.layDanhSachThuocChoKM();
    }

    public List<QuocGia> layListQuocGiaTheoThuoc(String tenThuoc) {
        if (tenThuoc == null || tenThuoc.trim().isEmpty()) return new ArrayList<>();
        return thuocRepo.layListQuocGiaTheoThuoc(tenThuoc);
    }

    public List<Object[]> thongKeThuocBanChay(LocalDate bd, LocalDate kt) {
        if (bd == null || kt == null) throw new IllegalArgumentException("Ngày không được để trống.");
        if (bd.isAfter(kt)) {
            throw new IllegalArgumentException("Ngày bắt đầu không được lớn hơn ngày kết thúc.");
        }
        return thuocRepo.layListTHThongKe(bd, kt);
    }

    // ================== CÁC HÀM TÌM KIẾM / LẤY THÔNG TIN ĐƠN LẺ ==================

    public Thuoc timThuocTheoMa(String maThuoc) {
        if (maThuoc == null || maThuoc.trim().isEmpty()) return null;
        return thuocRepo.timThuocTheoMa(maThuoc);
    }

    public Thuoc layThuocDeDat(String maThuoc) {
        if (maThuoc == null || maThuoc.trim().isEmpty()) return null;
        return thuocRepo.layThuocDeDat(maThuoc);
    }

    public boolean kiemTraTrungTenVaQuocGia(String tenThuoc, String maQG) {
        if (tenThuoc == null || tenThuoc.trim().isEmpty() || maQG == null || maQG.trim().isEmpty()) {
            return false;
        }
        return thuocRepo.kiemTraTrungTenVaQuocGia(tenThuoc, maQG);
    }

    public String layMaQuocGiaTheoTen(String tenQG) {
        if (tenQG == null || tenQG.trim().isEmpty()) return null;
        return thuocRepo.layMaQuocGiaTheoTen(tenQG);
    }

    public String timMaQGTheoTen(String tenQG) {
        if (tenQG == null || tenQG.trim().isEmpty()) return null;
        return thuocRepo.timMaQGTheoTen(tenQG);
    }

    public String layMaThuocTheoTenVaQG(String tenThuoc, String tenQG) {
        if (tenThuoc == null || tenThuoc.trim().isEmpty() || tenQG == null || tenQG.trim().isEmpty()) {
            return null;
        }
        return thuocRepo.layMaThuocTheoTenVaQG(tenThuoc, tenQG);
    }

    public QuocGia layQuocGiaTheoMa(String maQG) {
        if (maQG == null || maQG.trim().isEmpty()) return null;
        return thuocRepo.layQuocGiaTheoMa(maQG);
    }

    public String layTenDonViTinhTheoMaThuoc(String maThuoc) {
        if (maThuoc == null || maThuoc.trim().isEmpty()) return null;
        return thuocRepo.layTenDonViTinhTheoMaThuoc(maThuoc);
    }

    public String layMaThuocTheoTen(String tenThuoc) {
        if (tenThuoc == null || tenThuoc.trim().isEmpty()) return null;
        return thuocRepo.layMaThuocTheoTen(tenThuoc);
    }

    public Number layGiaBanTheoDVT(String maThuoc, String maDVT) {
        if (maThuoc == null || maThuoc.trim().isEmpty() || maDVT == null || maDVT.trim().isEmpty()) {
            return 0;
        }
        return thuocRepo.layGiaBanTheoDVT(maThuoc, maDVT);
    }

    public String timTenQGTheoMaThuoc(String maThuoc) {
        if (maThuoc == null || maThuoc.trim().isEmpty()) return null;
        return thuocRepo.timTenQGTheoMaThuoc(maThuoc);
    }

    public String layMaKMTheoMaThuoc(String maThuoc) {
        if (maThuoc == null || maThuoc.trim().isEmpty()) return null;
        return thuocRepo.layMaKMTheoMaThuoc(maThuoc);
    }

    public double layDonGiaTheoMaThuoc(String maThuoc) {
        if (maThuoc == null || maThuoc.trim().isEmpty()) return 0.0;
        return thuocRepo.layDonGiaTheoMaThuoc(maThuoc);
    }

    public int laySoLuongTon(String maThuoc) {
        if (maThuoc == null || maThuoc.trim().isEmpty()) return 0;
        return thuocRepo.laySoLuongTon(maThuoc);
    }

    public boolean giamSoLuongTon(String maThuoc, String maDVT, int soLuongBan) {
        if (maThuoc == null || maThuoc.isBlank()) {
            throw new IllegalArgumentException("Mã thuốc không hợp lệ");
        }
        if (maDVT == null || maDVT.isBlank()) {
            throw new IllegalArgumentException("Mã đơn vị tính không hợp lệ");
        }
        if (soLuongBan <= 0) {
            throw new IllegalArgumentException("Số lượng bán phải > 0");
        }

        boolean result = thuocRepo.giamSoLuongTon(maThuoc, maDVT, soLuongBan);
        if (!result) {
            throw new RuntimeException("Không thể giảm số lượng tồn");
        }
        return true;
    }
}