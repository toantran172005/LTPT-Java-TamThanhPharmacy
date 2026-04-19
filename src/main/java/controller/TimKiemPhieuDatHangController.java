package controller;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import entity.PhieuDatHang;
import gui.ChiTietPhieuDatHang_GUI;
import gui.TimKiemPhieuDatHang_GUI;
import service.PhieuDatHangService;
import utils.ToolCtrl;

public class TimKiemPhieuDatHangController {

    public TimKiemPhieuDatHang_GUI tkpdhGUI;
    public PhieuDatHangService pdhService = new PhieuDatHangService();
    public ToolCtrl tool = new ToolCtrl();
    public ArrayList<PhieuDatHang> dsPDH;

    public TimKiemPhieuDatHangController(TimKiemPhieuDatHang_GUI tkpdhGUI) {
        this.tkpdhGUI = tkpdhGUI;
    }

    public ArrayList<PhieuDatHang> layTatCaPhieuDatHang() {
        return dsPDH = pdhService.layListPhieuDatHang();
    }

    public PhieuDatHang timTheoMa(String maPhieu) {
        for (PhieuDatHang pdh : dsPDH) {
            if (pdh.getMaPDH().equals(maPhieu)) {
                return pdh;
            }
        }
        return null;
    }

    public void moTrangChiTiet() {
        int selectedRow = tkpdhGUI.tblPhieuDatHang.getSelectedRow();
        if (selectedRow == -1) {
            tool.hienThiThongBao("Lỗi", "Vui lòng chọn một phiếu để xem chi tiết!", false);
            return;
        }

        String maPhieu = tkpdhGUI.tblPhieuDatHang.getValueAt(selectedRow, 0).toString();
        PhieuDatHang pdh = timTheoMa(maPhieu);

        if (pdh == null) {
            tool.hienThiThongBao("Lỗi", "Không tìm thấy thông tin phiếu!", false);
            return;
        }

        ChiTietPhieuDatHang_GUI chiTiet;
        if (tkpdhGUI.getMainFrameQL() != null) {
            chiTiet = new ChiTietPhieuDatHang_GUI(tkpdhGUI.getMainFrameQL(), pdh);
            tkpdhGUI.getMainFrameQL().setUpNoiDung(chiTiet);
        } else {
            chiTiet = new ChiTietPhieuDatHang_GUI(tkpdhGUI.getMainFrameNV(), pdh);
            tkpdhGUI.getMainFrameNV().setUpNoiDung(chiTiet);
        }
    }

    public void locTatCa() {
        dsPDH = pdhService.layListPhieuDatHang();
        String tenKH = tkpdhGUI.txtTenKH.getText().trim();
        String tenNV = tkpdhGUI.txtTenNV.getText().trim();
        String trangThai = (String) tkpdhGUI.cmbTrangThai.getSelectedItem();

        String nameRegex = "^[\\p{L} .'-]+$";

        if (!tenKH.isEmpty() && !tenKH.matches(nameRegex)) {
            tool.hienThiThongBao("Lỗi định dạng",
                    "Tên khách hàng không hợp lệ.\nVui lòng chỉ nhập chữ và khoảng trắng.", false);
            return;
        }
        if (!tenNV.isEmpty() && !tenNV.matches(nameRegex)) {
            tool.hienThiThongBao("Lỗi định dạng", "Tên nhân viên không hợp lệ.\nVui lòng chỉ nhập chữ và khoảng trắng.",
                    false);
            return;
        }

        ArrayList<PhieuDatHang> list = layTatCaPhieuDatHang();
        if (list == null)
            list = new ArrayList<>();

        ArrayList<PhieuDatHang> listLoc = new ArrayList<>();
        for (PhieuDatHang pdh : list) {
            boolean match = true;

            if (!tenKH.isEmpty()) {
                if (pdh.getKhachHang() == null || pdh.getKhachHang().getTenKH() == null
                        || !pdh.getKhachHang().getTenKH().toLowerCase().contains(tenKH.toLowerCase())) {
                    match = false;
                }
            }

            if (!tenNV.isEmpty()) {
                if (pdh.getNhanVien() == null || pdh.getNhanVien().getTenNV() == null
                        || !pdh.getNhanVien().getTenNV().toLowerCase().contains(tenNV.toLowerCase())) {
                    match = false;
                }
            }

            if (trangThai != null && !trangThai.equalsIgnoreCase("Tất cả")) {
                String tt = pdh.getTrangThai();
                if (tt == null || !tt.equalsIgnoreCase(trangThai)) {
                    match = false;
                }
            }

            if (match)
                listLoc.add(pdh);
        }

        setDataChoTable(listLoc);
    }

    public void lamMoi() {
        tkpdhGUI.txtTenKH.setText("");
        tkpdhGUI.txtTenNV.setText("");
        tkpdhGUI.cmbTrangThai.setSelectedItem("Tất cả");
        locTatCa();
    }

    public void setDataChoTable(ArrayList<PhieuDatHang> list) {
        DefaultTableModel model = (DefaultTableModel) tkpdhGUI.getTblPhieuDatHang().getModel();
        model.setRowCount(0);

        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        for (PhieuDatHang pdh : list) {
            Object[] row = {
                    pdh.getMaPDH(),
                    pdh.getNhanVien() != null ? pdh.getNhanVien().getTenNV() : "",
                    pdh.getKhachHang() != null ? pdh.getKhachHang().getTenKH() : "",
                    pdh.getNgayDat() != null ? pdh.getNgayDat().format(df) : "",
                    pdh.getNgayHen() != null ? pdh.getNgayHen().format(df) : "",
                    pdh.getTrangThai() != null ? pdh.getTrangThai() : ""
            };
            model.addRow(row);
        }
    }
}