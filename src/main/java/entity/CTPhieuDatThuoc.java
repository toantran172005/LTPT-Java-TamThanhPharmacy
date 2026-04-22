package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "CT_PhieuDatThuoc")
public class CTPhieuDatThuoc {

    @EmbeddedId
    private IdClass id = new IdClass();

    @Column(name = "ghiChu", length = 255)
    private String ghiChu;

    @Column(name = "soLuong", nullable = false)
    private Integer soLuong;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maPDT")
    @JoinColumn(name = "maPDT", referencedColumnName = "maPDT", columnDefinition = "varchar(20)")
    private PhieuDatThuoc phieuDatThuoc;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maThuoc")
    @JoinColumn(name = "maThuoc", referencedColumnName = "maThuoc", columnDefinition = "varchar(20)")
    private Thuoc thuoc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maDVT", columnDefinition = "varchar(20)")
    private DonViTinh donViTinh;

    @Embeddable
    public static class IdClass implements Serializable {

        @Column(name = "maPDT", length = 20, columnDefinition = "varchar(20)")
        private String maPDT;

        @Column(name = "maThuoc", length = 20, columnDefinition = "varchar(20)")
        private String maThuoc;

        @Override
        public boolean equals(Object object) {
            if (object == null || getClass() != object.getClass()) return false;
            IdClass idClass = (IdClass) object;
            return Objects.equals(maPDT, idClass.maPDT) && Objects.equals(maThuoc, idClass.maThuoc);
        }

        @Override
        public int hashCode() {
            return Objects.hash(maPDT, maThuoc);
        }
    }
}