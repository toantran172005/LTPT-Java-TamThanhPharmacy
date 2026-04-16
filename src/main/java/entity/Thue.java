package entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "Thue")
public class Thue {

    @Id
    @Column(name = "maThue", length = 20)
    private String maThue;

    @Nationalized
    @Column(name = "loaiThue", length = 100, nullable = false)
    private String loaiThue;

    @Column(name = "tyLeThue", nullable = false)
    private Double tyLeThue;

    @Nationalized
    @Column(name = "moTa", length = 255)
    private String moTa;

    @Column(name = "trangThai")
    private Boolean trangThai = true;

}
