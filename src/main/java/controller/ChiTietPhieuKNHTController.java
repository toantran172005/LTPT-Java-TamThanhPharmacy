package controller;

import entity.KhachHang;
import entity.NhanVien;
import entity.PhieuKhieuNaiHoTroKH;
import gui.ChiTietPhieuKNHT_GUI;
import gui.DanhSachKhieuNaiVaHoTroHK_GUI;
import service.PhieuKNHTService;
import utils.ToolCtrl;

public class ChiTietPhieuKNHTController {
    public ChiTietPhieuKNHT_GUI ctknhtGUI;
    public ToolCtrl tool;
    public PhieuKNHTService service;

    public ChiTietPhieuKNHTController(ChiTietPhieuKNHT_GUI ctknhtGUI) {
        super();
        this.ctknhtGUI = ctknhtGUI;
        this.tool = new ToolCtrl();
        this.service = new PhieuKNHTService();
    }

    public void capNhatPhieuKNHT() {
        if (ctknhtGUI.btnCapNhat.getText().equals("Cập nhật")) {
            ctknhtGUI.btnCapNhat.setText("Hoàn tất");
            choPhepEdit(true);
        } else {
            if (tool.hienThiXacNhan("Cập nhật", "Xác nhận cập nhật thông tin phiếu?", null)) {
                PhieuKhieuNaiHoTroKH knht = kiemTraThongTin();
                if (knht == null) return;

                if (service.capNhatPhieu(knht)) {
                    tool.hienThiThongBao("Cập nhập", "Cập nhật phiếu " + knht.getLoaiDon() + " thành công!", true);
                    ctknhtGUI.btnCapNhat.setText("Cập nhật");
                    choPhepEdit(false);
                } else {
                    tool.hienThiThongBao("Lỗi", "Không thể cập nhật phiếu!", false);
                }
            }
        }
    }

    public PhieuKhieuNaiHoTroKH kiemTraThongTin() {
        if (ktTenKhachHangHopLe() && ktTenNhanVienHopLe() && ktSoDienThoaiHopLe() && ktNoiDungHopLe()) {

            String tenKH = ctknhtGUI.txtTenKH.getText().trim();
            String tenNV = ctknhtGUI.txtTenNV.getText().trim();
            String sdt = tool.chuyenSoDienThoai(ctknhtGUI.txtSdt.getText().trim());
            String noiDung = ctknhtGUI.txaNoiDung.getText().trim();
            String loaiDon = ctknhtGUI.cmbLoaiDon.getSelectedItem().toString();
            String trangThai = ctknhtGUI.cmbTrangThai.getSelectedItem().toString();

            KhachHang kh = ctknhtGUI.phieu.getKhachHang();
            kh.setTenKH(tenKH);
            kh.setSdt(sdt);

            NhanVien nv = ctknhtGUI.phieu.getNhanVien();
            nv.setTenNV(tenNV);

            PhieuKhieuNaiHoTroKH phieuHienTai = ctknhtGUI.phieu;

            phieuHienTai.setNhanVien(nv);
            phieuHienTai.setKhachHang(kh);
            phieuHienTai.setNoiDung(noiDung);
            phieuHienTai.setLoaiDon(loaiDon);
            phieuHienTai.setTrangThai(trangThai);

            return phieuHienTai;
        }
        return null;
    }

    public boolean ktTenKhachHangHopLe() {
        String ten = ctknhtGUI.txtTenKH.getText().trim();
        String regex = "^[\\p{L}\\s]+$";
        if (ten.isEmpty()) {
            tool.hienThiThongBao("Lỗi", "Tên khách hàng không được để trống", false);
            ctknhtGUI.txtTenKH.requestFocus();
            return false;
        } else if (!ten.matches(regex)) {
            tool.hienThiThongBao("Lỗi", "Tên khách hàng không được chứa số hoặc ký tự đặc biệt", false);
            ctknhtGUI.txtTenKH.requestFocus();
            ctknhtGUI.txtTenKH.selectAll();
            return false;
        }
        return true;
    }

    public boolean ktTenNhanVienHopLe() {
        String ten = ctknhtGUI.txtTenNV.getText().trim();
        String regex = "^[\\p{L}\\s]+$";
        if (ten.isEmpty()) {
            tool.hienThiThongBao("Lỗi", "Tên nhân viên không được để trống", false);
            ctknhtGUI.txtTenNV.requestFocus();
            return false;
        } else if (!ten.matches(regex)) {
            tool.hienThiThongBao("Lỗi", "Tên nhân viên không được chứa số hoặc ký tự đặc biệt", false);
            ctknhtGUI.txtTenNV.requestFocus();
            ctknhtGUI.txtTenNV.selectAll();
            return false;
        }
        return true;
    }

    public boolean ktNoiDungHopLe() {
        String nd = ctknhtGUI.txaNoiDung.getText().trim();
        if (nd.isEmpty()) {
            tool.hienThiThongBao("Lỗi", "Nội dung không được để trống", false);
            ctknhtGUI.txaNoiDung.requestFocus();
            return false;
        }
        return true;
    }

    public boolean ktSoDienThoaiHopLe() {
        String sdt = ctknhtGUI.txtSdt.getText().trim();
        String regex = "^0\\d{9}$";
        if (sdt.isEmpty()) {
            tool.hienThiThongBao("Lỗi", "Số điện thoại không được để trống", false);
            ctknhtGUI.txtSdt.requestFocus();
            return false;
        } else if (!sdt.matches(regex)) {
            tool.hienThiThongBao("Lỗi", "Số điện thoại phải gồm 10 chữ số và bắt đầu bằng 0", false);
            ctknhtGUI.txtSdt.requestFocus();
            ctknhtGUI.txtSdt.selectAll();
            return false;
        }
        return true;
    }

    public void choPhepEdit(boolean edit) {
        ctknhtGUI.txtTenKH.setEditable(edit);
        ctknhtGUI.txtTenNV.setEditable(edit);
        ctknhtGUI.txtSdt.setEditable(edit);
        ctknhtGUI.txaNoiDung.setEditable(edit);
        ctknhtGUI.cmbLoaiDon.setEnabled(edit);
        ctknhtGUI.cmbTrangThai.setEnabled(edit);
    }

    public void quayLaiDanhSachKNHT() {
        DanhSachKhieuNaiVaHoTroHK_GUI dsknhtGUI = new DanhSachKhieuNaiVaHoTroHK_GUI();
        tool.doiPanel(ctknhtGUI, dsknhtGUI);
    }
}