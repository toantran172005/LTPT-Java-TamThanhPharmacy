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
@Table(name = "CT_PhieuDatHang")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CTPhieuDatHang {

    @EmbeddedId
    private IdClass id = new IdClass();

    @Column(name = "soLuong", nullable = false)
    private Integer soLuong;

    @Column(name = "donGia")
    private Double donGia;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maPDH")
    @JoinColumn(name = "maPDH", referencedColumnName = "maPDH", columnDefinition = "varchar(20)")
    private PhieuDatHang phieuDatHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maThuoc")
    @JoinColumn(name = "maThuoc", referencedColumnName = "maThuoc", columnDefinition = "varchar(20)")
    private Thuoc thuoc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maDVT", nullable = false, columnDefinition = "varchar(20)")
    private DonViTinh donViTinh;

    @Embeddable
    public static class IdClass implements Serializable {

        @Column(name = "maPDH", length = 20, columnDefinition = "varchar(20)")
        private String maPDH;

        @Column(name = "maThuoc", length = 20, columnDefinition = "varchar(20)")
        private String maThuoc;

        @Override
        public boolean equals(Object object) {
            if (object == null || getClass() != object.getClass()) return false;
            IdClass idClass = (IdClass) object;
            return Objects.equals(maPDH, idClass.maPDH) && Objects.equals(maThuoc, idClass.maThuoc);
        }

        @Override
        public int hashCode() {
            return Objects.hash(maPDH, maThuoc);
        }
    }
}