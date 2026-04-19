package controller;

import gui.ThemKhachHang_GUI;
import service.KhachHangService;
import utils.ToolCtrl;

public class ThemKhachHangController {

    public ThemKhachHang_GUI tkhGUI;
    public ToolCtrl tool;
    public KhachHangService khService;

    public ThemKhachHangController(ThemKhachHang_GUI tkhGUI) {
        this.tkhGUI = tkhGUI;
        tool = new ToolCtrl();
        khService = new KhachHangService();
    }

    public void ktTatCaTruocKhiThem() {
        if (ktTenKhachHangHopLe() && ktSoDienThoaiHopLe() && ktTuoiHopLe()) {
            themKhachHang();
        }
    }

    public void themKhachHang() {
        if (tool.hienThiXacNhan("Thêm khách hàng", "Xác nhận thêm khách hàng?", null)) {
            if (khService.timKhachHangTheoSDT(tool.chuyenSoDienThoai(tkhGUI.txtSdt.getText().trim())).getSdt()
                    .equals(tool.chuyenSoDienThoai(tkhGUI.txtSdt.getText().trim()))) {
                tool.hienThiThongBao("Thêm khách hàng", "Đã có số điện thoại này!", false);

            } else {
                if (khService.themKhachHang(tool.taoKhoaChinh("KH"), tkhGUI.txtTenKH.getText().trim(),
                        tool.chuyenSoDienThoai(tkhGUI.txtSdt.getText().trim()), tkhGUI.txtTuoi.getText().trim())) {
                    lamMoi();
                }
            }
        }

    }

    public boolean ktTenKhachHangHopLe() {
        String ten = tkhGUI.txtTenKH.getText().trim();
        String regex = "^[\\p{L}\\s]+$";

        if (ten.isEmpty()) {
            tool.hienThiThongBao("Tên khách hàng không hợp lệ!", "Tên không được để trống", false);
            tkhGUI.txtTenKH.requestFocus();
            return false;
        } else if (!ten.matches(regex)) {
            tool.hienThiThongBao("Tên khách hàng không hợp lệ!", "Tên không được chứa số hoặc ký tự đặc biệt", false);
            tkhGUI.txtTenKH.requestFocus();
            tkhGUI.txtTenKH.selectAll();
            return false;
        }
        tkhGUI.txtSdt.requestFocus();
        return true;
    }

    public boolean ktSoDienThoaiHopLe() {
        String sdt = tkhGUI.txtSdt.getText().trim();
        String regex = "^0\\d{9}$";

        if (sdt.isEmpty()) {
            tool.hienThiThongBao("Số điện thoại không hợp lệ!", "Không được để trống", false);
            tkhGUI.txtSdt.requestFocus();
            return false;
        } else if (!sdt.matches(regex)) {
            tool.hienThiThongBao("Số điện thoại không hợp lệ!", "Phải gồm 10 chữ số và bắt đầu bằng 0", false);
            tkhGUI.txtSdt.requestFocus();
            tkhGUI.txtSdt.selectAll();
            return false;
        }
        tkhGUI.txtTuoi.requestFocus();
        return true;
    }

    public boolean ktTuoiHopLe() {
        String tuoiStr = tkhGUI.txtTuoi.getText().trim();

        try {
            int tuoi = Integer.parseInt(tuoiStr);

            if (tuoi < 0) {
                tool.hienThiThongBao("Tuổi không hợp lệ!", "Tuổi không được là số âm.", false);
                tkhGUI.txtTuoi.requestFocus();
                tkhGUI.txtTuoi.selectAll();
                return false;
            }
        } catch (NumberFormatException e) {
            tool.hienThiThongBao("Tuổi không hợp lệ!", "Tuổi phải là số nguyên.", false);
            tkhGUI.txtTuoi.requestFocus();
            tkhGUI.txtTuoi.selectAll();
            return false;
        }
        return true;
    }

    public void lamMoi() {
        tkhGUI.txtTenKH.setText("");
        tkhGUI.txtSdt.setText("");
        tkhGUI.txtTuoi.setText("");
        tkhGUI.txtTenKH.requestFocus();
    }

}
