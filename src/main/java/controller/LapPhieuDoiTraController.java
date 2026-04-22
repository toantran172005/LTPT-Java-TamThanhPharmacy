package controller;

import entity.*;
import service.DonViTinhService;
import service.HoaDonService;
import service.PhieuDoiTraService;
import service.ThuocService;
import gui.ChiTietHoaDon_GUI;
import gui.ChiTietPhieuDoiTra_GUI;
import gui.LapPhieuDoiTra_GUI;
import gui.TimKiemHD_GUI;
import gui.TrangChuQL_GUI;
import gui.TrangChuNV_GUI;
import utils.ToolCtrl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LapPhieuDoiTraController {
    public LapPhieuDoiTra_GUI gui;
    public HoaDonService hdService = new HoaDonService();
    public PhieuDoiTraService pdtService = new PhieuDoiTraService();
    public ToolCtrl tool = new ToolCtrl();
    public DonViTinhService dvtService = new DonViTinhService();
    public ThuocService thuocService = new ThuocService();

    public LapPhieuDoiTra_GUI getGui() {
        return gui;
    }

    public String maHD;
    public double tongTienHoan = 0;

    public LapPhieuDoiTraController(LapPhieuDoiTra_GUI gui) {
        this.gui = gui;
        suKien();
    }

    // ========== GẮN SỰ KIỆN ==========
    public void suKien() {
        gui.getBtnThem().addActionListener(e -> themThuocVaoPhieu());
        gui.getBtnXoa().addActionListener(e -> xuLyXoaDong());
        gui.getBtnLamMoi().addActionListener(e -> lamMoi());
        gui.getBtnTaoPhieuDT().addActionListener(e -> taoPhieuDoiTra());
        gui.getBtnQuayLai().addActionListener(e -> quayLai());

        gui.getTblHDThuoc().getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int selectedRow = gui.getTblHDThuoc().getSelectedRow();
                if (selectedRow != -1) {
                    String tenThuoc = gui.getTblHDThuoc().getValueAt(selectedRow, 1).toString();
                    gui.getTxtTenThuoc().setText(tenThuoc);
                }
            }
        });
    }

    // ========== GÁN MÃ HÓA ĐƠN VÀ TẢI DỮ LIỆU ==========
    public void setMaHD(String maHD) {
        this.maHD = maHD;
        gui.getLblMaHD().setText(maHD);
        taiDuLieuHoaDon(maHD);
        tinhTongTienHoan();
    }

    // ========== LẤY DỮ LIỆU TỪ HOÁ ĐƠN ==========
    public void taiDuLieuHoaDon(String maHD) {
        HoaDon hd = hdService.timHoaDonTheoMa(maHD);
        if (hd != null && hd.getKhachHang() != null) {
            gui.getLblKhachHang().setText(hd.getKhachHang().getTenKH());
        } else {
            gui.getLblKhachHang().setText("Khách lẻ");
        }
        capNhatBangThuocDaMua(maHD);
    }

    // ========== ĐƯA NHỮNG THUỐC ĐÃ MUA LÊN BẢNG ==========
    public void capNhatBangThuocDaMua(String maHD) {
        DefaultTableModel model = (DefaultTableModel) gui.getTblHDThuoc().getModel();
        model.setRowCount(0);
        String noiSanXuat;

        List<Object[]> chiTietList = hdService.layChiTietHoaDon(maHD);
        for (Object[] ct : chiTietList) {
            noiSanXuat = thuocService.timTenQGTheoMaThuoc(ct[1].toString());
            model.addRow(new Object[] { ct[1], // maThuoc
                    ct[2], // tenThuoc
                    noiSanXuat, ct[3], // soLuong
                    ct[5], // donVi
                    tool.dinhDangVND(ct[6] instanceof Number ? ((Number) ct[6]).doubleValue() : 0),
                    tool.dinhDangVND(ct[7] instanceof Number ? ((Number) ct[7]).doubleValue() : 0) });
        }

        gui.getTblHDThuoc().getColumnModel().getColumn(0).setMinWidth(0);
        gui.getTblHDThuoc().getColumnModel().getColumn(0).setMaxWidth(0);
    }

    public void themThuocVaoPhieu() {
        String tenThuoc = gui.getTxtTenThuoc().getText().trim();
        String soLuongStr = gui.getTxtSoLuong().getText().trim();
        String mucHoanStr = (String) gui.getCmbMucHoan().getSelectedItem();
        String ghiChu = gui.getTxaGhiChu().getText().trim();

        if (tenThuoc.isEmpty() || soLuongStr.isEmpty()) {
            tool.hienThiThongBao("Lỗi", "Vui lòng nhập tên thuốc và số lượng!", false);
            return;
        }

        int soLuong;
        try {
            soLuong = Integer.parseInt(soLuongStr);
            if (soLuong <= 0)
                throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            tool.hienThiThongBao("Lỗi", "Số lượng phải là số nguyên dương!", false);
            return;
        }

        JTable tblHD = gui.getTblHDThuoc();
        int row = -1;
        for (int i = 0; i < tblHD.getRowCount(); i++) {
            if (tblHD.getValueAt(i, 1).toString().equalsIgnoreCase(tenThuoc)) {
                row = i;
                break;
            }
        }

        if (row == -1) {
            tool.hienThiThongBao("Lỗi", "Thuốc không tồn tại trong hóa đơn!", false);
            return;
        }

        int slMua = Integer.parseInt(tblHD.getValueAt(row, 3).toString().replaceAll("[^0-9]", ""));
        String maThuoc = tblHD.getValueAt(row, 0).toString();
        String tenDVT = tblHD.getValueAt(row, 4).toString();
        String maDVT = dvtService.timMaDVTTheoTen(tenDVT);

        int daDoiTra = pdtService.tongSoLuongDaDoiTra(maHD, maThuoc, maDVT);
        int soLuongConLai = slMua - daDoiTra;

        if (soLuong > soLuongConLai) {
            tool.hienThiThongBao("Lỗi", "Số lượng đổi trả vượt quá số lượng còn lại!\n" + "Đã mua: " + slMua
                    + " | Đã đổi trả: " + daDoiTra + " | Còn lại: " + soLuongConLai, false);
            return;
        }

        double thanhTien = tool.chuyenTienSangSo(tblHD.getValueAt(row, 6).toString());
        double donGia = thanhTien / slMua;
        double tyLeHoan = Double.parseDouble(mucHoanStr.replace("%", "")) / 100.0;
        double tienHoan = (donGia * soLuong) * tyLeHoan;

        DefaultTableModel modelDT = (DefaultTableModel) gui.getTblPhieuDTThuoc().getModel();
        modelDT.addRow(new Object[] { tenThuoc, tblHD.getValueAt(row, 2), soLuong, tblHD.getValueAt(row, 4),
                tblHD.getValueAt(row, 5), mucHoanStr, tool.dinhDangVND(tienHoan), ghiChu.isEmpty() ? "Không" : ghiChu,
                "Xóa" });

        tinhTongTienHoan();
        lamMoiInput();
    }

    // ========== XOÁ 1 DÒNG TRONG TABLE PHIẾU ĐỔI TRẢ ==========
    public void xuLyXoaDong() {
        JTable table = gui.getTblPhieuDTThuoc();
        int row = table.getSelectedRow();

        if (row == -1) {
            tool.hienThiThongBao("Thông báo", "Vui lòng chọn dòng cần xóa!", false);
            return;
        }

        boolean confirm = tool.hienThiXacNhan("Xác nhận", "Bạn có chắc chắn muốn xoá dòng này?", null);

        if (confirm) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.removeRow(row);

            // Cập nhật lại STT
            for (int i = 0; i < model.getRowCount(); i++) {
                model.setValueAt(i + 1, i, 0);
            }

            tinhTongTienHoan();
        }
    }

    // ========== TÍNH TỔNG TIỀN HOÀN==========
    public void tinhTongTienHoan() {
        JTable tblDT = gui.getTblPhieuDTThuoc();
        DefaultTableModel model = (DefaultTableModel) tblDT.getModel();

        double tong = 0;

        for (int i = 0; i < model.getRowCount(); i++) {
            Object tienHoanObj = model.getValueAt(i, 6); // cột [6] = tiền hoàn
            if (tienHoanObj != null) {
                try {
                    double tienHoan = tool.chuyenTienSangSo(tienHoanObj.toString());
                    tong += tienHoan;
                } catch (Exception e) {
                    System.err.println("Lỗi khi cộng tiền hoàn dòng " + i + ": " + e.getMessage());
                }
            }
        }

        tongTienHoan = tong;
        gui.getLblTongTienHoan().setText(tool.dinhDangVND(tongTienHoan));
    }

    // ========== LÀM MỚI CÁC Ô NHẬP ==========
    public void lamMoiInput() {
        gui.getTxtTenThuoc().setText("");
        gui.getTxtSoLuong().setText("");
        gui.getCmbMucHoan().setSelectedIndex(0);
        gui.getTxaGhiChu().setText("");
    }

    // ========== LÀM MỚI ==========
    public void lamMoi() {
        lamMoiInput();
        DefaultTableModel modelDT = (DefaultTableModel) gui.getTblPhieuDTThuoc().getModel();
        modelDT.setRowCount(0);
        tongTienHoan = 0;
        tinhTongTienHoan();
    }

    // ========== TẠO PHIẾU ĐỔI TRẢ ==========
    public void taoPhieuDoiTra() {
        // 1. KIỂM TRA ĐIỀU KIỆN ĐẦU VÀO
        if (gui.getTblPhieuDTThuoc().getRowCount() == 0) {
            tool.hienThiThongBao("Lỗi", "Chưa có thuốc nào để đổi trả!", false);
            return;
        }

        String lyDo = gui.getTxaLyDo().getText().trim();
        if (lyDo.isEmpty()) {
            tool.hienThiThongBao("Lỗi", "Vui lòng nhập lý do đổi trả!", false);
            return;
        }

        HoaDon hd = hdService.timHoaDonTheoMa(maHD);
        if (hd == null) {
            tool.hienThiThongBao("Lỗi", "Không tìm thấy hóa đơn!", false);
            return;
        }

        NhanVien nvDangNhap = null;
        if (gui.getTrangChuQL() != null) {
            nvDangNhap = gui.getTrangChuQL().layNhanVien();
        } else if (gui.getTrangChuNV() != null) {
            nvDangNhap = gui.getTrangChuNV().layNhanVien();
        }

        if (nvDangNhap == null || nvDangNhap.getMaNV() == null) {
            tool.hienThiThongBao("Lỗi", "Không xác định được nhân viên lập phiếu!", false);
            return;
        }

        // 2. TẠO ĐỐI TƯỢNG PHIẾU (MASTER)
        PhieuDoiTra pdt = new PhieuDoiTra();
        pdt.setMaPhieuDT(tool.taoKhoaChinh("PDT"));
        pdt.setHoaDon(hd);
        pdt.setNhanVien(nvDangNhap);
        pdt.setNgayDoiTra(LocalDate.now());
        pdt.setLyDo(lyDo);

        // 3. TẠO DANH SÁCH CHI TIẾT (DETAIL) VÀ GẮN VÀO PHIẾU
        JTable tblDT = gui.getTblPhieuDTThuoc();
        List<CTPhieuDoiTra> listChiTiet = new ArrayList<>();

        for (int i = 0; i < tblDT.getRowCount(); i++) {
            String tenThuocDT = tblDT.getValueAt(i, 0).toString();
            String maThuoc = null;

            // Tìm mã thuốc tương ứng bên bảng hóa đơn
            for (int j = 0; j < gui.getTblHDThuoc().getRowCount(); j++) {
                if (gui.getTblHDThuoc().getValueAt(j, 1).toString().equalsIgnoreCase(tenThuocDT)) {
                    maThuoc = gui.getTblHDThuoc().getValueAt(j, 0).toString();
                    break;
                }
            }

            if (maThuoc == null) continue;

            String tenDVT = tblDT.getValueAt(i, 3).toString();
            String maDVT = dvtService.timMaDVTTheoTen(tenDVT);

            // Khởi tạo Object Chi Tiết
            CTPhieuDoiTra chiTiet = new CTPhieuDoiTra();
            chiTiet.setPhieuDoiTra(pdt); // Quan trọng: Gắn ngược Phiếu Master vào Chi tiết

            // Set Đối tượng Thuốc
            chiTiet.setThuoc(thuocService.timThuocTheoMa(maThuoc));

            // Set Đối tượng Đơn Vị Tính
            // (Nếu Entity CTPhieuDoiTra của bạn có quan hệ với DonViTinh, bạn phải set object DVT vào đây)
            DonViTinh donViTinh = new DonViTinh();
            donViTinh.setMaDVT(maDVT);
            chiTiet.setDonViTinh(donViTinh);

            // Set các thuộc tính số lượng, tiền
            chiTiet.setSoLuong(Integer.parseInt(tblDT.getValueAt(i, 2).toString()));
            chiTiet.setGhiChu(tblDT.getValueAt(i, 7).toString());
            chiTiet.setTienHoan(tool.chuyenTienSangSo((String) tblDT.getValueAt(i, 6)));
            chiTiet.setMucHoan(Double.parseDouble(tblDT.getValueAt(i, 5).toString().replace("%", "")) / 100.0);

            listChiTiet.add(chiTiet);
        }

        // Nhét danh sách chi tiết vào phiếu Master
        pdt.setDanhSachChiTiet(listChiTiet);

        // 4. LƯU VÀO DATABASE (JPA CASCADE SẼ TỰ ĐỘNG LƯU CẢ MASTER & DETAIL)
        if (!pdtService.themPDT(pdt)) {
            tool.hienThiThongBao("Lỗi", "Tạo phiếu đổi trả thất bại. Vui lòng kiểm tra lại cấu hình DB!", false);
            return;
        }

        // 5. CẬP NHẬT TỒN KHO THUỐC SAU KHI LƯU THÀNH CÔNG
        for (int i = 0; i < tblDT.getRowCount(); i++) {
            String tenThuocDT = tblDT.getValueAt(i, 0).toString();
            String maThuoc = null;

            for (int j = 0; j < gui.getTblHDThuoc().getRowCount(); j++) {
                if (gui.getTblHDThuoc().getValueAt(j, 1).toString().equalsIgnoreCase(tenThuocDT)) {
                    maThuoc = gui.getTblHDThuoc().getValueAt(j, 0).toString();
                    break;
                }
            }

            if (maThuoc != null) {
                String tenDVT = tblDT.getValueAt(i, 3).toString();
                String maDVT = dvtService.timMaDVTTheoTen(tenDVT);
                int soLuongTra = Integer.parseInt(tblDT.getValueAt(i, 2).toString());

                // Trả hàng nên số lượng tồn kho được TĂNG lên (true)
                thuocService.capNhatSoLuongTon(maThuoc, maDVT, soLuongTra, true);
            }
        }

        // 6. THÔNG BÁO VÀ CHUYỂN GIAO DIỆN
        tool.hienThiThongBao("Thành công", "Tạo phiếu đổi trả thành công!", true);

        // Mở màn hình xem chi tiết phiếu vừa lập
        ChiTietPhieuDoiTra_GUI chiTietPanel;
        if (gui.getTrangChuQL() != null) {
            chiTietPanel = new ChiTietPhieuDoiTra_GUI(gui.getTrangChuQL());
            gui.getTrangChuQL().setUpNoiDung(chiTietPanel);
        } else if (gui.getTrangChuNV() != null) {
            chiTietPanel = new ChiTietPhieuDoiTra_GUI(gui.getTrangChuNV());
            gui.getTrangChuNV().setUpNoiDung(chiTietPanel);
        } else {
            return;
        }

        // Load lại phiếu hoàn chỉnh từ DB để tránh lỗi LazyInitializationException
        PhieuDoiTra pdtHoanChinh = pdtService.timPhieuDoiTraTheoMa(pdt.getMaPhieuDT());
        chiTietPanel.getCtrl().hienThiThongTinPhieuDT(pdtHoanChinh);
    }

    // ========== QUAY LẠI ==========
    public void quayLai() {
        if (gui.getTrangChuQL() != null) {
            tool.doiPanel(gui, new TimKiemHD_GUI(gui.getTrangChuQL()));
        } else if (gui.getTrangChuNV() != null) {
            tool.doiPanel(gui, new TimKiemHD_GUI(gui.getTrangChuNV()));
        }
    }

}