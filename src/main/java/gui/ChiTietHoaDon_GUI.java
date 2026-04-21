package gui;

import controller.ChiTietHoaDonController;
import utils.ToolCtrl;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
//import controller.ChiTietHoaDonCtrl;

import java.awt.*;

public class ChiTietHoaDon_GUI extends JPanel {
	public JLabel lblDiaChi, lblHotline, lblMaHD, lblNgayLap, lblNhanVien, lblKhachHang;
	public JLabel lblGhiChu, lblTongTien, lblTienNhan, lblTienThua;
	public JTable tblThuoc;
	public JButton btnInHoaDon, btnQuayLai, btnTaoPhieuDoiTra;
	public ChiTietHoaDonController ctrl;

	public TrangChuQL_GUI mainFrameQL;
	public TrangChuNV_GUI mainFrameNV;

	Font font1 = new Font("Times New Roman", Font.BOLD, 18);
	Font font2 = new Font("Times New Roman", Font.PLAIN, 15);
	public ToolCtrl tool = new ToolCtrl();

	public ChiTietHoaDonController getCtrl() {
		return ctrl;
	}

	public ChiTietHoaDon_GUI(TrangChuQL_GUI mainFrame) {
		this.mainFrameQL = mainFrame;
		initUI();
		this.ctrl = new ChiTietHoaDonController(this);
	}

	public ChiTietHoaDon_GUI(TrangChuNV_GUI mainFrame) {
		this.mainFrameNV = mainFrame;
		initUI();
		this.ctrl = new ChiTietHoaDonController(this);
	}

