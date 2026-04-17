package gui;

import utils.ToolCtrl;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

//import controller.ToolCtrl;

public class DangNhap_GUI extends JFrame {

	public JTextField txtTenDangNhap;
	public JPasswordField txpPassClose;
	public JTextField txtPassOpen;
	public JLabel imgEye;
	public JButton btnDangNhap;
	public boolean hienMatKhau = false;
    public ToolCtrl tool = new ToolCtrl();

    Font font1 = new Font("Times New Roman", Font.BOLD, 22);
    Font font2 = new Font("Times New Roman", Font.PLAIN, 16);

    public DangNhap_GUI() {
        setTitle("Đăng nhập hệ thống");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 550);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout(0, 0));

        // ======= PANEL TRÊN =======
        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.setBackground(Color.WHITE);
        pnlTop.setBorder(new EmptyBorder(20, 0, 10, 0));

        JLabel lblLogo = new JLabel(scaleIcon("/picture/trangChu/logo.jpg", 180, 130));
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        pnlTop.add(lblLogo, BorderLayout.CENTER);

        JLabel lblTieuDe = new JLabel("HIỆU THUỐC TAM THANH", SwingConstants.CENTER);
        lblTieuDe.setFont(font1);
        lblTieuDe.setForeground(new Color(0x1A1A1A));
        lblTieuDe.setBorder(new EmptyBorder(10, 0, 0, 0));
        pnlTop.add(lblTieuDe, BorderLayout.SOUTH);

        add(pnlTop, BorderLayout.NORTH);

        // ======= PANEL GIỮA =======
        JPanel pnlCenter = new JPanel(null);
        pnlCenter.setBackground(Color.WHITE);

        int startX = 80;
        int fieldWidth = 290;
        int height = 34;

        JLabel lblTenDangNhap = taoLabel("Tên đăng nhập:");
        lblTenDangNhap.setBounds(startX, 20, 200, 25);
        pnlCenter.add(lblTenDangNhap);

        txtTenDangNhap = tool.taoTextField("");
        txtTenDangNhap.setBounds(startX, 50, fieldWidth, height);
        pnlCenter.add(txtTenDangNhap);

        JLabel iconUser = new JLabel(scaleIcon("/picture/dangNhap/user.png", 30, 30));
        iconUser.setBounds(375, 50, 34, 34);
        pnlCenter.add(iconUser);

        JLabel lblMatKhau = taoLabel("Mật khẩu:");
        lblMatKhau.setBounds(startX, 100, 200, 25);
        pnlCenter.add(lblMatKhau);

        txpPassClose = new JPasswordField();
        txpPassClose.setFont(font2);
        txpPassClose.setBounds(startX, 130, fieldWidth, height);
        pnlCenter.add(txpPassClose);

        txtPassOpen = tool.taoTextField("");
        txtPassOpen.setBounds(startX, 130, fieldWidth, height);
        txtPassOpen.setVisible(false);
        pnlCenter.add(txtPassOpen);

        imgEye = new JLabel(scaleIcon("/picture/dangNhap/eye.png", 30, 30));
        imgEye.setBounds(375, 130, 34, 34);
        imgEye.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        pnlCenter.add(imgEye);

        // Ẩn/Hiện mật khẩu
        imgEye.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                hienMatKhau = !hienMatKhau;
                if (hienMatKhau) {
                    txtPassOpen.setText(new String(txpPassClose.getPassword()));
                    txtPassOpen.setVisible(true);
                    txpPassClose.setVisible(false);
                    imgEye.setIcon(scaleIcon("/picture/dangNhap/closed-eyes.png", 30, 30));
                } else {
                    txpPassClose.setText(txtPassOpen.getText());
                    txtPassOpen.setVisible(false);
                    txpPassClose.setVisible(true);
                    imgEye.setIcon(scaleIcon("/picture/dangNhap/eye.png", 30, 30));
                }
            }
        });

        add(pnlCenter, BorderLayout.CENTER);

        // ======= PANEL DƯỚI =======
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 25));
        pnlBottom.setBackground(Color.WHITE);

        btnDangNhap = new JButton("Đăng nhập");
        btnDangNhap.setFont(font2);
        btnDangNhap.setForeground(Color.WHITE);
        btnDangNhap.setBackground(new Color(0, 174, 254));
        btnDangNhap.setPreferredSize(new Dimension(290, 42));
        btnDangNhap.setFocusPainted(false);
        btnDangNhap.setBorderPainted(false);
        btnDangNhap.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//        btnDangNhap.addActionListener(e -> chuyenDenTrangChuNhanVien());

        pnlBottom.add(btnDangNhap);
        add(pnlBottom, BorderLayout.SOUTH);
    }

    // ======= HÀM TẠO LABEL =======
    public JLabel taoLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font2);
        lbl.setForeground(new Color(0x333333));
        return lbl;
    }

    // ======= HÀM SCALE ICON =======
    public ImageIcon scaleIcon(String path, int width, int height) {
        URL imgURL = getClass().getResource(path);
        if (imgURL == null) {
            System.err.println("Không tìm thấy ảnh: " + path);
            return null;
        }
        ImageIcon icon = new ImageIcon(imgURL);
        Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }
    
    public JButton getBtnDangNhap() { return btnDangNhap; }

    // ======= CHUYỂN TRANG =======
//    public void chuyenDenTrangChuNhanVien() {
//        new TrangChuQL_GUI().setVisible(true);
//        this.dispose();
//    }

}
