package service;

import entity.TaiKhoan;
import repository.impl.TaiKhoanRepositoryImpl;
import repository.intf.TaiKhoanRepository;

public class TaiKhoanService {
    private final TaiKhoanRepository taiKhoanRepository;

    public TaiKhoanService() {
        this.taiKhoanRepository = new TaiKhoanRepositoryImpl();
    }

    public TaiKhoan kiemTraDangNhap(TaiKhoan taiKhoan) {
        if (taiKhoan == null) {
            throw new IllegalArgumentException("Dữ liệu tài khoản không được null!");
        }
        if (taiKhoan.getTenDangNhap() == null || taiKhoan.getTenDangNhap().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên đăng nhập không được để trống!");
        }
        if (taiKhoan.getMatKhau() == null || taiKhoan.getMatKhau().trim().isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu không được để trống!");
        }

        return taiKhoanRepository.kiemTraDangNhap(taiKhoan);
    }
}