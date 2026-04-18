package controller;

import java.util.ArrayList;
import java.util.List;

import entity.KeThuoc;
import entity.Thuoc;
import gui.ChiTietKeThuoc_GUI;
import gui.DanhSachKeThuoc_GUI;
import service.KeThuocService;
import utils.ToolCtrl;

public class ChiTietKeThuocController {

    public ChiTietKeThuoc_GUI ctktGUI;
    public ToolCtrl tool;

    private KeThuocService keService;
    public ArrayList<Thuoc> listThuoc;

    public ChiTietKeThuocController(ChiTietKeThuoc_GUI ctktGUI) {
        super();
        this.ctktGUI = ctktGUI;
        this.tool = new ToolCtrl();
        this.keService = new KeThuocService();

        List<Thuoc> dsThuoc = keService.layListThuocTrongKe(ctktGUI.ke.getMaKe());
        this.listThuoc = (dsThuoc != null) ? new ArrayList<>(dsThuoc) : new ArrayList<>();
    }

    public List<String> layTatCaTenKe() {
        return keService.layTatCaTenKe();
    }

    public void xuLyCapNhat() {
        if (ctktGUI.btnCapNhat.getText().equals("Cập nhật")) {
            choPhepEdit(true);
            ctktGUI.btnCapNhat.setText("Lưu");
        } else {
            if (tool.hienThiXacNhan("Cập nhật kệ thuốc", "Xác nhận cập nhật kệ thuốc?", null)) {
                KeThuoc kt = kiemTraThongTin();
                if (kt == null) {
                    return;
                }

                if (keService.capNhatKeThuoc(kt)) {
                    tool.hienThiThongBao("Cập nhật kệ thuốc", "Cập nhật kệ thuốc thành công!", true);
                    choPhepEdit(false);
                    ctktGUI.btnCapNhat.setText("Cập nhật");
                } else {
                    tool.hienThiThongBao("Cập nhật kệ thuốc", "Không thể cập nhật kệ thuốc!", false);
                }
            }
        }
    }

    public KeThuoc kiemTraThongTin() {
        if (kiemTraCmb() && kiemTraSucChua()) {
            String maKe = ctktGUI.txtMaKe.getText().trim();
            String loaiKe = ctktGUI.cmbLoaiKe.getSelectedItem().toString();
            int sucChua = Integer.valueOf(ctktGUI.txtSucChua.getText().trim());
            String moTa = ctktGUI.txaMoTa.getText().trim();
            String trangThai = ctktGUI.cmbTrangThai.getSelectedItem().toString();
            if (trangThai.equals("Hoạt động")) {
                return new KeThuoc(maKe, loaiKe, sucChua, moTa, true);
            } else {
                return new KeThuoc(maKe, loaiKe, sucChua, moTa, false);
            }
        }
        return null;
    }

    public boolean kiemTraCmb() {
        String text = ctktGUI.cmbLoaiKe.getSelectedItem().toString();

        if (text == null || text.trim().isEmpty()) {
            tool.hienThiThongBao("Lỗi ComboBox", "Vui lòng chọn hoặc nhập loại kệ!", false);
            return false;
        }
        ctktGUI.cmbLoaiKe.setSelectedItem(text.trim());
        return true;
    }

    public boolean kiemTraSucChua() {
        try {
            int sucChua = Integer.parseInt(ctktGUI.txtSucChua.getText().trim());

            if (sucChua > 900) {
                tool.hienThiThongBao("Lỗi sức chứa", "Sức chứa phải dưới 900!", false);
                ctktGUI.txtSucChua.selectAll();
                return false;
            }
            return true;

        } catch (NumberFormatException e) {
            tool.hienThiThongBao("Lỗi sức chứa", "Vui lòng nhập số cho sức chứa!", false);
            ctktGUI.txtSucChua.selectAll();
            return false;
        }
    }

    public void quayLaiDS() {
        tool.doiPanel(ctktGUI, new DanhSachKeThuoc_GUI());
    }

    public void choPhepEdit(boolean editable) {
        ctktGUI.txtSucChua.setEditable(editable);
        ctktGUI.txaMoTa.setEditable(editable);

        ctktGUI.cmbLoaiKe.setEnabled(editable);
        ctktGUI.cmbTrangThai.setEnabled(editable);
    }

    public void setDataChoTable(ArrayList<Thuoc> list) {
        ctktGUI.model.setRowCount(0);

        for (Thuoc thuoc : list) {
            Object[] row = { thuoc.getMaThuoc(), thuoc.getTenThuoc(), thuoc.getTrangThai() ? "Hoạt động" : "Đã xóa" };
            ctktGUI.model.addRow(row);
        }
    }
}