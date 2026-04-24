package repository.impl;

import entity.CTPhieuDatHang;
import entity.PhieuDatHang;
import jakarta.persistence.EntityManager;
import repository.GenericJpa;
import repository.intf.PhieuDatHangRepository;

import java.util.List;

public class PhieuDatHangRepositoryImpl extends GenericJpa implements PhieuDatHangRepository {

    @Override
    public List<PhieuDatHang> layListPhieuDatHang() {
        return doInTransaction(em -> {
            String jpql = "SELECT p FROM PhieuDatHang p JOIN FETCH p.khachHang JOIN FETCH p.nhanVien ORDER BY p.maPDH DESC";
            return em.createQuery(jpql, PhieuDatHang.class).getResultList();
        });
    }

    @Override
    public PhieuDatHang timTheoMa(String maPDH) {
        return doInTransaction(em -> {
            String jpql = "SELECT p FROM PhieuDatHang p JOIN FETCH p.khachHang JOIN FETCH p.nhanVien WHERE p.maPDH = :maPDH";
            return em.createQuery(jpql, PhieuDatHang.class)
                    .setParameter("maPDH", maPDH)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        });
    }

    @Override
    public boolean themPhieuDatHang(PhieuDatHang pdh) {
        try {
            inTransaction(em -> em.persist(pdh));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean capNhatTrangThai(PhieuDatHang pdh) {
        try {
            inTransaction(em -> em.merge(pdh));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int capNhatTrangThaiPhieu(String maPDH, String trangThaiMoi) {
        return doInTransaction(em -> {
            try {
                PhieuDatHang pdh = em.find(PhieuDatHang.class, maPDH);
                if (pdh == null) return 2;

                String trangThaiCu = pdh.getTrangThai();
                if (trangThaiCu.equalsIgnoreCase(trangThaiMoi)) return 3;

                // Lấy danh sách chi tiết của phiếu này bằng JPQL
                String getCtJpql = "SELECT c FROM CTPhieuDatHang c WHERE c.phieuDatHang.maPDH = :maPDH";
                List<CTPhieuDatHang> dsChiTiet = em.createQuery(getCtJpql, CTPhieuDatHang.class)
                        .setParameter("maPDH", maPDH)
                        .getResultList();

                if (trangThaiMoi.equals("Chờ hàng") && trangThaiCu.equals("Đã hủy")) {

                    // 1. Kiểm tra tồn kho trước
                    for (CTPhieuDatHang ct : dsChiTiet) {
                        String checkKhoJpql = "SELECT SUM(k.soLuongTon) FROM CTKho k WHERE k.thuoc.maThuoc = :maThuoc";
                        // Dùng Number.class để hứng mọi kiểu dữ liệu (Double, Long, Integer...) chống lỗi Mismatch
                        Number tongTonNum = em.createQuery(checkKhoJpql, Number.class)
                                .setParameter("maThuoc", ct.getThuoc().getMaThuoc())
                                .getSingleResult();

                        int tongTon = (tongTonNum != null) ? tongTonNum.intValue() : 0;

                        if (tongTon < ct.getSoLuong()) {
                            em.getTransaction().setRollbackOnly();
                            return 1; // Hụt kho
                        }
                    }

                    // 2. Trừ kho
                    for (CTPhieuDatHang ct : dsChiTiet) {
                        String updateKhoJpql = "UPDATE CTKho k SET k.soLuongTon = k.soLuongTon - :soLuong WHERE k.thuoc.maThuoc = :maThuoc";
                        em.createQuery(updateKhoJpql)
                                .setParameter("soLuong", ct.getSoLuong())
                                .setParameter("maThuoc", ct.getThuoc().getMaThuoc())
                                .executeUpdate();
                    }
                }
                else if (trangThaiMoi.equals("Đã hủy") && !trangThaiCu.equals("Đã hủy")) {
                    // Cộng lại số lượng vào kho nếu hủy phiếu
                    for (CTPhieuDatHang ct : dsChiTiet) {
                        String updateKhoJpql = "UPDATE CTKho k SET k.soLuongTon = k.soLuongTon + :soLuong WHERE k.thuoc.maThuoc = :maThuoc";
                        em.createQuery(updateKhoJpql)
                                .setParameter("soLuong", ct.getSoLuong())
                                .setParameter("maThuoc", ct.getThuoc().getMaThuoc())
                                .executeUpdate();
                    }
                }

                // Cập nhật trạng thái và lưu
                pdh.setTrangThai(trangThaiMoi);
                em.merge(pdh);

                return 0; // Thành công
            } catch (Exception e) {
                e.printStackTrace();
                if (em.getTransaction().isActive()) {
                    em.getTransaction().setRollbackOnly();
                }
                return 2; // Lỗi hệ thống
            }
        });
    }

    @Override
    public List<Object[]> layDanhSachThuocTheoPDH(String maPDH) {
        return doInTransaction(em -> {
            String jpql = """
                SELECT 
                    c.phieuDatHang.maPDH, 
                    c.thuoc.maThuoc, 
                    c.thuoc.tenThuoc, 
                    c.soLuong, 
                    c.donViTinh.maDVT, 
                    c.donViTinh.tenDVT, 
                    c.donGia,
                    c.thuoc.khuyenMai
                FROM CTPhieuDatHang c 
                WHERE c.phieuDatHang.maPDH = :maPDH
                """;
            return em.createQuery(jpql, Object[].class)
                    .setParameter("maPDH", maPDH)
                    .getResultList();
        });
    }

    @Override
    public int taoPhieuDatHangVaChiTiet(PhieuDatHang pdh, List<CTPhieuDatHang> dsChiTiet) {
        return doInTransaction(em -> {
            try {
                // Bước 1: KIỂM TRA TỒN KHO TRƯỚC
                for (CTPhieuDatHang ct : dsChiTiet) {
                    String maThuoc = ct.getThuoc().getMaThuoc();
                    int soLuongCan = ct.getSoLuong();

                    String checkKhoJpql = "SELECT SUM(k.soLuongTon) FROM CTKho k WHERE k.thuoc.maThuoc = :maThuoc";
                    // Tương tự, dùng Number.class ở đây
                    Number tongTonNum = em.createQuery(checkKhoJpql, Number.class)
                            .setParameter("maThuoc", maThuoc)
                            .getSingleResult();

                    int tongTon = (tongTonNum != null) ? tongTonNum.intValue() : 0;

                    if (tongTon < soLuongCan) {
                        em.getTransaction().setRollbackOnly();
                        return 0; // Không đủ tồn kho
                    }
                }

                // Bước 2: LƯU PHIẾU ĐẶT HÀNG (Parent)
                em.persist(pdh);

                // Bước 3: LƯU TỪNG CHI TIẾT & CẬP NHẬT KHO (Child)
                for (CTPhieuDatHang ct : dsChiTiet) {
                    ct.setPhieuDatHang(pdh);
                    em.persist(ct);

                    String updateKhoJpql = "UPDATE CTKho k SET k.soLuongTon = k.soLuongTon - :soLuong WHERE k.thuoc.maThuoc = :maThuoc";
                    em.createQuery(updateKhoJpql)
                            .setParameter("soLuong", ct.getSoLuong())
                            .setParameter("maThuoc", ct.getThuoc().getMaThuoc())
                            .executeUpdate();
                }

                return 1; // Hoàn tất thành công

            } catch (Exception e) {
                e.printStackTrace();
                if (em.getTransaction().isActive()) {
                    em.getTransaction().setRollbackOnly();
                }
                return -1; // Lỗi hệ thống
            }
        });
    }
}