package controller;

//import service.HoaDonService;
import service.HoaDonService;
import service.PhieuDoiTraService;
import service.ThuocService;
import entity.HoaDon;
import entity.PhieuDoiTra;
import gui.ChiTietPhieuDoiTra_GUI;
import gui.TimKiemHD_GUI;
import gui.TimKiemPhieuDoiTra_GUI;
import gui.TrangChuNV_GUI;
import gui.TrangChuQL_GUI;
import utils.ToolCtrl;

import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ChiTietPhieuDoiTraController {
    public ChiTietPhieuDoiTra_GUI gui;

    // NÂNG CẤP: Thay DAO bằng Service chuẩn 4 lớp
    public PhieuDoiTraService phieuDTService = new PhieuDoiTraService();
    public HoaDonService hdService = new HoaDonService();
    public ThuocService thuocService = new ThuocService();

    public ToolCtrl tool = new ToolCtrl();
    public TrangChuNV_GUI trangChuNV;
    public TrangChuQL_GUI trangChuQL;

    public ChiTietPhieuDoiTraController(ChiTietPhieuDoiTra_GUI gui) {
        this.gui = gui;
        suKien();
    }

    // ========== GẮN SỰ KIỆN ==========
    public void suKien() {
        gui.getBtnQuayLai().addActionListener(e -> quayLai());
        gui.getBtnInPhieu().addActionListener(e -> xuatPhieuDoiTraRaTXT());
    }

    // ========== HIỂN THỊ THÔNG TIN PHIẾU ĐỔI TRẢ ==========
    public void hienThiThongTinPhieuDT(PhieuDoiTra phieuDT) {
        if (phieuDT == null)
            return;

        String maPhieuDT = phieuDT.getMaPhieuDT();
        String maHD = phieuDT.getHoaDon().getMaHD();
        String tenKH = phieuDT.getHoaDon().getKhachHang().getTenKH();
        String tenNV = phieuDT.getNhanVien().getTenNV();

        // Gọi Service thay vì DAO
        HoaDon hd = hdService.timHoaDonTheoMa(maHD);
        String diaChi = (hd != null && hd.getDiaChiHT() != null) ? hd.getDiaChiHT() : "";
        String hotline = (hd != null && hd.getHotline() != null) ? hd.getHotline() : "";
        String lyDo = phieuDT.getLyDo();

        gui.getLblMaPhieuDT().setText(maPhieuDT);
        gui.getLblMaHD().setText(maHD);
        gui.getLblKhachHang().setText(tenKH);
        gui.getLblNhanVien().setText(tenNV);
        gui.getLblDiaChi().setText(diaChi);
        gui.getLblHotline().setText(tool.chuyenSoDienThoai(hotline));
        gui.getLblLyDo().setText(lyDo);

        if (phieuDT.getNgayDoiTra() instanceof LocalDate ngayDoiTra) {
            gui.getLblNgayLap().setText(tool.dinhDangLocalDate(ngayDoiTra));
        } else {
            gui.getLblNgayLap().setText("");
        }

        setDataChoTable(maPhieuDT);

        // Gọi Service
        double tongTienHoan = phieuDTService.tinhTongTienHoanTheoPhieuDT(maPhieuDT);
        gui.getLblTongTienHoan().setText(tool.dinhDangVND(tongTienHoan));
    }

    // ========== ĐƯA DỮ LIỆU VÀO BẢNG THUỐC ==========
    public void setDataChoTable(String maPhieuDT) {
        // Dùng List interface từ JPA Service
        List<Object[]> listCT = phieuDTService.layDanhSachThuocTheoPhieuDT(maPhieuDT);
        DefaultTableModel model = (DefaultTableModel) gui.getTblThuoc().getModel();
        model.setRowCount(0);
        String noiSanXuat;

        for (Object[] row : listCT) {
            double tienHoan = row[7] instanceof Number ? ((Number) row[7]).doubleValue() : 0;
            // Gọi qua Service
            noiSanXuat = thuocService.timTenQGTheoMaThuoc(row[1].toString());
            model.addRow(new Object[] {
                    row[2], // tên thuốc
                    noiSanXuat,
                    row[3], // số lượng
                    row[5], // đơn vị
                    row[6], // mức hoàn
                    tool.dinhDangVND(tienHoan),
                    row[8]  // ghi chú
            });
        }
    }

    // ========== IN PHIẾU ĐỔI TRẢ ==========
    public void xuatPhieuDoiTraRaTXT() {
        String maPhieuDT = gui.getLblMaPhieuDT().getText();
        if (maPhieuDT == null || maPhieuDT.isEmpty()) {
            tool.hienThiThongBao("Lỗi", "Không có phiếu đổi trả để xuất!", false);
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Lưu phiếu đổi trả (.txt)");
        chooser.setSelectedFile(new File("PhieuDoiTra_" + maPhieuDT + ".txt"));

        if (chooser.showSaveDialog(gui) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = chooser.getSelectedFile();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {

            bw.write("=========================================\n");
            bw.write("        PHIẾU ĐỔI TRẢ - TAM THANH         \n");
            bw.write("=========================================\n\n");

            bw.write("Mã phiếu đổi trả : " + gui.getLblMaPhieuDT().getText() + "\n");
            bw.write("Mã hóa đơn       : " + gui.getLblMaHD().getText() + "\n");
            bw.write("Ngày lập         : " + gui.getLblNgayLap().getText() + "\n");
            bw.write("Nhân viên        : " + gui.getLblNhanVien().getText() + "\n");
            bw.write("Khách hàng       : " + gui.getLblKhachHang().getText() + "\n");
            bw.write("Địa chỉ          : " + gui.getLblDiaChi().getText() + "\n");
            bw.write("Hotline          : " + gui.getLblHotline().getText() + "\n");
            bw.write("Lý do đổi trả    : " + gui.getLblLyDo().getText() + "\n\n");

            bw.write(String.format(
                    "%-25s %-15s %-8s %-10s %-10s %-15s %-20s%n",
                    "Tên thuốc", "Nơi SX", "SL", "Đơn vị", "Mức hoàn", "Tiền hoàn", "Ghi chú"));
            bw.write("----------------------------------------------------------------------------------------------\n");

            DefaultTableModel model = (DefaultTableModel) gui.getTblThuoc().getModel();
            for (int i = 0; i < model.getRowCount(); i++) {
                bw.write(String.format(
                        "%-25s %-15s %-8s %-10s %-10s %-15s %-20s%n",
                        model.getValueAt(i, 0), // Tên thuốc
                        model.getValueAt(i, 1), // Nơi SX
                        model.getValueAt(i, 2), // Số lượng
                        model.getValueAt(i, 3), // Đơn vị
                        model.getValueAt(i, 4), // Mức hoàn
                        model.getValueAt(i, 5), // Tiền hoàn
                        model.getValueAt(i, 6)  // Ghi chú
                ));
            }

            bw.write("\n-----------------------------------------\n");

            bw.write("Tổng tiền hoàn : " + gui.getLblTongTienHoan().getText() + "\n");

            bw.write("\n=========================================\n");
            bw.write("   CẢM ƠN QUÝ KHÁCH - HẸN GẶP LẠI!   \n");
            bw.write("=========================================\n");

            tool.hienThiThongBao("Xuất phiếu đổi trả", "Xuất file TXT thành công!", true);

        } catch (IOException e) {
            e.printStackTrace();
            tool.hienThiThongBao("Lỗi", "Không thể ghi file TXT!", false);
        }
    }

    // ========== QUAY LẠI TRANG TÌM KIẾM PHIẾU ĐỔI TRẢ ==========
    public void quayLai() {
        if (gui.getMainFrameQL() != null) {
            tool.doiPanel(gui, new TimKiemPhieuDoiTra_GUI(gui.getMainFrameQL()));
        } else if (gui.getMainFrameNV() != null) {
            tool.doiPanel(gui, new TimKiemPhieuDoiTra_GUI(gui.getMainFrameNV()));
        }
    }
}