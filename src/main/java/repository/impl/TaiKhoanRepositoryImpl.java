package repository.impl;

import entity.TaiKhoan;
import repository.GenericJpa;
import repository.intf.TaiKhoanRepository;

public class TaiKhoanRepositoryImpl extends GenericJpa implements TaiKhoanRepository {
    @Override
    public TaiKhoan kiemTraDangNhap(TaiKhoan taiKhoan) {
        return doInTransaction(em -> {

            String jpql = """
                SELECT t FROM TaiKhoan t 
                JOIN FETCH t.nhanVien 
                WHERE t.tenDangNhap = :tenDN 
                  AND t.matKhau = :matKhau 
                  AND t.trangThai = true
            """;

            return em.createQuery(jpql, TaiKhoan.class)
                    .setParameter("tenDN", taiKhoan.getTenDangNhap())
                    .setParameter("matKhau", taiKhoan.getMatKhau())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        });
    }
}
