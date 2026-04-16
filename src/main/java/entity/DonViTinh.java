package entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "DonViTinh")
public class DonViTinh {

    @Id
    @Column(name = "maDVT", length = 20)
    private String maDVT;

    @Nationalized
    @Column(name = "tenDVT", length = 50, nullable = false)
    private String tenDVT;

    @Column(name = "trangThai", nullable = false)
    private Boolean trangThai = true;

}