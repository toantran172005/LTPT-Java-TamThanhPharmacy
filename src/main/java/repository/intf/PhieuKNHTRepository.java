package repository.intf;

import entity.PhieuKhieuNaiHoTroKH;
import java.util.List;

public interface PhieuKNHTRepository {
    boolean themPhieu(PhieuKhieuNaiHoTroKH knht);
    boolean capNhatPhieu(PhieuKhieuNaiHoTroKH knht);
    boolean doiTrangThaiPhieu(String maPhieu, String trangThaiMoi);
    List<PhieuKhieuNaiHoTroKH> layTatCaPhieu();
}