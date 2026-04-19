package gui;

import controller.ThemKhachHangController;
import utils.ToolCtrl;

import javax.swing.*;
import java.awt.*;

public class ThemKhachHang_GUI extends JPanel {

	public ToolCtrl tool = new ToolCtrl();
    public JTextField txtTenKH;
    public JTextField txtSdt;
    public JTextField txtTuoi;
    public JButton btnLamMoi;
    public JButton btnThem;
    public ThemKhachHangController tkhCtrl;


    public ThemKhachHang_GUI() {
    	tkhCtrl = new ThemKhachHangController(this);
    	tool = new ToolCtrl();
        khoiTaoUI();
        setHoatDong();
    }
    
    public void setHoatDong() {
    	btnLamMoi.addActionListener(e -> tkhCtrl.lamMoi());
    	btnThem.addActionListener(e -> tkhCtrl.ktTatCaTruocKhiThem());
    }
    
    public void khoiTaoUI() {
    	setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);
        top.setBorder(BorderFactory.createEmptyBorder(20, 10, 30, 10));

        JLabel lblTitle = tool.taoLabel("THÊM KHÁCH HÀNG");
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 22));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
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

        center.add(taoDong("Tên khách hàng:", txtTenKH = tool.taoTextField("Nhập tên..."), labelWidth, fieldWidth));
        center.add(Box.createVerticalStrut(rowSpacing));

        center.add(taoDong("Số điện thoại:", txtSdt = tool.taoTextField("Nhập số điện thoại..."), labelWidth, fieldWidth));
        center.add(Box.createVerticalStrut(rowSpacing));

        center.add(taoDong("Tuổi:", txtTuoi = tool.taoTextField("Nhập tuổi..."), labelWidth, fieldWidth));
        center.add(Box.createVerticalStrut(rowSpacing));

        add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 20));
        bottom.setBackground(Color.WHITE);

        btnLamMoi = tool.taoButton("Làm mới", "/picture/khachHang/return.png");
        btnThem = tool.taoButton("Thêm", "/picture/khachHang/addUser.png");

        bottom.add(btnLamMoi);
        bottom.add(btnThem);

        add(bottom, BorderLayout.SOUTH);
    }

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
