package entity;
import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
@Table(name = "HoaDon")
public class HoaDon {
    @Id
    @Column(name = "maHD", length = 20)
    private String maHD;

    @Nationalized
    @Column(name = "loaiTT", length = 30)
    private String loaiTT;

    @Column(name = "ngayLap", nullable = false)
    private LocalDate ngayLap;

    @Nationalized
    @Column(name = "diaChiHT", length = 255)
    private String diaChiHT;

    @Column(name = "tienNhan", precision = 18, scale = 2)
    private BigDecimal tienNhan;

    @Column(name = "trangThai", nullable = false)
    private Boolean trangThai = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maKH", nullable = false)
    private KhachHang khachHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maNV", nullable = false)
    private NhanVien nhanVien;

}
