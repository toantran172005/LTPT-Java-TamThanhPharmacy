package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "Thuoc_DonViTinh")
public class ThuocDonViTinh {

    @EmbeddedId
    private IdClass id = new IdClass();

    @Column(name = "tiLe")
    private Double tiLe;

    @Column(name = "giaBan")
    private Double giaBan;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maThuoc")
    @JoinColumn(name = "maThuoc",
            referencedColumnName = "maThuoc",
            columnDefinition = "varchar(20)")
    private Thuoc thuoc;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maDVT")
    @JoinColumn(name = "maDVT",
            referencedColumnName = "maDVT",
            columnDefinition = "varchar(20)")
    private DonViTinh donViTinh;

    @Embeddable
    public static class IdClass implements Serializable {

        @Column(name = "maThuoc", length = 20, columnDefinition = "varchar(20)")
        private String maThuoc;

        @Column(name = "maDVT", length = 20, columnDefinition = "varchar(20)")
        private String maDVT;

        @Override
        public boolean equals(Object object) {
            if (object == null || getClass() != object.getClass()) return false;
            IdClass idClass = (IdClass) object;
            return Objects.equals(maThuoc, idClass.maThuoc) && Objects.equals(maDVT, idClass.maDVT);
        }

        @Override
        public int hashCode() {
            return Objects.hash(maThuoc, maDVT);
        }
    }
}