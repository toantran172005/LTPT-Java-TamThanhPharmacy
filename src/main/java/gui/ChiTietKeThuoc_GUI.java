package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

//import controller.ChiTietKeThuocCtrl;
//import controller.ToolCtrl;
//import dao.KeThuocDAO;
import entity.KeThuoc;
import utils.ToolCtrl;

import java.awt.*;

public class ChiTietKeThuoc_GUI extends JPanel {

	public JTextField txtMaKe, txtSucChua;
	public JTextArea txaMoTa;
	public JComboBox<String> cmbLoaiKe, cmbTrangThai;
	public JTable tblChiTietKT;
	public JButton btnCapNhat, btnThoat;
	public ToolCtrl tool;
	public DefaultTableModel model;
//	public KeThuocDAO ktDAO;
//	public ChiTietKeThuocCtrl ctktCtrl;
	public KeThuoc ke;

	public ChiTietKeThuoc_GUI(KeThuoc ke) {
		this.ke = ke;
		tool = new ToolCtrl();
//		ktDAO = new KeThuocDAO();
//		ctktCtrl = new ChiTietKeThuocCtrl(this);
		khoiTaoUI();
		ganData(ke);
		ganAction();
	}

	public void ganAction() {
//		btnThoat.addActionListener(e -> ctktCtrl.quayLaiDS());
//		btnCapNhat.addActionListener(e -> ctktCtrl.xuLyCapNhat());
	}

	public void ganData(KeThuoc ke) {
//		txtMaKe.setText(ke.getMaKe());
//		txtSucChua.setText(String.valueOf(ke.getSucChua()));
//		txaMoTa.setText(ke.getMoTa());
//		khoiTaoCmb();
//		ctktCtrl.choPhepEdit(false);
//		ctktCtrl.setDataChoTable(ctktCtrl.listThuoc);
	}

//	public void khoiTaoCmb() {
//		cmbTrangThai.setSelectedItem(ke.isTrangThai() ? "Hoạt động" : "Ngừng hoạt động");
//		for (String lk : ktDAO.layTatCaKeThuoc()) {
//			cmbLoaiKe.addItem(lk);
//		}
//		cmbLoaiKe.setSelectedItem(ke.getLoaiKe());
//	}

	public void khoiTaoUI() {
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);

		JPanel top = new JPanel(new BorderLayout());
		top.setBackground(Color.WHITE);
		top.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

		JLabel lblTitle = new JLabel("CHI TIẾT KỆ THUỐC", SwingConstants.CENTER);
		lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
		top.add(lblTitle, BorderLayout.NORTH);

		JPanel grid = new JPanel(new GridLayout(3, 2, 40, 20));
		grid.setBackground(Color.WHITE);

		grid.add(taoDong("Mã kệ:", txtMaKe = tool.taoTextField("")));
		grid.add(taoDong("Loại kệ:", cmbLoaiKe = tool.taoComboBox(new String[] { "" })));
		cmbLoaiKe.setEditable(false);
		txtMaKe.setEditable(false);

		JScrollPane scrollMoTa = new JScrollPane(txaMoTa = tool.taoTextArea(80));
		scrollMoTa.setPreferredSize(new Dimension(280, 85));
		grid.add(taoDong("Mô tả:", scrollMoTa));
		grid.add(taoDong("Trạng thái:",
				cmbTrangThai = tool.taoComboBox(new String[] { "Hoạt động", "Ngừng hoạt động" })));
		cmbTrangThai.setEditable(false);

		grid.add(taoDong("Sức chứa:", txtSucChua = tool.taoTextField("")));
		grid.add(new JLabel());

		top.add(grid, BorderLayout.CENTER);
		add(top, BorderLayout.NORTH);

		String[] cols = { "Mã thuốc", "Tên thuốc", "Trạng thái" };
		model = new DefaultTableModel(cols, 0) {
			@Override
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		};
		tblChiTietKT = new JTable(model);
		tblChiTietKT.setRowHeight(38);
		tblChiTietKT.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		tblChiTietKT.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 14));
		tblChiTietKT.setSelectionBackground(new Color(0xE3F2FD));
		tblChiTietKT.setGridColor(new Color(0xDDDDDD));
		tblChiTietKT.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tblChiTietKT.getColumnModel().getColumn(0).setPreferredWidth(97);
		tblChiTietKT.getColumnModel().getColumn(1).setPreferredWidth(199);
		tblChiTietKT.getColumnModel().getColumn(2).setPreferredWidth(435);
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < tblChiTietKT.getColumnCount(); i++) {
			tblChiTietKT.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

		JTableHeader header = tblChiTietKT.getTableHeader();
		header.setBackground(new Color(240, 240, 240));
		header.setFont(new Font("Times New Roman", Font.BOLD, 18));
		((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

		JScrollPane scrollTable = new JScrollPane(tblChiTietKT);
		scrollTable.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30),
				BorderFactory.createLineBorder(new Color(0xCCCCCC))));

		tblChiTietKT.setBackground(Color.WHITE);
		tblChiTietKT.setOpaque(true);
		tblChiTietKT.getTableHeader().setBackground(Color.WHITE);
		tblChiTietKT.getTableHeader().setOpaque(true);

		scrollTable.getViewport().setBackground(Color.WHITE);
		scrollTable.setBackground(Color.WHITE);

		add(scrollTable, BorderLayout.CENTER);

		JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 80, 15));
		bottom.setBackground(Color.WHITE);
		btnCapNhat = tool.taoButton("Cập nhật", "/picture/keThuoc/edit.png");
		btnThoat = tool.taoButton("Quay lại", "/picture/trangChu/signOut.png");
		bottom.add(btnCapNhat);
		bottom.add(btnThoat);
		add(bottom, BorderLayout.SOUTH);
	}

	public JPanel taoDong(String text, JComponent comp) {
		JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
		row.setBackground(Color.WHITE);
		JLabel lbl = tool.taoLabel(text);
		lbl.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		comp.setPreferredSize(new Dimension(250, comp.getPreferredSize().height));
		row.add(lbl);
		row.add(comp);
		return row;
	}
}
