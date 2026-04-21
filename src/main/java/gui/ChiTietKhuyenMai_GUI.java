package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import com.toedter.calendar.JDateChooser;
import controller.KhuyenMaiController;
import utils.ToolCtrl;
import java.awt.*;

public class ChiTietKhuyenMai_GUI extends JPanel {

    public JTextField txtTenKM;
    public JComboBox<String> cmbLoaiKM, cmbThemThuoc;
    public JTable tblChiTietKM;
    public JButton btnThemThuoc, btnCapNhat, btnQuayLai;
    public JDateChooser dpNgayBD, dpNgayKT; 
    public KhuyenMaiController kmCtrl;
    private final ToolCtrl tool = new ToolCtrl();
    public JTextField txtSoLuongMua;
    public JButton btnTru2;
    public JButton btnCong2;
	public JTextField txtSoLuongTang;
	public JButton btnTru1;
	public JButton btnCong1;
	public JTextField txtMucKM;
	public JButton btnTru;
	public JButton btnCong;
	public boolean dangChinhSua = false;
	public JButton btnXoaThuoc;



    public ChiTietKhuyenMai_GUI() {
    	kmCtrl = new KhuyenMaiController(this);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        // ========== TOP PANEL ==========
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.WHITE);

        JLabel lblTitle = tool.taoLabel("CHI TIẾT KHUYẾN MÃI");
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(lblTitle);
        topPanel.add(Box.createVerticalStrut(20));

        // -----Tên KM + Loại KM -----
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        row1.setBackground(Color.WHITE);

        txtTenKM = tool.taoTextField("Tên khuyến mãi...");
        String[] items1 = {"Mua tặng", "Giảm giá"};
        cmbLoaiKM = tool.taoComboBox(items1);

        row1.add(tool.taoLabel("Tên Khuyến Mãi:"));
        row1.add(txtTenKM);
        row1.add(Box.createHorizontalStrut(40));
        row1.add(tool.taoLabel("Loại Khuyến Mãi:"));
        row1.add(cmbLoaiKM);
        topPanel.add(row1);
        topPanel.add(Box.createVerticalStrut(15));

        // ---- Ngày bắt đầu + Ngày kết thúc -----
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        row2.setBackground(Color.WHITE);

        dpNgayBD = tool.taoDateChooser();
        dpNgayKT = tool.taoDateChooser();

        row2.add(tool.taoLabel("Ngày Bắt Đầu:"));
        row2.add(dpNgayBD);
        row2.add(Box.createHorizontalStrut(40));
        row2.add(tool.taoLabel("Ngày Kết Thúc:"));
        row2.add(dpNgayKT);
        topPanel.add(row2);
        topPanel.add(Box.createVerticalStrut(15));

        // Mức khuyến mãi (%) 
        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        row3.setBackground(Color.WHITE);

        txtMucKM = tool.taoTextField("");
        btnTru = tool.taoButton("", "/picture/hoaDon/minus-sign.png");
        btnCong = tool.taoButton("", "/picture/hoaDon/plus.png");
        JPanel pnlMKM = taoDongStepper("Mức khuyến mãi (%):", txtMucKM, btnTru, btnCong);

        row3.add(pnlMKM);
        topPanel.add(row3);
        topPanel.add(Box.createVerticalStrut(15)); 
        //Số lượng mua & Số lượng tặng
        JPanel row4 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        row4.setBackground(Color.WHITE);

        // Số lượng mua
        txtSoLuongMua = tool.taoTextField("");
        btnTru2 = tool.taoButton("", "/picture/hoaDon/minus-sign.png");
        btnCong2 = tool.taoButton("", "/picture/hoaDon/plus.png");
        JPanel pnlSLM = taoDongStepper("Số lượng mua:", txtSoLuongMua, btnTru2, btnCong2);

        // Số lượng tặng
        txtSoLuongTang = tool.taoTextField("");
        btnTru1 = tool.taoButton("", "/picture/hoaDon/minus-sign.png");
        btnCong1 = tool.taoButton("", "/picture/hoaDon/plus.png");
        JPanel pnlSLT = taoDongStepper("Số lượng tặng:", txtSoLuongTang, btnTru1, btnCong1);

