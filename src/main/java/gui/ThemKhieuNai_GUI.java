package gui;

import javax.swing.*;
import java.awt.*;
import com.toedter.calendar.JDateChooser;
import utils.ToolCtrl;

//import controller.ThemKhieuNaiCtrl;
//import controller.ToolCtrl;

public class ThemKhieuNai_GUI extends JPanel {

    public ToolCtrl tool = new ToolCtrl();
    public JTextField txtTenKhachHang, txtSdt;
    public JComboBox<String> cmbLoaiDon;
    public JTextArea txaNoiDung;
    public JButton btnQuayLai, btnLamMoi, btnThem;
    public JDateChooser dateNgayLap;
//    public ThemKhieuNaiCtrl tknCtrl;


    public ThemKhieuNai_GUI() {
    	tool = new ToolCtrl();
//    	tknCtrl = new ThemKhieuNaiCtrl(this);
    	khoiTaoUI();
//    	ganSuKien();
    }
//
//    public void ganSuKien() {
//    	btnQuayLai.addActionListener(e -> tknCtrl.quayLaiDanhSach());
//    	btnLamMoi.addActionListener(e -> tknCtrl.lamMoi());
//    	btnThem.addActionListener(e -> tknCtrl.themPhieu());
//    }
//
    public void khoiTaoUI() {
    	setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ===== TOP =====
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);
        top.setBorder(BorderFactory.createEmptyBorder(20, 10, 30, 10));

        JLabel lblTitle = tool.taoLabel("THÊM KHIẾU NẠI & HỖ TRỢ KHÁCH HÀNG");
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

        center.add(taoDong("Tên khách hàng:", txtTenKhachHang = tool.taoTextField(""), labelWidth, fieldWidth));
        center.add(Box.createVerticalStrut(rowSpacing));
        center.add(taoDong("Số điện thoại:", txtSdt = tool.taoTextField(""), labelWidth, fieldWidth));
        center.add(Box.createVerticalStrut(rowSpacing));

        dateNgayLap = tool.taoDateChooser();
        center.add(taoDong("Ngày lập:", dateNgayLap, labelWidth, fieldWidth));
        center.add(Box.createVerticalStrut(rowSpacing));

        cmbLoaiDon = tool.taoComboBox(new String[] { "Khiếu nại", "Hỗ trợ"});
        cmbLoaiDon.setEditable(false);
        center.add(taoDong("Loại đơn:", cmbLoaiDon, labelWidth, fieldWidth));
        center.add(Box.createVerticalStrut(rowSpacing));

        txaNoiDung = tool.taoTextArea(115);
        JScrollPane scroll = new JScrollPane(txaNoiDung);
        scroll.setPreferredSize(new Dimension(fieldWidth, 115));
        center.add(taoDong("Nội dung:", scroll, labelWidth, fieldWidth));

        add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 20));
        bottom.setBackground(Color.WHITE);

        btnQuayLai = tool.taoButton("Quay lại", "/picture/nhanVien/signOut.png");
        btnLamMoi = tool.taoButton("Làm mới", "/picture/nhanVien/refresh.png");
        btnThem = tool.taoButton("Thêm", "/picture/nhanVien/plus.png");

        bottom.add(btnQuayLai);
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
