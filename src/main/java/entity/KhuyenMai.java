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
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "KhuyenMai")
public class KhuyenMai {

    @Id
    @Column(name = "maKM", length = 20)
    private String maKM;

    @Nationalized
    @Column(name = "tenKM", length = 100, nullable = false)
    private String tenKM;

    @Column(name = "mucKM")
    private Integer mucKM;

    @Column(name = "ngayBD", nullable = false)
    private LocalDate ngayBD;

    @Column(name = "ngayKT", nullable = false)
    private LocalDate ngayKT;

    @Column(name = "trangThai", nullable = false)
    private Boolean trangThai;

    @Nationalized
    @Column(name = "loaiKM", length = 20)
    private String loaiKM;

    @Column(name = "soLuongMua")
    private Integer soLuongMua;

    @Column(name = "soLuongTang")
    private Integer soLuongTang;

}