        row4.add(pnlSLM);
        row4.add(Box.createHorizontalStrut(20));
        row4.add(pnlSLT);

        topPanel.add(row4);
        topPanel.add(Box.createVerticalStrut(15));


        // Thêm thuốc
        JPanel row5 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        row5.setBackground(Color.WHITE);
        String[] items = {""};
        cmbThemThuoc = tool.taoComboBox(items);
        btnThemThuoc = tool.taoButton("Thêm", "/picture/khachHang/plus.png");
        btnXoaThuoc = tool.taoButton("Xoá thuốc", "/picture/keThuoc/trash.png");

        row5.add(tool.taoLabel("Thêm thuốc:"));
        row5.add(cmbThemThuoc);
        row5.add(btnThemThuoc);
        row5.add(btnXoaThuoc);
        row5.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        topPanel.add(row5);
        add(topPanel, BorderLayout.NORTH);

        // ========== TABLE CENTER ==========
        String[] cols = {"Mã Thuốc", "Tên Thuốc", "Loại Khuyến Mãi", "Mức Khuyến Mãi", "Hoạt Động"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        tblChiTietKM = new JTable(model);
        tblChiTietKM.setRowHeight(35);
        tblChiTietKM.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        tblChiTietKM.setSelectionBackground(new Color(0xE3F2FD));
		tblChiTietKM.setSelectionForeground(Color.BLACK);
        tblChiTietKM.setGridColor(new Color(0xDDDDDD));
        tblChiTietKM.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblChiTietKM.setBackground(Color.WHITE);
		tblChiTietKM.setForeground(Color.BLACK);
        
      //Căn giữa cho dữ liệu trong cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        TableColumnModel columnModel = tblChiTietKM.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setCellRenderer(centerRenderer);
        }
        
        //Căn giữa cho tiêu đề table
        ((DefaultTableCellRenderer) tblChiTietKM.getTableHeader().getDefaultRenderer())
        .setHorizontalAlignment(SwingConstants.CENTER);

        JTableHeader header = tblChiTietKM.getTableHeader();
        header.setFont(new Font("Times New Roman", Font.BOLD, 14));
        header.setBackground(Color.WHITE);
        header.setForeground(Color.BLACK);

