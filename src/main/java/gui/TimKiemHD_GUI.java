package gui;

import controller.TimKiemHDController;
import utils.ToolCtrl;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
//import controller.TimKiemHDCtrl;
//import controller.ToolCtrl;
import java.awt.*;

public class TimKiemHD_GUI extends JPanel {
	public JTable tblHoaDon;
	public JTextField txtKhachHang, txtTenNV;
	public JButton btnLamMoi, btnChiTiet, btnLichSuXoa, btnXoaHoanTac;
	public TrangChuQL_GUI mainFrame;
	public TrangChuNV_GUI mainFrameNV;
	public DefaultTableModel model;

	Font font1 = new Font("Times New Roman", Font.BOLD, 18);
	Font font2 = new Font("Times New Roman", Font.PLAIN, 15);
	public ToolCtrl tool = new ToolCtrl();

	public TimKiemHD_GUI(TrangChuQL_GUI mainFrame) {
		this.mainFrame = mainFrame;
		initUI();
		new TimKiemHDController(this);
	}

	public TimKiemHD_GUI(TrangChuNV_GUI mainFrameNV) {
		this.mainFrameNV = mainFrameNV;
		initUI();
		new TimKiemHDController(this);
	}

	public void initUI() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(1058, 509));
		setBackground(Color.WHITE);

		// ========== TOP PANEL ==========
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(Color.WHITE);
		topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		add(topPanel, BorderLayout.NORTH);

		// ===== Tiêu đề =====
		JLabel lblTitle = new JLabel("DANH SÁCH HOÁ ĐƠN", SwingConstants.CENTER);
		lblTitle.setFont(font1);
		topPanel.add(lblTitle, BorderLayout.NORTH);

		// ===== Tìm kiếm =====
		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		searchPanel.setBackground(Color.WHITE);

		JLabel lblKhachHang = tool.taoLabel("Tên khách hàng: ");
		txtKhachHang = tool.taoTextField("Tên khách hàng...");
		JLabel lblNhanVien = tool.taoLabel("Tên nhân viên: ");
		txtTenNV = tool.taoTextField("Tên nhân viên...");
		//btnTimKiem = tool.taoButton("Tìm kiếm", "/picture/hoaDon/search.png");

		searchPanel.add(lblKhachHang);
		searchPanel.add(txtKhachHang);
		searchPanel.add(lblNhanVien);
		searchPanel.add(txtTenNV);
		//searchPanel.add(btnTimKiem);
		topPanel.add(searchPanel, BorderLayout.CENTER);

		// ===== Các nút =====
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
		buttonPanel.setBackground(Color.WHITE);
		btnLamMoi = tool.taoButton("Làm mới", "/picture/hoaDon/refresh.png");
		btnChiTiet = tool.taoButton("Xem chi tiết", "/picture/hoaDon/xemChiTiet.png");
		btnLichSuXoa = tool.taoButton("Lịch sử xoá", "/picture/hoaDon/document.png");
		btnXoaHoanTac = tool.taoButton("Xóa", "/picture/hoaDon/trash.png");

		buttonPanel.add(btnLamMoi);
		buttonPanel.add(btnChiTiet);
		buttonPanel.add(btnLichSuXoa);
		buttonPanel.add(btnXoaHoanTac);
		topPanel.add(buttonPanel, BorderLayout.SOUTH);

		// ========== BẢNG DỮ LIỆU ==========
		String[] cols = { "Mã hoá đơn", "Tên nhân viên", "Tên khách hàng", "Thời gian", "Tổng tiền hàng" };
		model = new DefaultTableModel(cols, 0) {
			@Override
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		};

		tblHoaDon = new JTable(model);
		tblHoaDon.setRowHeight(38);
		tblHoaDon.setFont(new Font("Times New Roman", Font.PLAIN, 15));

		tblHoaDon.setBackground(Color.WHITE);
		tblHoaDon.getTableHeader().setBackground(new Color(240, 240, 240));
		tblHoaDon.setGridColor(new Color(200, 200, 200));
		tblHoaDon.setShowGrid(true);
		tblHoaDon.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));

		tblHoaDon.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tblHoaDon.setForeground(new Color(0x33, 0x33, 0x33));

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < tblHoaDon.getColumnCount(); i++) {
			tblHoaDon.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

		JTableHeader header = tblHoaDon.getTableHeader();
		header.setBackground(new Color(240, 240, 240));
		header.setFont(new Font("Times New Roman", Font.BOLD, 18));
		((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

		JScrollPane scrollPane = new JScrollPane(tblHoaDon);
		scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0xCCCCCC)));
		scrollPane.getViewport().setBackground(Color.WHITE);
		scrollPane.setBackground(Color.WHITE);

		add(scrollPane, BorderLayout.CENTER);

	}

	// ========== Getters ==========
	public JTable getTblHoaDon() {
		return tblHoaDon;
	}

	public JTextField getTxtKhachHang() {
		return txtKhachHang;
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

	public JButton getBtnChiTiet() {
		return btnChiTiet;
	}

	public JButton getBtnLichSuXoa() {
		return btnLichSuXoa;
	}

	public TrangChuQL_GUI getMainFrame() {
		return mainFrame;
	}

	public TrangChuNV_GUI getMainFrameNV() {
		return mainFrameNV;
	}

	public JButton getBtnXoaHoanTac() {
		return btnXoaHoanTac;
	}
}