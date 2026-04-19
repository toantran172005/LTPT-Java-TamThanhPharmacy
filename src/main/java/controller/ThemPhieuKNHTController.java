package controller;

import java.time.ZoneId;

import entity.KhachHang;
import entity.NhanVien;
import entity.PhieuKhieuNaiHoTroKH;
import entity.PhieuKhieuNaiHoTroKH;
import gui.DanhSachKhieuNaiVaHoTroHK_GUI;
import gui.ThemKhieuNai_GUI;
import service.KhachHangService;
import service.NhanVienService;
import service.PhieuKNHTService;
import utils.ToolCtrl;

public class ThemPhieuKNHTController {
    public ThemKhieuNai_GUI tknGUI;
    public ToolCtrl tool;
    public PhieuKNHTService knhtService;
    public KhachHangService khService;
    public NhanVienService nvServeice;

    public ThemPhieuKNHTController(ThemKhieuNai_GUI tknGUI) {
        super();
        this.tknGUI = tknGUI;
        tool = new ToolCtrl();
        knhtService = new PhieuKNHTService();
        khService = new KhachHangService();
        nvServeice = new NhanVienService();
    }

    public void themPhieu() {
        if (tool.hienThiXacNhan("Thêm phiếu",
                "Xác nhận thêm phiếu " + tknGUI.cmbLoaiDon.getSelectedItem().toString() + "?", null)) {
            PhieuKhieuNaiHoTroKH knht = kiemTraThongTin();
            if (knhtService.themPhieu(knht)) {
                tool.hienThiThongBao("Thêm phiếu",
                        "Thêm phiếu " + tknGUI.cmbLoaiDon.getSelectedItem().toString() + " thành công!", true);
                lamMoi();
            } else {
                tool.hienThiThongBao("Thêm phiếu", "Không thể thêm phiếu!", false);
            }
        }
    }

    public PhieuKhieuNaiHoTroKH kiemTraThongTin() {
        if (ktTenKhachHangHopLe() && ktNoiDungHopLe() && ktSoDienThoaiHopLe()) {

            String tenKH = tknGUI.txtTenKhachHang.getText().trim();
            String sdt = tool.chuyenSoDienThoai(tknGUI.txtSdt.getText().trim());
            String noiDung = tknGUI.txaNoiDung.getText().trim();
            String loaiDon = tknGUI.cmbLoaiDon.getSelectedItem().toString();

            KhachHang kh = khService.timKhachHangTheoSDT(sdt);
            NhanVien nv = nvServeice.timNhanVienTheoMa("TTNV1");

            if (kh == null) {
                khService.themKhachHang(tool.taoKhoaChinh("KH"), tenKH, String.valueOf(sdt), String.valueOf(30));
            }
            kh = khService.timKhachHangTheoSDT(sdt);

            return new PhieuKhieuNaiHoTroKH(tool.taoKhoaChinh("PKN"),
                    tknGUI.dateNgayLap.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), noiDung,
                    loaiDon, "Chờ xử lý", nv, kh);
        }

        return null;
    }

    public boolean ktTenKhachHangHopLe() {
        String ten = tknGUI.txtTenKhachHang.getText().trim();
        String regex = "^[\\p{L}\\s]+$";

        if (ten.isEmpty()) {
            tool.hienThiThongBao("Tên khách hàng không hợp lệ!", "Tên không được để trống", false);
            tknGUI.txtTenKhachHang.requestFocus();
            return false;
        } else if (!ten.matches(regex)) {
            tool.hienThiThongBao("Tên khách hàng không hợp lệ!", "Tên không được chứa số hoặc ký tự đặc biệt", false);
            tknGUI.txtTenKhachHang.requestFocus();
            tknGUI.txtTenKhachHang.selectAll();
            return false;
        }
        return true;
    }

    public boolean ktNoiDungHopLe() {
        String nd = tknGUI.txaNoiDung.getText().trim();
        if (nd.isEmpty()) {
            tool.hienThiThongBao("Nội dung không hợp lệ!", "Nội dung không được để trống", false);
            tknGUI.txaNoiDung.requestFocus();
            return false;
        }
        return true;
    }

    public boolean ktSoDienThoaiHopLe() {
        String sdt = tknGUI.txtSdt.getText().trim();
        String regex = "^0\\d{9}$";

        if (sdt.isEmpty()) {
            tool.hienThiThongBao("Số điện thoại không hợp lệ!", "Không được để trống", false);
            tknGUI.txtSdt.requestFocus();
            return false;
        } else if (!sdt.matches(regex)) {
            tool.hienThiThongBao("Số điện thoại không hợp lệ!", "Phải gồm 10 chữ số và bắt đầu bằng 0", false);
            tknGUI.txtSdt.requestFocus();
            tknGUI.txtSdt.selectAll();
            return false;
        }
        return true;
    }

    public void quayLaiDanhSach() {
        DanhSachKhieuNaiVaHoTroHK_GUI dsGUI = new DanhSachKhieuNaiVaHoTroHK_GUI();
        tool.doiPanel(tknGUI, dsGUI);
    }

    public void lamMoi() {
        tknGUI.txtTenKhachHang.setText("");
        tknGUI.txtSdt.setText("");
        tknGUI.txaNoiDung.setText("");
        tknGUI.cmbLoaiDon.setSelectedItem("Khiếu nại");
        tknGUI.dateNgayLap.setDate(null);
    }

}
