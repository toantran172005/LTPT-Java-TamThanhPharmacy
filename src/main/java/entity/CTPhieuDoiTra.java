package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "CT_PhieuDoiTra")
public class CTPhieuDoiTra {

    @EmbeddedId
    private IdClass id = new IdClass();

    @Column(name = "soLuong", nullable = false)
    private Integer soLuong;

    @Column(name = "ghiChu", length = 100, nullable = false)
    private String ghiChu;

    @Column(name = "tienHoan", nullable = false)
    private Double tienHoan;

    @Column(name = "mucHoan", nullable = false)
    private Double mucHoan;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maPhieuDT")
    @JoinColumn(name = "maPhieuDT", referencedColumnName = "maPhieuDT", columnDefinition = "varchar(20)")
    private PhieuDoiTra phieuDoiTra;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maThuoc")
    @JoinColumn(name = "maThuoc", referencedColumnName = "maThuoc", columnDefinition = "varchar(20)")
    private Thuoc thuoc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maDVT", columnDefinition = "varchar(20)")
    private DonViTinh donViTinh;

    @Embeddable
    public static class IdClass implements Serializable {

        @Column(name = "maPhieuDT", length = 20, columnDefinition = "varchar(20)")
        private String maPhieuDT;

        @Column(name = "maThuoc", length = 20, columnDefinition = "varchar(20)")
        private String maThuoc;

        @Override
        public boolean equals(Object object) {
            if (object == null || getClass() != object.getClass()) return false;
            IdClass idClass = (IdClass) object;
            return Objects.equals(maPhieuDT, idClass.maPhieuDT) && Objects.equals(maThuoc, idClass.maThuoc);
        }

        @Override
        public int hashCode() {
            return Objects.hash(maPhieuDT, maThuoc);
        }
    }
}