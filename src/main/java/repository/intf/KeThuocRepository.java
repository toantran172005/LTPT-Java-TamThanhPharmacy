package repository.intf;

import entity.KeThuoc;
import entity.Thuoc;
import java.util.List;

public interface KeThuocRepository {
    boolean themKeThuoc(KeThuoc kt);
    boolean capNhatKeThuoc(KeThuoc kt);
    boolean xoaKeThuoc(String maKe);
    boolean khoiPhucKeThuoc(String maKe);
    List<KeThuoc> layListKeThuoc();
    List<String> layTatCaTenKe();
    List<Thuoc> layListThuocTrongKe(String maKe);
    KeThuoc timTheoTen(String tenKe);
}