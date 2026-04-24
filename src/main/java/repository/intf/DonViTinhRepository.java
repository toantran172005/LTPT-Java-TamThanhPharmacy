package repository.intf;

import entity.DonViTinh;
import java.util.List;

public interface DonViTinhRepository {
    List<DonViTinh> layDanhSachTheoTrangThai(boolean trangThai);
    DonViTinh timTheoTen(String tenDVT);
//    boolean themDonViTinh(DonViTinh dvt);
    boolean capNhatTrangThai(String maDVT, boolean trangThai);
    List<DonViTinh> layListDVT();

    //NewInstance (21/04)
    DonViTinh timTheoMa(String maDVT);
    boolean themDVT(DonViTinh dvt);
}