package entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "QuocGia")
public class QuocGia {

    @Id
    @Column(name = "maQuocGia", length = 100)
    private String maQuocGia;

    @Nationalized
    @Column(name = "tenQuocGia", length = 100, nullable = false)
    private String tenQuocGia;

}
