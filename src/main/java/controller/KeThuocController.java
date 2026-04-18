package controller;

import java.util.ArrayList;
import java.util.List;

import entity.KeThuoc;
import entity.Thuoc;
import gui.ChiTietKeThuoc_GUI;
import gui.DanhSachKeThuoc_GUI;
import service.KeThuocService;
import utils.ToolCtrl;

public class KeThuocController {

    public DanhSachKeThuoc_GUI ktGUI;
    private KeThuocService keService = new KeThuocService();
    public ToolCtrl tool = new ToolCtrl();

    public ArrayList<KeThuoc> listKe;
    public boolean hienThiHoatDong = true;

    public KeThuocController(DanhSachKeThuoc_GUI ktGUI) {
        super();
        this.ktGUI = ktGUI;
    }

    public void xemChiTietKT() {
        int selectedRow = ktGUI.tblKeThuoc.getSelectedRow();
        if (selectedRow == -1) {
            tool.hienThiThongBao("Lỗi", "Vui lòng chọn một phiếu để xem chi tiết!", false);
            return;
        }
        int modelRow = ktGUI.tblKeThuoc.convertRowIndexToModel(selectedRow);
        Object maObj = ktGUI.tblKeThuoc.getModel().getValueAt(modelRow, 0);
        String maKe = maObj == null ? "" : maObj.toString();

        KeThuoc ke = null;
        for (KeThuoc k : listKe) {
            if (k.getMaKe().equals(maKe)) {
                ke = k;
                break;
            }
        }

        if (ke != null) {
            ChiTietKeThuoc_GUI ctKTGUI = new ChiTietKeThuoc_GUI(ke);
            tool.doiPanel(ktGUI, ctKTGUI);
        }
    }

    public void xoaKeThuoc() {
        listKe = layListKeThuoc();
        if (ktGUI.btnXoa.getText().equals("Xóa")) {
            if (tool.hienThiXacNhan("Xóa kệ thuốc", "Xác nhận xóa kệ thuốc?", null)) {
                int viewRow = ktGUI.tblKeThuoc.getSelectedRow();
                if (viewRow != -1) {
                    int modelRow = ktGUI.tblKeThuoc.convertRowIndexToModel(viewRow);
                    Object maObj = ktGUI.tblKeThuoc.getModel().getValueAt(modelRow, 0);
                    String maKe = maObj == null ? "" : maObj.toString();

                    if (keService.xoaKeThuoc(maKe)) {
                        tool.hienThiThongBao("Xóa kệ thuốc", "Đã xóa kệ thuốc thành công!", true);
                    }
                }
                locTatCa(hienThiHoatDong);
            }
        } else {
            if (tool.hienThiXacNhan("Khôi phục kệ thuốc", "Xác nhận khôi phục kệ thuốc?", null)) {
                int viewRow = ktGUI.tblKeThuoc.getSelectedRow();
                if (viewRow != -1) {
                    int modelRow = ktGUI.tblKeThuoc.convertRowIndexToModel(viewRow);
                    Object maObj = ktGUI.tblKeThuoc.getModel().getValueAt(modelRow, 0);
                    String maKe = maObj == null ? "" : maObj.toString();

                    if (keService.khoiPhucKeThuoc(maKe)) {
                        tool.hienThiThongBao("Khôi phục kệ thuốc", "Đã khôi phục kệ thuốc thành công!", true);
                    }
                }
                locTatCa(hienThiHoatDong);
            }
        }
    }

    public void xuLyBtnLichSuXoa() {
        hienThiHoatDong = !hienThiHoatDong;
        ktGUI.btnLichSuXoa.setText(!hienThiHoatDong ? "Danh sách hiện tại" : "Lịch sử xóa");
        ktGUI.btnXoa.setText(!hienThiHoatDong ? "Khôi phục" : "Xóa");
        locTatCa(hienThiHoatDong);
    }

    public void lamMoi() {
        ktGUI.cmbLoaiKe.setSelectedItem("Tất cả");
        ktGUI.cmbSucChua.setSelectedItem("Tất cả");
        locTatCa(hienThiHoatDong);
    }

    public void locTatCa(boolean hienThiHoatDong) {
        listKe = layListKeThuoc();
        ArrayList<KeThuoc> ketQua = new ArrayList<>();

        String loaiKe = ktGUI.cmbLoaiKe.getSelectedItem().toString();
        String sucChuaStr = ktGUI.cmbSucChua.getSelectedItem().toString();

        for (KeThuoc ke : listKe) {
            boolean trungLoaiKe = loaiKe.equals("Tất cả") || ke.getLoaiKe().equalsIgnoreCase(loaiKe);

            boolean trungSucChua = true;
            if (!sucChuaStr.equals("Tất cả")) {
                int max = Integer.parseInt(sucChuaStr.replace("<", "").trim());
                trungSucChua = ke.getSucChua() < max;
            }

            if (trungLoaiKe && trungSucChua && ke.getTrangThai() == hienThiHoatDong) {
                ketQua.add(ke);
            }
        }

        setDataChoTable(ketQua);
    }

    public ArrayList<KeThuoc> layListKeThuoc() {
        // Ép List của JPA về dạng ArrayList để không báo lỗi GUI
        return new ArrayList<>(keService.layListKeThuoc());
    }

    public ArrayList<String> layListTenKe() {
        return new ArrayList<>(keService.layTatCaTenKe());
    }

    public void setDataChoTable(ArrayList<KeThuoc> list) {
        ktGUI.model.setRowCount(0);
        int sucChuaHienTai = 0;

        for (KeThuoc ke : list) {
            // Lấy danh sách thuốc thông qua Service
            List<Thuoc> listThuocTrongKe = keService.layListThuocTrongKe(ke.getMaKe());
            // Tránh NullPointerException nếu list rỗng
            int soLuongThuoc = (listThuocTrongKe != null) ? listThuocTrongKe.size() : 0;

            sucChuaHienTai = ke.getSucChua() - soLuongThuoc;
            Object[] row = { ke.getMaKe(), ke.getLoaiKe(), ke.getSucChua(), sucChuaHienTai, ke.getMoTa(),
                    ke.getTrangThai() ? "Hoạt động" : "Đã xóa" };
            ktGUI.model.addRow(row);
        }
    }
}