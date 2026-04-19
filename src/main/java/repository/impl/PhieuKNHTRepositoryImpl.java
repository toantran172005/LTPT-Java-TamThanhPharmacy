package repository.impl;

import entity.PhieuKhieuNaiHoTroKH;
import jakarta.persistence.EntityManager;
import repository.GenericJpa;
import repository.intf.PhieuKNHTRepository;

import java.util.List;

public class PhieuKNHTRepositoryImpl extends GenericJpa implements PhieuKNHTRepository {

    @Override
    public boolean themPhieu(PhieuKhieuNaiHoTroKH knht) {
        try {
            inTransaction(em -> em.persist(knht));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean capNhatPhieu(PhieuKhieuNaiHoTroKH knht) {
        try {
            inTransaction(em -> {
                // JPA tự động cập nhật các Cascade nếu đã định nghĩa trong Entity
                // merge sẽ đồng bộ trạng thái của cả Phieu, KhachHang và NhanVien
                em.merge(knht);
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean doiTrangThaiPhieu(String maPhieu, String trangThaiMoi) {
        try {
            inTransaction(em -> {
                PhieuKhieuNaiHoTroKH phieu = em.find(PhieuKhieuNaiHoTroKH.class, maPhieu);
                if (phieu != null) {
                    phieu.setTrangThai(trangThaiMoi);
                    em.merge(phieu);
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<PhieuKhieuNaiHoTroKH> layTatCaPhieu() {
        return doInTransaction(em -> {
            String jpql = "SELECT p FROM PhieuKhieuNaiHoTroKH p JOIN FETCH p.nhanVien JOIN FETCH p.khachHang";
            //            String jpql = "SELECT p FROM PhieuKhieuNaiHoTroKH p " +
//                    "LEFT JOIN FETCH p.nhanVien " +
//                    "LEFT JOIN FETCH p.khachHang";
            return em.createQuery(jpql, PhieuKhieuNaiHoTroKH.class).getResultList();
        });
    }
}