        JScrollPane scroll = new JScrollPane(tblChiTietKM);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0xCCCCCC)));
        add(scroll, BorderLayout.CENTER);

        // ========== BOTTOM PANEL ==========
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 15));
        bottomPanel.setBackground(Color.WHITE);

        btnCapNhat = tool.taoButton("Cập nhật", "/picture/khachHang/edit.png");
        btnQuayLai = tool.taoButton("Quay lại", "/picture/khuyenMai/signOut.png");

        bottomPanel.add(btnCapNhat);
        bottomPanel.add(btnQuayLai);
        add(bottomPanel, BorderLayout.SOUTH);

        // ====== SỰ KIỆN ======
        ganSuKien();
    }
    
    //========== XỬ LÝ SỰ KIỆN ==============
    
    public void ganSuKien() {
    	kmCtrl.thietLapTrangThaiSua(false);

    	btnCapNhat.addActionListener(e -> {
            if (btnCapNhat.getText().equalsIgnoreCase("Cập nhật")) {
                kmCtrl.thietLapTrangThaiSua(true);
            } else {
                kmCtrl.luuCapNhat();
            }
        });

    	btnQuayLai.addActionListener(e -> {
    	    JPanel parent = (JPanel) this.getParent();
    	    tool.doiPanel(parent, new DanhSachKhuyenMai_GUI());
    	});
    	cmbLoaiKM.addActionListener(e -> chonPhuongThucKM());
        btnCong.addActionListener(e -> tangGiaTri(txtMucKM));
        btnTru.addActionListener(e -> giamGiaTri(txtMucKM));
        btnCong1.addActionListener(e -> tangGiaTri(txtSoLuongTang));
        btnTru1.addActionListener(e -> giamGiaTri(txtSoLuongTang));
        btnCong2.addActionListener(e -> tangGiaTri(txtSoLuongMua));
        btnTru2.addActionListener(e -> giamGiaTri(txtSoLuongMua));
        cmbLoaiKM.addActionListener(e -> chonPhuongThucKM());
        chonPhuongThucKM();
        //kmCtrl.setDuLieuChoCmbThuoc(cmbThemThuoc);
        //btnThemThuoc.addActionListener(e -> kmCtrl.themThuocVaoBang());
        btnXoaThuoc.addActionListener(e -> kmCtrl.xoaThuocTuBang(tblChiTietKM));
    }
    
    public void tangGiaTri(JTextField txt) {
        try {
            int value = Integer.parseInt(txt.getText().trim());
            txt.setText(String.valueOf(value + 1));
        } catch (Exception e) {
            txt.setText("1");
        }
    }

    public void giamGiaTri(JTextField txt) {
        try {
            int value = Integer.parseInt(txt.getText().trim());
            if (value > 0) txt.setText(String.valueOf(value - 1));
        } catch (Exception e) {
            txt.setText("0");
        }
    }

    public JPanel taoDongStepper(String labelText, JTextField txt, JButton btnMinus, JButton btnPlus) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setBackground(Color.WHITE);

        JLabel lbl = tool.taoLabel(labelText);
        JPanel stepPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        stepPanel.setBackground(Color.WHITE);

        txt.setHorizontalAlignment(JTextField.CENTER);
        txt.setPreferredSize(new Dimension(70, 35));

        stepPanel.add(btnMinus);
        stepPanel.add(txt);
        stepPanel.add(btnPlus);

        row.add(lbl);
        row.add(stepPanel);
        return row;
    }

  //Chọn phương thức khuyến mãi
    public void chonPhuongThucKM() {
        String selected = cmbLoaiKM.getSelectedItem().toString();
        boolean isMuaTang = selected.equalsIgnoreCase("Mua tặng");

        txtMucKM.setEditable(!isMuaTang);
        txtMucKM.setBackground(isMuaTang ? new Color(240, 240, 240) : Color.WHITE);
        btnCong.setEnabled(!isMuaTang);
        btnTru.setEnabled(!isMuaTang);
        if (isMuaTang) txtMucKM.setText("0");

        txtSoLuongMua.setEditable(isMuaTang);
        txtSoLuongMua.setBackground(isMuaTang ? Color.WHITE : new Color(240, 240, 240));
        btnCong2.setEnabled(isMuaTang);
        btnTru2.setEnabled(isMuaTang);

        txtSoLuongTang.setEditable(isMuaTang);
        txtSoLuongTang.setBackground(isMuaTang ? Color.WHITE : new Color(240, 240, 240));
        btnCong1.setEnabled(isMuaTang);
        btnTru1.setEnabled(isMuaTang);

        if (!isMuaTang) {
            txtSoLuongMua.setText("0");
            txtSoLuongTang.setText("0");
        }
    }

    public void setTrangThaiCacNutBam(boolean editable) {
        String selected = cmbLoaiKM.getSelectedItem().toString();
        boolean isMuaTang = selected.equalsIgnoreCase("Mua tặng");

        txtMucKM.setEditable(!isMuaTang && editable);
        btnCong.setEnabled(!isMuaTang && editable);
        btnTru.setEnabled(!isMuaTang && editable);

        txtSoLuongMua.setEditable(isMuaTang && editable);
        btnCong2.setEnabled(isMuaTang && editable);
        btnTru2.setEnabled(isMuaTang && editable);

        txtSoLuongTang.setEditable(isMuaTang && editable);
        btnCong1.setEnabled(isMuaTang && editable);
        btnTru1.setEnabled(isMuaTang && editable);
        
        Color bg = editable ? Color.WHITE : new Color(240, 240, 240);
        txtTenKM.setBackground(bg);
    }
}
