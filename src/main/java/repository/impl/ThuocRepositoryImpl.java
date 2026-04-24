package repository.impl;

import entity.*;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import repository.GenericJpa;
import repository.intf.ThuocRepository;
import java.time.LocalDate;
import java.util.List;

public class ThuocRepositoryImpl extends GenericJpa implements ThuocRepository {

    @Override
    public boolean kiemTraTrungTenVaQuocGia(String tenThuoc, String maQG) {
        return doInTransaction(em -> {
            String jpql = "SELECT COUNT(t) FROM Thuoc t WHERE t.tenThuoc = :tenThuoc AND t.quocGia.maQuocGia = :maQG";
            Long count = em.createQuery(jpql, Long.class)
                    .setParameter("tenThuoc", tenThuoc)
                    .setParameter("maQG", maQG)
                    .getSingleResult();
            return count > 0;
        });
    }

    @Override
    public String layMaQuocGiaTheoTen(String tenQG) {
        return doInTransaction(em -> {
            try {
                String jpql = "SELECT q.maQuocGia FROM QuocGia q WHERE q.tenQuocGia = :tenQG";
                return em.createQuery(jpql, String.class).setParameter("tenQG", tenQG).setMaxResults(1).getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        });
    }

    @Override
    public String layMaThuocTheoTenVaQG(String tenThuoc, String tenQG) {
        return doInTransaction(em -> {
            try {
                String jpql = "SELECT t.maThuoc FROM Thuoc t WHERE t.tenThuoc = :ten AND t.quocGia.tenQuocGia = :tenQG";
                return em.createQuery(jpql, String.class)
                        .setParameter("ten", tenThuoc)
                        .setParameter("tenQG", tenQG)
                        .setMaxResults(1).getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        });
    }

    @Override
    public QuocGia layQuocGiaTheoMa(String maQG) {
        return doInTransaction(em -> em.find(QuocGia.class, maQG));
    }

    @Override
    public String layTenDonViTinhTheoMaThuoc(String maThuoc) {
        return doInTransaction(em -> {
            try {
                String jpql = "SELECT t.donViTinh.tenDVT FROM Thuoc t WHERE t.maThuoc = :maThuoc";
                return em.createQuery(jpql, String.class).setParameter("maThuoc", maThuoc).getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        });
    }

    @Override
    public String layMaThuocTheoTen(String tenThuoc) {
        return doInTransaction(em -> {
            try {
                String jpql = "SELECT t.maThuoc FROM Thuoc t WHERE t.tenThuoc = :ten";
                return em.createQuery(jpql, String.class).setParameter("ten", tenThuoc).setMaxResults(1).getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        });
    }

    @Override
    public boolean capNhatSoLuongTon(String maThuoc, String maDVT, int soLuong, boolean isTang) {
        try {
            inTransaction(em -> {
                // Lấy tỉ lệ quy đổi
                String jpqlTiLe = "SELECT td.tiLe FROM ThuocDonViTinh td WHERE td.thuoc.maThuoc = :maThuoc AND td.donViTinh.maDVT = :maDVT";
                Double tiLe = 1.0;
                try {
                    tiLe = em.createQuery(jpqlTiLe, Double.class)
                            .setParameter("maThuoc", maThuoc)
                            .setParameter("maDVT", maDVT).getSingleResult();
                } catch (NoResultException ignored) {}

                double soLuongThucTe = soLuong * tiLe;

                // Cập nhật tồn kho
                String jpqlKho = "SELECT c FROM CTKho c WHERE c.thuoc.maThuoc = :maThuoc";
                CTKho ctKho = em.createQuery(jpqlKho, CTKho.class).setParameter("maThuoc", maThuoc).getSingleResult();

                if(isTang) {
                    ctKho.setSoLuongTon((int) (ctKho.getSoLuongTon() + soLuongThucTe));
                } else {
                    ctKho.setSoLuongTon((int) (ctKho.getSoLuongTon() - soLuongThucTe));
                }
                em.merge(ctKho);
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Number layGiaBanTheoDVT(String maThuoc, String maDVT) {
        return doInTransaction(em -> {
            try {
                String jpql = "SELECT td.giaBan FROM ThuocDonViTinh td WHERE td.thuoc.maThuoc = :maThuoc AND td.donViTinh.maDVT = :maDVT";
                return em.createQuery(jpql, Double.class)
                        .setParameter("maThuoc", maThuoc).setParameter("maDVT", maDVT).getSingleResult();
            } catch (NoResultException e) {
                return 0;
            }
        });
    }

    @Override
    public String layHoacTaoThue(Thue thue) {
        return doInTransaction(em -> {
            try {
                String jpql = "SELECT t.maThue FROM Thue t WHERE t.loaiThue = :loai AND t.tyLeThue = :tiLe";
                return em.createQuery(jpql, String.class)
                        .setParameter("loai", thue.getLoaiThue())
                        .setParameter("tiLe", thue.getTyLeThue() / 100.0)
                        .setMaxResults(1).getSingleResult();
            } catch (NoResultException e) {
                inTransaction(emIn -> {
                    thue.setTyLeThue(thue.getTyLeThue() / 100.0);
                    emIn.persist(thue);
                });
                return thue.getMaThue();
            }
        });
    }

    @Override
    public String layHoacTaoDVT(DonViTinh dvt) {
        return doInTransaction(em -> {
            try {
                String jpql = "SELECT d.maDVT FROM DonViTinh d WHERE d.tenDVT = :ten";
                return em.createQuery(jpql, String.class).setParameter("ten", dvt.getTenDVT()).setMaxResults(1).getSingleResult();
            } catch (NoResultException e) {
                inTransaction(emIn -> emIn.persist(dvt));
                return dvt.getMaDVT();
            }
        });
    }

    @Override
    public boolean themThuoc(Thuoc thuoc) {
        try {
            inTransaction(em -> {

                if (thuoc.getKeThuoc() != null) {
                    thuoc.setKeThuoc(em.find(
                            thuoc.getKeThuoc().getClass(),
                            thuoc.getKeThuoc().getMaKe()
                    ));
                }

                if (thuoc.getDonViTinh() != null) {
                    thuoc.setDonViTinh(em.find(
                            thuoc.getDonViTinh().getClass(),
                            thuoc.getDonViTinh().getMaDVT()
                    ));
                }

                if (thuoc.getQuocGia() != null) {
                    thuoc.setQuocGia(em.find(
                            thuoc.getQuocGia().getClass(),
                            thuoc.getQuocGia().getMaQuocGia()
                    ));
                }

                if (thuoc.getKhuyenMai() != null) {
                    thuoc.setKhuyenMai(em.find(
                            thuoc.getKhuyenMai().getClass(),
                            thuoc.getKhuyenMai().getMaKM()
                    ));
                }

                em.persist(thuoc);
                em.flush();
            });

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean luuData(String maPNT, String maNCC, String maNV, LocalDate ngayNhap, List<Thuoc> listThuoc) {
        try {
            inTransaction(em -> {
                for(Thuoc t : listThuoc) {
                    em.persist(t);
                }
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Object[]> layDanhSachThuocChoKM() {
        return doInTransaction(em -> {
            String jpql = "SELECT t.maThuoc, t.tenThuoc, t.keThuoc.loaiKe, t.donViTinh.maDVT, c.donGia " +
                    "FROM Thuoc t JOIN CTHoaDon c ON t.maThuoc = c.thuoc.maThuoc " +
                    "WHERE t.trangThai = true ORDER BY t.maThuoc";
            return em.createQuery(jpql, Object[].class).getResultList();
        });
    }

    @Override
    public List<QuocGia> layListQuocGiaTheoThuoc(String tenThuoc) {
        return doInTransaction(em -> {
            String jpql = "SELECT t.quocGia FROM Thuoc t WHERE t.tenThuoc = :ten";
            return em.createQuery(jpql, QuocGia.class).setParameter("ten", tenThuoc).getResultList();
        });
    }

    @Override
    public String timMaQGTheoTen(String tenQG) {
        return layMaQuocGiaTheoTen(tenQG);
    }

    @Override
    public String timTenQGTheoMaThuoc(String maThuoc) {
        return doInTransaction(em -> {
            try {
                String jpql = "SELECT t.quocGia.tenQuocGia FROM Thuoc t WHERE t.maThuoc = :maThuoc";
                return em.createQuery(jpql, String.class).setParameter("maThuoc", maThuoc).getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        });
    }

    @Override
    public List<Thuoc> layListThuocHoanChinh() {
        return doInTransaction(em -> {
            String jpql = "SELECT t FROM Thuoc t ORDER BY t.maThuoc";
            return em.createQuery(jpql, Thuoc.class).getResultList();
        });
    }

    @Override
    public List<Thuoc> layListThuoc() {
        return doInTransaction(em -> {
            String jpql = "SELECT t FROM Thuoc t WHERE t.trangThai = true";
            return em.createQuery(jpql, Thuoc.class).getResultList();
        });
    }

    @Override
    public Thuoc timThuocTheoMa(String maThuoc) {
        return doInTransaction(em -> {
            String jpql = "SELECT t FROM Thuoc t " +
                    "LEFT JOIN FETCH t.keThuoc " +
                    "LEFT JOIN FETCH t.donViTinh " +
                    "WHERE t.maThuoc = :maThuoc";

            return em.createQuery(jpql, Thuoc.class)
                    .setParameter("maThuoc", maThuoc)
                    .getSingleResult();
        });
    }

    @Override
    public String layMaKMTheoMaThuoc(String maThuoc) {
        return doInTransaction(em -> {
            try {
                String jpql = "SELECT t.khuyenMai.maKM FROM Thuoc t WHERE t.maThuoc = :maThuoc";
                return em.createQuery(jpql, String.class).setParameter("maThuoc", maThuoc).getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        });
    }

    @Override
    public double layDonGiaTheoMaThuoc(String maThuoc) {
        return doInTransaction(em -> {
            Thuoc t = em.find(Thuoc.class, maThuoc);
            return (t != null) ? t.getGiaBan() : 0.0;
        });
    }

    @Override
    public boolean capNhatTrangThaiThuoc(String maThuoc, boolean trangThai) {
        try {
            inTransaction(em -> {
                Thuoc t = em.find(Thuoc.class, maThuoc);
                if (t != null) {
                    t.setTrangThai(trangThai);
                    em.merge(t);
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean capNhatThuoc(Thuoc thuoc) {
        try {
            inTransaction(em -> em.merge(thuoc));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Object[]> layListTHThongKe(LocalDate ngayBD, LocalDate ngayKT) {
        return doInTransaction(em -> {
            String jpql = "SELECT t.maThuoc, t.tenThuoc, SUM(c.soLuong), SUM(c.soLuong * c.donGia) " +
                    "FROM CTHoaDon c JOIN c.hoaDon hd JOIN c.thuoc t " +
                    "WHERE hd.trangThai = true AND hd.ngayLap BETWEEN :ngayBD AND :ngayKT " +
                    "GROUP BY t.maThuoc, t.tenThuoc ORDER BY SUM(c.soLuong) DESC";

            TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
            query.setParameter("ngayBD", ngayBD);
            query.setParameter("ngayKT", ngayKT);
            query.setMaxResults(10);

            return query.getResultList();
        });
    }

    @Override
    public Thuoc layThuocDeDat(String maThuoc) {
        return doInTransaction(em -> em.find(Thuoc.class, maThuoc));
    }

    @Override
    public int laySoLuongTon(String maThuoc) {
        return doInTransaction(em -> {
            try {
                String jpql = "SELECT c.soLuongTon FROM CTKho c WHERE c.thuoc.maThuoc = :maThuoc";

                Number result = em.createQuery(jpql, Number.class)
                        .setParameter("maThuoc", maThuoc)
                        .getSingleResult();

                return (result != null) ? result.intValue() : 0;
            } catch (NoResultException e) {
                return 0;
            }
        });
    }

    @Override
    public List<QuocGia> layListQG() {
        return doInTransaction(em -> em.createQuery("SELECT q FROM QuocGia q", QuocGia.class).getResultList());
    }

    @Override
    public List<Thuoc> layListThuoc(boolean isTrangThai) {
        return doInTransaction(em -> {
            String jpql = "SELECT t FROM Thuoc t " +
                    "LEFT JOIN FETCH t.keThuoc " +
                    "LEFT JOIN FETCH t.donViTinh " +
                    "LEFT JOIN FETCH t.quocGia " +
                    "WHERE t.trangThai = :trangThai " +
                    "ORDER BY t.maThuoc";
            return em.createQuery(jpql, Thuoc.class)
                    .setParameter("trangThai", isTrangThai)
                    .getResultList();
        });
    }

    @Override
    public boolean giamSoLuongTon(String maThuoc, String maDVT, int soLuongBan) {
        try {
            inTransaction(em -> {

                Double tiLe = 1.0;
                try {
                    String jpqlTiLe = "SELECT td.tiLe FROM ThuocDonViTinh td " +
                            "WHERE td.thuoc.maThuoc = :maThuoc AND td.donViTinh.maDVT = :maDVT";

                    tiLe = em.createQuery(jpqlTiLe, Double.class)
                            .setParameter("maThuoc", maThuoc)
                            .setParameter("maDVT", maDVT)
                            .getSingleResult();

                } catch (NoResultException ignored) {
                }
                double soLuongThucTe = soLuongBan * tiLe;
                String jpqlKho = "SELECT c FROM CTKho c WHERE c.thuoc.maThuoc = :maThuoc";

                CTKho ctKho = em.createQuery(jpqlKho, CTKho.class)
                        .setParameter("maThuoc", maThuoc)
                        .getSingleResult();

                int tonMoi = (int) (ctKho.getSoLuongTon() - soLuongThucTe);

                if (tonMoi < 0) {
                    throw new RuntimeException("Tồn kho không đủ");
                }
                ctKho.setSoLuongTon((int) tonMoi);
                em.merge(ctKho);
            });

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}