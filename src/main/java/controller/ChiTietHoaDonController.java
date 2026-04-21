package controller;

import service.HoaDonService;
//import service.PhieuDoiTraService;
import service.PhieuDoiTraService;
import service.ThuocService;
import entity.HoaDon;
import gui.*;
import utils.ToolCtrl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChiTietHoaDonController {
    public ChiTietHoaDon_GUI gui;
    public HoaDonService hdService = new HoaDonService(); // Dùng Service thay vì DAO
    public ToolCtrl tool = new ToolCtrl();
    public ThuocService thuocService = new ThuocService();
    public PhieuDoiTraService pdtService = new PhieuDoiTraService();
    public HoaDon hd;

    public ChiTietHoaDonController(ChiTietHoaDon_GUI gui) {
        this.gui = gui;
        suKien();
    }

    // ========== GẮN SỰ KIỆN ==========
    public void suKien() {
        gui.getBtnQuayLai().addActionListener(e -> quayLai());
        gui.getBtnInHoaDon().addActionListener(e -> xuatHoaDonRaTXT());
        gui.getBtnTaoPhieuDoiTra().addActionListener(e -> taoPhieuDoiTra());
    }

    // ========== HIỂN THỊ THÔNG TIN HOÁ ĐƠN ==========
    public void hienThiThongTinHoaDon(HoaDon hd) {
        if (hd == null) return;
        this.hd = hd;

        gui.getLblMaHD().setText(hd.getMaHD());
        gui.getLblNgayLap().setText(tool.dinhDangLocalDate(hd.getNgayLap()));
        gui.getLblNhanVien().setText(hd.getNhanVien() != null ? hd.getNhanVien().getTenNV() : "Không xác định");
        gui.getLblKhachHang().setText(hd.getKhachHang() != null ? hd.getKhachHang().getTenKH() : "Khách lẻ");
        gui.getLblGhiChu().setText(hd.getGhiChu() != null ? hd.getGhiChu() : "Không có");
        gui.getLblDiaChi().setText(hd.getDiaChiHT() != null ? hd.getDiaChiHT() : "Chưa có");
        gui.getLblHotline().setText(hd.getHotline() != null ? tool.chuyenSoDienThoai(hd.getHotline()) : "Chưa có");

        // Gọi qua Service
        double tongTien = hdService.tinhTongTienTheoHoaDon(hd.getMaHD());
        double tienNhan = hd.getTienNhan();
        double tienThua = Math.max(0, tienNhan - tongTien);

        gui.getLblTongTien().setText(tool.dinhDangVND(tongTien));
        gui.getLblTienNhan().setText(tool.dinhDangVND(tienNhan));
        gui.getLblTienThua().setText(tool.dinhDangVND(tienThua));

        capNhatBangChiTiet(hd.getMaHD());
    }

    // ========== HIỂN THỊ THUỐC LÊN BẢNG ==========
    public void capNhatBangChiTiet(String maHD) {
        DefaultTableModel model = (DefaultTableModel) gui.getTblThuoc().getModel();
        model.setRowCount(0);
        String noiSanXuat;

        List<Object[]> chiTietList = new ArrayList<>(hdService.layChiTietHoaDon(maHD)); // Ép kiểu ArrayList
        for (Object[] ct : chiTietList) {
            noiSanXuat = thuocService.timTenQGTheoMaThuoc(ct[1].toString());
            model.addRow(new Object[] {
                    ct[2], // tenThuoc
                    noiSanXuat,
                    ct[3], // soLuong
                    ct[5], // donVi
                    tool.dinhDangVND(ct[6] instanceof Number ? ((Number) ct[6]).doubleValue() : 0),
                    tool.dinhDangVND(ct[7] instanceof Number ? ((Number) ct[7]).doubleValue() : 0)
            });
        }
    }

    // ========== IN HOÁ ĐƠN ==========
    public void xuatHoaDonRaTXT() {
        String maHD = gui.getLblMaHD().getText();
        if (maHD == null || maHD.isEmpty()) {
            tool.hienThiThongBao("Lỗi", "Không có hóa đơn để xuất!", false);
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Lưu hóa đơn (.txt)");
        chooser.setSelectedFile(new File("HoaDon_" + maHD + ".txt"));

        if (chooser.showSaveDialog(gui) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = chooser.getSelectedFile();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {

            // ===== TIÊU ĐỀ =====
            bw.write("=========================================\n");
            bw.write("         HÓA ĐƠN BÁN THUỐC TAM THANH       \n");
            bw.write("=========================================\n\n");

            // ===== THÔNG TIN CHUNG =====
            bw.write("Mã hóa đơn   : " + gui.getLblMaHD().getText() + "\n");
            bw.write("Ngày lập     : " + gui.getLblNgayLap().getText() + "\n");
            bw.write("Nhân viên    : " + gui.getLblNhanVien().getText() + "\n");
            bw.write("Khách hàng   : " + gui.getLblKhachHang().getText() + "\n");
            bw.write("Địa chỉ      : " + gui.getLblDiaChi().getText() + "\n");
            bw.write("Hotline      : " + gui.getLblHotline().getText() + "\n");
            bw.write("Ghi chú      : " + gui.getLblGhiChu().getText() + "\n\n");

            // ===== BẢNG CHI TIẾT =====
            bw.write(String.format("%-25s %-15s %-8s %-10s %-15s %-15s%n", "Tên thuốc", "Nơi SX", "SL", "Đơn vị", "Đơn giá", "Thành tiền"));
            bw.write("--------------------------------------------------------------------------\n");

            DefaultTableModel model = (DefaultTableModel) gui.getTblThuoc().getModel();
            for (int i = 0; i < model.getRowCount(); i++) {
                bw.write(String.format("%-25s %-15s %-8s %-10s %-15s %-15s%n",
                        model.getValueAt(i, 0), model.getValueAt(i, 1), model.getValueAt(i, 2),
                        model.getValueAt(i, 3), model.getValueAt(i, 4), model.getValueAt(i, 5)));
            }

            bw.write("\n-----------------------------------------\n");

            // ===== TỔNG TIỀN =====
            bw.write("Tổng tiền : " + gui.getLblTongTien().getText() + "\n");
            bw.write("Tiền nhận : " + gui.getLblTienNhan().getText() + "\n");
            bw.write("Tiền thừa : " + gui.getLblTienThua().getText() + "\n");

            bw.write("\n-----------------------------------------\n");
            bw.write("(Giá trên đã bao gồm thuế GTGT)\n");

            bw.write("\n=========================================\n");
            bw.write("   CẢM ƠN QUÝ KHÁCH - HẸN GẶP LẠI!   \n");
            bw.write("=========================================\n");

            tool.hienThiThongBao("Xuất hóa đơn", "Xuất file TXT thành công!", true);

        } catch (IOException e) {
            e.printStackTrace();
            tool.hienThiThongBao("Lỗi", "Không thể ghi file TXT!", false);
        }
    }

    // ========== QUAY LẠI TRANG TÌM KIẾM HOÁ ĐƠN ==========
    public void quayLai() {
        if (gui.getMainFrameQL() != null) {
            tool.doiPanel(gui, new TimKiemHD_GUI(gui.getMainFrameQL()));
        } else if (gui.getMainFrameNV() != null) {
            tool.doiPanel(gui, new TimKiemHD_GUI(gui.getMainFrameNV()));
        }
    }

    // ========== TẠO PHIẾU ĐỔI TRẢ ==========
    public void taoPhieuDoiTra() {
        if (pdtService.kiemTraHoaDonDaDoiTra(hd.getMaHD())) {
            tool.hienThiThongBao("Tạo phiếu đổi trả", "Hóa đơn này đã tạo phiếu đổi trả", false);
            return;
        } else {
            String maHD = gui.getLblMaHD().getText();
            if (maHD.isEmpty()) {
                tool.hienThiThongBao("Lỗi", "Không có hóa đơn để đổi trả!", false);
                return;
            }

            LapPhieuDoiTra_GUI panel = new LapPhieuDoiTra_GUI();
            panel.setTrangChuQL(gui.getMainFrameQL());
            panel.setTrangChuNV(gui.getMainFrameNV());

            LapPhieuDoiTraController ctrl = new LapPhieuDoiTraController(panel);
            ctrl.setMaHD(maHD);

            if (gui.getMainFrameQL() != null) {
                gui.getMainFrameQL().setUpNoiDung(panel);
            } else if (gui.getMainFrameNV() != null) {
                gui.getMainFrameNV().setUpNoiDung(panel);
            }
        }
    }
}