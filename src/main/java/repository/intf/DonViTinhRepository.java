package repository.intf;

import entity.DonViTinh;
import java.util.List;

public interface DonViTinhRepository {
    List<DonViTinh> layDanhSachTheoTrangThai(boolean trangThai);
    DonViTinh timTheoTen(String tenDVT);
    DonViTinh timTheoMa(String maDVT);
    boolean themDVT(DonViTinh dvt);
    boolean capNhatTrangThai(String maDVT, boolean trangThai);
}