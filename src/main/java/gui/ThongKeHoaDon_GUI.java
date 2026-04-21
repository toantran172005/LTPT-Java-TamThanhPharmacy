package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartPanel;
//import org.jfree.chart.JFreeChart;
//import org.jfree.chart.plot.PlotOrientation;
//import org.jfree.data.category.DefaultCategoryDataset;

import com.toedter.calendar.JDateChooser;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import utils.ToolCtrl;

//import controller.ToolCtrl;

public class ThongKeHoaDon_GUI extends JPanel {

	public JDateChooser dpNgayBatDau, dpNgayKetThuc;
	public JButton btnThongKe, btnXuatExcel, btnLamMoi;
	public JLabel lblTongDoanhThu, lblTongHD, lblTBDT;
	public JComboBox<String> cmbTopTK;
	public JTable tblThongKeHD;
	public JPanel mainCenter;
	public ChartPanel chartPanel;
	public JPanel pnlBieuDo;

	Font font1 = new Font("Times New Roman", Font.BOLD, 18);
	Font font2 = new Font("Times New Roman", Font.PLAIN, 15);
	public ToolCtrl tool = new ToolCtrl();

	public ThongKeHoaDon_GUI() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(1134, 617));
		setBackground(Color.WHITE);

		JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
		mainPanel.setBackground(Color.white);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		add(mainPanel);

		// ========== TOP PANEL ==========
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		topPanel.setBackground(Color.white);

		JLabel lblTitle = new JLabel("THỐNG KÊ HÓA ĐƠN", SwingConstants.CENTER);
		lblTitle.setFont(font1);
		lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		topPanel.add(lblTitle);

		JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
		filterPanel.setBackground(Color.white);
		JLabel lblTuNgay = tool.taoLabel("Từ ngày:");
		dpNgayBatDau = tool.taoDateChooser();

		JLabel lblDenNgay = tool.taoLabel("Đến ngày:");
		dpNgayKetThuc = tool.taoDateChooser();

		btnThongKe = tool.taoButton("Thống kê", "/picture/hoaDon/statistic.png");

		filterPanel.add(lblTuNgay);
		filterPanel.add(dpNgayBatDau);
		filterPanel.add(lblDenNgay);
		filterPanel.add(dpNgayKetThuc);
		filterPanel.add(btnThongKe);

		topPanel.add(filterPanel);
		mainPanel.add(topPanel, BorderLayout.NORTH);

		// ========== CENTER ==========
		mainCenter = new JPanel();
		mainCenter.setLayout(new BoxLayout(mainCenter, BoxLayout.Y_AXIS));
		mainCenter.setBackground(Color.WHITE);

		// ===== Thông tin tổng quan =====
		JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
		summaryPanel.setBackground(Color.WHITE);
		lblTongDoanhThu = tool.taoLabel("Tổng doanh thu: 0 VND");
		lblTongHD = tool.taoLabel("Tổng hóa đơn: 0");
		lblTBDT = tool.taoLabel("Doanh thu TB/Hóa đơn: 0");
		for (JLabel lbl : new JLabel[] { lblTongDoanhThu, lblTongHD, lblTBDT }) {
			summaryPanel.add(lbl);
		}
		mainCenter.add(summaryPanel);

		// ===== Biểu đồ doanh thu =====
		pnlBieuDo = new JPanel(new BorderLayout());
		pnlBieuDo.setBackground(Color.WHITE);
		pnlBieuDo.setBorder(BorderFactory.createTitledBorder("Biểu đồ doanh thu"));
		pnlBieuDo.setPreferredSize(new Dimension(900, 320));
		pnlBieuDo.setMaximumSize(new Dimension(Short.MAX_VALUE, 320)); // KHÓA CHIỀU CAO
		pnlBieuDo.setAlignmentX(Component.CENTER_ALIGNMENT);

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		JFreeChart chart = ChartFactory.createLineChart("Doanh thu theo ngày", "Ngày", "Doanh thu (VND)", dataset,
				PlotOrientation.VERTICAL, false, true, false);

		chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(850, 250));
		chartPanel.setMaximumSize(new Dimension(850, 250));
		chartPanel.setMinimumSize(new Dimension(850, 250));
		chartPanel.setMouseWheelEnabled(true);
		chartPanel.setDomainZoomable(false);
		chartPanel.setRangeZoomable(false);

		// ===== Đảm bảo biểu đồ không tự mở rộng trong BoxLayout =====
		pnlBieuDo.add(chartPanel, BorderLayout.CENTER);
		mainCenter.add(pnlBieuDo);

		// ===== Bảng Top khách hàng =====
		JPanel tablePanel = new JPanel(new BorderLayout(10, 10));
		tablePanel.setBackground(Color.WHITE);
		tablePanel.setBorder(BorderFactory.createTitledBorder("Top khách hàng"));
		tablePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));

		JPanel topTablePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		topTablePanel.setBackground(Color.WHITE);
		topTablePanel.add(new JLabel("Thống kê theo top:"));
		cmbTopTK = new JComboBox<>(new String[] { "5", "10", "20" });
		cmbTopTK.setPreferredSize(new Dimension(40, 25));
		topTablePanel.add(cmbTopTK);
		tablePanel.add(topTablePanel, BorderLayout.NORTH);

		String[] columnNames = { "STT", "Tên khách hàng", "Số điện thoại", "Số hóa đơn", "Tổng mua" };
		DefaultTableModel model = new DefaultTableModel(columnNames, 0);
		tblThongKeHD = new JTable(model);
		tblThongKeHD.setRowHeight(28);
		tblThongKeHD.getTableHeader().setFont(font2);
		tblThongKeHD.setFont(font2);
		tblThongKeHD.setBackground(Color.WHITE);
		tblThongKeHD.getTableHeader().setBackground(new Color(240, 240, 240));
		tblThongKeHD.setGridColor(new Color(200, 200, 200));
		tblThongKeHD.setShowGrid(true);

		// ===== Căn giữa header =====
		DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) tblThongKeHD.getTableHeader()
				.getDefaultRenderer();
		headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		// ===== Áp dụng renderer cho header =====
		JTableHeader header = tblThongKeHD.getTableHeader();
		header.setDefaultRenderer(headerRenderer);
		header.setPreferredSize(new Dimension(header.getWidth(), 28));

		JScrollPane scrollPaneTable = new JScrollPane(tblThongKeHD);
		scrollPaneTable.getViewport().setBackground(Color.WHITE);
		tablePanel.add(scrollPaneTable, BorderLayout.CENTER);

		// ===== Các nút =====
		JPanel bottomButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
		bottomButtons.setBackground(Color.WHITE);
		btnXuatExcel = tool.taoButton("Xuất Excel", "/picture/hoaDon/export.png");
		btnLamMoi = tool.taoButton("Làm mới", "/picture/hoaDon/return.png");
		bottomButtons.add(btnXuatExcel);
		bottomButtons.add(btnLamMoi);
		tablePanel.add(bottomButtons, BorderLayout.SOUTH);

		mainCenter.add(tablePanel);

		// ===== Thanh cuộn =====
		JScrollPane scrollMainCenter = new JScrollPane(mainCenter);
		scrollMainCenter.setBorder(null);
		scrollMainCenter.getVerticalScrollBar().setUnitIncrement(16);
		scrollMainCenter.getViewport().setBackground(Color.WHITE);

		mainPanel.add(scrollMainCenter, BorderLayout.CENTER);

		new controller.ThongKeHDController(this);
	}

	// ========== GETTER ==========
	public JButton getBtnThongKe() {
		return btnThongKe;
	}

	public JButton getBtnXuatExcel() {
		return btnXuatExcel;
	}

	public JButton getBtnLamMoi() {
		return btnLamMoi;
	}

	public JComboBox<String> getCmbTopTK() {
		return cmbTopTK;
	}

	public JDateChooser getDpNgayBatDau() {
		return dpNgayBatDau;
	}

	public JDateChooser getDpNgayKetThuc() {
		return dpNgayKetThuc;
	}

	public JTable getTblThongKeHD() {
		return tblThongKeHD;
	}

	public JLabel getLblTongDoanhThu() {
		return lblTongDoanhThu;
	}

	public JLabel getLblTongHD() {
		return lblTongHD;
	}

	public JLabel getLblTBDT() {
		return lblTBDT;
	}

	public JPanel getMainCenter() {
		return mainCenter;
	}

	public ChartPanel getChartPanel() {
		return chartPanel;
	}

	public JPanel getPnlBieuDo() {
		return pnlBieuDo;
	}
}
