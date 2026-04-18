package entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "KeThuoc")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KeThuoc {

    @Id
    @Column(name = "maKe", length = 20)
    private String maKe;

    @Nationalized
    @Column(name = "loaiKe", length = 50)
    private String loaiKe;

    @Column(name = "sucChua", nullable = false)
    private Integer sucChua;

    @Nationalized
    @Column(name = "moTa", length = 255)
    private String moTa;

    @Column(name = "trangThai", nullable = false)
    private Boolean trangThai = true;

}