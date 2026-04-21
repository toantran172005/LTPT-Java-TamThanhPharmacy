package service;

import entity.DonViTinh;
import entity.QuocGia;
import entity.Thue;
import entity.Thuoc;
import repository.impl.ThuocRepositoryImpl;
import repository.intf.ThuocRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ThuocService {

    private final ThuocRepository thuocRepo = new ThuocRepositoryImpl();

    public boolean capNhatTrangThaiThuoc(String maThuoc, boolean isKhuyenPhuc) {
        if (maThuoc == null || maThuoc.trim().isEmpty()) {
            return false;
        }
        return thuocRepo.capNhatTrangThaiThuoc(maThuoc.trim(), isKhuyenPhuc);
    }

    public List<Thuoc> layListThuocHoanChinh() {
        return thuocRepo.layListThuocHoanChinh();
    }

    public ArrayList<Thuoc> layListThuoc() {
        // 1. Kéo dữ liệu từ DB lên
        List<Thuoc> listDb = thuocRepo.layListThuoc();

        if (listDb == null || listDb.isEmpty()) {
            return new ArrayList<>();
        }

        // 2. Tái tạo logic: ORDER BY TRY_CAST(REPLACE(t.maThuoc, 'TTTH', '') AS INT)
        listDb.sort((t1, t2) -> {
            try {
                // Cắt chữ 'TTTH' và biến phần còn lại thành số nguyên để so sánh
                int num1 = Integer.parseInt(t1.getMaThuoc().replace("TTTH", ""));
                int num2 = Integer.parseInt(t2.getMaThuoc().replace("TTTH", ""));
                return Integer.compare(num1, num2);
            } catch (Exception e) {
                return 0; // Giữ nguyên nếu mã thuốc không đúng định dạng
            }
        });

        // 3. Ép kiểu về ArrayList để Controller đưa lên DefaultTableModel
        return new ArrayList<>(listDb);
    }

    public List<Object[]> thongKeThuocBanChay(LocalDate bd, LocalDate kt) {
        if (bd.isAfter(kt)) {
            throw new IllegalArgumentException("Ngày bắt đầu không được lớn hơn ngày kết thúc.");
        }
        return thuocRepo.layListTHThongKe(bd, kt);
    }

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

    public Thuoc timThuocTheoMa(String maThuoc) {
        if (maThuoc == null || maThuoc.trim().isEmpty()) return null;
        return thuocRepo.timThuocTheoMa(maThuoc);
    }

    public List<QuocGia> layListQG() {
        return thuocRepo.layListQG();
    }

    public String timTenQGTheoMaThuoc(String maThuoc) {
        if (maThuoc == null || maThuoc.trim().isEmpty()) {
            return null;
        }
        return thuocRepo.timTenQGTheoMaThuoc(maThuoc.trim());
    }

    public String layMaKMTheoMaThuoc(String maThuoc) {
        if (maThuoc == null || maThuoc.trim().isEmpty()) {
            return null;
        }
        return thuocRepo.layMaKMTheoMaThuoc(maThuoc.trim());
    }

    public String layMaThuocTheoTen(String tenThuoc) {
        if (tenThuoc == null || tenThuoc.trim().isEmpty()) return null;
        return thuocRepo.layMaThuocTheoTen(tenThuoc.trim());
    }

    public ArrayList<QuocGia> layListQuocGiaTheoThuoc(String tenThuoc) {
        if (tenThuoc == null || tenThuoc.trim().isEmpty()) {
            return new ArrayList<>();
        }
        List<QuocGia> list = thuocRepo.layListQuocGiaTheoThuoc(tenThuoc.trim());
        // Ép kiểu (Cast) sang ArrayList để bảo vệ cấu trúc UI cũ
        return list != null ? new ArrayList<>(list) : new ArrayList<>();
    }

    public String layTenDonViTinhTheoMaThuoc(String maThuoc) {
        if (maThuoc == null || maThuoc.trim().isEmpty()) return null;
        return thuocRepo.layTenDonViTinhTheoMaThuoc(maThuoc.trim());
    }

    public String layMaThuocTheoTenVaQG(String tenThuoc, String tenQG) {
        if (tenThuoc == null || tenThuoc.trim().isEmpty() ||
                tenQG == null || tenQG.trim().isEmpty()) {
            return null;
        }
        return thuocRepo.layMaThuocTheoTenVaQG(tenThuoc.trim(), tenQG.trim());
    }

    public int laySoLuongTon(String maThuoc) {
        if (maThuoc == null || maThuoc.trim().isEmpty()) return 0;
        return thuocRepo.laySoLuongTon(maThuoc.trim());
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
    public List<Thuoc> layListThuoc(boolean trangThai) {
        return thuocRepo.layListThuoc(trangThai);
    }

    public List<Object[]> layDanhSachThuocChoKM() {
        return thuocRepo.layDanhSachThuocChoKM();
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

    public QuocGia layQuocGiaTheoMa(String maQG) {
        if (maQG == null || maQG.trim().isEmpty()) return null;
        return thuocRepo.layQuocGiaTheoMa(maQG);
    }

    public Number layGiaBanTheoDVT(String maThuoc, String maDVT) {
        if (maThuoc == null || maThuoc.trim().isEmpty() || maDVT == null || maDVT.trim().isEmpty()) {
            return 0;
        }
        return thuocRepo.layGiaBanTheoDVT(maThuoc, maDVT);
    }

    public double layDonGiaTheoMaThuoc(String maThuoc) {
        if (maThuoc == null || maThuoc.trim().isEmpty()) return 0.0;
        return thuocRepo.layDonGiaTheoMaThuoc(maThuoc);
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