	public void initUI() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(1134, 617));
		setBackground(Color.WHITE);

		// ========== TOP PANEL ==========
		JPanel pnlTop = new JPanel();
		pnlTop.setLayout(new BoxLayout(pnlTop, BoxLayout.Y_AXIS));
		pnlTop.setBackground(Color.WHITE);

		JLabel lblTieuDe = new JLabel("HIỆU THUỐC TAM THANH", SwingConstants.CENTER);
		lblTieuDe.setFont(font1);
		lblTieuDe.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlTop.add(lblTieuDe);

		pnlTop.add(Box.createVerticalStrut(10));
		JPanel pnlDiaChi = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
		pnlDiaChi.setBackground(Color.WHITE);
		JLabel lblDC = tool.taoLabel("Địa chỉ: ");
		lblDiaChi = tool.taoLabel("");
		pnlDiaChi.add(lblDC);
		pnlDiaChi.add(lblDiaChi);
		pnlTop.add(pnlDiaChi);

		JPanel pnlHotline = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
		pnlHotline.setBackground(Color.WHITE);
		JLabel lblHL = tool.taoLabel("Hotline: ");
		lblHotline = tool.taoLabel("");
		pnlHotline.add(lblHL);
		pnlHotline.add(lblHotline);
		pnlTop.add(pnlHotline);

		pnlTop.add(Box.createVerticalStrut(10));
		JLabel lblChiTiet = new JLabel("CHI TIẾT HOÁ ĐƠN", SwingConstants.CENTER);
		lblChiTiet.setFont(font1);
		lblChiTiet.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlTop.add(lblChiTiet);

		pnlTop.add(taoDongThongTin("Mã hoá đơn: ", lblMaHD = tool.taoLabel("")));
		pnlTop.add(taoDongThongTin("Ngày lập: ", lblNgayLap = tool.taoLabel("")));
		pnlTop.add(taoDongThongTin("Nhân viên: ", lblNhanVien = tool.taoLabel("")));
		pnlTop.add(taoDongThongTin("Khách hàng: ", lblKhachHang = tool.taoLabel("")));

		add(pnlTop, BorderLayout.NORTH);

		// ========== BẢNG ==========
		String[] columnNames = { "Tên thuốc", "Nơi sản xuất", "Số lượng", "Đơn vị", "Đơn giá(Đã áp dụng khuyến mãi)", "Thành tiền" };
		DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		};
		tblThuoc = new JTable(model);
		tblThuoc.setRowHeight(30);
		tblThuoc.setFont(font2);
		tblThuoc.setBackground(Color.WHITE);
		tblThuoc.getTableHeader().setBackground(new Color(240, 240, 240));
		tblThuoc.setGridColor(new Color(200, 200, 200));
		tblThuoc.setShowGrid(true);
		tblThuoc.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));

		JScrollPane scrollPane = new JScrollPane(tblThuoc);
		scrollPane.getViewport().setBackground(Color.WHITE);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < tblThuoc.getColumnCount(); i++) {
			tblThuoc.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

		JTableHeader header = tblThuoc.getTableHeader();
		((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		header.setFont(new Font("Times New Roman", Font.BOLD, 16));
		add(scrollPane, BorderLayout.CENTER);

		// ========== BOTTOM PANEL ==========
		JPanel pnlBottom = new JPanel();
		pnlBottom.setLayout(new BoxLayout(pnlBottom, BoxLayout.Y_AXIS));
		pnlBottom.setBorder(new EmptyBorder(10, 0, 10, 0));
		pnlBottom.setBackground(Color.WHITE);

		pnlBottom.add(taoDongThongTin("Ghi chú:", lblGhiChu = tool.taoLabel("")));
		pnlBottom.add(taoDongThongTin("Tổng tiền:", lblTongTien = tool.taoLabel("")));
		pnlBottom.add(taoDongThongTin("Tiền nhận:", lblTienNhan = tool.taoLabel("")));
		pnlBottom.add(taoDongThongTin("Tiền thừa:", lblTienThua = tool.taoLabel("")));

		JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 60, 10));
		pnlButtons.setBackground(Color.WHITE);
		btnInHoaDon = tool.taoButton("In hoá đơn", "/picture/hoaDon/print.png");
		btnQuayLai = tool.taoButton("Quay lại", "/picture/hoaDon/signOut.png");
		btnTaoPhieuDoiTra = tool.taoButton("Tạo phiếu đổi trả", "/picture/hoaDon/plus.png");
		pnlButtons.add(btnInHoaDon);
		pnlButtons.add(btnQuayLai);
		pnlButtons.add(btnTaoPhieuDoiTra);
		pnlBottom.add(pnlButtons);

		add(pnlBottom, BorderLayout.SOUTH);
	}

	// ========== TẠO THÔNG TIN ==========
	public JPanel taoDongThongTin(String label, JLabel valueLabel) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
		panel.setBackground(Color.WHITE);
		JLabel lbl = tool.taoLabel(label);
		lbl.setPreferredSize(new Dimension(120, 25));
		panel.add(lbl);
		panel.add(valueLabel);
		return panel;
	}

	// ========== GETTERS ==========
	public JLabel getLblMaHD() {
		return lblMaHD;
	}

	public JLabel getLblNgayLap() {
		return lblNgayLap;
	}

	public JLabel getLblNhanVien() {
		return lblNhanVien;
	}

	public JLabel getLblKhachHang() {
		return lblKhachHang;
	}

	public JLabel getLblDiaChi() {
		return lblDiaChi;
	}

	public JLabel getLblHotline() {
		return lblHotline;
	}

	public JLabel getLblGhiChu() {
		return lblGhiChu;
	}

	public JLabel getLblTongTien() {
		return lblTongTien;
	}

	public JLabel getLblTienNhan() {
		return lblTienNhan;
	}

	public JLabel getLblTienThua() {
		return lblTienThua;
	}

	public JTable getTblThuoc() {
		return tblThuoc;
	}

	public JButton getBtnInHoaDon() {
		return btnInHoaDon;
	}

	public JButton getBtnQuayLai() {
		return btnQuayLai;
	}

	public JButton getBtnTaoPhieuDoiTra() {
		return btnTaoPhieuDoiTra;
	}

	public TrangChuQL_GUI getMainFrameQL() {
		return mainFrameQL;
	}

	public TrangChuNV_GUI getMainFrameNV() {
		return mainFrameNV;
	}
}