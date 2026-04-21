package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "DonViTinh")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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