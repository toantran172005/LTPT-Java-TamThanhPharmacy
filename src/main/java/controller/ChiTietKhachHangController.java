package controller;

import entity.KhachHang;
import gui.ChiTietKhachHang_GUI;
import gui.TimKiemKH_GUI;
import service.KhachHangService;
import utils.ToolCtrl;

public class ChiTietKhachHangController {

    public ChiTietKhachHang_GUI ctkhGUI;
    public ToolCtrl tool;
    public KhachHangService khService;

    public ChiTietKhachHangController(ChiTietKhachHang_GUI ctkhGUI) {
        super();
        this.ctkhGUI = ctkhGUI;
        tool = new ToolCtrl();
        khService = new KhachHangService();
    }

    public void capNhatThongTin() {
        if (ctkhGUI.btnCapNhat.getText().trim().equals("Cập nhật")) {
            ctkhGUI.btnCapNhat.setText("Lưu");
            choPhepEdit(true);
        } else { // cập nhật thông tin
            if (tool.hienThiXacNhan("Cập nhật khách hàng", "Xác nhận cập nhật khách hàng?", null)) {
                KhachHang kh = kiemTraThongTin();
                if (kh == null) {
                    return;
                }

                if (khService.capNhatKhachHang(kh)) {
                    tool.hienThiThongBao("Cập nhật khách hàng", "Cập nhật khách hàng thành công!", true);
                    ctkhGUI.btnCapNhat.setText("Cập nhật");
                    choPhepEdit(false);
                } else {
                    tool.hienThiThongBao("Cập nhật khách hàng", "Không thể cập nhật khách hàng!", false);
                }
            }
        }
    }

    public KhachHang kiemTraThongTin() {
        String sdt = tool.chuyenSoDienThoai(ctkhGUI.txtSdt.getText().trim());
        if (khService.timKhachHangTheoSDT(sdt).getSdt().equals(sdt)) {
            tool.hienThiThongBao("Cập nhật", "Đã có số điện thoại này!", false);
            return null;
        }
        if (ktTenKhachHangHopLe() && ktSoDienThoaiHopLe() && ktTuoiHopLe()) {
            String maKH = ctkhGUI.txtMaKH.getText().trim();
            String tenKH = ctkhGUI.txtTenKH.getText().trim();
            String tuoi = ctkhGUI.txtTuoi.getText().trim();
            String trangThai = ctkhGUI.cmbTrangThai.getSelectedItem().toString();
            if (trangThai.equals("Hoạt động")) {
                return new KhachHang(maKH, tenKH, sdt, Integer.valueOf(tuoi), true);
            } else {
                return new KhachHang(maKH, tenKH, sdt, Integer.valueOf(tuoi), false);
            }

        }
        return null;
    }

    public boolean ktTenKhachHangHopLe() {
        String ten = ctkhGUI.txtTenKH.getText().trim();
        String regex = "^[\\p{L}\\s]+$";

        if (ten.isEmpty()) {
            tool.hienThiThongBao("Tên khách hàng không hợp lệ!", "Tên không được để trống", false);
            ctkhGUI.txtTenKH.requestFocus();
            return false;
        } else if (!ten.matches(regex)) {
            tool.hienThiThongBao("Tên khách hàng không hợp lệ!", "Tên không được chứa số hoặc ký tự đặc biệt", false);
            ctkhGUI.txtTenKH.requestFocus();
            ctkhGUI.txtTenKH.selectAll();
            return false;
        }
        return true;
    }

    public boolean ktSoDienThoaiHopLe() {
        String sdt = ctkhGUI.txtSdt.getText().trim();
        String regex = "^0\\d{9}$";

        if (sdt.isEmpty()) {
            tool.hienThiThongBao("Số điện thoại không hợp lệ!", "Không được để trống", false);
            ctkhGUI.txtSdt.requestFocus();
            return false;
        } else if (!sdt.matches(regex)) {
            tool.hienThiThongBao("Số điện thoại không hợp lệ!", "Phải gồm 10 chữ số và bắt đầu bằng 0", false);
            ctkhGUI.txtSdt.requestFocus();
            ctkhGUI.txtSdt.selectAll();
            return false;
        }
        return true;
    }

    public boolean ktTuoiHopLe() {
        String tuoiStr = ctkhGUI.txtTuoi.getText().trim();

        try {
            int tuoi = Integer.parseInt(tuoiStr);

            if (tuoi < 0) {
                tool.hienThiThongBao("Tuổi không hợp lệ!", "Tuổi không được là số âm.", false);
                ctkhGUI.txtTuoi.requestFocus();
                ctkhGUI.txtTuoi.selectAll();
                return false;
            }
        } catch (NumberFormatException e) {
            tool.hienThiThongBao("Tuổi không hợp lệ!", "Tuổi phải là số nguyên.", false);
            ctkhGUI.txtTuoi.requestFocus();
            ctkhGUI.txtTuoi.selectAll();
            return false;
        }
        return true;
    }

    public void quayLaiTKKH() {
        tool.doiPanel(ctkhGUI, new TimKiemKH_GUI());
    }

    public void choPhepEdit(boolean editable) {
        ctkhGUI.txtTenKH.setEditable(editable);
        ctkhGUI.txtSdt.setEditable(editable);
        ctkhGUI.txtTuoi.setEditable(editable);
        ctkhGUI.cmbTrangThai.setEnabled(editable);
    }

}
