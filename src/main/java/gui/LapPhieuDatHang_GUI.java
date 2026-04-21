package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.toedter.calendar.JDateChooser;
import controller.LapPhieuDatHangController;
import utils.ToolCtrl;

//import controller.LapPhieuDatHangCtrl;
//import controller.ToolCtrl;

import java.util.Date;

public class LapPhieuDatHang_GUI extends JPanel {
	public JTextField txtSdt, txtTenKH, txtTuoi, txtSoLuong;
	public JComboBox<String> cmbSanPham, cmbDonVi, cmbQuocGia;
	public JTable tblThuoc;
	public JTextArea txaGhiChu;
	public JButton btnThem, btnLamMoi, btnTaoPhieuDat, btnXoa;
	public JDateChooser ngayHen;
	Font font1 = new Font("Time New Roman", Font.BOLD, 18);
	Font font2 = new Font("Time New Roman", Font.PLAIN, 15);
	public ToolCtrl tool = new ToolCtrl();
	public LapPhieuDatHangController lpdhCtrl = new LapPhieuDatHangController(this);
	public DefaultTableModel model;
	public TrangChuQL_GUI trangChuQL;
	public TrangChuNV_GUI trangChuNV;
	
	public void setUpDuLieu() {
		lpdhCtrl.setUpComboBox();
		lpdhCtrl.setUpGoiY();
	}
	
	public void setHoatDong() {
		btnLamMoi.addActionListener(e -> lpdhCtrl.lamMoi());
		btnThem.addActionListener(e -> lpdhCtrl.themVaoTable());
		btnXoa.addActionListener(e -> lpdhCtrl.xoaThuoc());
		btnTaoPhieuDat.addActionListener(e -> lpdhCtrl.taoPhieuDat());
	}
	
	public LapPhieuDatHang_GUI(TrangChuQL_GUI trangChuQL) {
	    this.trangChuQL = trangChuQL;
	    initUI();
	    setUpDuLieu();
	    setHoatDong();
	}
	
	public LapPhieuDatHang_GUI(TrangChuNV_GUI trangChuNV) {
	    this.trangChuNV = trangChuNV;
	    initUI();
	    setUpDuLieu();
	    setHoatDong();
	}

	public void initUI() {
		setLayout(new BorderLayout(0, 15));
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(1027, 900));
		setBorder(new EmptyBorder(10, 20, 10, 20));

		// =================== TOP ===================
		JPanel pnlTop = new JPanel();
		pnlTop.setLayout(new BoxLayout(pnlTop, BoxLayout.Y_AXIS));
		pnlTop.setBackground(Color.WHITE);
		pnlTop.setBorder(new EmptyBorder(20, 20, 10, 20));

		JLabel lblTieuDe = new JLabel("LẬP PHIẾU ĐẶT THUỐC", SwingConstants.CENTER);
		lblTieuDe.setFont(font1);
		lblTieuDe.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlTop.add(lblTieuDe);

		pnlTop.add(Box.createVerticalStrut(10));
		pnlTop.add(new JSeparator());
		pnlTop.add(Box.createVerticalStrut(15));

