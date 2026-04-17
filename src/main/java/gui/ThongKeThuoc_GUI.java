package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.util.ArrayList;

import com.toedter.calendar.JDateChooser; // cần thư viện JCalendar
//
//import controller.ThuocCtrl;
//import controller.ToolCtrl;
import entity.Thuoc;
import utils.ToolCtrl;

public class ThongKeThuoc_GUI extends JPanel {

	public JTable tblThongKe;
	public DefaultTableModel model;
	public JButton btnThongKe, btnLamMoi, btnLuu;
	public JDateChooser dpNgayBD, dpNgayKT;
	public DefaultCategoryDataset dataset;
	public ChartPanel pnlChart;
	public JFreeChart chart;
	public final ToolCtrl tool = new ToolCtrl();
//	public ThuocCtrl thCtrl;
	public ArrayList<Thuoc> list = new ArrayList<Thuoc>();

    public ThongKeThuoc_GUI() {
//    	thCtrl = new ThuocCtrl(this);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        // ===== TOP =====
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Thống kê thuốc bán chạy");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));
        topPanel.add(lblTitle);

        // === Bộ lọc ngày ===
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
        filterPanel.setBackground(Color.WHITE);

        dpNgayBD = new JDateChooser();
        dpNgayKT = new JDateChooser();

        dpNgayBD.setDateFormatString("dd/MM/yyyy");
        dpNgayKT.setDateFormatString("dd/MM/yyyy");
        dpNgayBD.setPreferredSize(new Dimension(160, 35));
        dpNgayKT.setPreferredSize(new Dimension(160, 35));

        filterPanel.add(tool.taoLabel("Ngày bắt đầu:"));
        filterPanel.add(dpNgayBD);
        filterPanel.add(tool.taoLabel("Ngày kết thúc:"));
        filterPanel.add(dpNgayKT);

        // === Nhóm nút chức năng ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 5));
        buttonPanel.setBackground(Color.WHITE);

        btnThongKe = tool.taoButton("Thống kê", "/picture/hoaDon/statistic.png");
        btnLamMoi = tool.taoButton("Làm mới", "/picture/hoaDon/return.png");
        btnLuu = tool.taoButton("Xuất file", "/picture/khachHang/export.png");

        buttonPanel.add(btnThongKe);
        buttonPanel.add(btnLamMoi);
        buttonPanel.add(btnLuu);

        topPanel.add(filterPanel);
        topPanel.add(buttonPanel);
        add(topPanel, BorderLayout.NORTH);

        // ===== CENTER: TABLE =====
        String[] cols = { "STT", "Mã thuốc", "Tên thuốc", "Số lượng bán", "Doanh thu" };
        model = new DefaultTableModel(cols, 0);

        tblThongKe = new JTable(model);
        tblThongKe.setRowHeight(36);
        tblThongKe.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        tblThongKe.setSelectionBackground(new Color(0xE3F2FD));
        tblThongKe.setGridColor(new Color(0xDDDDDD));

        JTableHeader header = tblThongKe.getTableHeader();
        header.setFont(new Font("Times New Roman", Font.BOLD, 14));
        header.setBackground(Color.WHITE);
        header.setForeground(new Color(0x333333));

      //Căn giữa cho dữ liệu trong cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        TableColumnModel columnModel = tblThongKe.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setCellRenderer(centerRenderer);
        }
        
        //Căn giữa cho tiêu đề table
        ((DefaultTableCellRenderer) tblThongKe.getTableHeader().getDefaultRenderer())
        .setHorizontalAlignment(SwingConstants.CENTER);
        
        JScrollPane scrollTable = new JScrollPane(tblThongKe);
        scrollTable.setBorder(BorderFactory.createTitledBorder("Bảng Top 10 thuốc bán chạy nhất"));
        scrollTable.getViewport().setBackground(Color.WHITE);
        add(scrollTable, BorderLayout.CENTER);

        // ===== SOUTH: BIỂU ĐỒ =====
        dataset = new DefaultCategoryDataset();
        
        chart = ChartFactory.createBarChart("Top 10 thuốc bán chạy", "Thuốc", "Số lượng bán", dataset, PlotOrientation.VERTICAL, false, true, false);
        pnlChart = new ChartPanel(chart);
        pnlChart.setPreferredSize(new Dimension(900, 400));
        add(pnlChart, BorderLayout.SOUTH);

        // ===== EVENTS =====
//        ganSuKien();
    }

    
    // =================== Xử lý ===================
//    public void ganSuKien() {
//    	btnThongKe.addActionListener(e -> {
//    		thCtrl.onThongKe();
//    	});
//
//    	btnLamMoi.addActionListener(e -> thCtrl.onLamMoi());
//    	btnLuu.addActionListener(e -> thCtrl.xuatFileExcel());
//    }
    

}
