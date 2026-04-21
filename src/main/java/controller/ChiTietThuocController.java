package controller;

import java.awt.Image;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import service.DonViTinhService;
import service.KeThuocService;
import service.ThueService;
import service.ThuocService;
import utils.ToolCtrl;

import entity.DonViTinh;
import entity.KeThuoc;
import entity.QuocGia;
import entity.Thue;
import entity.Thuoc;
import gui.ChiTietThuoc_GUI;
import gui.TimKiemThuoc_GUI;

public class ChiTietThuocController {

    public ToolCtrl tool = new ToolCtrl();

    // Nâng cấp: Sử dụng Service thay vì DAO
    public ThuocService thuocService = new ThuocService();
    public KeThuocService keThuocService = new KeThuocService();
    public ThueService thueService = new ThueService();
    public DonViTinhService dvtService = new DonViTinhService();

    public ChiTietThuoc_GUI ctThuoc;
    public String duongDanAnhHienTai = null;
    public ArrayList<Thue> dsThue;

    public ChiTietThuocController(ChiTietThuoc_GUI ctThuoc) {
        this.ctThuoc = ctThuoc;
    }

    public void setCmbQuocGia() {
        ArrayList<QuocGia> listQG = new ArrayList<>(thuocService.layListQG());
        for (QuocGia qg : listQG) {
            ctThuoc.cmbQuocGia.addItem(qg.getTenQuocGia()); // Đổi thành getTenQuocGia() nếu Entity định nghĩa vậy
        }
    }

    public void setCmbKeThuoc() {
        ArrayList<KeThuoc> listKT = new ArrayList<>(keThuocService.layListKeThuoc());
        for (KeThuoc kt : listKT) {
            ctThuoc.cmbKeThuoc.addItem(kt.getLoaiKe());
        }
    }

    public void setCmbDonVi() {
        ArrayList<DonViTinh> listDV = new ArrayList<>(dvtService.layListDVT());
        for (DonViTinh dvt : listDV) {
            ctThuoc.cmbDonViTinh.addItem(dvt.getTenDVT());
        }
    }

    private String taoChuoiHienThiThue(Thue t) {
        double tyLe = t.getTyLeThue() * 100; // Khớp với tên getTyLeThue() trong Entity của bạn
        if (tyLe == (long) tyLe) {
            return String.format("%s %d%%", t.getLoaiThue(), (long) tyLe);
        } else {
            return String.format("%s %s%%", t.getLoaiThue(), tyLe);
        }
    }

    public void setCmbThue() {
        dsThue = new ArrayList<>(thueService.layListThue());
        ctThuoc.cmbThue.removeAllItems();

        for (Thue th : dsThue) {
            if (!th.getLoaiThue().equalsIgnoreCase("Thuế TNCN")) {
                ctThuoc.cmbThue.addItem(taoChuoiHienThiThue(th));
            }
        }
    }

    public Thue getThueDangChon() {
        String selected = (String) ctThuoc.cmbThue.getSelectedItem();
        if (selected == null)
            return null;

        for (Thue th : dsThue) {
            if (taoChuoiHienThiThue(th).equals(selected)) {
                return th;
            }
        }
        return null;
    }

