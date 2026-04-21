package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;


@Entity
@Table(name = "QuocGia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuocGia {

    @Id
    @Column(name = "maQuocGia", length = 100)
    private String maQuocGia;

    @Nationalized
    @Column(name = "tenQuocGia", length = 100, nullable = false)
    private String tenQuocGia;

}
