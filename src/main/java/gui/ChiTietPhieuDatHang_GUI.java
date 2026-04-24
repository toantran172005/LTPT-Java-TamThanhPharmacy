package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

//import controller.ChiTietPDHCtrl;
//import controller.ToolCtrl;
//import dao.DonViTinhDAO;
//import dao.KhuyenMaiDAO;
//import dao.PhieuDatHangDAO;
//import dao.ThuocDAO;
import controller.ChiTietPDHController;
import entity.KhuyenMai;
import entity.PhieuDatHang;
import service.KhuyenMaiService;
import service.PhieuDatHangService;
import service.ThuocService;
import utils.ToolCtrl;

import java.awt.*;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.ArrayList;

public class ChiTietPhieuDatHang_GUI extends JPanel {

	public JLabel lblDiaChi, lblHotline, lblMaPhieu, lblNgayDat, lblNgayHen, lblNhanVien, lblKhachHang;
	public JTable tblThuoc;
	public JTextArea txaGhiChu;
	public DefaultTableModel model;
	public JComboBox<String> cmbTrangThai;
	public JButton btnTaoHD, btnCapNhat, btnQuayLai;
	public TrangChuQL_GUI mainFrameQL;
	public TrangChuNV_GUI mainFrameNV;

	// NÂNG CẤP: Dùng Service thay vì DAO
	public PhieuDatHangService pdhService = new PhieuDatHangService();
	public ThuocService thService = new ThuocService();
	public KhuyenMaiService kmService = new KhuyenMaiService();
	Font font1 = new Font("Time New Roman", Font.BOLD, 18);
	Font font2 = new Font("Time New Roman", Font.PLAIN, 15);
	public ToolCtrl tool = new ToolCtrl();
	public PhieuDatHang pdh;
	public ChiTietPDHController ctpdhCtrl = new ChiTietPDHController(this);
	public JComboBox<String> cmbPTThanhToan;
	public JLabel lblTongTien;
	public JTextField txtTienNhan;
	public JLabel lblTienThua;
	public boolean daChuyenHD = false;

	public ChiTietPhieuDatHang_GUI(TrangChuQL_GUI mainFrame, PhieuDatHang pdh) {
		this.mainFrameQL = mainFrame;
		this.pdh = pdh;
		initUI();
		hienThiThongTin(pdh);
		choPhepCapNhap();
		setHoatDong();
	}

	public ChiTietPhieuDatHang_GUI(TrangChuNV_GUI mainFrame, PhieuDatHang pdh) {
		this.mainFrameNV = mainFrame;
		this.pdh = pdh;
		initUI();
		hienThiThongTin(pdh);
		choPhepCapNhap();
		setHoatDong();
	}

	public void setHoatDong() {
		btnQuayLai.addActionListener(e -> ctpdhCtrl.quayLaiTrangDanhSach());
		btnCapNhat.addActionListener(e -> ctpdhCtrl.capNhatPDH());
		btnTaoHD.addActionListener(e -> ctpdhCtrl.taoHoaDon());
	}

	public void choPhepCapNhap() {
		if (btnCapNhat.getText().trim().equals("Cập nhật")) {
			cmbTrangThai.setEnabled(false);
			txaGhiChu.setEditable(false);
		} else {
			cmbTrangThai.setEnabled(true);
			txaGhiChu.setEditable(true);
		}
	}

