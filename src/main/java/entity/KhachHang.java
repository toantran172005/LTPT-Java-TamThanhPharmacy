package entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;

import java.util.List;

@Entity
@Table(name = "KhachHang")
public class KhachHang {
    @Id
    @Column(name = "maKH", length = 20)
    private String maKH;

    @Nationalized
    @Column(name = "tenKH", length = 100, nullable = false)
    private String tenKH;

    @Column(name = "sdt", length = 20, nullable = false)
    private String sdt;

    @Column(name = "tuoi", nullable = false)
    private Integer tuoi;

    @Column(name = "trangThai", nullable = false)
    private Boolean trangThai = true;

    @OneToMany(mappedBy = "khachHang", cascade = CascadeType.ALL)
    private List<HoaDon> danhSachHoaDon;

}
