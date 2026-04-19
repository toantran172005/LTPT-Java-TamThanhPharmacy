package repository.intf;

import entity.PhieuDatHang;
import java.util.List;

public interface PhieuDatHangRepository {
    List<PhieuDatHang> layListPhieuDatHang();
    PhieuDatHang timTheoMa(String maPDH);
    boolean themPhieuDatHang(PhieuDatHang pdh);
    boolean capNhatTrangThai(PhieuDatHang pdh);
    int capNhatTrangThaiPhieu(String maPDH, String trangThaiMoi);
}