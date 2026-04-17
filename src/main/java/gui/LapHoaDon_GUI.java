package gui;

import utils.ToolCtrl;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
//
//import controller.LapHoaDonCtrl;
//import controller.TimKiemHDCtrl;
//import controller.ToolCtrl;

import java.awt.*;

public class LapHoaDon_GUI extends JPanel {
	public JTextField txtSdt, txtTenKH, txtTuoi, txtSoLuong, txtTienNhan;
	public JComboBox<String> cmbSanPham, cmbDonVi, cmbHTThanhToan, cmbQuocGia;
	public JLabel lblTongTien, lblTienThua;
	public JTable tblThuoc;
	public JButton btnThem, btnLamMoi, btnTaoHD, btnXoa;
	public TrangChuQL_GUI mainFrame;
	public TrangChuNV_GUI mainFrameNV;
//	public LapHoaDonCtrl controller;

	Font font1 = new Font("Times New Roman", Font.BOLD, 18);
	Font font2 = new Font("Times New Roman", Font.PLAIN, 15);
	public ToolCtrl tool = new ToolCtrl();
	public DefaultTableModel model;

	public LapHoaDon_GUI(TrangChuQL_GUI mainFrame) {
		this.mainFrame = mainFrame;
		initUI();
//		khoiTaoController();
	}

	public LapHoaDon_GUI(TrangChuNV_GUI mainFrameNV) {
		this.mainFrameNV = mainFrameNV;
		initUI();
//		khoiTaoController();
	}

//	public void khoiTaoController() {
//		this.controller = new LapHoaDonCtrl(this);
//	}

	public void initUI() {
		setLayout(new BorderLayout(0, 15));
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(1027, 900));
		setBorder(new EmptyBorder(10, 20, 10, 20));

		// ========== PHẦN TRÊN ==========
		JPanel pnlTop = new JPanel();
		pnlTop.setLayout(new BoxLayout(pnlTop, BoxLayout.Y_AXIS));
		pnlTop.setBackground(Color.WHITE);
		pnlTop.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel lblTitle = new JLabel("LẬP HÓA ĐƠN", SwingConstants.CENTER);
		lblTitle.setFont(font1);
		lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlTop.add(lblTitle);
		pnlTop.add(Box.createVerticalStrut(10));
		pnlTop.add(new JSeparator());

		// ===== SDT & Tên KH =====
		JPanel row1 = taoHang();
		row1.add(tool.taoLabel("Số điện thoại:"));
		txtSdt = tool.taoTextField("Số điện thoại...");
		row1.add(txtSdt);
		row1.add(tool.taoLabel("Tên khách hàng:"));
		txtTenKH = tool.taoTextField("Tên khách hàng...");
		row1.add(txtTenKH);
		pnlTop.add(row1);

		// ===== Tuổi & Sản phẩm & Quốc gia=====
		JPanel row2 = taoHang();
		row2.add(tool.taoLabel("Tuổi:"));
		txtTuoi = tool.taoTextField("Tuổi...");
		row2.add(txtTuoi);
		row2.add(tool.taoLabel("Sản phẩm:"));
		cmbSanPham = tool.taoComboBox(new String[] {});
		cmbSanPham.setEditable(true);
		cmbSanPham.setPreferredSize(new Dimension(180, 28));
		row2.add(cmbSanPham);
		setFieldHeight(cmbSanPham);
		row2.add(tool.taoLabel("Quốc gia sản xuất:"));
		cmbQuocGia = tool.taoComboBox(new String[] {});
		cmbQuocGia.setEditable(true);
		cmbQuocGia.setPreferredSize(new Dimension(180, 28));
		row2.add(cmbQuocGia);
		setFieldHeight(cmbQuocGia);
		pnlTop.add(row2);

		// ===== Số lượng, Đơn vị, nút Thêm, Xoá =====
		JPanel row3 = taoHang();
		row3.add(tool.taoLabel("Số lượng:"));
		txtSoLuong = tool.taoTextField("Nhập số lượng...");
		row3.add(txtSoLuong);

		row3.add(tool.taoLabel("Đơn vị tính:"));
		cmbDonVi = tool.taoComboBox(new String[] {});
		cmbDonVi.setEditable(false);
		cmbDonVi.setEnabled(false);
		cmbDonVi.setPreferredSize(new Dimension(150, 28));
		cmbDonVi.setBackground(Color.white);
		row3.add(cmbDonVi);
		setFieldHeight(cmbDonVi);

		btnThem = tool.taoButton("Thêm", "/picture/hoaDon/plus.png");
		btnXoa = tool.taoButton("Xoá", "/picture/hoaDon/trash.png");
		row3.add(btnThem);
		row3.add(btnXoa);
		pnlTop.add(row3);

		add(pnlTop, BorderLayout.NORTH);
		
