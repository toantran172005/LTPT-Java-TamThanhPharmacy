package controller;

import entity.PhieuDatHang;
import gui.ChiTietPhieuDatHang_GUI;
import gui.LapHoaDon_GUI;
import gui.TimKiemPhieuDatHang_GUI;
import service.DonViTinhService;
import service.PhieuDatHangService;
import service.ThuocService;
import utils.ToolCtrl;

public class ChiTietPDHController {

    public ChiTietPhieuDatHang_GUI ctpdhGUI;
    public ToolCtrl tool = new ToolCtrl();

    // NÂNG CẤP: Thay thế toàn bộ DAO bằng Service
    public PhieuDatHangService pdhService = new PhieuDatHangService();
    public DonViTinhService dvtService = new DonViTinhService();
    public ThuocService thService = new ThuocService();

    public ChiTietPDHController(ChiTietPhieuDatHang_GUI ctpdhGUI) {
        super();
        this.ctpdhGUI = ctpdhGUI;
    }

    public void taoHoaDon() {
        if (ctpdhGUI.btnCapNhat.getText().equals("Lưu")) {
            tool.hienThiThongBao("Tạo hóa đơn", "Vui lòng lưu phiếu trước khi tạo hóa đơn", false);
            return;
        } else if (ctpdhGUI.pdh.getTrangThai().equals("Đã hủy")) {
            tool.hienThiThongBao("Tạo hóa đơn", "Không thể tạo hóa đơn với phiếu đã hủy!", false);
            return;
        } else if (ctpdhGUI.daChuyenHD) {
            tool.hienThiThongBao("Tạo hóa đơn", "Phiếu này đã tạo hóa đơn rồi!", false);
            return;
        } else {
            String maPDH = ctpdhGUI.getLblMaPhieuDat().getText();
            LapHoaDon_GUI lapHD = new LapHoaDon_GUI(ctpdhGUI.getMainFrameQL());
            LapHoaDonController ctrl = new LapHoaDonController(lapHD);
            ctrl.loadTuPhieuDatHang(maPDH);
            tool.doiPanel(ctpdhGUI, lapHD);
        }
    }

    public void capNhatPDH() {
        if (ctpdhGUI.daChuyenHD) {
            tool.hienThiThongBao("Cập nhật", "Không được phép cập nhật vì phiếu này đã chuyển thành hóa đơn", false);
            return;
        } else {
            if (ctpdhGUI.btnCapNhat.getText().equals("Cập nhật")) {
                ctpdhGUI.btnCapNhat.setText("Lưu");
                ctpdhGUI.choPhepCapNhap();
            } else {
                if (tool.hienThiXacNhan("Cập nhật", "Xác nhận cập nhật trạng thái?", null)) {
                    if (capNhatTrangThai()) {
                        ctpdhGUI.btnCapNhat.setText("Cập nhật");
                        ctpdhGUI.choPhepCapNhap();
                    }
                }
            }
        }
    }

    public boolean capNhatTrangThai() {
        String trangThai = String.valueOf(ctpdhGUI.cmbTrangThai.getSelectedItem());

        // GỌI SERVICE: Trả về int y hệt như DAO cũ
        int trangThaiCapNhat = pdhService.capNhatTrangThaiPhieu(ctpdhGUI.pdh.getMaPDH(), trangThai);

        if (trangThaiCapNhat == 1) {
            tool.hienThiThongBao("Cập nhật", "Không đủ số lượng tồn kho để khôi phục phiếu này!", false);
            ctpdhGUI.cmbTrangThai.setSelectedItem(ctpdhGUI.pdh.getTrangThai());
            return false;
        } else if (trangThaiCapNhat == 0) {
            tool.hienThiThongBao("Cập nhật", "Cập nhật phiếu đặt hàng thành công!", true);
            // Cập nhật lại Object đang hiển thị trên giao diện thông qua Service
            PhieuDatHang pdhNew = pdhService.timTheoMa(ctpdhGUI.pdh.getMaPDH());
            ctpdhGUI.pdh = pdhNew;
        } else if (trangThaiCapNhat == 3) {
            // Không có gì thay đổi
            tool.hienThiThongBao("Cập nhật", "Cập nhật phiếu đặt hàng thành công!", true);
            return true;
        } else {
            tool.hienThiThongBao("Lỗi", "Lỗi cập nhật dữ liệu.", false);
            ctpdhGUI.cmbTrangThai.setSelectedItem(ctpdhGUI.pdh.getTrangThai());
            return false;
        }
        return true;
    }

    public void quayLaiTrangDanhSach() {
        if (ctpdhGUI.mainFrameQL != null) {
            ctpdhGUI.mainFrameQL.setUpNoiDung(new TimKiemPhieuDatHang_GUI(ctpdhGUI.mainFrameQL));
        } else {
            ctpdhGUI.mainFrameNV.setUpNoiDung(new TimKiemPhieuDatHang_GUI(ctpdhGUI.mainFrameNV));
        }
    }
}