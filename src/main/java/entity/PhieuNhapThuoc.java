package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "PhieuNhapThuoc")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhieuNhapThuoc {

    @Id
    @Column(name = "maPNT", length = 20)
    private String maPNT;

    @Column(name = "ngayNhap", nullable = false)
    private LocalDate ngayNhap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maNCC", nullable = false)
    private NhaCungCap nhaCungCap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maNV", nullable = false)
    private NhanVien nhanVien;
}