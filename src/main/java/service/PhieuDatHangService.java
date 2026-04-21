package service;

import entity.*;
import repository.impl.PhieuDatHangRepositoryImpl;
import repository.intf.PhieuDatHangRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class PhieuDatHangService {

    private final PhieuDatHangRepository pdhRepo = new PhieuDatHangRepositoryImpl();

    public ArrayList<PhieuDatHang> layListPhieuDatHang() {
        List<PhieuDatHang> list = pdhRepo.layListPhieuDatHang();
        // Ép kiểu chuẩn xác sang ArrayList cho giao diện Swing
        return list != null ? new ArrayList<>(list) : new ArrayList<>();
    }

    public PhieuDatHang timTheoMa(String maPDH) {
        if (maPDH == null || maPDH.trim().isEmpty()) {
            return null;
        }
        return pdhRepo.timTheoMa(maPDH.trim());
    }

    public boolean themPhieuDatHang(PhieuDatHang pdh) {
        if (pdh == null || pdh.getMaPDH() == null || pdh.getMaPDH().trim().isEmpty()) {
            return false;
        }
        return pdhRepo.themPhieuDatHang(pdh);
    }

    public boolean capNhatTrangThai(PhieuDatHang pdh, String trangThaiMoi) {
        if (pdh == null || trangThaiMoi == null || trangThaiMoi.trim().isEmpty()) {
            return false;
        }
        // Logic cập nhật: Set trạng thái mới rồi đẩy xuống merge
        pdh.setTrangThai(trangThaiMoi);
        return pdhRepo.capNhatTrangThai(pdh);
    }

    public int capNhatTrangThaiPhieu(String maPDH, String trangThaiMoi) {
        if (maPDH == null || maPDH.trim().isEmpty()) return 2;
        if (trangThaiMoi == null || trangThaiMoi.trim().isEmpty()) return 2;

        return pdhRepo.capNhatTrangThaiPhieu(maPDH.trim(), trangThaiMoi.trim());
    }

    public ArrayList<Object[]> layDanhSachThuocTheoPDH(String maPDH) {
        if (maPDH == null || maPDH.trim().isEmpty()) return new ArrayList<>();

        List<Object[]> listDb = pdhRepo.layDanhSachThuocTheoPDH(maPDH);
        ArrayList<Object[]> ketQua = new ArrayList<>();

        for (Object[] row : listDb) {
            int soLuong = (int) row[3];
            double donGia = (double) row[6];
            KhuyenMai km = (KhuyenMai) row[7];

            double thanhTien = soLuong * donGia;

            if (km != null && km.getTrangThai()) {
                String loaiKM = km.getLoaiKM();
                if ("Giảm giá".equalsIgnoreCase(loaiKM)) {
                    thanhTien = soLuong * donGia * (1 - km.getMucKM() / 100.0);
                } else if ("Mua tặng".equalsIgnoreCase(loaiKM)) {
                    int mua = km.getSoLuongMua();
                    int tang = km.getSoLuongTang();
                    int soSuatTang = soLuong / (mua + tang);
                    thanhTien = (soLuong - (soSuatTang * tang)) * donGia;
                }
            }

            Object[] newRow = {
                    row[0], // maPDH
                    row[1], // maThuoc
                    row[2], // tenThuoc
                    soLuong,
                    row[4], // maDVT
                    row[5], // tenDVT
                    donGia,
                    thanhTien
            };
            ketQua.add(newRow);
        }
        return ketQua;
    }

    public int taoPhieuDatHangVaChiTiet(String maPDH, String maKH, String maNV, LocalDate ngayDat, LocalDate ngayHen, String ghiChu, List<Object[]> dsChiTiet) {
        if (maPDH == null || dsChiTiet == null || dsChiTiet.isEmpty()) return -1;

        // 1. Khởi tạo và thiết lập PhieuDatHang
        PhieuDatHang pdh = new PhieuDatHang();
        pdh.setMaPDH(maPDH);
        pdh.setNgayDat(ngayDat);
        pdh.setNgayHen(ngayHen);
        pdh.setGhiChu(ghiChu);
        pdh.setTrangThai("Chờ hàng");
        pdh.setDiaChiHT("456 Nguyễn Huệ, TP.HCM");
        pdh.setTenHT("Hiệu Thuốc Tam Thanh");
        pdh.setHotline("+84-912345689");

        // Set Khóa ngoại bằng Proxy Object (JPA tự hiểu đây là khóa ngoại)
        KhachHang kh = new KhachHang(); kh.setMaKH(maKH);
        pdh.setKhachHang(kh);

        NhanVien nv = new NhanVien(); nv.setMaNV(maNV);
        pdh.setNhanVien(nv);

        // 2. Map List<Object[]> sang List<ChiTietPhieuDatHang>
        List<CTPhieuDatHang> listCT = new ArrayList<>();
        for (Object[] row : dsChiTiet) {
            CTPhieuDatHang ct = new CTPhieuDatHang();

            Thuoc thuoc = new Thuoc(); thuoc.setMaThuoc((String) row[0]);
            ct.setThuoc(thuoc);

            ct.setSoLuong((int) row[1]);

            DonViTinh dvt = new DonViTinh(); dvt.setMaDVT((String) row[2]);
            ct.setDonViTinh(dvt);

            ct.setDonGia((double) row[3]);

            listCT.add(ct);
        }

        // KHÔNG GỌI pdh.setChiTietPhieuDatHangs(listCT) nữa vì Entity không có hàm này.
        // Thay vào đó, truyền cả 2 vào Repository:

        return pdhRepo.taoPhieuDatHangVaChiTiet(pdh, listCT);
    }
}