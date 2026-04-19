package service;

import entity.PhieuDatHang;
import repository.impl.PhieuDatHangRepositoryImpl;
import repository.intf.PhieuDatHangRepository;

import java.util.ArrayList;
import java.util.List;

public class PhieuDatHangService {

    private final PhieuDatHangRepository pdhRepo = new PhieuDatHangRepositoryImpl();

    public ArrayList<PhieuDatHang> layListPhieuDatHang() {
        List<PhieuDatHang> list = pdhRepo.layListPhieuDatHang();
        // Ép kiểu chuẩn xác sang ArrayList cho giao diện Swing
        return list != null ? new ArrayList<>(list) : new ArrayList<>();
    }

    public PhieuDatHang timTheoMa(String maPDH) {
        if (maPDH == null || maPDH.trim().isEmpty()) {
            return null;
        }
        return pdhRepo.timTheoMa(maPDH.trim());
    }

    public boolean themPhieuDatHang(PhieuDatHang pdh) {
        if (pdh == null || pdh.getMaPDH() == null || pdh.getMaPDH().trim().isEmpty()) {
            return false;
        }
        return pdhRepo.themPhieuDatHang(pdh);
    }

    public boolean capNhatTrangThai(PhieuDatHang pdh, String trangThaiMoi) {
        if (pdh == null || trangThaiMoi == null || trangThaiMoi.trim().isEmpty()) {
            return false;
        }
        // Logic cập nhật: Set trạng thái mới rồi đẩy xuống merge
        pdh.setTrangThai(trangThaiMoi);
        return pdhRepo.capNhatTrangThai(pdh);
    }

    public int capNhatTrangThaiPhieu(String maPDH, String trangThaiMoi) {
        if (maPDH == null || maPDH.trim().isEmpty()) return 2;
        if (trangThaiMoi == null || trangThaiMoi.trim().isEmpty()) return 2;

        return pdhRepo.capNhatTrangThaiPhieu(maPDH.trim(), trangThaiMoi.trim());
    }
}