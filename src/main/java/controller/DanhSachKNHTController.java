package controller;

import entity.PhieuKhieuNaiHoTroKH;
import gui.ChiTietPhieuKNHT_GUI;
import gui.DanhSachKhieuNaiVaHoTroHK_GUI;
import gui.ThemKhieuNai_GUI;
import service.PhieuKNHTService;
import utils.ToolCtrl;

import java.util.ArrayList;
import java.util.List;

public class DanhSachKNHTController {

    public DanhSachKhieuNaiVaHoTroHK_GUI knhtGUI;
    public PhieuKNHTService service = new PhieuKNHTService();
    public ToolCtrl tool = new ToolCtrl();
    public ArrayList<PhieuKhieuNaiHoTroKH> listDangHienThi;

    public DanhSachKNHTController(DanhSachKhieuNaiVaHoTroHK_GUI knhtGUI) {
        super();
        this.knhtGUI = knhtGUI;
    }

    public void chuyenSangThem() {
        ThemKhieuNai_GUI tknGUI = new ThemKhieuNai_GUI();
        tool.doiPanel(knhtGUI, tknGUI);
    }

    public void chuyenSangChiTiet() {
        int row = knhtGUI.tblKNHT.getSelectedRow();
        if (row == -1) {
            tool.hienThiThongBao("Thông báo", "Bạn chưa chọn dòng nào!", false);
            return;
        }

        int modelRow = knhtGUI.tblKNHT.convertRowIndexToModel(row);
        PhieuKhieuNaiHoTroKH phieu = listDangHienThi.get(modelRow);

        ChiTietPhieuKNHT_GUI ctknhtGUI = new ChiTietPhieuKNHT_GUI(phieu);
        tool.doiPanel(knhtGUI, ctknhtGUI);
    }

    public void lamMoi() {
        knhtGUI.txtTenKH.setText("");
        knhtGUI.txtTenNV.setText("");
        knhtGUI.cmbLoaiDon.setSelectedItem("Tất cả");
        knhtGUI.cmbTrangThai.setSelectedItem("Chờ xử lý");
        locTatCa();
    }

    public void locTatCa() {
        // Ép kiểu List từ JPA sang ArrayList để không làm vỡ TableModel
        List<PhieuKhieuNaiHoTroKH> listFromDb = service.getAll();
        ArrayList<PhieuKhieuNaiHoTroKH> listKNHT = new ArrayList<>(listFromDb);

        ArrayList<PhieuKhieuNaiHoTroKH> ketQua = new ArrayList<>();

        String tenKH = knhtGUI.txtTenKH.getText().trim().toLowerCase();
        String tenNV = knhtGUI.txtTenNV.getText().trim().toLowerCase();
        String loaiDon = knhtGUI.cmbLoaiDon.getSelectedItem().toString();
        String trangThai = knhtGUI.cmbTrangThai.getSelectedItem().toString();

        if (tenKH.equals("nhập tên khách hàng...")) tenKH = "";
        if (tenNV.equals("nhập tên nhân viên...")) tenNV = "";

        for (PhieuKhieuNaiHoTroKH pk : listKNHT) {
            boolean trungTenKH = tenKH.isEmpty() || pk.getKhachHang().getTenKH().toLowerCase().contains(tenKH);
            boolean trungTenNV = tenNV.isEmpty() || pk.getNhanVien().getTenNV().toLowerCase().contains(tenNV);
            boolean trungLoaiDon = loaiDon.equals("Tất cả") || pk.getLoaiDon().equalsIgnoreCase(loaiDon);


            String statusDB = pk.getTrangThai();
            boolean trungTrangThai = false;

            if (trangThai.equals("Tất cả")) {
                trungTrangThai = true;
            } else if (statusDB != null && statusDB.length() >= 2) {
                trungTrangThai = statusDB.substring(0, 2).equalsIgnoreCase(trangThai.substring(0, 2));
            }

            if (trungTenKH && trungTenNV && trungLoaiDon && trungTrangThai) {
                ketQua.add(pk);
            }
        }

        listDangHienThi = ketQua;
        setDataChoTable(ketQua);
    }

    public void setDataChoTable(ArrayList<PhieuKhieuNaiHoTroKH> list) {
        if (list == null) return;

        knhtGUI.model.setRowCount(0);

        for (PhieuKhieuNaiHoTroKH pk : list) {
            String tenKH = (pk.getKhachHang() != null) ? pk.getKhachHang().getTenKH() : "Không xác định";
            String tenNV = (pk.getNhanVien() != null) ? pk.getNhanVien().getTenNV() : "Chưa phân công";

            Object[] row = {
                    pk.getMaPhieu(),
                    tenKH,
                    tenNV,
                    pk.getLoaiDon(),
                    tool.dinhDangLocalDate(pk.getNgayLap()),
                    pk.getTrangThai()
            };
            knhtGUI.model.addRow(row);
        }
    }
}