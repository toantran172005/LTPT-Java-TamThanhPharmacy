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
@Table(name = "PhieuDoiTra")
public class PhieuDoiTra {

    @Id
    @Column(name = "maPhieuDT", length = 20)
    private String maPhieuDT;

    @Column(name = "ngayDoiTra", nullable = false)
    private LocalDate ngayDoiTra;

    @Nationalized
    @Column(name = "lyDo", length = 255, nullable = false)
    private String lyDo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maHD", nullable = false)
    private HoaDon hoaDon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maNV", nullable = false)
    private NhanVien nhanVien;
}