		// --- Hàng 1: SĐT, Tên KH, Tuổi ---
		JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 5));
		row1.setBackground(Color.WHITE);

		row1.add(tool.taoLabel("Số điện thoại:"));
		txtSdt = tool.taoTextField("Số điện thoại...");
		row1.add(txtSdt);

		row1.add(tool.taoLabel("Tên khách hàng:"));
		txtTenKH = tool.taoTextField("Tên khách hàng...");
		row1.add(txtTenKH);

		row1.add(tool.taoLabel("Tuổi:"));
		txtTuoi = tool.taoTextField("Tuổi...");
		row1.add(txtTuoi);
		pnlTop.add(row1);

		// --- Hàng 2: Sản phẩm, Số lượng ---
		JPanel row2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 5));
		row2.setBackground(Color.WHITE);

		row2.add(tool.taoLabel("Sản phẩm:"));
		cmbSanPham = tool.taoComboBox(new String[] {});
		cmbSanPham.setEditable(true);
		cmbSanPham.setPreferredSize(new Dimension(200, 30));
		row2.add(cmbSanPham);

		row2.add(tool.taoLabel("Số lượng:"));
		txtSoLuong = tool.taoTextField("Số lượng...");
		row2.add(txtSoLuong);
		
		row2.add(tool.taoLabel("Quốc gia sản xuất:"));
		cmbQuocGia = tool.taoComboBox(new String[] {});
		cmbQuocGia.setEditable(true);
		cmbQuocGia.setPreferredSize(new Dimension(180, 28));
		row2.add(cmbQuocGia);
		setFieldHeight(cmbQuocGia);
		pnlTop.add(row2);

		// --- Hàng 3: Ngày hẹn, Đơn vị, Thêm ---
		JPanel row3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 5));
		row3.setBackground(Color.WHITE);

		// --- JDateChooser ---
		row3.add(tool.taoLabel("Ngày hẹn:"));
		ngayHen = tool.taoDateChooser();
		ngayHen.setDate(new Date()); // ngày hiện tại mặc định
		row3.add(ngayHen);

		row3.add(tool.taoLabel("Đơn vị:"));
		cmbDonVi = tool.taoComboBox(new String[] {});
		cmbDonVi.setEditable(false);
		cmbDonVi.setEnabled(false);
		row3.add(cmbDonVi);

		btnThem = tool.taoButton("Thêm", "/picture/hoaDon/plus.png");
		btnXoa = tool.taoButton("Xóa", "/picture/khachHang/trash.png");
		row3.add(btnThem);
		row3.add(btnXoa);
		pnlTop.add(row3);

		add(pnlTop, BorderLayout.NORTH);

		// =================== CENTER ===================
		String[] cols = {"Mã thuốc", "STT", "Tên thuốc", "Nơi sản xuất", "Số lượng", "Đơn vị", "Đơn giá", "Thành tiền", "Ghi chú"};
		model = new DefaultTableModel(cols, 0);
		tblThuoc = new JTable(model);
		tblThuoc.setRowHeight(25);
		tblThuoc.setFont(font2);
		tblThuoc.getTableHeader().setFont(font2);
		
		// Ẩn cột mã thuốc
		tblThuoc.getColumnModel().getColumn(0).setMinWidth(0);
		tblThuoc.getColumnModel().getColumn(0).setMaxWidth(0);
		tblThuoc.getColumnModel().getColumn(0).setPreferredWidth(0);
		tblThuoc.getColumnModel().getColumn(8).setPreferredWidth(120);

		// Đặt nền trắng cho bảng
		tblThuoc.setBackground(Color.WHITE);

		// Đặt nền trắng cho vùng header và vùng chứa
		tblThuoc.getTableHeader().setBackground(new Color(240, 240, 240)); // xám rất nhạt
		tblThuoc.setGridColor(new Color(200, 200, 200)); // Màu đường kẻ ô (nhẹ)
		tblThuoc.setShowGrid(true); // Bật hiển thị đường kẻ

		// Viền cho bảng
		tblThuoc.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));

		// Nền của JScrollPane (bao quanh bảng)
		JScrollPane scrollPane = new JScrollPane(tblThuoc);
		scrollPane.getViewport().setBackground(Color.WHITE); // nền vùng chứa bảng

		// Căn giữa nội dung các ô
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < tblThuoc.getColumnCount(); i++) {
			tblThuoc.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

		// Căn giữa tiêu đề cột
		JTableHeader header = tblThuoc.getTableHeader();
		header.setBackground(new Color(240, 240, 240));
		header.setFont(new Font("Times New Roman", Font.BOLD, 17));
		((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

		add(scrollPane, BorderLayout.CENTER);

		// =================== BOTTOM ===================
		JPanel pnlBottom = new JPanel();
		pnlBottom.setLayout(new BoxLayout(pnlBottom, BoxLayout.Y_AXIS));
		pnlBottom.setBorder(new EmptyBorder(10, 20, 20, 20));
		pnlBottom.setBackground(Color.WHITE);

		// --- Ghi chú ---
		JPanel rowNote = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
		rowNote.setBackground(Color.WHITE);
		txaGhiChu = tool.taoTextArea(50);
		rowNote.add(tool.taoLabel("Ghi chú:"));
		rowNote.add(new JScrollPane(txaGhiChu));
		pnlBottom.add(rowNote);

		// --- Nút chức năng ---
		JPanel rowBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
		rowBtns.setBackground(Color.WHITE);
		btnLamMoi = tool.taoButton("Làm mới", "/picture/hoaDon/refresh.png");
		btnTaoPhieuDat = tool.taoButton("Tạo phiếu đặt", "/picture/hoaDon/plus.png");
		rowBtns.add(btnLamMoi);
		rowBtns.add(btnTaoPhieuDat);

		pnlBottom.add(rowBtns);
		add(pnlBottom, BorderLayout.SOUTH);
	}
	
	public void setFieldHeight(JComponent comp) {
		Dimension d = comp.getPreferredSize();
		d.height = 36;
		comp.setPreferredSize(d);
	}

	public TrangChuQL_GUI getTrangChuQL() {
		return trangChuQL;
	}

	public TrangChuNV_GUI getTrangChuNV() {
		return trangChuNV;
	}

	public void setTrangChuQL(TrangChuQL_GUI trangChuQL) {
		this.trangChuQL = trangChuQL;
	}

	public void setTrangChuNV(TrangChuNV_GUI trangChuNV) {
		this.trangChuNV = trangChuNV;
	}
	
	public JComboBox<String> getCmbSanPham() { return cmbSanPham; }
	public JComboBox<String> getCmbQuocGia() { return cmbQuocGia; }
	
}