	public void hienThiThongTin(PhieuDatHang pdh) {
		if (pdh == null)
			return;

		lblMaPhieu.setText(pdh.getMaPDH());
		lblNgayDat.setText(tool.dinhDangLocalDate(pdh.getNgayDat()));
		lblNgayHen.setText(tool.dinhDangLocalDate(pdh.getNgayHen()));
		lblNhanVien.setText(pdh.getNhanVien() != null ? pdh.getNhanVien().getTenNV() : "");
		lblKhachHang.setText(pdh.getKhachHang() != null ? pdh.getKhachHang().getTenKH() : "");
		txaGhiChu.setText(pdh.getGhiChu());
		lblDiaChi.setText(pdh.getDiaChiHT());
		lblHotline.setText(tool.chuyenSoDienThoai(pdh.getHotline()));
		if(pdh.getTrangThai().equals("Đã giao")) {
			cmbTrangThai.setSelectedItem("Đã giao");
			daChuyenHD = true;
		} else {
			cmbTrangThai.setSelectedItem(pdh.getTrangThai());
		}

		DefaultTableModel modelThuoc = (DefaultTableModel) tblThuoc.getModel();
		modelThuoc.setRowCount(0);

		ArrayList<Object[]> dsThuoc = pdhService.layDanhSachThuocTheoPDH(pdh.getMaPDH());

		if (dsThuoc == null || dsThuoc.isEmpty()) {
			tool.hienThiThongBao("Thông báo", "Không có chi tiết thuốc cho phiếu này!", false);
			return;
		}

		for (Object[] row : pdhService.layDanhSachThuocTheoPDH(pdh.getMaPDH())) {

			String maThuoc = row[1].toString();
			String tenThuoc = row[2].toString();
			String noiSanXuat = thService.timTenQGTheoMaThuoc(maThuoc);
			int soLuong = Integer.parseInt(row[3].toString());
			String tenDVT = row[5].toString();
			double donGia = (double) row[6];

			double thanhTien = (double) row[7];

			double mucGiam;
			String moTaKM = "Không có KM";
			String maKM = thService.layMaKMTheoMaThuoc(maThuoc);
			LocalDate homNay = LocalDate.now();

			if (maKM != null && !maKM.isEmpty()) {
				KhuyenMai km = kmService.layKhuyenMaiTheoMa(maKM);
				if (km != null && !homNay.isBefore(km.getNgayBD()) && !homNay.isAfter(km.getNgayKT())) {
					switch (km.getLoaiKM().toLowerCase()) {
					case "giảm giá":
						mucGiam = (double) km.getMucKM();
						moTaKM = "Giảm " + mucGiam + "%";
						break;

					case "mua tặng":
						int soLuongTang = (soLuong / km.getSoLuongMua()) * km.getSoLuongTang();
						if (soLuongTang > 0) {
							moTaKM = String.format("Mua %d tặng %d (Tặng: %d)", km.getSoLuongMua(), km.getSoLuongTang(),
									soLuongTang);
						}
						break;
					}
				}
			}

			modelThuoc.addRow(new Object[] { tenThuoc, noiSanXuat, soLuong, tenDVT, tool.dinhDangVND(donGia),
					tool.dinhDangVND(thanhTien), moTaKM });
		}

	}

	public static String boDau(String s) {
		if (s == null)
			return "";
		return Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
				.toLowerCase().trim();
	}

