package entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

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

    @Override
    public String toString() {
        return this.loaiThue + " - " + (this.tyLeThue * 100) + "%";
    }

}
