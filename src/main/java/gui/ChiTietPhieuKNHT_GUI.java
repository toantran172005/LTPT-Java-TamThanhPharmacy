package gui;

import controller.ChiTietPhieuKNHTController;
import entity.PhieuKhieuNaiHoTroKH;
import utils.ToolCtrl;

import java.awt.*;
import javax.swing.*;

public class ChiTietPhieuKNHT_GUI extends JPanel {

	public ToolCtrl tool = new ToolCtrl();
	public JTextField txtTenKH, txtSdt, txtTenNV;
	public JComboBox<String> cmbLoaiDon, cmbTrangThai;
	public JTextArea txaNoiDung;
	public JButton btnCapNhat, btnQuayLai;
	public PhieuKhieuNaiHoTroKH phieu;
	public ChiTietPhieuKNHTController ctknhtCtrl;

	public ChiTietPhieuKNHT_GUI(PhieuKhieuNaiHoTroKH phieu) {
		this.phieu = phieu;
		ctknhtCtrl = new ChiTietPhieuKNHTController(this);
		tool = new ToolCtrl();
		khoiTaoUI();
		ganData();
		ganSuKien();
	}

	public void ganSuKien() {
		ctknhtCtrl.choPhepEdit(false);
		btnQuayLai.addActionListener(e -> ctknhtCtrl.quayLaiDanhSachKNHT());
		btnCapNhat.addActionListener(e -> ctknhtCtrl.capNhatPhieuKNHT());
	}

	public void ganData() {
		txtTenKH.setText(phieu.getKhachHang().getTenKH());
		txtTenNV.setText(phieu.getNhanVien().getTenNV());
		txtSdt.setText(tool.chuyenSoDienThoai(phieu.getKhachHang().getSdt()));
		txaNoiDung.setText(phieu.getNoiDung());
		cmbLoaiDon.setSelectedItem(phieu.getLoaiDon());
		cmbTrangThai.setSelectedItem(phieu.getTrangThai());
	}

	public void khoiTaoUI() {
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);

		JPanel top = new JPanel(new BorderLayout());
		top.setBackground(Color.WHITE);
		top.setBorder(BorderFactory.createEmptyBorder(20, 10, 30, 10));

		JLabel lblTitle = tool.taoLabel("CHI TIẾT PHIẾU KHIẾU NẠI & HỖ TRỢ");
		lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setVerticalAlignment(SwingConstants.CENTER);
		lblTitle.setPreferredSize(new Dimension(600, 40));

		top.add(lblTitle, BorderLayout.CENTER);
		add(top, BorderLayout.NORTH);

		JPanel center = new JPanel();
		center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
		center.setBackground(Color.WHITE);

		int rowSpacing = 20;
		int labelWidth = 150;
		int fieldWidth = 300;

		center.add(Box.createVerticalStrut(10));

		center.add(taoDong("Tên khách hàng:", txtTenKH = tool.taoTextField(""), labelWidth, fieldWidth));
		center.add(Box.createVerticalStrut(rowSpacing));

		center.add(taoDong("Số điện thoại:", txtSdt = tool.taoTextField(""), labelWidth, fieldWidth));
		center.add(Box.createVerticalStrut(rowSpacing));

		center.add(taoDong("Tên nhân viên:", txtTenNV = tool.taoTextField(""), labelWidth, fieldWidth));
		center.add(Box.createVerticalStrut(rowSpacing));

		cmbLoaiDon = tool.taoComboBox(new String[] { "Khiếu nại", "Hỗ trợ" });
		cmbLoaiDon.setEditable(false);
		center.add(taoDong("Loại đơn:", cmbLoaiDon, labelWidth, fieldWidth));
		center.add(Box.createVerticalStrut(rowSpacing));

		cmbTrangThai = tool.taoComboBox(new String[] { "Hoàn tất", "Chờ xử lý" });
		cmbTrangThai.setEditable(false);
		center.add(taoDong("Trạng thái:", cmbTrangThai, labelWidth, fieldWidth));
		center.add(Box.createVerticalStrut(rowSpacing));

		txaNoiDung = tool.taoTextArea(115);
		JScrollPane scroll = new JScrollPane(txaNoiDung);
		scroll.setPreferredSize(new Dimension(fieldWidth, 115));
		center.add(taoDong("Nội dung:", scroll, labelWidth, fieldWidth));

		add(center, BorderLayout.CENTER);

		JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 20));
		bottom.setBackground(Color.WHITE);

		btnCapNhat = tool.taoButton("Cập nhật", "/picture/khachHang/edit.png");
		btnQuayLai = tool.taoButton("Quay lại", "/picture/khachHang/refresh.png");

		bottom.add(btnCapNhat);
		bottom.add(btnQuayLai);
		add(bottom, BorderLayout.SOUTH);
	}


	// === Hàm tạo 1 hàng label + control ===
	public JPanel taoDong(String text, JComponent comp, int labelWidth, int fieldWidth) {
		JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 0));
		row.setBackground(Color.WHITE);

		JLabel lbl = tool.taoLabel(text);
		lbl.setPreferredSize(new Dimension(labelWidth, 25));
		comp.setPreferredSize(new Dimension(fieldWidth, comp.getPreferredSize().height));

		row.add(lbl);
		row.add(comp);
		return row;
	}

}
