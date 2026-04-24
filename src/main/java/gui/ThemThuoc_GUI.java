package gui;

import javax.swing.*;

import com.toedter.calendar.JDateChooser;
import controller.ThemThuocController;
import utils.ToolCtrl;

import java.awt.*;

//import controller.ThemThuocCtrl;
//import controller.ThuocCtrl;
//import controller.ToolCtrl;

public class ThemThuoc_GUI extends JPanel {

	public JTextField txtTenThuoc;
	public JTextField txtDangThuoc;
	public JComboBox<String> cmbDonVi;
	public JTextField txtGiaBan;
	public JComboBox<String> cmbThue;
	public JComboBox<String> cmbKeThuoc;
	public JDateChooser dpHanSuDung;
	public JLabel imgThuoc;
	public JButton btnChonAnh, btnLamMoi, btnThem;
	public String urlAnh = null;

	public ThemThuocController thCtrl;
	public ToolCtrl tool = new ToolCtrl();
	public JComboBox cmbQuocGia;
	public JSpinner spSoLuongTon;

	public ThemThuoc_GUI() {
		this.thCtrl = new ThemThuocController(this);
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);

		// ===== TOP: Tiêu đề =====
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
		topPanel.setBackground(Color.WHITE);
		JLabel lblTitle = new JLabel("THÊM THUỐC", SwingConstants.CENTER);
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
		topPanel.add(lblTitle);
		add(topPanel, BorderLayout.NORTH);

		// ===== CENTER: Form nhập =====
		JPanel centerPanel = new JPanel(new GridBagLayout());
		centerPanel.setBackground(Color.WHITE);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 12, 8, 12);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		int row = 0;

		// === Cột trái: Ảnh + nút chọn ảnh ===
		gbc.gridx = 0;
		gbc.gridy = row;
		gbc.gridheight = 5;
		imgThuoc = new JLabel();
		imgThuoc.setPreferredSize(new Dimension(160, 220));
		imgThuoc.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		imgThuoc.setHorizontalAlignment(SwingConstants.CENTER);
		centerPanel.add(imgThuoc, gbc);

		gbc.gridheight = 1;
		gbc.gridy = row + 5;
		btnChonAnh = tool.taoButton("Chọn ảnh", "/picture/keThuoc/folder.png");
		centerPanel.add(btnChonAnh, gbc);

		// === Cột giữa ===
		gbc.gridx = 1;
		gbc.gridy = 0;
		centerPanel.add(taoDong("Tên thuốc:", txtTenThuoc = tool.taoTextField("Tên thuốc...")), gbc);
		setFieldHeight(txtTenThuoc);

		gbc.gridy++;
		centerPanel.add(taoDong("Dạng thuốc:", txtDangThuoc = tool.taoTextField("Dạng thuốc...")), gbc);
		setFieldHeight(txtDangThuoc);

		gbc.gridy++;
		centerPanel.add(taoDong("Đơn vị:", cmbDonVi = new JComboBox<>()), gbc);
		setFieldHeight(cmbDonVi);

		// Thêm dòng Quốc gia
		gbc.gridy++;
		centerPanel.add(taoDong("Quốc gia:", cmbQuocGia = new JComboBox<>()), gbc);
		setFieldHeight(cmbQuocGia);

		// Thêm dòng Số lượng tồn
		gbc.gridy++;
		spSoLuongTon = new JSpinner(new SpinnerNumberModel(0, 0, 100000, 1));
		JTextField txtSoLuongTon = ((JSpinner.DefaultEditor) spSoLuongTon.getEditor()).getTextField();
		txtSoLuongTon.setEditable(true);
		centerPanel.add(taoDong("Số lượng tồn:", spSoLuongTon), gbc);
		setFieldHeight(spSoLuongTon);

		// === Cột phải ===
		gbc.gridy++;
		centerPanel.add(taoDong("Giá bán:", txtGiaBan = tool.taoTextField("Giá bán...")), gbc);
		setFieldHeight(txtGiaBan);

		gbc.gridy++;
		dpHanSuDung = tool.taoDateChooser();
		centerPanel.add(taoDong("Hạn sử dụng:", dpHanSuDung), gbc);
		setFieldHeight(dpHanSuDung);

		gbc.gridy++;
		centerPanel.add(taoDong("Thuế VAT:", cmbThue = new JComboBox<>()), gbc);
		setFieldHeight(cmbThue);

		gbc.gridy++;
		centerPanel.add(taoDong("Kệ thuốc:", cmbKeThuoc = new JComboBox<>()), gbc);
		setFieldHeight(cmbKeThuoc);

		add(centerPanel, BorderLayout.CENTER);

		// ===== BOTTOM: Các nút =====
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10));
		bottomPanel.setBackground(Color.WHITE);

		btnLamMoi = tool.taoButton("Làm mới", "/picture/keThuoc/refresh.png");
		btnThem = tool.taoButton("Thêm", "/picture/keThuoc/edit.png");

		bottomPanel.add(btnLamMoi);
		bottomPanel.add(btnThem);
		add(bottomPanel, BorderLayout.SOUTH);

		// ===== SỰ KIỆN =====
		ganSuKien();
	}

	public void setFieldHeight(JComponent comp) {
		Dimension d = comp.getPreferredSize();
		d.height = 36;
		comp.setPreferredSize(d);
	}

	// Gắn sự kiện
	public void ganSuKien() {
		btnLamMoi.addActionListener(e -> onBtnLamMoi());
		btnThem.addActionListener(e -> thCtrl.themThuoc());
		btnChonAnh.addActionListener(e -> onBtnChonAnh());
		thCtrl.setCmbKeThuoc();
		thCtrl.setCmbDonVi();
		thCtrl.setCmbQuocGia();
		thCtrl.setCmbThue();
	}

	// ====== Hàm tạo 1 dòng nhãn + ô nhập ======
	public JPanel taoDong(String label, JComponent comp) {
		JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		row.setBackground(Color.WHITE);

		JLabel lbl = tool.taoLabel(label);
		lbl.setPreferredSize(new Dimension(120, 25));
		comp.setPreferredSize(new Dimension(180, 25));

		row.add(lbl);
		row.add(comp);
		return row;
	}

	// ===== Event handlers =====
	public void onBtnLamMoi() {
		txtTenThuoc.setText("");
		txtDangThuoc.setText("");
		cmbDonVi.setSelectedItem("Tất cả");
		txtGiaBan.setText("");
		dpHanSuDung.setDate(new java.util.Date());
		cmbThue.setSelectedItem("Tất cả");
		cmbKeThuoc.setSelectedItem("Tất cả");
		imgThuoc.setIcon(null);
		txtTenThuoc.requestFocus();
		cmbQuocGia.setSelectedItem("Tất cả");
	}

	public void onBtnChonAnh() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Chọn ảnh thuốc");
		fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Hình ảnh (.jpg, .png, .jpeg)",
				"jpg", "png", "jpeg"));

		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			java.io.File file = fileChooser.getSelectedFile();
			urlAnh = "/picture/thuoc/" + file.getName();
			ImageIcon icon = new ImageIcon(file.getAbsolutePath());
			Image scaled = icon.getImage().getScaledInstance(160, 220, Image.SCALE_SMOOTH);
			imgThuoc.setIcon(new ImageIcon(scaled));
		}
	}
}
