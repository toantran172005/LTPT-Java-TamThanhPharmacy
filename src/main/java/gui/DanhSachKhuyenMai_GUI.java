package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import com.toedter.calendar.JDateChooser;
import controller.KhuyenMaiController;
import utils.ToolCtrl;
//
//import controller.KhuyenMaiCtrl;
//import controller.ToolCtrl;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class DanhSachKhuyenMai_GUI extends JPanel {

	public JTextField txtTenKM;
    public JComboBox<String> cmbTrangThai;
    public JDateChooser dpNgay; 
    public JTable tblKhuyenMai;
    public DefaultTableModel model;
    public JButton btnXemChiTiet, btnLamMoi, btnLichSuXoa, btnXoaTatCa;

    private final ToolCtrl tool = new ToolCtrl();
    private KhuyenMaiController kmCtrl;

    public DanhSachKhuyenMai_GUI() {
    	kmCtrl = new KhuyenMaiController(this);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // ======= NORTH: TIÊU ĐỀ + BỘ LỌC =======
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("DANH SÁCH KHUYẾN MÃI", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        topPanel.add(lblTitle);

        // ======= HÀNG 1: Tên khuyến mãi + Nút =======
        JPanel row1 = new JPanel(new BorderLayout());
        row1.setBackground(Color.WHITE);

        // Left: nhập tên khuyến mãi
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        left.setBackground(Color.WHITE);
        JLabel lblTenKM = tool.taoLabel("Tên khuyến mãi:");
        txtTenKM = tool.taoTextField("Nhập tên khuyến mãi...");
        txtTenKM.setPreferredSize(new Dimension(220, 35));
        left.add(lblTenKM);
        left.add(txtTenKM);

        row1.add(left, BorderLayout.WEST);
        row1.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // ======= HÀNG 2: Trạng thái + Ngày + Nút =======
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        row2.setBackground(Color.WHITE);
        
        btnXemChiTiet = tool.taoButton("Xem chi tiết", "/picture/khuyenMai/find.png");
        btnLamMoi = tool.taoButton("Làm mới", "/picture/keThuoc/refresh.png");
        
        JLabel lblTrangThai = tool.taoLabel("Trạng thái:");
        cmbTrangThai = new JComboBox<>(new String[]{"Tất cả", "Đang hoạt động", "Đã kết thúc"});
        cmbTrangThai.setPreferredSize(new Dimension(200, 35));

        JLabel lblNgay = tool.taoLabel("Ngày:");
        dpNgay = tool.taoDateChooser();

        //btnLichSuXoa = tool.taoButton("Lịch sử xoá", "/picture/nhanVien/document.png");

        row2.add(lblTrangThai);
        row2.add(cmbTrangThai);
        row2.add(Box.createHorizontalStrut(30));
        row2.add(lblNgay);
        row2.add(dpNgay);
        row2.add(btnXemChiTiet);
        row2.add(Box.createHorizontalStrut(30));
        row2.add(btnLamMoi);
        row2.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        topPanel.add(row1);
        topPanel.add(row2);

        add(topPanel, BorderLayout.NORTH);

        // ======= CENTER: BẢNG DANH SÁCH =======
        String[] cols = {
                "Mã khuyến mãi", "Tên khuyến mãi",
                "Hình thức", "Mức khuyến mãi",
                "Ngày bắt đầu", "Ngày kết thúc",
                "Trạng thái"
        };

        model = new DefaultTableModel(cols, 0);

        tblKhuyenMai = new JTable(model);
        tblKhuyenMai.setRowHeight(35);
        tblKhuyenMai.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        tblKhuyenMai.setSelectionBackground(new Color(0xE3F2FD));
		tblKhuyenMai.setSelectionForeground(Color.BLACK);
        tblKhuyenMai.setGridColor(new Color(0xDDDDDD));
        tblKhuyenMai.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblKhuyenMai.setBackground(Color.WHITE);
		tblKhuyenMai.setForeground(Color.BLACK);

        JTableHeader header = tblKhuyenMai.getTableHeader();
        header.setFont(new Font("Times New Roman", Font.BOLD, 14));
        header.setBackground(Color.WHITE);
        header.setForeground(Color.BLACK);
        header.setForeground(new Color(0x33, 0x33, 0x33));
        header.setBorder(BorderFactory.createLineBorder(new Color(0xCCCCCC)));
        
        //Căn giữa cho dữ liệu trong cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        TableColumnModel columnModel = tblKhuyenMai.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setCellRenderer(centerRenderer);
        }
        
        //Căn giữa cho tiêu đề table
        ((DefaultTableCellRenderer) tblKhuyenMai.getTableHeader().getDefaultRenderer())
        .setHorizontalAlignment(SwingConstants.CENTER);

        JScrollPane scroll = new JScrollPane(tblKhuyenMai);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0xCCCCCC)));

        add(scroll, BorderLayout.CENTER);

        // ======= SỰ KIỆN =======
        ganSuKien();
    }

    // ====== XỬ LÝ SỰ KIỆN ======
    
    public void ganSuKien() {
    	kmCtrl.setDataChoTable();
        btnLamMoi.addActionListener(e -> lamMoi());
        btnXemChiTiet.addActionListener(e -> {
            JPanel parent = (JPanel) this.getParent();
            kmCtrl.xemChiTietKM(parent);
        });
        cmbTrangThai.addActionListener(e -> kmCtrl.locKMTheoTrangThai());
        txtTenKM.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                kmCtrl.timKiemNhanhTheoTen();
            }
        });
        kmCtrl.locKMTheoNgay();
        dpNgay.addPropertyChangeListener("date", e -> {
        	if (dpNgay.getDate() != null) {
                kmCtrl.locKMTheoNgay();
            }
        });
    }


    public void lamMoi() {
        txtTenKM.setText("");
        dpNgay.setDate(null);
        cmbTrangThai.setSelectedItem("Tất cả");
        kmCtrl.setDataChoTable();
    }

}
