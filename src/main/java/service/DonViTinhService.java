package service;

import entity.DonViTinh;
import repository.impl.DonViTinhRepositoryImpl;
import repository.intf.DonViTinhRepository;

import java.util.ArrayList;
import java.util.List;

public class DonViTinhService {
    private final DonViTinhRepository donViTinhRepo = new DonViTinhRepositoryImpl();

    public ArrayList<DonViTinh> layDanhSachTheoTrangThai(boolean trangThai) {
        List<DonViTinh> list = donViTinhRepo.layDanhSachTheoTrangThai(trangThai);
        ArrayList<DonViTinh> arrayList = new ArrayList<>(list);

        // Chuyển logic ORDER BY TRY_CAST(REPLACE(maDVT, 'TTDVT', '') AS int) từ SQL lên RAM
        arrayList.sort((d1, d2) -> {
            try {
                int num1 = Integer.parseInt(d1.getMaDVT().replace("TTDVT", ""));
                int num2 = Integer.parseInt(d2.getMaDVT().replace("TTDVT", ""));
                return Integer.compare(num1, num2);
            } catch (NumberFormatException e) {
                return d1.getMaDVT().compareTo(d2.getMaDVT());
            }
        });

        return arrayList;
    }

    public String timMaDVTTheoTen(String tenDVT) {
        if (tenDVT == null || tenDVT.trim().isEmpty()) return null;
        DonViTinh dvt = donViTinhRepo.timTheoTen(tenDVT.trim());
        return dvt != null ? dvt.getMaDVT() : null;
    }

    public DonViTinh timTheoTen(String tenDVT) {
        if (tenDVT == null || tenDVT.trim().isEmpty()) return null;
        return donViTinhRepo.timTheoTen(tenDVT.trim());
    }

    //NewEdit (21/04) (Edit function name)
    public boolean themDVT(DonViTinh dvt) {
        if (dvt == null || dvt.getTenDVT() == null || dvt.getTenDVT().trim().isEmpty()) {
            return false;
        }
        return donViTinhRepo.themDVT(dvt);
    }

//    // Tách riêng 2 hàm Xóa và Khôi phục để Controller dễ gọi
//    public boolean xoaDonViTinh(String maDVT) {
//        if (maDVT == null || maDVT.trim().isEmpty()) return false;
//        return donViTinhRepo.capNhatTrangThai(maDVT, false);
//    }

//    public boolean khoiPhucDonViTinh(String maDVT) {
//        if (maDVT == null || maDVT.trim().isEmpty()) return false;
//        return donViTinhRepo.capNhatTrangThai(maDVT, true);
//    }

    public ArrayList<DonViTinh> layListDVT() {
        // 1. Lấy dữ liệu từ Repository
        List<DonViTinh> list = donViTinhRepo.layListDVT();
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }

        // 2. Xử lý logic sắp xếp trực tiếp trên RAM bằng Java (thay thế TRY_CAST và REPLACE của SQL)
        list.sort((d1, d2) -> {
            try {
                // Cắt bỏ chữ 'TTDVT' và chuyển phần còn lại thành số nguyên
                int num1 = Integer.parseInt(d1.getMaDVT().replace("TTDVT", ""));
                int num2 = Integer.parseInt(d2.getMaDVT().replace("TTDVT", ""));

                // So sánh 2 số nguyên
                return Integer.compare(num1, num2);
            } catch (Exception e) {
                return 0; // Giữ nguyên vị trí nếu mã không đúng định dạng
            }
        });

        // 3. Ép kiểu về ArrayList trả về cho UI Controller để không vỡ DefaultTableModel
        return new ArrayList<>(list);
    }

    //NewInstance (21/04)
    // Lấy danh sách đơn vị đang hoạt động (trangThai = true)
//    public List<DonViTinh> layListDVT() {
//        return donViTinhRepo.layDanhSachTheoTrangThai(true);
//    }

    //NewInstance (21/04)
    // Lấy danh sách đơn vị đã xóa (trangThai = false)
    public List<DonViTinh> layDanhSachDaXoa() {
        return donViTinhRepo.layDanhSachTheoTrangThai(false);
    }

    //NewInstance (21/04)
    // Xóa đơn vị tính (chuyển trạng thái sang false)
    public boolean xoaDVT(String maDVT) {
        if (maDVT == null || maDVT.trim().isEmpty()) {
            return false;
        }
        return donViTinhRepo.capNhatTrangThai(maDVT.trim(), false);
    }

    //NewInstance (21/04)
    // Khôi phục đơn vị tính (chuyển trạng thái sang true)
    public boolean khoiPhucDVT(String maDVT) {
        if (maDVT == null || maDVT.trim().isEmpty()) {
            return false;
        }
        return donViTinhRepo.capNhatTrangThai(maDVT.trim(), true);
    }
}