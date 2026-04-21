package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;

@Entity
@Table(name = "Thuoc")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Thuoc {

    @Id
    @Column(name = "maThuoc", length = 20)
    private String maThuoc;

    @Nationalized
    @Column(name = "tenThuoc", length = 100, nullable = false)
    private String tenThuoc;

    @Nationalized
    @Column(name = "dangThuoc", length = 50, nullable = false)
    private String dangThuoc;

    @Column(name = "soLuong", nullable = false)
    private int soLuong;

    @Column(name = "giaBan", nullable = false)
    private Double giaBan;

    @Column(name = "hanSuDung", nullable = false)
    private LocalDate hanSuDung;

    @Column(name = "trangThai", nullable = false)
    private Boolean trangThai = true;

    @Column(name = "anh", length = 100)
    private String anh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maThue", nullable = false)
    private Thue thue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maNCC", nullable = false)
    private NhaCungCap nhaCungCap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maKe", nullable = false)
    private KeThuoc keThuoc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maKM")
    private KhuyenMai khuyenMai;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maDVT")
    private DonViTinh donViTinh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maQuocGia")
    private QuocGia quocGia;

}
