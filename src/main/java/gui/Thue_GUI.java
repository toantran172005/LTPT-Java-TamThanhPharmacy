package gui;

import controller.ThueController;
import utils.ToolCtrl;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

//import controller.ThueCtrl;
//import controller.ToolCtrl;

public class Thue_GUI extends JPanel {

    public JTable tblThue;
    public DefaultTableModel model;
    public JButton btnThem, btnSua, btnXoa, btnLamMoi;
    public JTextField txtMaThue, txtLoaiThue, txtTyLeThue, txtMoTa;
    public JTextField txtTimKiem;
    
    public ToolCtrl tool = new ToolCtrl();
    public ThueController thueCtrl;

    public Thue_GUI() {
        this.thueCtrl = new ThueController(this);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ================== 1. TOP PANEL (TIÊU ĐỀ + FORM + NÚT) ==================
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        //Tiêu đề
        JLabel lblTitle = new JLabel("QUẢN LÝ DANH MỤC THUẾ", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        topPanel.add(lblTitle);

        //Form nhập liệu 
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Thông tin loại thuế",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 14),
                Color.BLACK
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        //Hàng 1
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(tool.taoLabel("Mã thuế:"), gbc);
        
        gbc.gridx = 1;
        txtMaThue = tool.taoTextField("Tự động tạo...");
        txtMaThue.setEditable(false);
        txtMaThue.setPreferredSize(new Dimension(200, 34));
        inputPanel.add(txtMaThue, gbc);

        gbc.gridx = 2;
        inputPanel.add(tool.taoLabel("Tên loại thuế:"), gbc);
        
        gbc.gridx = 3;
        txtLoaiThue = tool.taoTextField("VD: VAT 8%");
        txtLoaiThue.setPreferredSize(new Dimension(200, 34));
        inputPanel.add(txtLoaiThue, gbc);

        //Hàng 2
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(tool.taoLabel("Tỷ lệ thuế (%):"), gbc);
        
        gbc.gridx = 1;
        txtTyLeThue = tool.taoTextField("VD: 8");
        txtTyLeThue.setPreferredSize(new Dimension(200, 34));
        inputPanel.add(txtTyLeThue, gbc);

        gbc.gridx = 2;
        inputPanel.add(tool.taoLabel("Mô tả / Ghi chú:"), gbc);
        
        gbc.gridx = 3;
        txtMoTa = tool.taoTextField("");
        txtMoTa.setPreferredSize(new Dimension(200, 34));
        inputPanel.add(txtMoTa, gbc);

        topPanel.add(inputPanel);
        topPanel.add(Box.createVerticalStrut(15));

        //Khu vực Tìm kiếm & Nút chức năng
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        actionPanel.setBackground(Color.WHITE);

        // Ô tìm kiếm
        JPanel searchBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        searchBox.setBackground(Color.WHITE);
        searchBox.add(tool.taoLabel("Tìm kiếm:"));
        txtTimKiem = tool.taoTextField("Nhập tên thuế...");
        txtTimKiem.setPreferredSize(new Dimension(200, 34));
        searchBox.add(txtTimKiem);

        // Các nút
        btnThem = tool.taoButton("Thêm", "/picture/khachHang/plus.png"); 
        btnSua = tool.taoButton("Sửa", "/picture/khachHang/edit.png");
        btnXoa = tool.taoButton("Xóa", "/picture/khachHang/trash.png");
        btnLamMoi = tool.taoButton("Làm mới", "/picture/khachHang/refresh.png");

        actionPanel.add(searchBox);
        actionPanel.add(Box.createHorizontalStrut(30));
        actionPanel.add(btnThem);
        actionPanel.add(btnSua);
        actionPanel.add(btnXoa);
        actionPanel.add(btnLamMoi);

        topPanel.add(actionPanel);
        topPanel.add(Box.createVerticalStrut(10)); 

        add(topPanel, BorderLayout.NORTH);

        // ================== 2. CENTER PANEL (BẢNG) ==================
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        // Cấu hình bảng
        String[] cols = {"Mã thuế", "Tên loại thuế", "Tỷ lệ (%)", "Mô tả"};
        model = new DefaultTableModel(cols, 0);

        tblThue = new JTable(model);
        tblThue.setRowHeight(38);
        tblThue.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        
        tblThue.setBackground(Color.WHITE);
        tblThue.getTableHeader().setBackground(new Color(240, 240, 240));
        tblThue.setGridColor(new Color(200, 200, 200));
        tblThue.setShowGrid(true);
        tblThue.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        tblThue.setForeground(Color.BLACK);
        
        // Header Font & Style
        JTableHeader header = tblThue.getTableHeader();
        header.setBackground(new Color(240, 240, 240));
        header.setFont(new Font("Times New Roman", Font.BOLD, 18));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        // Căn giữa nội dung các cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for(int i=0; i<tblThue.getColumnCount(); i++) {
            tblThue.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scroll = new JScrollPane(tblThue);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0xCCCCCC)));
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBackground(Color.WHITE);

        centerPanel.add(scroll, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // ===== Event Handlers =====
        ganSuKien();
    }

    public void ganSuKien() {
        // Load dữ liệu khi mở giao diện
        thueCtrl.loadData();

        // Sự kiện click bảng
        tblThue.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tblThue.getSelectedRow();
                if (row >= 0) {
                    txtMaThue.setText(model.getValueAt(row, 0).toString());
                    txtLoaiThue.setText(model.getValueAt(row, 1).toString());
                    String tyLeStr = model.getValueAt(row, 2).toString().replace("%", "");
                    txtTyLeThue.setText(tyLeStr);
                    Object moTa = model.getValueAt(row, 3);
                    txtMoTa.setText(moTa != null ? moTa.toString() : "");
                }
            }
        });

        // Sự kiện các nút chức năng
        btnThem.addActionListener(e -> thueCtrl.themThue());
        btnSua.addActionListener(e -> thueCtrl.suaThue());
        btnXoa.addActionListener(e -> thueCtrl.xoaThue());
        btnLamMoi.addActionListener(e -> thueCtrl.lamMoi());

        // Sự kiện tìm kiếm khi gõ phím
        txtTimKiem.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                thueCtrl.timKiem();
            }
        });
    }
}