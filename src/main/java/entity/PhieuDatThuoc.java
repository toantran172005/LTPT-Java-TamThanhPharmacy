package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;

@Entity
@Table(name = "PhieuDatThuoc")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhieuDatThuoc {

    @Id
    @Column(name = "maPDT", length = 20)
    private String maPDT;

    @Column(name = "ngayLap", nullable = false)
    private LocalDate ngayLap;

    @Column(name = "trangThai", length = 50, nullable = false)
    private String trangThai;

    @Nationalized
    @Column(name = "ghiChu", length = 255)
    private String ghiChu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maNCC", nullable = false)
    private NhaCungCap nhaCungCap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maNV", nullable = false)
    private NhanVien nhanVien;
}
