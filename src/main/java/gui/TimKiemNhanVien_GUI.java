package gui;

import utils.ToolCtrl;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

//import controller.ToolCtrl;

import java.awt.*;

public class TimKiemNhanVien_GUI extends JPanel {
    private JTable tblNhanVien;
    private JTextField txtSdt, txtTenNV;
    private JButton btnXemChiTiet, btnLamMoi, btnLichSuXoa, btnXoa;
    Font font1 = new Font("Arial", Font.BOLD, 18);
	Font font2 = new Font("Arial", Font.PLAIN, 15);
	public ToolCtrl tool = new ToolCtrl();

    public TimKiemNhanVien_GUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // ========== TOP PANEL ==========
        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.setBackground(Color.WHITE);

        // --- Panel nhập thông tin ---
        JPanel pnlInput = new JPanel();
        pnlInput.setLayout(new BoxLayout(pnlInput, BoxLayout.Y_AXIS));
        pnlInput.setBackground(Color.WHITE);

        // Dòng nhập số điện thoại
        JPanel row1 = taoHang();
        row1.add(taoLabel("Số điện thoại:"));
        txtSdt = taoTextField("Số điện thoại...");
        row1.add(txtSdt);
        pnlInput.add(row1);

        // Dòng nhập tên nhân viên
        JPanel row2 = taoHang();
        row2.add(taoLabel("Tên nhân viên:"));
        txtTenNV = taoTextField("Tên nhân viên...");
        row2.add(txtTenNV);
        pnlInput.add(row2);

        pnlTop.add(pnlInput, BorderLayout.CENTER);

        // --- Panel nút chức năng ---
        JPanel pnlButtons = new JPanel(new GridLayout(2, 2, 10, 10));
        pnlButtons.setBackground(Color.WHITE);
        pnlButtons.setBorder(new EmptyBorder(0, 20, 0, 0));

        btnXemChiTiet = tool.taoButton("Xem chi tiết", "/picture/hoaDon/find.png");
        btnLamMoi = tool.taoButton("Làm mới", "/picture/hoaDon/refresh.png");
        btnLichSuXoa = tool.taoButton("Lịch sử xoá", "/picture/hoaDon/document.png");
        btnXoa = tool.taoButton("Xoá tất cả", "/picture/hoaDon/trash.png");

        pnlButtons.add(btnXemChiTiet);
        pnlButtons.add(btnLamMoi);
        pnlButtons.add(btnLichSuXoa);
        pnlButtons.add(btnXoa);

        pnlTop.add(pnlButtons, BorderLayout.EAST);

        add(pnlTop, BorderLayout.NORTH);

        // ========== CENTER TABLE ==========
        String[] columnNames = {"", "Mã nhân viên", "Tên nhân viên", "Số điện thoại", "Giới tính", "Chức vụ", "Hoạt động"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        tblNhanVien = new JTable(model);

        // Thêm checkbox vào cột đầu tiên
        tblNhanVien.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        tblNhanVien.getColumnModel().getColumn(0).setMaxWidth(30);

        JScrollPane scrollPane = new JScrollPane(tblNhanVien);
        add(scrollPane, BorderLayout.CENTER);
    }

    // ========== CÁC HÀM HỖ TRỢ ==========
    private JPanel taoHang() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        p.setBackground(Color.WHITE);
        return p;
    }

    private JLabel taoLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font2);
        return lbl;
    }

    private JTextField taoTextField(String placeholder) {
        JTextField txt = new JTextField(15);
        txt.setFont(font2);
        txt.setForeground(Color.GRAY);
        txt.setText(placeholder);

        // Placeholder effect
        txt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (txt.getText().equals(placeholder)) {
                    txt.setText("");
                    txt.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (txt.getText().isEmpty()) {
                    txt.setText(placeholder);
                    txt.setForeground(Color.GRAY);
                }
            }
        });

        return txt;
    }

//    // ========== MAIN TEST ==========
//    public static void main(String[] args) {
//        JFrame frame = new JFrame("Tìm kiếm nhân viên");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(850, 600);
//        frame.add(new TimKiemNhanVien_GUI());
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
//    }
}
