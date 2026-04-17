package gui;

import utils.ToolCtrl;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

//import controller.DanhSachKNHTCtrl;
//import controller.ToolCtrl;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class DanhSachKhieuNaiVaHoTroHK_GUI extends JPanel {

	public JTextField txtTenKH, txtTenNV;
	public JComboBox<String> cmbLoaiDon, cmbTrangThai;
	public JButton btnXemCT, btnLamMoi, btnThem;
	public JTable tblKNHT;
	public ToolCtrl tool = new ToolCtrl();
	public DefaultTableModel model;

//	public DanhSachKNHTCtrl knhtCtrl;

	public DanhSachKhieuNaiVaHoTroHK_GUI() {
//		knhtCtrl = new DanhSachKNHTCtrl(this);
		khoiTaoUI();
//		setHoatDong();
//		knhtCtrl.locTatCa();
	}

//	public void setHoatDong() {
//		//btnTimKiem.addActionListener(e -> knhtCtrl.locTatCa());
//		btnLamMoi.addActionListener(e -> knhtCtrl.lamMoi());
//		cmbLoaiDon.addActionListener(e -> knhtCtrl.locTatCa());
//		cmbTrangThai.addActionListener(e -> knhtCtrl.locTatCa());
//		btnXemCT.addActionListener(e -> knhtCtrl.chuyenSangChiTiet());
//		btnThem.addActionListener(e -> knhtCtrl.chuyenSangThem());
//		txtTenKH.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyReleased(KeyEvent e) {
//				knhtCtrl.locTatCa();
//			}
//		});
//
//		txtTenNV.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyPressed(KeyEvent e) {
//				knhtCtrl.locTatCa();
//			}
//		});
//	}

	public void khoiTaoUI() {
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);

		// ====================== TOP ======================
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
		topPanel.setBackground(Color.WHITE);

		JLabel lblTitle = new JLabel("DANH SÁCH KHIẾU NẠI VÀ HỖ TRỢ KHÁCH HÀNG", SwingConstants.CENTER);
		lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		topPanel.add(lblTitle);
		topPanel.add(Box.createVerticalStrut(15));

		JPanel filtersPanel = new JPanel();
		filtersPanel.setLayout(new GridLayout(2, 2, 40, 15));
		filtersPanel.setBackground(Color.WHITE);

		JLabel lblTenKH = tool.taoLabel("Tên khách hàng:");
		txtTenKH = tool.taoTextField("Nhập tên khách hàng...");
		JPanel p1 = taoDong(lblTenKH, txtTenKH);

		JLabel lblLoaiDon = tool.taoLabel("Loại đơn:");
		cmbLoaiDon = tool.taoComboBox(new String[] { "Tất cả", "Khiếu nại", "Hỗ trợ" });
		cmbLoaiDon.setEditable(false);
		cmbLoaiDon.setSelectedItem("Tất cả");
		JPanel p2 = taoDong(lblLoaiDon, cmbLoaiDon);

		JLabel lblTenNV = tool.taoLabel("Tên nhân viên:");
		txtTenNV = tool.taoTextField("Nhập tên nhân viên...");
		JPanel p3 = taoDong(lblTenNV, txtTenNV);

		JLabel lblTrangThai = tool.taoLabel("Trạng thái:");
		cmbTrangThai = tool.taoComboBox(new String[] { "Hoàn tất", "Chờ xử lý" });
		cmbTrangThai.setEditable(false);
		cmbTrangThai.setSelectedItem("Chờ xử lý");
		JPanel p4 = taoDong(lblTrangThai, cmbTrangThai);

		filtersPanel.add(p1);
		filtersPanel.add(p2);
		filtersPanel.add(p3);
		filtersPanel.add(p4);

		topPanel.add(filtersPanel);
		topPanel.add(Box.createVerticalStrut(10));

		JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 5));
		btnPanel.setBackground(Color.WHITE);

		btnThem = tool.taoButton("Thêm", "/picture/khachHang/plus.png");
		//btnTimKiem = tool.taoButton("Tìm kiếm", "/picture/khachHang/search.png");
		btnXemCT = tool.taoButton("Xem chi tiết", "/picture/khachHang/find.png");
		btnLamMoi = tool.taoButton("Làm mới", "/picture/khachHang/return.png");

		//btnPanel.add(btnTimKiem);
		btnPanel.add(btnXemCT);
		btnPanel.add(btnThem);
		btnPanel.add(btnLamMoi);

		topPanel.add(btnPanel);

		// ====================== TABLE ======================
		String[] cols = { "Mã phiếu", "Tên khách hàng", "Tên nhân viên", "Loại đơn", "Ngày tạo", "Trạng thái" };
		model = new DefaultTableModel(cols, 0) {
			@Override
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		};

		tblKNHT = new JTable(model);
		tblKNHT.setRowHeight(38);
		tblKNHT.setFont(new Font("Times New Roman", Font.PLAIN, 15));

		tblKNHT.setBackground(Color.WHITE);
		tblKNHT.getTableHeader().setBackground(new Color(240, 240, 240));
		tblKNHT.setGridColor(new Color(200, 200, 200));
		tblKNHT.setShowGrid(true);
		tblKNHT.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));

		tblKNHT.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tblKNHT.setForeground(new Color(0x33, 0x33, 0x33));

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < tblKNHT.getColumnCount(); i++) {
			tblKNHT.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

		JTableHeader header = tblKNHT.getTableHeader();
		header.setBackground(new Color(240, 240, 240));
		header.setFont(new Font("Times New Roman", Font.BOLD, 18));
		((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

		JScrollPane scrollTable = new JScrollPane(tblKNHT);
		scrollTable.setBorder(BorderFactory.createLineBorder(new Color(0xCCCCCC)));
		scrollTable.getViewport().setBackground(Color.WHITE);
		scrollTable.setBackground(Color.WHITE);

		add(topPanel, BorderLayout.NORTH);
		add(scrollTable, BorderLayout.CENTER);
	}

	public JPanel taoDong(JLabel lbl, JComponent comp) {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
		p.setBackground(Color.WHITE);
		lbl.setPreferredSize(new Dimension(120, 30));
		comp.setPreferredSize(new Dimension(220, 32));
		p.add(lbl);
		p.add(comp);
		return p;
	}

}
