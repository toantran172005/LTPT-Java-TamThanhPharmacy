package entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

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
    @Column(name = "tenHT", length = 100)
    private String tenHT;

    @Nationalized
    @Column(name = "ghiChu", length = 255)
    private String ghiChu;

    @Nationalized
    @Column(name = "hotline", length = 20)
    private String hotline;

    @Nationalized
    @Column(name = "diaChiHT", length = 255)
    private String diaChiHT;

    @Column(name = "tienNhan", precision = 18)
    private Double tienNhan;

    @Column(name = "trangThai", nullable = false)
    private Boolean trangThai = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maKH", nullable = false)
    private KhachHang khachHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maNV", nullable = false)
    private NhanVien nhanVien;

    public HoaDon(String maHD, KhachHang khHD, NhanVien nv, String hinhThucTT, LocalDate now, String diaChiHT, String tenHT, String ghiChu, String hotline, double tienNhan, boolean trangThai) {
        this.maHD = maHD;
        this.khachHang = khHD;
        this.nhanVien = nv;
        this.loaiTT = hinhThucTT;
        this.ngayLap = now;
        this.tenHT = tenHT;
        this.diaChiHT = diaChiHT;
        this.ghiChu = ghiChu;
        this.hotline = hotline;
        this.tienNhan = tienNhan;
        this.trangThai = trangThai;
    }
}
