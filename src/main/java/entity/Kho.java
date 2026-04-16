package entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "Kho")
public class Kho {

    @Id
    @Column(name = "maKho", length = 20)
    private String maKho;

    @Nationalized
    @Column(name = "tenKho", length = 100, nullable = false)
    private String tenKho;

    @Nationalized
    @Column(name = "diaChi", length = 255, nullable = false)
    private String diaChi;

    @Column(name = "sucChua", nullable = false)
    private Double sucChua;
}