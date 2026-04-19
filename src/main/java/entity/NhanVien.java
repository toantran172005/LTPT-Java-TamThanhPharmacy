package entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "NhanVien")
public class NhanVien {

    @Id
    @Column(name = "maNV", length = 20)
    private String maNV;

    @Nationalized
    @Column(name = "tenNV", length = 100, nullable = false)
    private String tenNV;

    @Nationalized
    @Column(name = "chucVu", length = 50, nullable = false)
    private String chucVu;

    @Column(name = "ngaySinh", nullable = false)
    private LocalDate ngaySinh;

    @Column(name = "gioiTinh", nullable = false)
    private Boolean gioiTinh;

    @Column(name = "sdt", length = 20, nullable = false)
    private String sdt;

    @Column(name = "ngayVaoLam", nullable = false)
    private LocalDate ngayVaoLam;

    @Column(name = "luong", nullable = false)
    private Double luong;

    @Column(name = "trangThai", nullable = false)
    private Boolean trangThai = true;

    @Column(name = "anh", length = 100)
    private String anh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maThue", nullable = false)
    private Thue thue;

}