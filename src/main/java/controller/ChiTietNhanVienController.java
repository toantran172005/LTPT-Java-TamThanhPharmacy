package controller;

import entity.NhanVien;
import gui.ChiTietNhanVien_GUI;
import service.NhanVienService; // Nâng cấp: Gọi Service thay vì gọi DAO hay Repository trực tiếp

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.toedter.calendar.JDateChooser;
import utils.ToolCtrl;

import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class ChiTietNhanVienController {
    public ChiTietNhanVien_GUI gui;
    public JComboBox<String> cmbGioiTinh;
    public JComboBox<String> cmbChucVu;
    public JButton btnChonAnh, btnCapNhat, btnLamMoi, btnLuu;
    public JTextField txtMaNV, txtTenNV, txtSdt, txtLuong, txtEmail, txtThue;
    public JDateChooser dtpNgaySinh, dtpNgayVaoLam;
    public JLabel lblTrangThai;
    public JLabel imgAnhNV;
    public String tenNV_goc, luong_goc, thue_goc, sdt_goc, chucVu_goc, gioiTinh_goc, email_goc;
    public Image anh_goc;
    public File fileAnhDaChon;
    public String duongDanAnh;
    public ToolCtrl tool = new ToolCtrl();

    // NÂNG CẤP: Sử dụng NhanVienService
    public NhanVienService nvService = new NhanVienService();

    public ChiTietNhanVienController(ChiTietNhanVien_GUI gui) {
        this.gui = gui;

        this.cmbGioiTinh = gui.getCmbGioiTinh();
        this.cmbChucVu = gui.getCmbChucVu();
        this.btnChonAnh = gui.getBtnChonAnh();
        this.btnCapNhat = gui.getBtnCapNhat();
        this.btnLamMoi = gui.getBtnLamMoi();
        this.btnLuu = gui.getBtnLuu();
        this.txtMaNV = gui.getTxtMaNV();
        this.txtTenNV = gui.getTxtTenNV();
        this.txtSdt = gui.getTxtSdt();
        this.txtLuong = gui.getTxtLuong();
        this.txtEmail = gui.getTxtEmail();
        this.txtThue = gui.getTxtThue();
        this.dtpNgaySinh = gui.getDtpNgaySinh();
        this.dtpNgayVaoLam = gui.getDtpNgayVaoLam();
        this.lblTrangThai = gui.getLblTrangThai();
        this.imgAnhNV = gui.getImgAnhNV();

        suKien();
    }

    // ========== SỰ KIỆN ==========
    public void suKien() {
        setItemComboBoxGioiTinh();
        setItemComboBoxChucVu();
        khoiTaoCheDoEdit(false);

        btnLamMoi.addActionListener(e -> khoiPhucDuLieu());
        btnCapNhat.addActionListener(e -> khoiTaoCheDoEdit(true));
        btnChonAnh.addActionListener(e -> chonAnhMoi());
        btnLuu.addActionListener(e -> capNhatNhanVien());
    }

    // ========== COMBOBOX ==========
    public void setItemComboBoxGioiTinh() {
        cmbGioiTinh.removeAllItems();
        cmbGioiTinh.addItem("Nam");
        cmbGioiTinh.addItem("Nữ");
    }

    public void setItemComboBoxChucVu() {
        cmbChucVu.removeAllItems();
        cmbChucVu.addItem("Nhân viên quản lý");
        cmbChucVu.addItem("Nhân viên bán hàng");
    }

    // ========== HIỂN THỊ THÔNG TIN NV ==========
    public void setNhanVienHienTai(NhanVien nv) {
        if (nv == null)
            return;

        txtMaNV.setText(nv.getMaNV());
        txtTenNV.setText(nv.getTenNV());
        cmbChucVu.setSelectedItem(nv.getChucVu());
        txtSdt.setText(tool.chuyenSoDienThoai(nv.getSdt()));
        txtLuong.setText(tool.dinhDangVND(nv.getLuong()));
        cmbGioiTinh.setSelectedItem(nv.getGioiTinh() ? "Nam" : "Nữ"); // Dùng getGioiTinh() thay cho isGioiTinh() với Boolean object
        dtpNgaySinh.setDate(Date.from(nv.getNgaySinh().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        dtpNgayVaoLam.setDate(Date.from(nv.getNgayVaoLam().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        // NÂNG CẤP: Gọi qua Service
        txtEmail.setText(nvService.layEmailNV(nv.getMaNV()));

        txtThue.setText(nv.getThue() != null ? nv.getThue().getMaThue() + "" : "");
        lblTrangThai.setText(nv.getTrangThai() ? "Còn làm" : "Đã nghỉ");
        imgAnhNV.setIcon(new ImageIcon(taiAnh(nv.getAnh())));

        luuDuLieuGoc();
    }

    // ========== TẢI HÌNH ẢNH ==========
    public Image taiAnh(String duongDanTuDB) {
        Image img = null;
        try {
            if (duongDanTuDB != null && !duongDanTuDB.trim().isEmpty()) {
                File file = new File(System.getProperty("user.dir") + "/src/main/resources" + duongDanTuDB);
                if (file.exists()) {
                    img = new ImageIcon(file.getAbsolutePath()).getImage();
                }
            }

            // Nếu không tìm thấy file hoặc đường dẫn rỗng -> Lấy ảnh mặc định
            if (img == null) {
                img = new ImageIcon(System.getProperty("user.dir") + "/src/main/resources/picture/default.png").getImage();
            }

            // ÉP SCALE ẢNH ĐỂ VỪA VỚI JLABEL (Kích thước chuẩn thẻ nhân viên: 150x200)
            return img.getScaledInstance(180, 220, Image.SCALE_SMOOTH);

        } catch (Exception e) {
            e.printStackTrace();
            return new ImageIcon(System.getProperty("user.dir") + "/src/main/resources/picture/default.png").getImage();
        }
    }

    // ========== CHỌN ẢNH ==========
    public void chonAnhMoi() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Chọn ảnh nhân viên");
        chooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg"));

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            fileAnhDaChon = chooser.getSelectedFile();

            // Fix lỗi không hiện ảnh khi vừa chọn xong (Do ảnh chưa được scale)
            Image imgDaChon = new ImageIcon(fileAnhDaChon.getAbsolutePath()).getImage();
            Image scaledImg = imgDaChon.getScaledInstance(180, 220, Image.SCALE_SMOOTH); // Cùng kích thước ở trên

            imgAnhNV.setIcon(new ImageIcon(scaledImg));
        }
    }

    // ========== LƯU DỮ LIỆU GỐC ==========
    public void luuDuLieuGoc() {
        tenNV_goc = txtTenNV.getText();
        sdt_goc = txtSdt.getText();
        luong_goc = txtLuong.getText();
        email_goc = txtEmail.getText();
        chucVu_goc = (String) cmbChucVu.getSelectedItem();
        gioiTinh_goc = (String) cmbGioiTinh.getSelectedItem();

        Icon icon = imgAnhNV.getIcon();
        if (icon instanceof ImageIcon) {
            anh_goc = ((ImageIcon) icon).getImage();
        }
    }

    // ========== KHÔI PHỤC ==========
    public void khoiPhucDuLieu() {
        txtTenNV.setText(tenNV_goc);
        txtSdt.setText(sdt_goc);
        txtLuong.setText(luong_goc);
        txtEmail.setText(email_goc);
        cmbChucVu.setSelectedItem(chucVu_goc);
        cmbGioiTinh.setSelectedItem(gioiTinh_goc);
        imgAnhNV.setIcon(new ImageIcon(anh_goc));
    }

    // ========== LƯU ẢNH ==========
    public String luuAnh(String maNV) {
        try {
            if (fileAnhDaChon == null)
                return null;

            String projectPath = System.getProperty("user.dir");
            // Cập nhật lại đường dẫn lưu file chuẩn xác vào thư mục Maven
            File destFolder = new File(projectPath + "/src/main/resources/picture/nhanVien");

            // Tạo thư mục nếu nó chưa tồn tại
            if (!destFolder.exists()) {
                destFolder.mkdirs();
            }

            String extension = fileAnhDaChon.getName().substring(fileAnhDaChon.getName().lastIndexOf("."));
            String tenMoi = maNV + extension;
            File fileDich = new File(destFolder, tenMoi);

            Files.copy(fileAnhDaChon.toPath(), fileDich.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            return "/picture/nhanVien/" + tenMoi;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ========== SETUP CẬP NHẬT ==========
    public void khoiTaoCheDoEdit(boolean isEdit) {
        txtTenNV.setEditable(isEdit);
        txtLuong.setEditable(isEdit);
        txtSdt.setEditable(isEdit);
        txtEmail.setEditable(isEdit);

        cmbChucVu.setEnabled(isEdit);
        cmbGioiTinh.setEnabled(isEdit);

        btnLamMoi.setVisible(isEdit);
        btnChonAnh.setVisible(isEdit);
        btnLuu.setVisible(isEdit);

        if (isEdit == true) {
            btnCapNhat.setVisible(false);
        } else {
            btnCapNhat.setVisible(true);
        }
    }

    // ========== CẬP NHẬT NV ==========
    // ========== CẬP NHẬT NV ==========
    public void capNhatNhanVien() {
        try {
            if (!kiemTraHopLe()) return;

            String maNV = txtMaNV.getText();

            NhanVien nvUpdate = nvService.timNhanVienTheoMa(maNV);
            if (nvUpdate == null) {
                tool.hienThiThongBao("Lỗi", "Không tìm thấy nhân viên trong CSDL!", false);
                return;
            }

            // SET CÁC THÔNG TIN MỚI
            nvUpdate.setTenNV(txtTenNV.getText());
            nvUpdate.setChucVu((String) cmbChucVu.getSelectedItem());
            nvUpdate.setSdt(tool.chuyenSoDienThoai(txtSdt.getText()));
            nvUpdate.setGioiTinh(cmbGioiTinh.getSelectedItem().equals("Nam"));
            nvUpdate.setNgaySinh(dtpNgaySinh.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            nvUpdate.setNgayVaoLam(dtpNgayVaoLam.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            nvUpdate.setLuong(tool.chuyenTienSangSo(txtLuong.getText()));
            nvUpdate.setTrangThai(lblTrangThai.getText().equals("Còn làm"));

            // Xử lý ảnh
            if (fileAnhDaChon != null) {
                nvUpdate.setAnh(luuAnh(maNV)); // Cập nhật đường dẫn ảnh mới
            }

            boolean kq = nvService.capNhatNhanVien(nvUpdate);

            if (kq) {
                tool.hienThiThongBao("Thành công", "Cập nhật nhân viên thành công!", true);
                khoiTaoCheDoEdit(false);
            } else {
                tool.hienThiThongBao("Lỗi", "Không thể cập nhật nhân viên!", false);
            }

        } catch (Exception e) {
            e.printStackTrace();
            tool.hienThiThongBao("Lỗi", "Đã xảy ra lỗi hệ thống!", false);
        }
    }

    // ========== KIỂM TRA HỢP LỆ ==========
    public boolean kiemTraHopLe() {
        String ten = txtTenNV.getText().trim();

        if (ten.isEmpty()) {
            tool.hienThiThongBao("Lỗi", "Tên nhân viên không được để trống!", false);
            return false;
        }
        if (!ten.matches("^[\\p{L}\\s]+$")) {
            tool.hienThiThongBao("Lỗi", "Tên nhân viên chỉ chứa chữ cái!", false);
            return false;
        }

        String sdt = txtSdt.getText().trim();
        if (!sdt.matches("^0\\d{9}$")) {
            tool.hienThiThongBao("Lỗi", "SĐT phải có 10 chữ số!", false);
            return false;
        }

        String email = txtEmail.getText().trim();
        if (!email.isEmpty() && !email.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
            tool.hienThiThongBao("Lỗi", "Email không hợp lệ!", false);
            return false;
        }

        try {
            double luong = tool.chuyenTienSangSo(txtLuong.getText());
            if (luong <= 0) {
                tool.hienThiThongBao("Lỗi", "Lương phải > 0!", false);
                return false;
            }
        } catch (Exception e) {
            tool.hienThiThongBao("Lỗi", "Lương không hợp lệ!", false);
            return false;
        }

        return true;
    }
}