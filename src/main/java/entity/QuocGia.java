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
@Table(name = "QuocGia")
public class QuocGia {

    @Id
    @Column(name = "maQuocGia", length = 100)
    private String maQuocGia;

    @Nationalized
    @Column(name = "tenQuocGia", length = 100, nullable = false)
    private String tenQuocGia;

}
