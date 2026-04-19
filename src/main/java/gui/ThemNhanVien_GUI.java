package gui;


import controller.ThemNhanVienController;
import entity.Thue;

import com.toedter.calendar.JDateChooser;
import utils.ToolCtrl;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ThemNhanVien_GUI extends JPanel {
	public JLabel imgAnhNV;
	public JButton btnChonAnh, btnLamMoi, btnThem;
	public JTextField txtTenNV, txtSdt, txtLuong, txtEmail;
	public JComboBox<String> cmbChucVu, cmbGioiTinh;
	public JComboBox<Thue> cmbThue;
	public JDateChooser dtpNgaySinh, dtpNgayVaoLam;
	public ThemNhanVienController ctrl;

	public ToolCtrl tool = new ToolCtrl();

	public ThemNhanVien_GUI() {
		initUI();
		ctrl = new ThemNhanVienController(this);
	}

	public void initUI() {
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(15, 25, 25, 25));
		setBackground(Color.WHITE);

		// ========== TIÊU ĐỀ ==========
		JLabel lblTitle = new JLabel("THÊM NHÂN VIÊN", SwingConstants.CENTER);
		lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblTitle.setForeground(new Color(0x1E3A8A));
		add(lblTitle, BorderLayout.NORTH);

		// ===== Nội dung =====
		JPanel centerPanel = new JPanel(new BorderLayout(30, 0));
		centerPanel.setBackground(Color.WHITE);

		// ===== Ảnh + Chọn ảnh =====
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.setBackground(Color.WHITE);
		leftPanel.setPreferredSize(new Dimension(280, 400));

		// ===== Ảnh =====
		imgAnhNV = new JLabel(loadPlaceholderImage(227, 269));
		imgAnhNV.setPreferredSize(new Dimension(227, 269));
		imgAnhNV.setHorizontalAlignment(SwingConstants.CENTER);
		imgAnhNV.setBorder(BorderFactory.createLineBorder(new Color(0xD1D1D1), 1, true));

		JPanel imgWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		imgWrapper.setBackground(Color.WHITE);
		imgWrapper.add(imgAnhNV);

		// ===== Nút chọn ảnh =====
		btnChonAnh = tool.taoButton("Chọn ảnh", "/picture/nhanVien/folder.png");
		JPanel btnWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
		btnWrapper.setBackground(Color.WHITE);
		btnWrapper.add(btnChonAnh);

		JPanel contentGroup = new JPanel();
		contentGroup.setLayout(new BoxLayout(contentGroup, BoxLayout.Y_AXIS));
		contentGroup.setBackground(Color.WHITE);

		contentGroup.add(imgWrapper);
		contentGroup.add(Box.createVerticalStrut(15));
		contentGroup.add(btnWrapper);

		// ===== Căn giữa dọc =====
		JPanel centeredWrapper = new JPanel(new GridBagLayout());
		centeredWrapper.setBackground(Color.WHITE);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.CENTER;
		centeredWrapper.add(contentGroup, gbc);

		leftPanel.add(centeredWrapper);
		centerPanel.add(leftPanel, BorderLayout.WEST);

		// ========== RIGHT: ĐIỀN THÔNG TIN ==========
		JPanel rightPanel = new JPanel(new BorderLayout());
		rightPanel.setBackground(Color.WHITE);

		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setBackground(Color.WHITE);
		GridBagConstraints formGbc = new GridBagConstraints();
		formGbc.insets = new Insets(18, 0, 18, 15);
		formGbc.fill = GridBagConstraints.HORIZONTAL;
		formGbc.anchor = GridBagConstraints.WEST;

		// ===== Khởi tạo field =====
		txtTenNV = tool.taoTextField("Nhập tên nhân viên");
		cmbChucVu = tool.taoComboBox(new String[] { "Quản lý bán hàng", "Nhân viên quản lý"});
		cmbChucVu.setEditable(true);
		txtSdt = tool.taoTextField("Nhập số điện thoại");
		txtLuong = tool.taoTextField("Nhập lương");
		cmbThue = new JComboBox<>();
		cmbThue.setEditable(true);

		cmbGioiTinh = tool.taoComboBox(new String[] { "Nam", "Nữ", "Khác" });
		cmbGioiTinh.setEditable(true);
		dtpNgaySinh = tool.taoDateChooser();
		dtpNgayVaoLam = tool.taoDateChooser();
		txtEmail = tool.taoTextField("Nhập email");

		// DÒNG 1
		formGbc.gridx = 0;
		formGbc.gridy = 0;
		formPanel.add(createFormRow("Tên nhân viên: ", txtTenNV), formGbc);
		formGbc.gridx = 1;
		formPanel.add(createFormRow("Giới tính: ", cmbGioiTinh), formGbc);

		// DÒNG 2
		formGbc.gridx = 0;
		formGbc.gridy = 1;
		formPanel.add(createFormRow("Ngày sinh: ", dtpNgaySinh), formGbc);
		formGbc.gridx = 1;
		formPanel.add(createFormRow("Ngày vào làm: ", dtpNgayVaoLam), formGbc);

		// DÒNG 3
		formGbc.gridx = 0;
		formGbc.gridy = 2;
		formPanel.add(createFormRow("Chức vụ: ", cmbChucVu), formGbc);
		formGbc.gridx = 1;
		formPanel.add(createFormRow("Email: ", txtEmail), formGbc);

		// DÒNG 4
		formGbc.gridx = 0;
		formGbc.gridy = 3;
		formPanel.add(createFormRow("Số điện thoại: ", txtSdt), formGbc);
		formGbc.gridx = 1;
		formPanel.add(createFormRow("Lương: ", txtLuong), formGbc);

		// DÒNG 5
		formGbc.gridx = 0;
		formGbc.gridy = 4;
		formPanel.add(createFormRow("Thuế: ", cmbThue), formGbc);

		// ===== Các nút =====
		JPanel buttonPanel = new JPanel(new GridBagLayout());
		buttonPanel.setBackground(Color.WHITE);
		buttonPanel.setBorder(new EmptyBorder(30, 0, 0, 0));

		GridBagConstraints btnGbc = new GridBagConstraints();
		btnGbc.insets = new Insets(0, 15, 0, 15);

		btnLamMoi = tool.taoButton("Làm mới", "/picture/nhanVien/refresh.png");
		btnThem = tool.taoButton("Thêm", "/picture/nhanVien/plus.png");

		btnGbc.gridx = 0;
		buttonPanel.add(btnLamMoi, btnGbc);
		btnGbc.gridx = 1;
		buttonPanel.add(btnThem, btnGbc);

		rightPanel.add(formPanel, BorderLayout.CENTER);
		rightPanel.add(buttonPanel, BorderLayout.SOUTH);

		centerPanel.add(rightPanel, BorderLayout.CENTER);
		add(centerPanel, BorderLayout.CENTER);
	}

    // ========== TẠO DÒNG FORM ==========
    public JPanel createFormRow(String labelText, JComponent field) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(Short.MAX_VALUE, 60));
		JLabel lbl = tool.taoLabel(labelText);
		lbl.setPreferredSize(new Dimension(130, 40));
		lbl.setMinimumSize(new Dimension(130, 40));

		field.setPreferredSize(new Dimension(180, 40));

		row.add(lbl);
		row.add(Box.createHorizontalStrut(12));
		row.add(field);
		row.add(Box.createHorizontalGlue());

		return row;
	}

    // ========== PLACEHOLDER IMAGE ==========
    public ImageIcon loadPlaceholderImage(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();
        g2.setColor(new Color(240, 240, 240));
        g2.fillRect(0, 0, w, h);
        g2.setColor(Color.GRAY);
        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        g2.drawString("No Image", w / 2 - 50, h / 2);
        g2.dispose();
        return new ImageIcon(img.getScaledInstance(w, h, Image.SCALE_SMOOTH));
    }

	// ========== GETTER ==========
	public JButton getBtnChonAnh() {
		return btnChonAnh;
	}

	public JButton getBtnLamMoi() {
		return btnLamMoi;
	}

	public JButton getBtnThem() {
		return btnThem;
	}

	public JLabel getImgAnhNV() {
		return imgAnhNV;
	}

	public JTextField getTxtTenNV() {
		return txtTenNV;
	}

	public JTextField getTxtLuong() {
		return txtLuong;
	}

	public JTextField getTxtEmail() {
		return txtEmail;
	}

	public JTextField getTxtSdt() {
		return txtSdt;
	}

	public JComboBox<String> getCmbChucVu() {
		return cmbChucVu;
	}

	public JComboBox<String> getCmbGioiTinh() {
		return cmbGioiTinh;
	}
	
	public JComboBox<Thue> getCmbThue() {
		return cmbThue;
	}

	public JDateChooser getDtpNgaySinh() {
		return dtpNgaySinh;
	}

	public JDateChooser getDtpNgayVaoLam() {
		return dtpNgayVaoLam;
	}

}