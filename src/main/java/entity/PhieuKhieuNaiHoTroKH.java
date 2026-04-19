package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Phieu_KhieuNai_HoTroKH")
public class PhieuKhieuNaiHoTroKH {

    @Id
    @Column(name = "maPhieu", length = 20)
    private String maPhieu;

    @Column(name = "ngayLap", nullable = false)
    private LocalDate ngayLap;

    @Nationalized
    @Column(name = "noiDung", length = 255, nullable = false)
    private String noiDung;

    @Nationalized
    @Column(name = "loaiDon", nullable = false)
    private String loaiDon;

    @Nationalized
    @Column(name = "trangThai", length = 50, nullable = false)
    private String trangThai;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maNV", nullable = false)
    private NhanVien nhanVien;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maKH", nullable = false)
    private KhachHang khachHang;

}
