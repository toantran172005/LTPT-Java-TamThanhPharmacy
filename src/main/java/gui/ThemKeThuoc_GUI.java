package gui;

import controller.ThemKeThuocController;
import utils.ToolCtrl;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;


public class ThemKeThuoc_GUI extends JPanel {

	public JTextField txtLoaiKe;
	public JTextField txtSucChua, txtMoTa;
	public JButton btnLamMoi, btnThem;
	public ToolCtrl tool = new ToolCtrl();
	public ThemKeThuocController tktCtrl = new ThemKeThuocController(this);

	public ThemKeThuoc_GUI() {
		initUI();
		ganHoatDong();
	}

	public void ganHoatDong() {
		txtSucChua.addActionListener(event -> tktCtrl.kiemTraSucChua());
		txtMoTa.addActionListener(event -> tktCtrl.kiemTraTatCa());
		btnLamMoi.addActionListener(event -> tktCtrl.lamMoi());
		btnThem.addActionListener(event -> tktCtrl.kiemTraTatCa());
	}

	public void initUI() {
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		setBorder(new EmptyBorder(20, 40, 20, 40));

		JLabel lblTitle = tool.taoLabel("THÊM KỆ THUỐC");
		lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblTitle, BorderLayout.NORTH);

		JPanel pnlCenter = new JPanel();
		pnlCenter.setLayout(new BoxLayout(pnlCenter, BoxLayout.Y_AXIS));
		pnlCenter.setBackground(Color.WHITE);
		pnlCenter.setBorder(new EmptyBorder(40, 100, 40, 100));

		pnlCenter.add(createInputRow("Loại kệ:", txtLoaiKe = tool.taoTextField("Nhập tên kệ mới..."), null));
		pnlCenter.add(Box.createVerticalStrut(25));
		pnlCenter.add(createInputRow("Sức chứa:", txtSucChua = tool.taoTextField("Sức chứa dưới 900..."), null));
		pnlCenter.add(Box.createVerticalStrut(25));
		pnlCenter.add(createInputRow("Mô tả:", txtMoTa = tool.taoTextField("Mô tả..."), null));

		add(pnlCenter, BorderLayout.CENTER);

		JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 10));
		pnlBottom.setBackground(Color.WHITE);

		btnLamMoi = tool.taoButton("Làm mới", "/picture/keThuoc/refresh.png");
		btnThem = tool.taoButton("Thêm", "/picture/keThuoc/edit.png");

		pnlBottom.add(btnLamMoi);
		pnlBottom.add(btnThem);
		add(pnlBottom, BorderLayout.SOUTH);
	}

	public JPanel createInputRow(String label, JComponent input, String comboPrompt) {
		JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
		row.setBackground(Color.WHITE);

		JLabel lbl = tool.taoLabel(label);
		lbl.setFont(new Font("Times New Roman", Font.BOLD, 15));
		lbl.setPreferredSize(new Dimension(100, 30));

		input.setPreferredSize(new Dimension(250, 35));
		input.setFont(new Font("Times New Roman", Font.PLAIN, 14));

		if (input instanceof JComboBox) {
			((JComboBox<?>) input).setEditable(true);
			((JComboBox<?>) input).setSelectedItem("");
			((JComboBox<?>) input).setToolTipText(comboPrompt);
		}

		row.add(lbl);
		row.add(input);
		return row;
	}

}
