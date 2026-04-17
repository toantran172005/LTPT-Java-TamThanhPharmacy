package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;
import java.util.ArrayList;
//
//import controller.DatThuocBanCtrl;
//import controller.ToolCtrl;
import entity.Thuoc;
import utils.ToolCtrl;

public class PhieuDatThuoc_GUI extends JPanel {

	public JTable tblPhieuDat;
	public DefaultTableModel model;
	public JLabel lblNgayDat, lblNguoiLap;
	public JButton btnLuuTep, btnTaoPhieuDat;

	public ToolCtrl tool = new ToolCtrl();
//	public DatThuocBanCtrl dtCtrl;

	public PhieuDatThuoc_GUI(DatThuoc_GUI dtGUI) {
//		dtCtrl = new DatThuocBanCtrl(dtGUI, this);
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);

		// ===== TOP PANEL =====
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(Color.WHITE);
		topPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 10, 30));

		// ---- Tiêu đề ----
		JLabel lblTieuDe = new JLabel("PHIẾU ĐẶT HÀNG", SwingConstants.CENTER);
		lblTieuDe.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblTieuDe.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
		topPanel.add(lblTieuDe, BorderLayout.NORTH);

		// ---- Thông tin nhà thuốc ----
		JPanel infoPanel = new JPanel(new GridLayout(3, 1, 0, 5));
		infoPanel.setBackground(Color.WHITE);

		JLabel lblTenNhaThuoc = tool.taoLabel("Hiệu thuốc tây Tam Thanh");
		lblTenNhaThuoc.setFont(new Font("Times New Roman", Font.BOLD, 15));

		JLabel lblDiaChi = tool.taoLabel("Địa chỉ: 12, Nguyễn Văn Bảo, Gò Vấp, TP.HCM");
		JLabel lblSdt = tool.taoLabel("SĐT: 0353611654");

		infoPanel.add(lblTenNhaThuoc);
		infoPanel.add(lblDiaChi);
		infoPanel.add(lblSdt);

		// ---- Số phiếu và ngày đặt ----
		JPanel rightInfo = new JPanel();
		rightInfo.setBackground(Color.WHITE);
		rightInfo.setLayout(new BoxLayout(rightInfo, BoxLayout.Y_AXIS));

		JLabel lblSoPhieu = new JLabel("Số phiếu: PDH-2025-001112");
		lblSoPhieu.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblNgayDat = new JLabel("Ngày đặt: 10/10/2025");
		lblNgayDat.setFont(new Font("Times New Roman", Font.PLAIN, 14));

		rightInfo.add(lblSoPhieu);
		rightInfo.add(Box.createVerticalStrut(5));
		rightInfo.add(lblNgayDat);

		// ---- Ghép hai bên ----
		JPanel topInfo = new JPanel(new BorderLayout());
		topInfo.setBackground(Color.WHITE);
		topInfo.add(infoPanel, BorderLayout.CENTER);
		topInfo.add(rightInfo, BorderLayout.EAST);

		topPanel.add(topInfo, BorderLayout.CENTER);
		add(topPanel, BorderLayout.NORTH);

		// ====================== CENTER: Table ======================
		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.setBackground(Color.WHITE);
		centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

		JLabel lblNCC = new JLabel("Nhà cung cấp: Công ty dược phẩm DHG Pharma");
		lblNCC.setFont(new Font("Times New Roman", Font.BOLD, 15));
		lblNCC.setBorder(BorderFactory.createEmptyBorder(5, 0, 8, 0));
		centerPanel.add(lblNCC, BorderLayout.NORTH);

		String[] cols = { "STT", "Tên thuốc", "Đơn vị", "Số lượng đặt" };
		model = new DefaultTableModel(cols, 0) {
			@Override
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		};

		tblPhieuDat = new JTable(model);
		tblPhieuDat.setRowHeight(38);
		tblPhieuDat.setFont(new Font("Times New Roman", Font.PLAIN, 15));

		tblPhieuDat.setBackground(Color.WHITE);
		tblPhieuDat.getTableHeader().setBackground(new Color(240, 240, 240));
		tblPhieuDat.setGridColor(new Color(200, 200, 200));
		tblPhieuDat.setShowGrid(true);
		tblPhieuDat.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));

		tblPhieuDat.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tblPhieuDat.setForeground(new Color(0x33, 0x33, 0x33));

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < tblPhieuDat.getColumnCount(); i++) {
			tblPhieuDat.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

		JTableHeader header = tblPhieuDat.getTableHeader();
		header.setBackground(new Color(240, 240, 240));
		header.setFont(new Font("Times New Roman", Font.BOLD, 18));
		((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

		JScrollPane scroll = new JScrollPane(tblPhieuDat);
		scroll.setBorder(BorderFactory.createLineBorder(new Color(0xCCCCCC)));
		scroll.getViewport().setBackground(Color.WHITE);
		scroll.setBackground(Color.WHITE);

		centerPanel.add(scroll, BorderLayout.CENTER);

		add(centerPanel, BorderLayout.CENTER);

		// ====================== BOTTOM ======================
		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setBackground(Color.WHITE);
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

		// ---- Người lập phiếu ----
		JPanel nguoiLapPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		nguoiLapPanel.setBackground(Color.WHITE);
		JLabel lblNguoiLapText = new JLabel("Người lập phiếu:");
		lblNguoiLapText.setFont(new Font("Times New Roman", Font.BOLD, 15));
		lblNguoiLap = new JLabel("Phan Chí Toàn");
		lblNguoiLap.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		nguoiLapPanel.add(lblNguoiLapText);
		nguoiLapPanel.add(lblNguoiLap);

		// ---- Nút chức năng ----
		JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
		btnPanel.setBackground(Color.WHITE);
		btnLuuTep = tool.taoButton("Lưu tệp", "/picture/thuoc/diskette.png");
		btnPanel.add(btnLuuTep);

		bottomPanel.add(btnPanel, BorderLayout.WEST);
		bottomPanel.add(nguoiLapPanel, BorderLayout.EAST);
		add(bottomPanel, BorderLayout.SOUTH);
//
//		ArrayList<Thuoc> listThuocDat = dtCtrl.layDanhSachThuocTuTable();
//		dtCtrl.setDataChoTablePDT(listThuocDat);

		ganSuKien();
	}

	// ===================== SỰ KIỆN =====================
	public void ganSuKien() {
		btnLuuTep.addActionListener(e -> {
			tool.doiPanel(this, new DatThuoc_GUI());
		});
	}
}
