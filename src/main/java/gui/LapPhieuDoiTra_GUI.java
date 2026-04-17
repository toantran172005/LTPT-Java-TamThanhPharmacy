package gui;

//import controller.ToolCtrl;
import utils.ToolCtrl;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;

public class LapPhieuDoiTra_GUI extends JPanel {
	public JLabel lblMaHD, lblKhachHang, lblTongTienHoan;
	public JTextField txtTenThuoc, txtSoLuong;
	public JTextArea txaLyDo, txaGhiChu;
	public JComboBox<String> cmbMucHoan;
	public JTable tblHDThuoc, tblPhieuDTThuoc;
	public JButton btnThem, btnLamMoi, btnTaoPhieuDT, btnQuayLai, btnXoa;
	public ToolCtrl toolCtrl = new ToolCtrl();
	public TrangChuQL_GUI trangChuQL;
	public TrangChuNV_GUI trangChuNV;

	Font font1 = new Font("Times New Roman", Font.BOLD, 18);
	Font font2 = new Font("Times New Roman", Font.PLAIN, 15);

	public LapPhieuDoiTra_GUI() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(813, 711));
		setBackground(Color.WHITE);

		// ========== TOP PANEL ==========
		JPanel pnlTop = new JPanel();
		pnlTop.setLayout(new BoxLayout(pnlTop, BoxLayout.Y_AXIS));
		pnlTop.setBackground(Color.WHITE);
		pnlTop.setBorder(new EmptyBorder(10, 20, 10, 20));

		// ===== Tiêu đề =====
		JLabel lblTieuDe = new JLabel("LẬP PHIẾU ĐỔI TRẢ", SwingConstants.CENTER);
		lblTieuDe.setFont(font1);
		lblTieuDe.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlTop.add(lblTieuDe);
		pnlTop.add(Box.createVerticalStrut(10));

		JPanel pnlMaHD = taoLabelHD("Mã hóa đơn:", "");
		lblMaHD = (JLabel) ((JPanel) pnlMaHD.getComponent(1)).getComponent(0);
		pnlTop.add(pnlMaHD);

		JPanel pnlKhach = taoLabelHD("Khách hàng:", "");
		lblKhachHang = (JLabel) ((JPanel) pnlKhach.getComponent(1)).getComponent(0);
		pnlTop.add(pnlKhach);

		JLabel lblDanhSachHD = new JLabel("DANH SÁCH THUỐC ĐÃ MUA", SwingConstants.CENTER);
		lblDanhSachHD.setFont(font1);
		lblDanhSachHD.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlTop.add(lblDanhSachHD);

		add(pnlTop, BorderLayout.NORTH);

		// ========== CENTER PANEL ==========
		String[] colsHD = { "Mã thuốc", "Tên thuốc", "Nơi sản xuất", "Số lượng", "Đơn vị", "Đơn giá", "Thành tiền" };
		DefaultTableModel modelHD = new DefaultTableModel(colsHD, 0);
		tblHDThuoc = new JTable(modelHD);
		tblHDThuoc.setFont(font2);
		tblHDThuoc.getTableHeader().setFont(font2);
		tblHDThuoc.setRowHeight(28);

		tblHDThuoc.setBackground(Color.WHITE);
		tblHDThuoc.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
		JScrollPane scrollPane = new JScrollPane(tblHDThuoc);
		scrollPane.getViewport().setBackground(Color.WHITE);

		// ===== Căn giữa nội dung =====
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < tblHDThuoc.getColumnCount(); i++) {
			tblHDThuoc.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

		// ===== Căn giữa tiêu đề cột =====
		JTableHeader header = tblHDThuoc.getTableHeader();
		header.setBackground(new Color(240, 240, 240));
		header.setFont(font2);
		((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		JScrollPane spHD = new JScrollPane(tblHDThuoc);
		add(spHD, BorderLayout.CENTER);

		// ========== BOTTOM PANEL ==========
		JPanel pnlBottom = new JPanel(new BorderLayout());
		pnlBottom.setBackground(Color.WHITE);
		pnlBottom.setBorder(new EmptyBorder(10, 10, 10, 10));

		// ===== Danh sách đổi trả =====
		JPanel pnlTopBottom = new JPanel();
		pnlTopBottom.setLayout(new BoxLayout(pnlTopBottom, BoxLayout.Y_AXIS));
		pnlTopBottom.setBackground(Color.WHITE);

		// ===== Tiêu đề  =====
		JLabel lblDSThuocDT = new JLabel("DANH SÁCH THUỐC ĐỔI TRẢ", SwingConstants.CENTER);
		lblDSThuocDT.setFont(font1);
		lblDSThuocDT.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlTopBottom.add(lblDSThuocDT);
		pnlTopBottom.add(Box.createVerticalStrut(10));

		// ===== Form nhập =====
		JPanel pnlForm = new JPanel(new GridBagLayout());
		pnlForm.setBackground(Color.WHITE);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 10, 5, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// ===== Tên thuốc + Mức hoàn =====
		JPanel col1 = new JPanel();
		col1.setLayout(new BoxLayout(col1, BoxLayout.Y_AXIS));
		col1.setBackground(Color.WHITE);
		col1.add(taoLabelPDT("Tên thuốc:", txtTenThuoc = toolCtrl.taoTextField("Tên thuốc..."), 180));
		col1.add(Box.createVerticalStrut(10));
		col1.add(taoLabelPDT("Mức hoàn:",
				cmbMucHoan = toolCtrl.taoComboBox(new String[] { "100%", "75%", "50%", "25%" }), 100));

		// ===== Số lượng + Ghi chú =====
		JPanel col2 = new JPanel();
		col2.setLayout(new BoxLayout(col2, BoxLayout.Y_AXIS));
		col2.setBackground(Color.WHITE);
		col2.add(taoLabelPDT("Số lượng:", txtSoLuong = toolCtrl.taoTextField("Số lượng..."), 80));
		col2.add(Box.createVerticalStrut(10));
		col2.add(taoLabelPDT("Ghi chú:", txaGhiChu = toolCtrl.taoTextArea(20), 150));

		// ===== Nút Thêm và Xoá =====
		JPanel col3 = new JPanel();
		col3.setLayout(new BoxLayout(col3, BoxLayout.Y_AXIS));
		col3.setBackground(Color.WHITE);
		btnThem = toolCtrl.taoButton("Thêm", "/picture/hoaDon/plus.png");
		btnXoa = toolCtrl.taoButton("Xoá", "/picture/hoaDon/trash.png");
		JPanel btnWrapper = new JPanel();
		btnWrapper.setBackground(Color.WHITE);
		btnWrapper.add(btnThem);
		btnWrapper.add(btnXoa);
		col3.add(btnWrapper);

		// ===== Đặt 3 cột vào GridBag =====
		gbc.gridx = 0;
		gbc.weightx = 0.4;
		pnlForm.add(col1, gbc);
		gbc.gridx = 1;
		gbc.weightx = 0.4;
		pnlForm.add(col2, gbc);
		gbc.gridx = 2;
		gbc.weightx = 0.2;
		pnlForm.add(col3, gbc);

		pnlTopBottom.add(pnlForm);
		pnlTopBottom.add(Box.createVerticalStrut(10));

		// ===== Bảng đổi trả =====
		String[] colsDT = { "Tên thuốc", "Nơi sản xuất", "Số lượng", "Đơn vị", "Đơn giá", "Mức hoàn", "Tiền hoàn", "Ghi chú"};
		DefaultTableModel modelDT = new DefaultTableModel(colsDT, 0);
		tblPhieuDTThuoc = new JTable(modelDT);
		tblPhieuDTThuoc.setFont(font2);
		tblPhieuDTThuoc.getTableHeader().setFont(font2);
		tblPhieuDTThuoc.setRowHeight(28);

		tblPhieuDTThuoc.setBackground(Color.WHITE);
		tblPhieuDTThuoc.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
		JScrollPane scrollPanePDT = new JScrollPane(tblPhieuDTThuoc);
		scrollPanePDT.getViewport().setBackground(Color.WHITE);

		// ===== Căn giữa nội dung các ô =====
		DefaultTableCellRenderer centerRendererPDT = new DefaultTableCellRenderer();
		centerRendererPDT.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < tblPhieuDTThuoc.getColumnCount(); i++) {
			tblPhieuDTThuoc.getColumnModel().getColumn(i).setCellRenderer(centerRendererPDT);
		}

		// ===== Căn giữa tiêu đề cột =====
		JTableHeader headerPDT = tblPhieuDTThuoc.getTableHeader();
		headerPDT.setBackground(new Color(240, 240, 240));
		headerPDT.setFont(font2);
		((DefaultTableCellRenderer) headerPDT.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

		JScrollPane spDT = new JScrollPane(tblPhieuDTThuoc);
		spDT.setPreferredSize(new Dimension(0, 150));
		pnlTopBottom.add(spDT);

		pnlBottom.add(pnlTopBottom, BorderLayout.NORTH);

		// ===== Lý do + Tổng tiền + Nút =====
		JPanel pnlFooter = new JPanel(new BorderLayout());
		pnlFooter.setBackground(Color.WHITE);

		// ===== Lý do + Tổng tiền =====
		JPanel pnlInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 10));
		pnlInfo.setBackground(Color.WHITE);

		JPanel pnlLyDoWrapper = new JPanel(new BorderLayout(10, 0));
		pnlLyDoWrapper.setBackground(Color.WHITE);
		pnlLyDoWrapper.add(toolCtrl.taoLabel("Lý do:"), BorderLayout.WEST);
		txaLyDo = toolCtrl.taoTextArea(20);
		txaLyDo.setPreferredSize(new Dimension(338, 54));
		pnlLyDoWrapper.add(txaLyDo, BorderLayout.CENTER);

		JPanel pnlTongTienWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		pnlTongTienWrapper.setBackground(Color.WHITE);
		pnlTongTienWrapper.add(toolCtrl.taoLabel("Tổng tiền hoàn:"));
		lblTongTienHoan = toolCtrl.taoLabel("0 VNĐ");
		pnlTongTienWrapper.add(lblTongTienHoan);

		pnlInfo.add(pnlLyDoWrapper);
		pnlInfo.add(pnlTongTienWrapper);

		// ===== Các nút =====
		JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 10));
		pnlButtons.setBackground(Color.WHITE);
		btnLamMoi = toolCtrl.taoButton("Làm mới", "/picture/hoaDon/return.png");
		btnTaoPhieuDT = toolCtrl.taoButton("Tạo phiếu đổi trả", "/picture/hoaDon/plus.png");
		btnQuayLai = toolCtrl.taoButton("Quay lại", "/picture/hoaDon/signOut.png");

		// ===== Đặt kích thước nút =====
		Dimension btnSize = new Dimension(140, 36);
		Dimension btnSize2 = new Dimension(190, 36);
		btnLamMoi.setPreferredSize(btnSize);
		btnTaoPhieuDT.setPreferredSize(btnSize2);
		btnQuayLai.setPreferredSize(btnSize);

		pnlButtons.add(btnLamMoi);
		pnlButtons.add(btnTaoPhieuDT);
		pnlButtons.add(btnQuayLai);

		pnlFooter.add(pnlInfo, BorderLayout.CENTER);
		pnlFooter.add(pnlButtons, BorderLayout.SOUTH);

		pnlBottom.add(pnlFooter, BorderLayout.SOUTH);
		add(pnlBottom, BorderLayout.SOUTH);
	}
	
	// ========== TẠO LABEL HOÁ ĐƠN ==========
	public JPanel taoLabelHD(String title, String value) {
		JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
		row.setBackground(Color.WHITE);
		row.add(toolCtrl.taoLabel(title));
		JLabel lblValue = toolCtrl.taoLabel(value);
		JPanel valuePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		valuePanel.setBackground(Color.WHITE);
		valuePanel.add(lblValue);
		row.add(valuePanel);
		return row;
	}

	// ========== TẠO LABEL PHIẾU ĐỔI TRẢ ==========
	public JPanel taoLabelPDT(String label, JComponent comp, int width) {
		JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		row.setBackground(Color.WHITE);
		JLabel lbl = toolCtrl.taoLabel(label);
		lbl.setPreferredSize(new Dimension(80, 30));
		comp.setPreferredSize(new Dimension(width, 32));
		if (comp instanceof JTextArea) {
			comp.setFont(font2);
		} else {
			comp.setFont(font2);
		}
		row.add(lbl);
		row.add(comp);
		return row;
	}

	// ========== GETTERS ==========
	public JLabel getLblMaHD() {
		return lblMaHD;
	}

	public JLabel getLblKhachHang() {
		return lblKhachHang;
	}

	public JLabel getLblTongTienHoan() {
		return lblTongTienHoan;
	}

	public JTextField getTxtTenThuoc() {
		return txtTenThuoc;
	}

	public JTextField getTxtSoLuong() {
		return txtSoLuong;
	}

	public JComboBox<String> getCmbMucHoan() {
		return cmbMucHoan;
	}

	public JTextArea getTxaGhiChu() {
		return txaGhiChu;
	}

	public JTextArea getTxaLyDo() {
		return txaLyDo;
	}

	public JTable getTblHDThuoc() {
		return tblHDThuoc;
	}

	public JTable getTblPhieuDTThuoc() {
		return tblPhieuDTThuoc;
	}

	public JButton getBtnThem() {
		return btnThem;
	}

	public JButton getBtnXoa() {
		return btnXoa;
	}
	
	public JButton getBtnLamMoi() {
		return btnLamMoi;
	}

	public JButton getBtnTaoPhieuDT() {
		return btnTaoPhieuDT;
	}

	public JButton getBtnQuayLai() {
		return btnQuayLai;
	}

	public TrangChuQL_GUI getTrangChuQL() {
		return trangChuQL;
	}

	public TrangChuNV_GUI getTrangChuNV() {
		return trangChuNV;
	}

	public void setTrangChuQL(TrangChuQL_GUI trangChuQL) {
		this.trangChuQL = trangChuQL;
	}

	public void setTrangChuNV(TrangChuNV_GUI trangChuNV) {
		this.trangChuNV = trangChuNV;
	}
}