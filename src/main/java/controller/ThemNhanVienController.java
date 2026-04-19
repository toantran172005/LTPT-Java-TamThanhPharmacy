package controller;

import entity.NhanVien;
import entity.TaiKhoan;
import entity.Thue;
import gui.ThemNhanVien_GUI;
import service.NhanVienService; // Nâng cấp: Dùng Service thay cho DAO
import service.ThueService;     // Nâng cấp: Dùng Service thay cho DAO
import utils.ToolCtrl;

import javax.swing.*;
import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;

public class ThemNhanVienController {
    public ThemNhanVien_GUI gui;
    public File fileAnhDaChon;
    public ToolCtrl tool = new ToolCtrl();

    // Khởi tạo Service
    private NhanVienService nvService = new NhanVienService();
    private ThueService thueService = new ThueService();

    public ThemNhanVienController(ThemNhanVien_GUI gui) {
        this.gui = gui;
        suKien();
        setItemComboBox();
    }

    // ========== SỰ KIỆN ==========
    public void suKien() {
        gui.getBtnChonAnh().addActionListener(e -> chonAnh());
        gui.getBtnLamMoi().addActionListener(e -> lamMoi());
        gui.getBtnThem().addActionListener(e -> themNhanVien());
    }

    // ========== COMBOBOX ==========
    public void setItemComboBox() {
        gui.getCmbGioiTinh().setModel(new DefaultComboBoxModel<>(new String[] { "Nam", "Nữ" }));
        gui.getCmbChucVu()
                .setModel(new DefaultComboBoxModel<>(new String[] { "Nhân viên bán hàng", "Nhân viên quản lý" }));

        // Gọi qua ThueService thay vì ThueDAO
        List<Thue> dsThue = thueService.layListThue();
        gui.getCmbThue().removeAllItems();
        for (Thue t : dsThue)
            gui.getCmbThue().addItem(t);
    }

    // ========== CHỌN ẢNH ==========
    public void chonAnh() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Chọn ảnh nhân viên");
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Ảnh", "png", "jpg", "jpeg"));

        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            fileAnhDaChon = fc.getSelectedFile();
            gui.getImgAnhNV().setIcon(new ImageIcon(fileAnhDaChon.getAbsolutePath()));
        }
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

    // ========== LÀM MỚI ==========
    public void lamMoi() {
        gui.getTxtTenNV().setText("");
        gui.getTxtSdt().setText("");
        gui.getTxtLuong().setText("");
        gui.getTxtEmail().setText("");
        gui.getCmbChucVu().setSelectedIndex(0);
        gui.getCmbGioiTinh().setSelectedIndex(0);
        gui.getDtpNgaySinh().setDate(null);
        gui.getDtpNgayVaoLam().setDate(null);
        gui.getImgAnhNV().setIcon(null);
    }

    // ========== THÊM NHÂN VIÊN ==========
    public void themNhanVien() {
        try {
            if (!kiemTraHopLe())
                return;

            String maNV = tool.taoKhoaChinh("NV");
            String tenNV = gui.getTxtTenNV().getText();
            String chucVu = gui.getCmbChucVu().getSelectedItem().toString();
            String sdt = tool.chuyenSoDienThoai(gui.getTxtSdt().getText());
            String email = gui.getTxtEmail().getText();
            boolean gioiTinh = gui.getCmbGioiTinh().getSelectedItem().equals("Nam");
            LocalDate ngaySinh = tool.utilDateSangLocalDate(gui.getDtpNgaySinh().getDate());
            LocalDate ngayVaoLam = tool.utilDateSangLocalDate(gui.getDtpNgayVaoLam().getDate());
            Thue thue = (Thue) gui.getCmbThue().getSelectedItem();
            double luong = tool.chuyenTienSangSo(gui.getTxtLuong().getText());
            String anh = luuAnh(maNV);

            // Gắn đối tượng Thue trực tiếp vào NhanVien (JPA sẽ map dựa trên khoá ngoại)
            NhanVien nv = new NhanVien(maNV, tenNV, chucVu, ngaySinh, gioiTinh, sdt, ngayVaoLam, luong, true,
                    anh, thue);

            String maTK = tool.taoKhoaChinh("TK");
            String loaiTK;
            if (chucVu.equalsIgnoreCase("Nhân viên bán hàng")) {
                loaiTK = "Nhân viên";
            } else {
                loaiTK = "Quản lý";
            }

            TaiKhoan tk = new TaiKhoan(maTK, maNV, maNV, true, email, loaiTK, nv);

            if (tool.hienThiXacNhan("Thêm nhân viên?", "Bạn có chắc muốn thêm?",
                    (JFrame) SwingUtilities.getWindowAncestor(gui))) {

                // Nâng cấp: Gọi qua NhanVienService
                boolean kqNV = nvService.themNhanVien(nv);
                boolean kqTK = false;

                // Chỉ thêm tài khoản nếu lưu nhân viên thành công để tránh lỗi khóa ngoại
                if (kqNV) {
                    kqTK = nvService.themTaiKhoan(tk);
                }

                if (kqNV && kqTK)
                    JOptionPane.showMessageDialog(null, "Thêm thành công!");
                else
                    JOptionPane.showMessageDialog(null, "Thêm thất bại!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        lamMoi();
    }

    // ========== KIỂM TRA HỢP LỆ ==========
    public boolean kiemTraHopLe() {

        String ten = gui.getTxtTenNV().getText().trim();
        if (ten.isEmpty()) {
            showError("Tên không được để trống");
            return false;
        }
        if (!ten.matches("^[\\p{L}\\s]+$")) {
            showError("Tên chỉ gồm chữ cái và khoảng trắng");
            return false;
        }

        String sdt = gui.getTxtSdt().getText().trim();
        if (!sdt.matches("^0\\d{9}$")) {
            showError("Số điện thoại phải 10 số và bắt đầu bằng 0");
            return false;
        }

        String email = gui.getTxtEmail().getText().trim();
        if (!email.isEmpty() && !email.matches("^[\\w._%+-]+@[\\w.-]+\\.[A-Za-z]{2,6}$")) {
            showError("Email không hợp lệ");
            return false;
        }

        String luongText = gui.getTxtLuong().getText().trim().replace(",", "");
        try {
            double luong = Double.parseDouble(luongText);
            if (luong <= 0) {
                showError("Lương phải > 0");
                return false;
            }
        } catch (Exception e) {
            showError("Lương không hợp lệ!");
            return false;
        }

        if (gui.getDtpNgaySinh().getDate() == null) {
            showError("Chưa chọn ngày sinh");
            return false;
        }

        if (gui.getDtpNgayVaoLam().getDate() == null) {
            showError("Chưa chọn ngày vào làm");
            return false;
        }

        return true;
    }

    public void showError(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
    }
}