package entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "TaiKhoan")
public class TaiKhoan {
    @Id
    @Column(name = "maTK", length = 20)
    private String maTK;

    @Column(name = "tenDangNhap", length = 30, nullable = false)
    private String tenDangNhap;

    @Column(name = "matKhau", length = 100, nullable = false)
    private String matKhau;

    @Column(name = "trangThai", nullable = false)
    private Boolean trangThai;

    @Column(name = "email", length = 100, nullable = false)
    private String email;

    @Nationalized
    @Column(name = "loaiTK", length = 20)
    private String loaiTK;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maNV", nullable = false)
    private NhanVien nhanVien;

}