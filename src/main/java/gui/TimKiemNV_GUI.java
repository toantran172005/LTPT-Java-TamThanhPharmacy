package gui;

import controller.TimKiemNhanVienController;
import utils.ToolCtrl;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

//import controller.TimKiemHDCtrl;


import java.awt.*;

public class TimKiemNV_GUI extends JPanel {

	public JTable tblNhanVien;
	public JTextField txtSdt, txtTenNV;
	public JButton btnXemChiTiet, btnLamMoi, btnLichSuXoa, btnXoaHoanTac;
	Font font1 = new Font("Time New Roman", Font.BOLD, 18);
	Font font2 = new Font("Time New Roman", Font.PLAIN, 15);
	public ToolCtrl tool = new ToolCtrl();

	public TrangChuQL_GUI mainFrame;
	public DefaultTableModel model;

	public TimKiemNV_GUI(TrangChuQL_GUI mainFrame) {
		this.mainFrame = mainFrame;
		initUI();
		new TimKiemNhanVienController(this);
	}

	public void initUI() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(1058, 509));
		setBackground(Color.WHITE);

		// =========== TOP PANEL ===========
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.setPreferredSize(new Dimension(1058, 140));
		topPanel.setBackground(Color.WHITE);

		// ===== Tiêu đề =====
		JLabel lblTitle = new JLabel("DANH SÁCH NHÂN VIÊN", SwingConstants.CENTER);
		lblTitle.setFont(font1);
		lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		topPanel.add(lblTitle);
		topPanel.add(Box.createVerticalStrut(15));

		// ===== tìm kiếm + nút =====
		JPanel centerContent = new JPanel();
		centerContent.setLayout(new BoxLayout(centerContent, BoxLayout.X_AXIS));
		centerContent.setBackground(Color.WHITE);

		// ===== Tìm kiếm =====
		JPanel leftVBox = new JPanel();
		leftVBox.setLayout(new BoxLayout(leftVBox, BoxLayout.Y_AXIS));
		leftVBox.setBackground(Color.WHITE);
		leftVBox.setPreferredSize(new Dimension(480, 120));

		txtTenNV = tool.taoTextField("Nhập tên nhân viên...");
		txtSdt = tool.taoTextField("Số điện thoại...");

		leftVBox.add(taoDong("Tên nhân viên:", txtTenNV, 150, 255));
		leftVBox.add(Box.createVerticalStrut(15));
		leftVBox.add(taoDong("Số điện thoại:", txtSdt, 150, 255));

		// ===== Các nút =====
		JPanel rightVBox = new JPanel();
		rightVBox.setLayout(new BoxLayout(rightVBox, BoxLayout.Y_AXIS));
		rightVBox.setBackground(Color.WHITE);
		rightVBox.setPreferredSize(new Dimension(500, 120));

		JPanel btnRow1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
		btnRow1.setBackground(Color.WHITE);

		//btnTimKiem = tool.taoButton("Tìm kiếm", "/picture/khachHang/search.png");
		btnXemChiTiet = tool.taoButton("Xem chi tiết", "/picture/khachHang/find.png");

		//btnRow1.add(btnTimKiem);
		btnRow1.add(btnXemChiTiet);

		JPanel btnRow2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
		btnRow2.setBackground(Color.WHITE);

		btnLichSuXoa = tool.taoButton("Lịch sử xóa", "/picture/khachHang/document.png");
		btnXoaHoanTac = tool.taoButton("Xóa", "/picture/khachHang/trash.png");
		btnLamMoi = tool.taoButton("Làm mới", "/picture/khachHang/refresh.png");
		btnRow2.add(btnLichSuXoa);
		btnRow2.add(btnXoaHoanTac);
		btnRow2.add(btnLamMoi);

		rightVBox.add(btnRow1);
		rightVBox.add(Box.createVerticalStrut(10));
		rightVBox.add(btnRow2);

		centerContent.add(Box.createHorizontalGlue()); 
		centerContent.add(leftVBox);
		centerContent.add(Box.createHorizontalStrut(30));
		centerContent.add(rightVBox);
		centerContent.add(Box.createHorizontalGlue());

		topPanel.add(centerContent);
		topPanel.add(Box.createVerticalStrut(10));

		add(topPanel, BorderLayout.NORTH);

		// ========== CENTER PANEL ==========
		String[] cols = { "Mã nhân viên", "Tên nhân viên", "Số điện thoại", "Giới tính", "Chức vụ" };
		model = new DefaultTableModel(cols, 0) {
			@Override
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		};

		tblNhanVien = new JTable(model);
		tblNhanVien.setRowHeight(38);
		tblNhanVien.setFont(new Font("Times New Roman", Font.PLAIN, 15));

		tblNhanVien.setBackground(Color.WHITE);
		tblNhanVien.getTableHeader().setBackground(new Color(240, 240, 240));
		tblNhanVien.setGridColor(new Color(200, 200, 200));
		tblNhanVien.setShowGrid(true);
		tblNhanVien.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));

		tblNhanVien.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tblNhanVien.setForeground(new Color(0x33, 0x33, 0x33));

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < tblNhanVien.getColumnCount(); i++) {
			tblNhanVien.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

		JTableHeader header = tblNhanVien.getTableHeader();
		header.setBackground(new Color(240, 240, 240));
		header.setFont(new Font("Times New Roman", Font.BOLD, 18));
		((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

		JScrollPane scrollPane = new JScrollPane(tblNhanVien);
		scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0xCCCCCC)));
		scrollPane.getViewport().setBackground(Color.WHITE);
		scrollPane.setBackground(Color.WHITE);

		add(scrollPane, BorderLayout.CENTER);

	}

	public JPanel taoDong(String text, JComponent comp, int labelWidth, int fieldWidth) {
		JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		row.setBackground(Color.WHITE);

		if (!text.isEmpty()) {
			JLabel lbl = tool.taoLabel(text);
			lbl.setPreferredSize(new Dimension(labelWidth, 25));
			row.add(lbl);
		}

		if (comp != null) {
			comp.setPreferredSize(new Dimension(fieldWidth, comp.getPreferredSize().height));
			row.add(comp);
		}

		return row;
	}

	// ========== GETTERS ==========
	public JTable getTblNhanVien() {
		return tblNhanVien;
	}

	public JTextField getTxtSdt() {
		return txtSdt;
	}

	public JTextField getTxtTenNV() {
		return txtTenNV;
	}

//	public JButton getBtnTimKiem() {
//		return btnTimKiem;
//	}

	public JButton getBtnLamMoi() {
		return btnLamMoi;
	}

	public JButton getBtnXemChiTiet() {
		return btnXemChiTiet;
	}

	public JButton getBtnLichSuXoa() {
		return btnLichSuXoa;
	}

	public TrangChuQL_GUI getMainFrame() {
		return mainFrame;
	}

	public JButton getBtnXoaHoanTac() {
		return btnXoaHoanTac;
	}

}
