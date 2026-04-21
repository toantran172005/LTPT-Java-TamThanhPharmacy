package gui;

import controller.TimKiemPhieuDoiTraController;
import utils.ToolCtrl;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

//import controller.ChiTietHoaDonCtrl;
//import controller.ChiTietPhieuDoiTraCtrl;
//import controller.TimKiemHDCtrl;
//import controller.TimKiemPhieuDoiTraCtrl;
//import controller.ToolCtrl;
import java.awt.*;

public class TimKiemPhieuDoiTra_GUI extends JPanel {
	public JTextField txtKhachHang, txtTenNV;
	public JButton btnTimKiem, btnLamMoi, btnChiTiet;
	public JTable tblPhieuDoiTra;
	public TrangChuQL_GUI mainFrame;
	public TrangChuNV_GUI mainFrameNV;
	public DefaultTableModel model;

	Font font1 = new Font("Times New Roman", Font.BOLD, 18);
	Font font2 = new Font("Times New Roman", Font.PLAIN, 15);
	public ToolCtrl tool = new ToolCtrl();

	public TimKiemPhieuDoiTra_GUI(TrangChuQL_GUI mainFrame) {
		this.mainFrame = mainFrame;
		initUI();
		new TimKiemPhieuDoiTraController(this);
	}

	public TimKiemPhieuDoiTra_GUI(TrangChuNV_GUI mainFrameNV) {
		this.mainFrameNV = mainFrameNV;
		initUI();
		new TimKiemPhieuDoiTraController(this);
	}

	public void initUI() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(1134, 617));
		setBackground(Color.WHITE);

		// ========== TOP PANEL ==========
		JPanel pnlTop = new JPanel();
		pnlTop.setLayout(new BoxLayout(pnlTop, BoxLayout.Y_AXIS));
		pnlTop.setBackground(Color.WHITE);
		pnlTop.setBorder(new EmptyBorder(10, 10, 10, 10));

		// ===== Tiêu đề =====
		JLabel lblTitle = new JLabel("DANH SÁCH PHIẾU ĐỔI TRẢ", SwingConstants.CENTER);
		lblTitle.setFont(font1);
		lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlTop.add(lblTitle);
		pnlTop.add(Box.createVerticalStrut(10));

		// ===== Tìm kiếm =====
		JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 5));
		row1.setBackground(Color.WHITE);

		JLabel lblKH = tool.taoLabel("Tên khách hàng:");
		txtKhachHang = tool.taoTextField("Tên khách hàng...");
		JLabel lblNV = tool.taoLabel("Tên nhân viên:");
		txtTenNV = tool.taoTextField("Tên nhân viên...");
		btnTimKiem = tool.taoButton("Tìm kiếm", "/picture/hoaDon/search.png");

		row1.add(lblKH);
		row1.add(txtKhachHang);
		row1.add(lblNV);
		row1.add(txtTenNV);
		row1.add(btnTimKiem);
		pnlTop.add(row1);

		// ===== Chức năng =====
		JPanel row2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 5));
		row2.setBackground(Color.WHITE);
		btnLamMoi = tool.taoButton("Làm mới", "/picture/hoaDon/refresh.png");
		btnChiTiet = tool.taoButton("Xem chi tiết", "/picture/hoaDon/xemChiTiet.png");
		row2.add(btnLamMoi);
		row2.add(btnChiTiet);
		pnlTop.add(row2);

		add(pnlTop, BorderLayout.NORTH);

		// ========== BẢNG DỮ LIỆU ==========
		String[] cols = { "Mã phiếu", "Tên nhân viên", "Tên khách hàng", "Ngày lập", "Lý do" };
		model = new DefaultTableModel(cols, 0) {
			@Override
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		};

		tblPhieuDoiTra = new JTable(model);
		tblPhieuDoiTra.setRowHeight(38);
		tblPhieuDoiTra.setFont(new Font("Times New Roman", Font.PLAIN, 15));

		tblPhieuDoiTra.setBackground(Color.WHITE);
		tblPhieuDoiTra.getTableHeader().setBackground(new Color(240, 240, 240));
		tblPhieuDoiTra.setGridColor(new Color(200, 200, 200));
		tblPhieuDoiTra.setShowGrid(true);
		tblPhieuDoiTra.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));

		tblPhieuDoiTra.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tblPhieuDoiTra.setForeground(new Color(0x33, 0x33, 0x33));

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < tblPhieuDoiTra.getColumnCount(); i++) {
			tblPhieuDoiTra.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

		JTableHeader header = tblPhieuDoiTra.getTableHeader();
		header.setBackground(new Color(240, 240, 240));
		header.setFont(new Font("Times New Roman", Font.BOLD, 18));
		((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

		JScrollPane scrollPane = new JScrollPane(tblPhieuDoiTra);
		scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0xCCCCCC)));
		scrollPane.getViewport().setBackground(Color.WHITE);
		scrollPane.setBackground(Color.WHITE);

		add(scrollPane, BorderLayout.CENTER);

		txtKhachHang.requestFocusInWindow();
	}

	// ========== GETTER ==========
	public JTextField getTxtKhachHang() {
		return txtKhachHang;
	}

	public JTextField getTxtTenNV() {
		return txtTenNV;
	}

	public JButton getBtnTimKiem() {
		return btnTimKiem;
	}

	public JButton getBtnLamMoi() {
		return btnLamMoi;
	}

	public JButton getBtnChiTiet() {
		return btnChiTiet;
	}

	public JTable getTblPhieuDoiTra() {
		return tblPhieuDoiTra;
	}

	public TrangChuQL_GUI getMainFrame() {
		return mainFrame;
	}

	public TrangChuNV_GUI getMainFrameNV() {
		return mainFrameNV;
	}

	// ========== SETTER ==========
	public void setMainFrame(TrangChuQL_GUI mainFrame) {
		this.mainFrame = mainFrame;
	}

	public void setMainFrameNV(TrangChuNV_GUI mainFrameNV) {
		this.mainFrameNV = mainFrameNV;
	}
}