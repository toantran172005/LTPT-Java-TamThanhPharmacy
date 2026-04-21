package controller;

import java.time.LocalDate;
import java.util.List;

import entity.DonViTinh;
import entity.KeThuoc;
import entity.QuocGia;
import entity.Thue;
import entity.Thuoc;
import gui.ThemThuoc_GUI;
import service.DonViTinhService;
import service.KeThuocService;
import service.ThueService;
import service.ThuocService;
import utils.ToolCtrl;

public class ThemThuocController {

    // Khai báo các Service thay thế cho các DAO
    private ThuocService thuocService = new ThuocService();
    private KeThuocService keThuocService = new KeThuocService();
    private DonViTinhService dvtService = new DonViTinhService();
    private ThueService thueService = new ThueService();

    private ToolCtrl tool = new ToolCtrl();
    private ThemThuoc_GUI themThuoc;
    private List<Thue> dsThue; // Sử dụng List thay vì ArrayList để linh hoạt hơn

    public ThemThuocController(ThemThuoc_GUI themThuoc) {
        this.themThuoc = themThuoc;
    }

    public ThemThuocController() {
        super();
    }

    // set cmb quốc gia
    public void setCmbQuocGia() {
        List<QuocGia> listQG = thuocService.layListQG();
        for (QuocGia qg : listQG) {
            themThuoc.cmbQuocGia.addItem(qg.getTenQuocGia());
        }
    }

    // set cmb kệ thuốc
    public void setCmbKeThuoc() {
        List<KeThuoc> listKT = keThuocService.layListKeThuoc();
        for (KeThuoc kt : listKT) {
            themThuoc.cmbKeThuoc.addItem(kt.getLoaiKe());
        }
    }

    // set cmb đơn vị
    public void setCmbDonVi() {
        List<DonViTinh> listDV = dvtService.layListDVT();
        for (DonViTinh dvt : listDV) {
            themThuoc.cmbDonVi.addItem(dvt.getTenDVT());
        }
    }

    // set cmb thuế
    public void setCmbThue() {
        dsThue = thueService.layListThue();
        themThuoc.cmbThue.removeAllItems();

        for (Thue th : dsThue) {
            if (!th.getLoaiThue().equalsIgnoreCase("Thuế TNCN")) {
                Double tyLe = th.getTyLeThue() * 100;
                String text = th.getLoaiThue() + " " + tyLe + "%";
                themThuoc.cmbThue.addItem(text);
            }
        }
    }

    public Thue getThueDangChon() {
        String selected = (String) themThuoc.cmbThue.getSelectedItem();
        if (selected == null)
            return null;

        for (Thue th : dsThue) {
            String text = th.getLoaiThue() + " " + (th.getTyLeThue() * 100) + "%";
            if (text.equals(selected)) {
                return th;
            }
        }
        return null;
    }

    // thêm thuốc
    public void themThuoc() {
        String tenThuoc = themThuoc.txtTenThuoc.getText().trim();
        String dangThuoc = themThuoc.txtDangThuoc.getText().trim();
        String donVi = themThuoc.cmbDonVi.getSelectedItem().toString();
        String quocGia = themThuoc.cmbQuocGia.getSelectedItem().toString();
        String keThuoc = themThuoc.cmbKeThuoc.getSelectedItem().toString();

        int soLuong = (int) themThuoc.spSoLuongTon.getValue();
        String giaBanStr = themThuoc.txtGiaBan.getText().trim();
        java.util.Date dateChooser = themThuoc.dpHanSuDung.getDate();
        String anh = themThuoc.urlAnh;

        if (tenThuoc.isEmpty() || dangThuoc.isEmpty()) {
            tool.hienThiThongBao("Lỗi", "Tên thuốc và dạng thuốc không được để trống!", false);
            return;
        }

        if (donVi.equals("Tất cả") || keThuoc.equals("Tất cả") || quocGia.equals("Tất cả")) {
            tool.hienThiThongBao("Lỗi", "Vui lòng chọn đầy đủ Đơn vị / Quốc gia / Kệ thuốc!", false);
            return;
        }

        if (soLuong < 0) {
            tool.hienThiThongBao("Lỗi", "Số lượng tồn phải >= 0!", false);
            return;
        }

        if (giaBanStr.isEmpty()) {
            tool.hienThiThongBao("Lỗi", "Giá bán không được để trống!", false);
            return;
        }

        if (dateChooser == null) {
            tool.hienThiThongBao("Lỗi", "Vui lòng chọn hạn sử dụng!", false);
            return;
        }

        LocalDate hanSuDung = tool.utilDateSangLocalDate(dateChooser);
        if (hanSuDung.isBefore(LocalDate.now())) {
            tool.hienThiThongBao("Lỗi", "Hạn sử dụng phải sau ngày hiện tại!", false);
            return;
        }

        if (anh == null) {
            tool.hienThiThongBao("Lỗi", "Vui lòng chọn ảnh thuốc!", false);
            return;
        }

        double giaBan;
        try {
            giaBan = Double.parseDouble(giaBanStr);
            if (giaBan <= 0)
                throw new NumberFormatException();
        } catch (Exception e) {
            tool.hienThiThongBao("Lỗi", "Giá bán không hợp lệ!", false);
            return;
        }

        // Chuyển sang dùng Service thay vì gọi DAO
        String maQG = thuocService.layMaQuocGiaTheoTen(quocGia);

        if (thuocService.kiemTraTrungTenVaQuocGia(tenThuoc, maQG)) {
            tool.hienThiThongBao("Cảnh báo",
                    "Thuốc '" + tenThuoc + "' thuộc quốc gia '" + quocGia + "' đã tồn tại trong hệ thống!",
                    false);
            return;
        }

        Thue thue = getThueDangChon();
        QuocGia qgObj = new QuocGia(maQG, quocGia);
        Thuoc thuoc = new Thuoc();

        thuoc.setMaThuoc(tool.taoKhoaChinh("TH"));
        thuoc.setTenThuoc(tenThuoc);
        thuoc.setDangThuoc(dangThuoc);
        thuoc.setSoLuong(soLuong);
        thuoc.setGiaBan(giaBan);
        thuoc.setHanSuDung(hanSuDung);
        thuoc.setQuocGia(qgObj);
        thuoc.setAnh(anh);

        // Chuyển sang dùng Service để tìm đối tượng liên quan
        thuoc.setDonViTinh(dvtService.timTheoTen(donVi));
        thuoc.setKeThuoc(keThuocService.timTheoTen(keThuoc));

        thuoc.setThue(thue);

        // Thêm thuốc thông qua tầng Service
        if (thuocService.themThuoc(thuoc)) {
            tool.hienThiThongBao("Thành công", "Thêm thuốc thành công!", true);
            resetForm();
        } else {
            tool.hienThiThongBao("Thất bại", "Thêm thuốc thất bại!", false);
        }
    }

    private void resetForm() {
        themThuoc.txtTenThuoc.setText("");
        themThuoc.txtDangThuoc.setText("");
        themThuoc.txtGiaBan.setText("");
        themThuoc.spSoLuongTon.setValue(0);
        themThuoc.dpHanSuDung.setDate(null);
        themThuoc.imgThuoc.setIcon(null);

        themThuoc.cmbDonVi.setSelectedIndex(0);
        themThuoc.cmbQuocGia.setSelectedIndex(0);
        themThuoc.cmbKeThuoc.setSelectedIndex(0);
        themThuoc.cmbThue.setSelectedIndex(0);
    }
}