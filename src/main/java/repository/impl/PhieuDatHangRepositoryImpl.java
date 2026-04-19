package repository.impl;

import entity.PhieuDatHang;
import jakarta.persistence.EntityManager;
import repository.GenericJpa;
import repository.intf.PhieuDatHangRepository;

import java.util.List;

public class PhieuDatHangRepositoryImpl extends GenericJpa implements PhieuDatHangRepository {

    @Override
    public List<PhieuDatHang> layListPhieuDatHang() {
        return doInTransaction(em -> {
            // Dùng JOIN FETCH để lấy luôn thông tin KH và NV.
            // Sắp xếp mã PDH giảm dần thay vì dùng SUBSTRING rườm rà của SQL Server
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
            // JPA sẽ tự động Insert Phiếu Đặt Hàng và các Chi Tiết (nếu cấu hình Cascade trong Entity)
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
                // 1. Find object lên theo chuẩn JPA
                PhieuDatHang pdh = em.find(PhieuDatHang.class, maPDH);
                if (pdh == null) return 2; // Lỗi: Không tìm thấy phiếu

                String trangThaiCu = pdh.getTrangThai();
                if (trangThaiCu.equalsIgnoreCase(trangThaiMoi)) return 3; // Không có gì thay đổi

                // 2. Logic kiểm tra và cập nhật Kho (Minh họa logic JPA thay cho SQL Update Kho cũ)
                // Lưu ý: Đoạn này dựa trên cấu trúc kho của bạn.
                // Bạn có thể dùng em.createQuery() để lấy danh sách ChiTietKho tương ứng với các Thuoc trong PhieuDatHang.

                if (trangThaiMoi.equals("Chờ hàng") && trangThaiCu.equals("Đã hủy")) {
                    // TODO: Dùng JPQL SELECT SUM(c.soLuongTon) FROM ChiTietKho c WHERE c.thuoc.maThuoc = :maThuoc
                    // Nếu tồn kho < số lượng cần -> return 1 (Hụt kho)
                    // Nếu đủ -> Dùng em.merge() để trừ tồn kho
                }
                else if (trangThaiMoi.equals("Đã hủy") && !trangThaiCu.equals("Đã hủy")) {
                    // TODO: Dùng em.merge() để cộng lại số lượng vào ChiTietKho
                }

                // 3. Set trạng thái mới và Merge
                pdh.setTrangThai(trangThaiMoi);
                em.merge(pdh);

                return 0; // Thành công
            } catch (Exception e) {
                e.printStackTrace();
                return 2; // Lỗi hệ thống
            }
        });
    }
}