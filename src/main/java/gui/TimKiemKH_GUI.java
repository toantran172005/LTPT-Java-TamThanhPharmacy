package gui;

import controller.TimKiemKhachHangController;
import utils.ToolCtrl;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

//import controller.TimKiemKhachHangCtrl;
//import controller.ToolCtrl;

public class TimKiemKH_GUI extends JPanel {

	public JTextField txtTenKH;
	public JTextField txtSdt;
	public JTable tblKhachHang;
	public JButton btnXemChiTiet;
	public JButton btnLamMoi, btnLichSuXoa, btnXoa;

	public TimKiemKhachHangController tkkhCtrl = new TimKiemKhachHangController(this);
	public ToolCtrl tool = new ToolCtrl();
	public DefaultTableModel model;

	public void setHoatDong() {
		btnLichSuXoa.addActionListener(e -> tkkhCtrl.xuLyBtnLichSuXoa());
		btnLamMoi.addActionListener(e -> tkkhCtrl.lamMoi());
		btnXoa.addActionListener(e -> tkkhCtrl.xoaKhachHang());
		btnXemChiTiet.addActionListener(e -> tkkhCtrl.xemChiTietKH());
		txtTenKH.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				tkkhCtrl.locTatCa(tkkhCtrl.hienThiHoatDong);
			}
		});

		txtSdt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				tkkhCtrl.locTatCa(tkkhCtrl.hienThiHoatDong);
			}
		});
	}

	public TimKiemKH_GUI() {
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);

		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(Color.WHITE);
		topPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 10, 30));

		JPanel mainBox = new JPanel();
		mainBox.setLayout(new BoxLayout(mainBox, BoxLayout.X_AXIS));
		mainBox.setBackground(Color.WHITE);

		JPanel leftVBox = new JPanel();
		leftVBox.setLayout(new BoxLayout(leftVBox, BoxLayout.Y_AXIS));
		leftVBox.setBackground(Color.WHITE);
		leftVBox.setPreferredSize(new Dimension(480, 120));

		txtTenKH = tool.taoTextField("Nhập tên...");
		txtSdt = tool.taoTextField("Số điện thoại...");

		leftVBox.add(taoDong("Tên khách hàng:", txtTenKH, 150, 255));
		leftVBox.add(Box.createVerticalStrut(15));
		leftVBox.add(taoDong("Số điện thoại:", txtSdt, 150, 255));

		JPanel rightVBox = new JPanel();
		rightVBox.setLayout(new BoxLayout(rightVBox, BoxLayout.Y_AXIS));
		rightVBox.setBackground(Color.WHITE);
		rightVBox.setPreferredSize(new Dimension(500, 120));

		JPanel btnRow1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
		btnRow1.setBackground(Color.WHITE);

		btnXemChiTiet = tool.taoButton("Xem chi tiết", "/picture/khachHang/find.png");

		btnRow1.add(btnXemChiTiet);

		JPanel btnRow2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
		btnRow2.setBackground(Color.WHITE);

		btnLichSuXoa = tool.taoButton("Lịch sử xóa", "/picture/khachHang/document.png");
		btnXoa = tool.taoButton("Xóa", "/picture/khachHang/trash.png");
		btnLamMoi = tool.taoButton("Làm mới", "/picture/khachHang/refresh.png");
		btnRow2.add(btnLichSuXoa);
		btnRow2.add(btnXoa);
		btnRow2.add(btnLamMoi);

		rightVBox.add(btnRow1);
		rightVBox.add(Box.createVerticalStrut(10));
		rightVBox.add(btnRow2);

		mainBox.add(leftVBox);
		mainBox.add(Box.createHorizontalStrut(100));
		mainBox.add(rightVBox);

		topPanel.add(mainBox, BorderLayout.CENTER);
		add(topPanel, BorderLayout.NORTH);

		String[] cols = { "Mã khách hàng", "Tên khách hàng", "Số điện thoại", "Tuổi", "Hoạt động" };
		model = new DefaultTableModel(cols, 0) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};

		tblKhachHang = new JTable(model);
		tblKhachHang.setRowHeight(38);
		tblKhachHang.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		
		tblKhachHang.setBackground(Color.WHITE);
		tblKhachHang.getTableHeader().setBackground(new Color(240, 240, 240));
		tblKhachHang.setGridColor(new Color(200, 200, 200));
		tblKhachHang.setShowGrid(true);
		tblKhachHang.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
		
		tblKhachHang.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tblKhachHang.setForeground(new Color(0x33, 0x33, 0x33));

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < tblKhachHang.getColumnCount(); i++) {
			tblKhachHang.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

		JTableHeader header = tblKhachHang.getTableHeader();
		header.setBackground(new Color(240, 240, 240));
		header.setFont(new Font("Times New Roman", Font.BOLD, 18));
		((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

		JScrollPane scrollTable = new JScrollPane(tblKhachHang);
		scrollTable.setBorder(BorderFactory.createLineBorder(new Color(0xCCCCCC)));
		scrollTable.getViewport().setBackground(Color.WHITE);
		scrollTable.setBackground(Color.WHITE);

		add(scrollTable, BorderLayout.CENTER);
		tkkhCtrl.locTatCa(true);
		setHoatDong();
	}

	public JPanel taoDong(String text, JComponent comp, int labelWidth, int fieldWidth) {
		JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		row.setBackground(Color.WHITE);

		if (!text.isEmpty()) {
			JLabel lbl = tool.taoLabel(text);
			lbl.setPreferredSize(new Dimension(labelWidth, 25));
			row.add(lbl);
		}

		if (comp != null) {
			comp.setPreferredSize(new Dimension(fieldWidth, comp.getPreferredSize().height));
			row.add(comp);
		}

		return row;
	}

}
