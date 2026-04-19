package gui;

import javax.swing.*;

import controller.ChiTietKhachHangController;
import entity.KhachHang;
import utils.ToolCtrl;

import java.awt.*;

public class ChiTietKhachHang_GUI extends JPanel {

	public JTextField txtMaKH;
	public JTextField txtTenKH;
	public JTextField txtSdt;
	public JTextField txtTuoi;
	public JComboBox<String> cmbTrangThai;
	public JButton btnCapNhat;
	public JButton btnQuayLai;
	public KhachHang kh;

	public ToolCtrl tool = new ToolCtrl();
	public ChiTietKhachHangController ctkhCtrl = new ChiTietKhachHangController(this);

	public ChiTietKhachHang_GUI(KhachHang kh) {
		this.kh = kh;
		khoiTaoUI();
		ganData(kh);
		setHoatDong();
	}
	
	public void setHoatDong() {
		ctkhCtrl.choPhepEdit(false);
		btnQuayLai.addActionListener(e -> ctkhCtrl.quayLaiTKKH());
		btnCapNhat.addActionListener(e -> ctkhCtrl.capNhatThongTin());
	}

	public void ganData(KhachHang kh) {
		txtMaKH.setText(kh.getMaKH());
		txtTenKH.setText(kh.getTenKH());
		txtSdt.setText(tool.chuyenSoDienThoai(kh.getSdt()));
		txtTuoi.setText(String.valueOf(kh.getTuoi()));
		cmbTrangThai.setSelectedItem((kh.getTrangThai()) ? "Hoạt động" : "Đã xóa");
	}

	public void khoiTaoUI() {
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);

		// ====================== TOP ======================
		JLabel lblTitle = new JLabel("CHI TIẾT KHÁCH HÀNG", SwingConstants.CENTER);
		lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));

		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(Color.WHITE);
		topPanel.add(lblTitle, BorderLayout.CENTER);

		// ====================== FORM ======================
		JPanel formPanel = new JPanel();
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
		formPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));
		formPanel.setBackground(Color.WHITE);

		formPanel.add(createFormRow("Mã khách hàng:", txtMaKH = tool.taoTextField("")));
		txtMaKH.setEditable(false);
		formPanel.add(Box.createVerticalStrut(30));

		formPanel.add(createFormRow("Tên khách hàng:", txtTenKH = tool.taoTextField("")));
		formPanel.add(Box.createVerticalStrut(30));

		formPanel.add(createFormRow("Số điện thoại:", txtSdt = tool.taoTextField("")));
		formPanel.add(Box.createVerticalStrut(30));

		formPanel.add(createFormRow("Tuổi:", txtTuoi = tool.taoTextField("")));
		formPanel.add(Box.createVerticalStrut(30));

		JPanel rowTrangThai = new JPanel(new GridBagLayout());
		rowTrangThai.setBackground(Color.WHITE);
		GridBagConstraints g5 = new GridBagConstraints();
		g5.fill = GridBagConstraints.HORIZONTAL;
		g5.weightx = 1.0;

		JPanel inner5 = new JPanel();
		inner5.setLayout(new BoxLayout(inner5, BoxLayout.X_AXIS));
		inner5.setMaximumSize(new Dimension(400, 50));
		inner5.setBackground(Color.WHITE);

		JLabel lblTrangThai = tool.taoLabel("Trạng thái:");
		lblTrangThai.setPreferredSize(new Dimension(130, 30));
		cmbTrangThai = tool.taoComboBox(new String[] { "Hoạt động", "Đã xóa" });
		cmbTrangThai.setMaximumSize(new Dimension(240, 35));
		cmbTrangThai.setEditable(false);

		inner5.add(Box.createHorizontalGlue());
		inner5.add(lblTrangThai);
		inner5.add(Box.createHorizontalStrut(50));
		inner5.add(cmbTrangThai);
		inner5.add(Box.createHorizontalGlue());

		rowTrangThai.add(inner5, g5);
		formPanel.add(rowTrangThai);
		formPanel.add(Box.createVerticalStrut(50));

		JPanel rowButtons = new JPanel(new GridBagLayout());
		rowButtons.setBackground(Color.WHITE);
		GridBagConstraints g6 = new GridBagConstraints();
		g6.fill = GridBagConstraints.HORIZONTAL;
		g6.weightx = 1.0;

		JPanel inner6 = new JPanel();
		inner6.setLayout(new BoxLayout(inner6, BoxLayout.X_AXIS));
		inner6.setMaximumSize(new Dimension(400, 80));
		inner6.setBackground(Color.WHITE);

		btnCapNhat = tool.taoButton("Cập nhật", "/picture/khachHang/edit.png");
		btnQuayLai = tool.taoButton("Quay lại", "/picture/khachHang/signOut.png");

		inner6.add(Box.createHorizontalGlue());
		inner6.add(btnCapNhat);
		inner6.add(Box.createHorizontalStrut(50));
		inner6.add(btnQuayLai);
		inner6.add(Box.createHorizontalGlue());

		rowButtons.add(inner6, g6);
		formPanel.add(rowButtons);

		add(topPanel, BorderLayout.NORTH);
		add(formPanel, BorderLayout.CENTER);
	}

	public JPanel createFormRow(String labelText, JTextField field) {
		JPanel row = new JPanel(new GridBagLayout());
		row.setBackground(Color.WHITE);
		GridBagConstraints g = new GridBagConstraints();
		g.fill = GridBagConstraints.HORIZONTAL;
		g.weightx = 1.0;

		JPanel inner = new JPanel();
		inner.setLayout(new BoxLayout(inner, BoxLayout.X_AXIS));
		inner.setMaximumSize(new Dimension(400, 50));
		inner.setBackground(Color.WHITE);

		JLabel label = tool.taoLabel(labelText);
		label.setPreferredSize(new Dimension(130, 30));
		field.setMaximumSize(new Dimension(240, 35));

		inner.add(Box.createHorizontalGlue());
		inner.add(label);
		inner.add(Box.createHorizontalStrut(50));
		inner.add(field);
		inner.add(Box.createHorizontalGlue());

		row.add(inner, g);
		return row;
	}

}