    // Nâng cấp quan trọng: Tìm trực tiếp qua Service thay vì lấy nguyên List rồi for-loop
    public void xemChiTietThuoc(String maThuoc) {
        if (maThuoc == null || maThuoc.trim().isEmpty()) {
            tool.hienThiThongBao("Lỗi!", "Mã thuốc không hợp lệ", false);
            return;
        }

        try {
            Thuoc t = thuocService.timThuocTheoMa(maThuoc);

            if (t != null) {
                duongDanAnhHienTai = t.getAnh();
                hienThiAnhLenLabel(duongDanAnhHienTai);

                ctThuoc.txtMaSP.setText(maThuoc);
                ctThuoc.txtTenSP.setText(t.getTenThuoc());
                ctThuoc.txtDangThuoc.setText(t.getDangThuoc());
                ctThuoc.txtGiaBan.setText(tool.dinhDangVND(t.getGiaBan()));
                ctThuoc.dpHanSuDung.setDate(tool.localDateSangUtilDate(t.getHanSuDung()));

                if (t.getDonViTinh() != null) {
                    ctThuoc.cmbDonViTinh.setSelectedItem(t.getDonViTinh().getTenDVT());
                }

                if (t.getThue() != null) {
                    String textHienThi = taoChuoiHienThiThue(t.getThue());
                    ctThuoc.cmbThue.setSelectedItem(textHienThi);
                }

                if (t.getKeThuoc() != null) {
                    ctThuoc.cmbKeThuoc.setSelectedItem(t.getKeThuoc().getLoaiKe());
                }

                if (t.getQuocGia() != null && t.getQuocGia().getTenQuocGia() != null) {
                    ctThuoc.cmbQuocGia.setSelectedItem(t.getQuocGia().getTenQuocGia());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chonAnh() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn ảnh thuốc");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Hình ảnh (JPG, PNG)", "jpg", "png", "jpeg"));

        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            try {
                String projectPath = System.getProperty("user.dir");
                String folderPath = projectPath + File.separator + "resource" + File.separator + "picture"
                        + File.separator + "thuoc";

                File folder = new File(folderPath);
                if (!folder.exists()) {
                    folder.mkdirs();
                }

                String fileName = selectedFile.getName();
                File destFile = new File(folderPath + File.separator + fileName);

                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                duongDanAnhHienTai = "/picture/thuoc/" + fileName;

                ImageIcon icon = new ImageIcon(destFile.getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(240, 240, Image.SCALE_SMOOTH);
                ctThuoc.lblAnh.setIcon(new ImageIcon(img));
                ctThuoc.lblAnh.setText("");

            } catch (Exception e) {
                e.printStackTrace();
                tool.hienThiThongBao("Lỗi", "Lỗi khi lưu ảnh: " + e.getMessage(), false);
            }
        }
    }

    public void hienThiAnhLenLabel(String pathRel) {
        try {
            ctThuoc.lblAnh.setIcon(null);
            ctThuoc.lblAnh.setText("Đang tải...");

            if (pathRel == null || pathRel.isEmpty()) {
                ctThuoc.lblAnh.setText("Không có ảnh");
                return;
            }

            ImageIcon icon = null;

            java.net.URL imgURL = getClass().getResource(pathRel);
            if (imgURL != null) {
                icon = new ImageIcon(imgURL);
            }

            if (icon == null) {
                String projectPath = System.getProperty("user.dir");
                String absolutePath = projectPath + "/resource" + pathRel;
                File file = new File(absolutePath);
                if (file.exists()) {
                    icon = new ImageIcon(absolutePath);
                }
            }

            if (icon != null) {
                Image img = icon.getImage().getScaledInstance(240, 240, Image.SCALE_SMOOTH);
                ctThuoc.lblAnh.setIcon(new ImageIcon(img));
                ctThuoc.lblAnh.setText("");
            } else {
                ctThuoc.lblAnh.setText("Ảnh lỗi");
            }

        } catch (Exception e) {
            e.printStackTrace();
            ctThuoc.lblAnh.setText("Lỗi ảnh");
        }
    }

    public void xuLyCapNhat() {
        String trangThaiHienTai = ctThuoc.btnCapNhat.getText();

        if (trangThaiHienTai.equalsIgnoreCase("Cập nhật")) {
            ctThuoc.thietLapKhoaChinhSua(true);
            ctThuoc.btnCapNhat.setText("Lưu");

            String giaHienTai = ctThuoc.txtGiaBan.getText();
            try {
                String giaSo = giaHienTai.replaceAll("[^0-9]", "");
                ctThuoc.txtGiaBan.setText(giaSo);
            } catch (Exception e) {
                ctThuoc.txtGiaBan.setText("0");
            }
            ctThuoc.txtTenSP.requestFocus();

        } else {
            if (kiemTraDuLieuDauVao()) {
                Thuoc t = layThuocTuGiaoDien();
                if (t == null) return;

                boolean ketQua = thuocService.capNhatThuoc(t);

                if (ketQua) {
                    tool.hienThiThongBao("Thành công", "Cập nhật thông tin thuốc thành công!", true);
                    ctThuoc.thietLapKhoaChinhSua(false);
                    ctThuoc.btnCapNhat.setText("Cập nhật");
                    ctThuoc.txtGiaBan.setText(tool.dinhDangVND(t.getGiaBan()));

                    xemChiTietThuoc(t.getMaThuoc());
                } else {
                    tool.hienThiThongBao("Thất bại", "Cập nhật thất bại, vui lòng kiểm tra lại!", false);
                }
            }
        }
    }

    public Thuoc layThuocTuGiaoDien() {
        try {
            String maThuoc = ctThuoc.txtMaSP.getText();
            String tenThuoc = ctThuoc.txtTenSP.getText().trim();
            String dangThuoc = ctThuoc.txtDangThuoc.getText().trim();

            String giaBanStr = ctThuoc.txtGiaBan.getText().trim().replaceAll("[^0-9]", "");

            double giaBan = 0;
            try {
                giaBan = giaBanStr.isEmpty() ? 0 : Double.parseDouble(giaBanStr);
            } catch (NumberFormatException e) {
                tool.hienThiThongBao("Lỗi nhập liệu", "Giá bán chứa ký tự không hợp lệ!", false);
                return null;
            }

            java.util.Date date = ctThuoc.dpHanSuDung.getDate();
            LocalDate hanSuDung = tool.utilDateSangLocalDate(date);

            String tenDVT = ctThuoc.cmbDonViTinh.getSelectedItem().toString();
            DonViTinh dvt = dvtService.timTheoTen(tenDVT);

            Thue thue = getThueDangChon();
            if (thue == null) {
                tool.hienThiThongBao("Lỗi", "Vui lòng chọn loại thuế!", false);
                return null;
            }

            String tenKe = ctThuoc.cmbKeThuoc.getSelectedItem().toString();
            KeThuoc ke = keThuocService.timTheoTen(tenKe);

            // Nâng cấp: Không cần chạy qua DAO, trực tiếp duyệt List Quốc Gia có sẵn
            String tenQG = ctThuoc.cmbQuocGia.getSelectedItem() != null ? ctThuoc.cmbQuocGia.getSelectedItem().toString() : null;
            QuocGia qg = null;
            if (tenQG != null) {
                for (QuocGia q : thuocService.layListQG()) {
                    if (q.getTenQuocGia().equalsIgnoreCase(tenQG)) {
                        qg = q;
                        break;
                    }
                }
            }

            Thuoc t = new Thuoc();
            t.setMaThuoc(maThuoc);
            t.setTenThuoc(tenThuoc);
            t.setDangThuoc(dangThuoc);
            t.setGiaBan(giaBan);
            t.setHanSuDung(hanSuDung);
            t.setDonViTinh(dvt);
            t.setThue(thue);
            t.setKeThuoc(ke);
            t.setQuocGia(qg);
            t.setTrangThai(true);
            t.setAnh(duongDanAnhHienTai);

            return t;

        } catch (Exception e) {
            e.printStackTrace();
            tool.hienThiThongBao("Lỗi", "Lỗi xử lý dữ liệu: " + e.getMessage(), false);
            return null;
        }
    }

    public boolean kiemTraDuLieuDauVao() {
        // Nâng cấp: Ép kiểu lại Arraylist từ dữ liệu JPA
        ArrayList<KeThuoc> listKe = new ArrayList<>(keThuocService.layListKeThuoc());
        int sucChuaHienTai = 0;

        for (KeThuoc ke : listKe) {
            ArrayList<Thuoc> listThuocTrongKe = new ArrayList<>(keThuocService.layListThuocTrongKe(ke.getMaKe()));
            sucChuaHienTai = ke.getSucChua() - listThuocTrongKe.size();

            // Kiểm tra kệ đang được người dùng chọn
            if (ctThuoc.cmbKeThuoc.getSelectedItem().toString().equalsIgnoreCase(ke.getLoaiKe())) {
                if (sucChuaHienTai <= 0) {
                    tool.hienThiThongBao("Cập nhật", "Kệ thuốc " + ke.getLoaiKe() + " đã đầy!", false);
                    return false;
                }
            }
        }

        if (ctThuoc.txtTenSP.getText().trim().isEmpty()) {
            tool.hienThiThongBao("Lỗi", "Tên thuốc không được để trống", false);
            ctThuoc.txtTenSP.requestFocus();
            return false;
        }
        if (ctThuoc.txtGiaBan.getText().trim().isEmpty()) {
            tool.hienThiThongBao("Lỗi", "Giá bán không được để trống", false);
            ctThuoc.txtGiaBan.requestFocus();
            return false;
        }

        if (ctThuoc.dpHanSuDung.getDate() == null) {
            tool.hienThiThongBao("Lỗi", "Vui lòng chọn hạn sử dụng", false);
            return false;
        }
        return true;
    }

    public void quayLaiTrangTimKiem() {
        tool.doiPanel(ctThuoc, new TimKiemThuoc_GUI());
    }
}