package entity;

public enum LoaiTaiKhoan {
    NHAN_VIEN("Nhân Viên"),
    QUAN_LY("Quản Lý"),
    ADMIN("Admin");

    private String value;

    LoaiTaiKhoan(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
