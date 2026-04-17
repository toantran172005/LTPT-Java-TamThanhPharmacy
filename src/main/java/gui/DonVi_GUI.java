package gui;

import utils.ToolCtrl;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;

//import controller.DonViTinhCtrl;
//import controller.ToolCtrl;

public class DonVi_GUI extends JPanel {

	public JTextField txtTimDV, txtTenDV;
	public JTable tblDonVi;
	public JButton btnLamMoi, btnLichSuXoa, btnXoa, btnThemDV;
	public JCheckBox chkChonTatCa;
	public DefaultTableModel model;

	public ToolCtrl tool = new ToolCtrl();
//	public DonViTinhCtrl dvCtrl;

	public DonVi_GUI() {
//		dvCtrl = new DonViTinhCtrl(this);
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);

		// ===== TOP =====
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.setBackground(Color.WHITE);
		topPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

		JLabel lblTitle = new JLabel("ĐƠN VỊ THUỐC", SwingConstants.CENTER);
		lblTitle.setFont(new Font("System", Font.BOLD, 18));
		lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		topPanel.add(lblTitle);
		topPanel.add(Box.createVerticalStrut(10));

		// ===== Hàng chính gồm 2 bên =====
		JPanel mainBox = new JPanel();
		mainBox.setLayout(new BoxLayout(mainBox, BoxLayout.X_AXIS));
		mainBox.setBackground(Color.WHITE);
		mainBox.setAlignmentX(Component.CENTER_ALIGNMENT);

		// ===== LEFT: Tìm kiếm + nút =====
		JPanel leftVBox = new JPanel();
		leftVBox.setLayout(new BoxLayout(leftVBox, BoxLayout.Y_AXIS));
		leftVBox.setBackground(Color.WHITE);
		leftVBox.setPreferredSize(new Dimension(480, 120));

		txtTimDV = tool.taoTextField("Nhập tên đơn vị...");
		leftVBox.add(taoDong("Tìm đơn vị:", txtTimDV, 120, 200));
		leftVBox.add(Box.createVerticalStrut(15));

		JPanel btnRowLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
		btnRowLeft.setBackground(Color.WHITE);

		btnLamMoi = tool.taoButton("Làm mới", "/picture/hoaDon/return.png");
		btnLichSuXoa = tool.taoButton("Lịch sử xoá", "/picture/nhanVien/document.png");
		btnXoa = tool.taoButton("Xoá", "/picture/nhanVien/trash.png");

		btnRowLeft.add(btnLamMoi);
		btnRowLeft.add(btnLichSuXoa);
		btnRowLeft.add(btnXoa);

		leftVBox.add(btnRowLeft);

		// ===== RIGHT: Thêm đơn vị =====
		JPanel rightVBox = new JPanel();
		rightVBox.setLayout(new BoxLayout(rightVBox, BoxLayout.Y_AXIS));
		rightVBox.setBackground(Color.WHITE);
		rightVBox.setPreferredSize(new Dimension(420, 120));

		JLabel lblThem = new JLabel("Thêm đơn vị", SwingConstants.CENTER);
		lblThem.setFont(new Font("System", Font.BOLD, 15));
		lblThem.setAlignmentX(Component.CENTER_ALIGNMENT);
		rightVBox.add(lblThem);
		rightVBox.add(Box.createVerticalStrut(10));

		txtTenDV = tool.taoTextField("Nhập tên đơn vị...");
		btnThemDV = tool.taoButton("Thêm", "/picture/hoaDon/plus.png");

		JPanel themRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
		themRow.setBackground(Color.WHITE);
		JLabel lblTenDV = tool.taoLabel("Tên đơn vị:");
		lblTenDV.setFont(new Font("System", Font.BOLD, 15));
		themRow.add(lblTenDV);
		themRow.add(txtTenDV);
		themRow.add(btnThemDV);

		rightVBox.add(themRow);

		// ===== Ghép 2 bên =====
		mainBox.add(leftVBox);
		mainBox.add(Box.createHorizontalStrut(50));
		mainBox.add(rightVBox);

		topPanel.add(mainBox);
		topPanel.add(Box.createVerticalStrut(10));

		JSeparator sep = new JSeparator();
		sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
		topPanel.add(sep);

		add(topPanel, BorderLayout.NORTH);

		// ===== CENTER: Table =====
		String[] cols = { "STT", "Mã Đơn Vị", "Tên Đơn Vị" };
		model = new DefaultTableModel(cols, 0) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};

		tblDonVi = new JTable(model);
		tblDonVi.setRowHeight(50);
		tblDonVi.setFont(new Font("Times New Roman", Font.PLAIN, 15));

		tblDonVi.setBackground(Color.WHITE);
		tblDonVi.getTableHeader().setBackground(new Color(240, 240, 240));
		tblDonVi.setGridColor(new Color(200, 200, 200));
		tblDonVi.setShowGrid(true);
		tblDonVi.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));

		tblDonVi.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tblDonVi.setForeground(new Color(0x33, 0x33, 0x33));

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < tblDonVi.getColumnCount(); i++) {
			tblDonVi.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

		JTableHeader header = tblDonVi.getTableHeader();
		header.setBackground(new Color(240, 240, 240));
		header.setFont(new Font("Times New Roman", Font.BOLD, 18));
		((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

		JScrollPane scroll = new JScrollPane(tblDonVi);
		scroll.setBorder(BorderFactory.createLineBorder(new Color(0xCCCCCC)));
		scroll.getViewport().setBackground(Color.WHITE);
		scroll.setBackground(Color.WHITE);

		add(scroll, BorderLayout.CENTER);

		// ===== SỰ KIỆN =====
//		ganSuKien();
	}

	public JPanel taoDong(String label, JComponent comp, int labelWidth, int fieldWidth) {
		JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		row.setBackground(Color.WHITE);

		JLabel lbl = tool.taoLabel(label);
		lbl.setPreferredSize(new Dimension(labelWidth, 25));
		row.add(lbl);

		comp.setPreferredSize(new Dimension(fieldWidth, 30));
		row.add(comp);

		return row;
	}
//
//	public void ganSuKien() {
//		dvCtrl.locTatCa(true);
//		btnThemDV.addActionListener(e -> dvCtrl.themDonVi());
//	    btnXoa.addActionListener(e -> dvCtrl.xoaDonVi());
//	    btnLichSuXoa.addActionListener(e -> dvCtrl.xuLyBtnLichSuXoa());
//	    btnLamMoi.addActionListener(e -> dvCtrl.lamMoi());
//	    txtTimDV.addKeyListener(new java.awt.event.KeyAdapter() {
//	        @Override
//	        public void keyReleased(java.awt.event.KeyEvent evt) {
//	            dvCtrl.locTatCa(dvCtrl.isHienThi);
//	        }
//	    });
//	}

}
