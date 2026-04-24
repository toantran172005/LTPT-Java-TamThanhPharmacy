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
@Table(name = "CT_HoaDon")
public class CTHoaDon {

    @EmbeddedId
    private CTHoaDonId id = new CTHoaDonId();

    @Column(name = "soLuong", nullable = false)
    private Integer soLuong;

    @Column(name = "donGia", nullable = false)
    private Double donGia;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maHD")
    @JoinColumn(name = "maHD", referencedColumnName = "maHD", columnDefinition = "varchar(20)")
    private HoaDon hoaDon;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maThuoc")
    @JoinColumn(name = "maThuoc", referencedColumnName = "maThuoc", columnDefinition = "varchar(20)")
    private Thuoc thuoc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maDVT", nullable = false, columnDefinition = "varchar(20)")
    private DonViTinh donViTinh;

    @Embeddable
    public static class CTHoaDonId implements Serializable {

        @Column(name = "maHD", length = 20, columnDefinition = "varchar(20)")
        private String maHD;

        @Column(name = "maThuoc", length = 20, columnDefinition = "varchar(20)")
        private String maThuoc;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CTHoaDonId that = (CTHoaDonId) o;
            return Objects.equals(maHD, that.maHD) && Objects.equals(maThuoc, that.maThuoc);
        }

        @Override
        public int hashCode() {
            return Objects.hash(maHD, maThuoc);
        }
    }
}