		// ========== BẢNG DỮ LIỆU THUỐC ==========
        String[] cols = { "STT", "Tên thuốc", "Nơi sản xuất", "Số lượng", "Đơn vị", "Đơn giá gốc",
                "Đơn giá (sau khuyến mãi)", "Thành tiền", "Ghi chú" };
        
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
        tblThuoc.setForeground(new Color(0x33, 0x33, 0x33));
        tblThuoc.getColumnModel().getColumn(6).setPreferredWidth(200);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tblThuoc.getColumnCount(); i++) {
            tblThuoc.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JTableHeader header = tblThuoc.getTableHeader();
        header.setBackground(new Color(240, 240, 240));
        header.setFont(new Font("Times New Roman", Font.BOLD, 18));
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        JScrollPane scrollTable = new JScrollPane(tblThuoc);
        scrollTable.setBorder(BorderFactory.createLineBorder(new Color(0xCCCCCC)));
        scrollTable.getViewport().setBackground(Color.WHITE);
        scrollTable.setBackground(Color.WHITE);

        add(scrollTable, BorderLayout.CENTER);

		// ========== PHẦN DƯỚI ==========
		JPanel pnlBottom = new JPanel();
		pnlBottom.setLayout(new BoxLayout(pnlBottom, BoxLayout.Y_AXIS));
		pnlBottom.setBackground(Color.WHITE);
		pnlBottom.setBorder(new EmptyBorder(10, 0, 10, 0));

		// ===== Thanh toán =====
		JPanel row4 = taoHangTrai();
		row4.add(tool.taoLabel("Phương thức thanh toán:"));
		cmbHTThanhToan = tool.taoComboBox(new String[] { "Tiền mặt", "Chuyển khoản" });
		cmbHTThanhToan.setPreferredSize(new Dimension(150, 28));
		row4.add(cmbHTThanhToan);
		setFieldHeight(cmbHTThanhToan);
		cmbHTThanhToan.setEditable(false);
		pnlBottom.add(row4);

		// ===== Tổng tiền =====
		JPanel row5 = taoHangTrai();
		row5.add(tool.taoLabel("Tổng tiền:"));
		lblTongTien = tool.taoLabel("0 VND");
		row5.add(lblTongTien);
		pnlBottom.add(row5);

		// ===== Tiền nhận =====
		JPanel row6 = taoHangTrai();
		row6.add(tool.taoLabel("Tiền nhận:"));
		txtTienNhan = tool.taoTextField("Tiền nhận...");
		row6.add(txtTienNhan);
		pnlBottom.add(row6);

		// ===== Tiền thừa =====
		JPanel row7 = taoHangTrai();
		row7.add(tool.taoLabel("Tiền thừa:"));
		lblTienThua = tool.taoLabel("0 VND");
		row7.add(lblTienThua);
		pnlBottom.add(row7);

		// ===== Các nút =====
		JPanel row8 = taoHang();
		row8.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnLamMoi = tool.taoButton("Làm mới", "/picture/hoaDon/refresh.png");
		btnTaoHD = tool.taoButton("Tạo hoá đơn", "/picture/hoaDon/plus.png");
		row8.add(btnLamMoi);
		row8.add(Box.createHorizontalStrut(50));
		row8.add(btnTaoHD);
		pnlBottom.add(row8);

		add(pnlBottom, BorderLayout.SOUTH);
	}

	// ========== HÀM TẠO CÁC THÀNH PHẦN CHUNG ==========
	public JPanel taoHang() {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
		p.setBackground(Color.WHITE);
		return p;
	}

	public JPanel taoHangTrai() {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
		p.setBackground(Color.WHITE);
		return p;
	}

	public void setFieldHeight(JComponent comp) {
		Dimension d = comp.getPreferredSize();
		d.height = 36;
		comp.setPreferredSize(d);
	}

	// ========== GETTER ==========
	public JTextField getTxtSdt() {
		return txtSdt;
	}

	public JTextField getTxtTenKH() {
		return txtTenKH;
	}

	public JTextField getTxtTuoi() {
		return txtTuoi;
	}

	public JTextField getTxtSoLuong() {
		return txtSoLuong;
	}

	public JTextField getTxtTienNhan() {
		return txtTienNhan;
	}

	public JComboBox<String> getCmbSanPham() {
		return cmbSanPham;
	}

	public JComboBox<String> getCmbDonVi() {
		return cmbDonVi;
	}

	public JComboBox<String> getCmbHTThanhToan() {
		return cmbHTThanhToan;
	}

	public JComboBox<String> getCmbQuocGia() {
		return cmbQuocGia;
	}

	public JLabel getLblTongTien() {
		return lblTongTien;
	}

	public JLabel getLblTienThua() {
		return lblTienThua;
	}

	public JTable getTblThuoc() {
		return tblThuoc;
	}

	public JButton getBtnThem() {
		return btnThem;
	}

	public JButton getBtnLamMoi() {
		return btnLamMoi;
	}

	public JButton getBtnTaoHD() {
		return btnTaoHD;
	}

	public JButton getBtnXoa() {
		return btnXoa;
	}

	public TrangChuQL_GUI getMainFrame() {
		return mainFrame;
	}

	public TrangChuNV_GUI getMainFrameNV() {
		return mainFrameNV;
	}
}
