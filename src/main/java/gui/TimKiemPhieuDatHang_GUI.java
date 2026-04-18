package gui;

import controller.TimKiemPhieuDatHangController;
import utils.ToolCtrl;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

//import controller.TimKiemPhieuDatHangCtrl;
//import controller.ToolCtrl;
//import dao.PhieuDatHangDAO;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TimKiemPhieuDatHang_GUI extends JPanel {

	public JTable tblPhieuDatHang;
	public JTextField txtTenKH, txtTenNV;
	public JComboBox<String> cmbTrangThai;
	public JButton btnChiTiet, btnLamMoi;
	public TrangChuQL_GUI mainFrameQL;
	public TrangChuNV_GUI mainFrameNV;
	Font font1 = new Font("Time New Roman", Font.BOLD, 18);
	Font font2 = new Font("Time New Roman", Font.PLAIN, 15);
	public ToolCtrl tool = new ToolCtrl();
	public TimKiemPhieuDatHangController dspdhCtrl = new TimKiemPhieuDatHangController(this);
	public DefaultTableModel model;

	public TimKiemPhieuDatHang_GUI(TrangChuNV_GUI mainFrameNV) {
		this.mainFrameNV = mainFrameNV;
		initUI();
		setHoatDong();
	}

	public TimKiemPhieuDatHang_GUI(TrangChuQL_GUI mainFrame) {
		this.mainFrameQL = mainFrame;
		initUI();
		setHoatDong();
	}

	public void setHoatDong() {
		btnLamMoi.addActionListener(e -> dspdhCtrl.lamMoi());
		btnChiTiet.addActionListener(e -> dspdhCtrl.moTrangChiTiet());
		txtTenKH.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				dspdhCtrl.locTatCa();
			}
		});
		txtTenNV.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				dspdhCtrl.locTatCa();
			}
		});
	}

	public void initUI() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(1058, 509));
		setBackground(Color.WHITE);

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.setBackground(Color.WHITE);
		topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		add(topPanel, BorderLayout.NORTH);

		JLabel lblTitle = new JLabel("DANH SÁCH PHIẾU ĐẶT THUỐC", SwingConstants.CENTER);
		lblTitle.setFont(font1);
		lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		topPanel.add(lblTitle);

		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
		searchPanel.setBackground(Color.WHITE);

		JLabel lblKH = tool.taoLabel("Tên khách hàng: ");
		txtTenKH = tool.taoTextField("Tên khách hàng...");

		JLabel lblNV = tool.taoLabel("Tên nhân viên:");
		txtTenNV = tool.taoTextField("Tên nhân viên...");

		//btnTimKiem = tool.taoButton("Tìm kiếm", "/picture/hoaDon/search.png");

		searchPanel.add(lblKH);
		searchPanel.add(txtTenKH);
		searchPanel.add(lblNV);
		searchPanel.add(txtTenNV);
		//searchPanel.add(btnTimKiem);

		JPanel functionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 60, 10));
		functionPanel.setBackground(Color.WHITE);

		JLabel lblTrangThai = tool.taoLabel("Trạng thái:");
		cmbTrangThai = tool.taoComboBox(new String[] { "Tất cả", "Chờ hàng", "Đã giao", "Đã hủy" });
		cmbTrangThai.setEditable(true);

		btnChiTiet = tool.taoButton("Xem chi tiết", "/picture/hoaDon/xemChiTiet.png");
		btnLamMoi = tool.taoButton("Làm mới", "/picture/hoaDon/refresh.png");

		functionPanel.add(lblTrangThai);
		functionPanel.add(cmbTrangThai);
		functionPanel.add(btnChiTiet);
		functionPanel.add(btnLamMoi);

		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.setBackground(Color.WHITE);
		topPanel.add(lblTitle);
		topPanel.add(searchPanel);
		topPanel.add(functionPanel);

		add(topPanel, BorderLayout.NORTH);

		String[] cols = { "Mã phiếu", "Tên nhân viên", "Tên khách hàng", "Ngày đặt", "Ngày hẹn", "Trạng thái" };
		model = new DefaultTableModel(cols, 0) {
			@Override
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		};

		tblPhieuDatHang = new JTable(model);
		tblPhieuDatHang.setRowHeight(38);
		tblPhieuDatHang.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		tblPhieuDatHang.setSelectionBackground(new Color(0xE3F2FD));
		tblPhieuDatHang.setGridColor(new Color(0xDDDDDD));
		tblPhieuDatHang.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		tblPhieuDatHang.setBackground(Color.WHITE);
		tblPhieuDatHang.setForeground(new Color(0x33, 0x33, 0x33));

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < tblPhieuDatHang.getColumnCount(); i++) {
			tblPhieuDatHang.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

		JTableHeader header = tblPhieuDatHang.getTableHeader();
		header.setBackground(new Color(240, 240, 240));
		header.setFont(new Font("Times New Roman", Font.BOLD, 18));
		((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

		JScrollPane scrollPane = new JScrollPane(tblPhieuDatHang);
		scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0xCCCCCC)));
		scrollPane.getViewport().setBackground(Color.WHITE);
		scrollPane.setBackground(Color.WHITE);

		add(scrollPane, BorderLayout.CENTER);

		dspdhCtrl.setDataChoTable(dspdhCtrl.layTatCaPhieuDatHang());
	}

	public JTable getTblPhieuDatHang() {
		return tblPhieuDatHang;
	}

	public TrangChuQL_GUI getMainFrameQL() {
		return mainFrameQL;
	}

	public void setMainFrameQL(TrangChuQL_GUI mainFrame) {
		this.mainFrameQL = mainFrame;
	}

	public TrangChuNV_GUI getMainFrameNV() {
		return mainFrameNV;
	}

	public void setMainFrameNV(TrangChuNV_GUI mainFrameNV) {
		this.mainFrameNV = mainFrameNV;
	}

}
