package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "NhaCungCap")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NhaCungCap {

    @Id
    @Column(name = "maNCC", length = 20)
    private String maNCC;

    @Nationalized
    @Column(name = "tenNCC", length = 100, nullable = false)
    private String tenNCC;

    @Nationalized
    @Column(name = "diaChi", length = 255, nullable = false)
    private String diaChi;

    @Column(name = "sdt", length = 20, nullable = false)
    private String sdt;

    @Column(name = "email", length = 100, nullable = false)
    private String email;

}
