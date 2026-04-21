package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "CT_Kho")
public class CTKho {

    @EmbeddedId
    private IdClass id = new IdClass();

    @Column(name = "soLuongTon", nullable = false)
    private int soLuongTon;

    @Nationalized
    @Column(name = "ghiChu", length = 255)
    private String ghiChu;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maKho")
    @JoinColumn(name = "maKho", referencedColumnName = "maKho", columnDefinition = "varchar(20)")
    private Kho kho;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maThuoc")
    @JoinColumn(name = "maThuoc", referencedColumnName = "maThuoc", columnDefinition = "varchar(20)")
    private Thuoc thuoc;

    @Embeddable
    public static class IdClass implements Serializable {

        @Column(name = "maKho", length = 20, columnDefinition = "varchar(20)")
        private String maKho;

        @Column(name = "maThuoc", length = 20, columnDefinition = "varchar(20)")
        private String maThuoc;

        @Override
        public boolean equals(Object object) {
            if (object == null || getClass() != object.getClass()) return false;
            IdClass idClass = (IdClass) object;
            return Objects.equals(maKho, idClass.maKho) && Objects.equals(maThuoc, idClass.maThuoc);
        }

        @Override
        public int hashCode() {
            return Objects.hash(maKho, maThuoc);
        }
    }
}