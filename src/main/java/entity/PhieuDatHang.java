package entity;
import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;

@Entity
@Table(name = "PhieuDatHang")
public class PhieuDatHang {
    @Id @Column(name = "maPDH", length = 20)
    private String maPDH;

    @Column(name = "ngayDat", nullable = false)
    private LocalDate ngayDat;

    @Column(name = "ngayHen", nullable = false)
    private LocalDate ngayHen;

    @Column(name = "trangThai", length = 30)
    private String trangThai = "Chờ hàng"; // Theo DEFAULT

    @Nationalized
    @Column(name = "ghiChu", length = 255)
    private String ghiChu;

    @Nationalized
    @Column(name = "diaChiHT", length = 255)
    private String diaChiHT;

    @Nationalized
    @Column(name = "tenHT", length = 100)
    private String tenHT;

    @Column(name = "hotline", length = 20)
    private String hotline;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "maHD")
    private HoaDon hoaDon;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "maKH", nullable = false)
    private KhachHang khachHang;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "maNV", nullable = false)
    private NhanVien nhanVien;
}