	public void initUI() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(1058, 509));
		setBackground(Color.WHITE);

		// ===== TOP =====
		JPanel pnlTop = new JPanel();
		pnlTop.setLayout(new BoxLayout(pnlTop, BoxLayout.Y_AXIS));
		pnlTop.setBorder(new EmptyBorder(10, 0, 10, 0));
		pnlTop.setBackground(Color.WHITE);

		JLabel lblTieuDe = new JLabel("HIỆU THUỐC TAM THANH", SwingConstants.CENTER);
		lblTieuDe.setFont(font1);
		lblTieuDe.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlTop.add(lblTieuDe);

		pnlTop.add(Box.createVerticalStrut(10));
		JPanel pnlDiaChi = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
		pnlDiaChi.setBackground(Color.WHITE);
		pnlDiaChi.add(tool.taoLabel("Địa chỉ: "));
		lblDiaChi = tool.taoLabel("");
		pnlDiaChi.add(lblDiaChi);
		pnlTop.add(pnlDiaChi);

		JPanel pnlHotline = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
		pnlHotline.setBackground(Color.WHITE);
		pnlHotline.add(tool.taoLabel("Hotline: "));
		lblHotline = tool.taoLabel("");
		pnlHotline.add(lblHotline);
		pnlTop.add(pnlHotline);

		JLabel lblChiTiet = new JLabel("CHI TIẾT PHIẾU ĐẶT THUỐC", SwingConstants.CENTER);
		lblChiTiet.setFont(font1);
		lblChiTiet.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlTop.add(lblChiTiet);

		pnlTop.add(taoDongThongTin("Mã phiếu:", lblMaPhieu = tool.taoLabel("")));
		pnlTop.add(taoDongThongTin("Ngày đặt:", lblNgayDat = tool.taoLabel("")));
		pnlTop.add(taoDongThongTin("Ngày hẹn:", lblNgayHen = tool.taoLabel("")));
		pnlTop.add(taoDongThongTin("Nhân viên:", lblNhanVien = tool.taoLabel("")));
		pnlTop.add(taoDongThongTin("Khách hàng:", lblKhachHang = tool.taoLabel("")));

		add(pnlTop, BorderLayout.NORTH);

		// ===== CENTER =====
		JPanel pnlCenter = new JPanel();
		pnlCenter.setLayout(new BoxLayout(pnlCenter, BoxLayout.Y_AXIS));
		pnlCenter.setBorder(new EmptyBorder(10, 20, 10, 20));
		pnlCenter.setBackground(Color.WHITE);

		String[] columnNames = { "Tên thuốc", "Nơi sản xuất", "Số lượng", "Đơn vị", "Đơn giá", "Thành tiền", "Ghi chú"};
		model = new DefaultTableModel(columnNames, 0);
		tblThuoc = new JTable(model);
		tblThuoc.setRowHeight(28);
		tblThuoc.getTableHeader().setFont(font2);
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
		header.setBackground(new Color(240, 240, 240));
		header.setFont(font2);
		((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		pnlCenter.add(scrollPane);

		pnlCenter.add(Box.createVerticalStrut(15));

		JPanel pnlThongTin = new JPanel(new BorderLayout(30, 0));
		pnlThongTin.setBackground(Color.WHITE);

		JPanel pnlLeft = new JPanel();
		pnlLeft.setLayout(new BoxLayout(pnlLeft, BoxLayout.Y_AXIS));
		pnlLeft.setBackground(Color.WHITE);

		pnlLeft.add(taoDongThongTin("Ghi chú:", txaGhiChu = tool.taoTextArea(30)));
		pnlLeft.add(Box.createVerticalStrut(10));

		JPanel trangThaiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
		trangThaiPanel.setBackground(Color.WHITE);
		trangThaiPanel.add(tool.taoLabel("Trạng thái:"));
		cmbTrangThai = tool.taoComboBox(new String[] {"Chờ hàng", "Đã hủy" });
		cmbTrangThai.setPreferredSize(new Dimension(150, 30));
		cmbTrangThai.setEditable(false);
		trangThaiPanel.add(cmbTrangThai);
		pnlLeft.add(trangThaiPanel);

		pnlThongTin.add(pnlLeft, BorderLayout.CENTER);

		pnlCenter.add(pnlThongTin);
		add(pnlCenter, BorderLayout.CENTER);

		// ===== BOTTOM =====
		JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
		pnlBottom.setBackground(Color.WHITE);

		btnTaoHD = tool.taoButton("Tạo hóa đơn", "/picture/hoaDon/print.png");
		btnCapNhat = tool.taoButton("Cập nhật", "/picture/hoaDon/edit.png");
		btnQuayLai = tool.taoButton("Quay lại", "/picture/hoaDon/signOut.png");

		pnlBottom.add(btnTaoHD);
		pnlBottom.add(btnCapNhat);
		pnlBottom.add(btnQuayLai);

		add(pnlBottom, BorderLayout.SOUTH);
	}

	public JPanel taoHangTrai() {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
		p.setBackground(Color.WHITE);
		return p;
	}

	public JPanel taoDongThongTin(String tieuDe, JComponent noiDung) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
		panel.setBackground(Color.WHITE);

		JLabel lblTieuDe = tool.taoLabel(tieuDe);
		panel.add(lblTieuDe);

		if (noiDung instanceof JTextArea) {
			JTextArea txa = (JTextArea) noiDung;
			txa.setLineWrap(true);
			txa.setWrapStyleWord(true);
			JScrollPane scroll = new JScrollPane(txa);
			scroll.setPreferredSize(new Dimension(250, 60));
			panel.add(scroll);
		} else {
			panel.add(noiDung);
		}

		return panel;
	}
	
	public JLabel getLblMaPhieuDat() {
		return lblMaPhieu;
	}

	public TrangChuQL_GUI getMainFrameQL() {
		return mainFrameQL;
	}

	public TrangChuNV_GUI getMainFrameNV() {
		return mainFrameNV;
	}

}