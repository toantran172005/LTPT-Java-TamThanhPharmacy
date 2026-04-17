package gui;

import utils.ToolCtrl;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
//import controller.ThuocCtrl;
//import controller.ToolCtrl;

public class TimKiemThuoc_GUI extends JPanel {

    public JComboBox<String> cmbLoaiThuoc;
    public JTable tblThuoc;
    public DefaultTableModel model;
    public JButton btnXemChiTiet, btnLamMoi, btnLichSuXoa, btnXoa;
    
    // Đã thay đổi từ JComboBox sang JTextField
    public JTextField txtTenThuoc;

    public ToolCtrl tool = new ToolCtrl();
//    public ThuocCtrl thCtrl;

    public TimKiemThuoc_GUI() {
//        this.thCtrl = new ThuocCtrl(this);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ====== TOP PANEL ======
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 10, 30));

        JPanel vboxTop = new JPanel();
        vboxTop.setLayout(new BoxLayout(vboxTop, BoxLayout.Y_AXIS));
        vboxTop.setBackground(Color.WHITE);

        // ===== Label tiêu đề =====
        JLabel lblTitle = new JLabel("TÌM KIẾM THUỐC", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        vboxTop.add(lblTitle);

        // ===== Hàng 1: Bộ lọc =====
        JPanel filterRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
        filterRow.setBackground(Color.WHITE);

        JPanel nameBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        nameBox.setBackground(Color.WHITE);
        JLabel lblTenThuoc = tool.taoLabel("Tên thuốc:");
        
        // Khởi tạo JTextField thay vì JComboBox
        txtTenThuoc = new JTextField();
        txtTenThuoc.setPreferredSize(new Dimension(190, 34));
        
        lblTenThuoc.setPreferredSize(new Dimension(100, 25));
        nameBox.add(lblTenThuoc);
        nameBox.add(txtTenThuoc);

        JPanel loaiBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        loaiBox.setBackground(Color.WHITE);
        JLabel lblLoaiThuoc = tool.taoLabel("Loại thuốc:");
        cmbLoaiThuoc = new JComboBox<>();
        cmbLoaiThuoc.setPreferredSize(new Dimension(190, 34));
        lblLoaiThuoc.setPreferredSize(new Dimension(100, 25));
        loaiBox.add(lblLoaiThuoc);
        loaiBox.add(cmbLoaiThuoc);

        filterRow.add(nameBox);
        filterRow.add(loaiBox);
        vboxTop.add(filterRow);

        // ===== Hàng 2: Nút chức năng =====
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnRow.setBackground(Color.WHITE);

        btnXemChiTiet = tool.taoButton("Xem chi tiết", "/picture/keThuoc/find.png");
        btnLamMoi = tool.taoButton("Làm mới", "/picture/keThuoc/refresh.png");
        btnLichSuXoa = tool.taoButton("Lịch sử xoá", "/picture/nhanVien/document.png");
        btnXoa = tool.taoButton("Xoá", "/picture/keThuoc/trash.png");

        btnRow.add(btnXemChiTiet);
        btnRow.add(btnLamMoi);
        btnRow.add(btnLichSuXoa);
        btnRow.add(btnXoa);
        vboxTop.add(btnRow);

        topPanel.add(vboxTop, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // ====== CENTER: Bảng thuốc ======
        String[] cols = { "Mã thuốc", "Tên thuốc", "Phân loại", "Giá bán", "Số lượng", "Nơi sản xuất", "Đơn vị tính", "Hạn sử dụng" };
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tblThuoc = new JTable(model);
        tblThuoc.setRowHeight(38);
        tblThuoc.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        tblThuoc.setBackground(Color.WHITE);
        tblThuoc.getTableHeader().setBackground(new Color(240, 240, 240));
        tblThuoc.setGridColor(new Color(200, 200, 200));
        tblThuoc.setShowGrid(true);
        tblThuoc.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        tblThuoc.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblThuoc.setForeground(Color.BLACK);

        tblThuoc.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);

                if (!isSelected) {
                    setBackground(Color.WHITE);
                    setForeground(Color.BLACK);
                }

                int modelRow = table.convertRowIndexToModel(row);
                Object hanSDObj = table.getModel().getValueAt(modelRow, 7);

                if (hanSDObj instanceof LocalDate hanSD) {
                    if (column == 7) {
                        setText(hanSD.format(fmt));
                    }
                    long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), hanSD);
                    if (daysLeft >= 0 && daysLeft <= 30 && !isSelected) {
                        setBackground(new Color(255, 180, 180));
                    }
                }
                return this;
            }
        });

        JTableHeader header = tblThuoc.getTableHeader();
        header.setBackground(new Color(240, 240, 240));
        header.setFont(new Font("Times New Roman", Font.BOLD, 18));
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        JScrollPane scroll = new JScrollPane(tblThuoc);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0xCCCCCC)));
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBackground(Color.WHITE);

        add(scroll, BorderLayout.CENTER);

//        ganSuKien();
    }

    // ================== XỬ LÝ SỰ KIỆN ==================
    public void onBtnLamMoi() {
        txtTenThuoc.setText("");
        cmbLoaiThuoc.setSelectedItem("Tất cả"); 
        model.setRowCount(0);
//        thCtrl.locTatCa(true);
    }

    // gắn sự kiện
//    public void ganSuKien() {
//        thCtrl.locTatCa(true);
//        btnXemChiTiet.addActionListener(e -> thCtrl.xemChiTiet());
//        btnLamMoi.addActionListener(e -> onBtnLamMoi());
//        btnLichSuXoa.addActionListener(e -> thCtrl.xuLyBtnLichSuXoa());
//        btnXoa.addActionListener(e -> thCtrl.xoaThuoc());
//        thCtrl.setCmbKeThuoc();
//        cmbLoaiThuoc.addActionListener(e -> thCtrl.locTatCa(thCtrl.isHienThi));
//        txtTenThuoc.addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyReleased(KeyEvent e) {
//                thCtrl.locTatCa(thCtrl.isHienThi);
//            }
//        });
//    }
}