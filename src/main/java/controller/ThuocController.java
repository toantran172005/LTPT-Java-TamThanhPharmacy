package controller;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import entity.KeThuoc;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import service.KeThuocService;
import service.ThuocService;
import entity.Thuoc;
import gui.ChiTietThuoc_GUI;
import gui.ThemThuoc_GUI;
import gui.ThongKeThuoc_GUI;
import gui.TimKiemThuoc_GUI;
import utils.ToolCtrl;

public class ThuocController {

    public ThuocService thuocService = new ThuocService();
    public KeThuocService keThuocService = new KeThuocService();
    public ToolCtrl tool = new ToolCtrl();

    public ThongKeThuoc_GUI tkThuoc;
    public TimKiemThuoc_GUI thuoc;
    public ChiTietThuoc_GUI ctThuoc;
    public ThemThuoc_GUI themThuoc;

    public boolean isHienThi = true;
    public ArrayList<Thuoc> listThuoc;

    public ThuocController(ThongKeThuoc_GUI tkThuoc) {
        this.tkThuoc = tkThuoc;
    }

    public ThuocController(ChiTietThuoc_GUI ctThuoc) {
        this.ctThuoc = ctThuoc;
    }

    public ThuocController(TimKiemThuoc_GUI thuoc) {
        this.thuoc = thuoc;
        lamMoiDuLieu();
    }

    public ThuocController() {
        super();
    }

    public void lamMoiDuLieu() {
        // Nâng cấp: Dùng Service và ép kiểu ArrayList
        listThuoc = new ArrayList<>(thuocService.layListThuocHoanChinh());
    }

    public File chonFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Hình ảnh", "jpg", "png", "gif"));
        int option = fileChooser.showOpenDialog(null);
        if(option == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    public void setCmbKeThuoc() {
         ArrayList<KeThuoc> listKT = (ArrayList<KeThuoc>) keThuocService.layListKeThuoc();
         thuoc.cmbLoaiThuoc.removeAllItems();
         thuoc.cmbLoaiThuoc.addItem("Tất cả");
         for (KeThuoc kt : listKT) {
             thuoc.cmbLoaiThuoc.addItem(kt.getLoaiKe());
         }
    }

    public void onThongKe() {
        java.util.Date dateBD = tkThuoc.dpNgayBD.getDate();
        java.util.Date dateKT = tkThuoc.dpNgayKT.getDate();

        LocalDate bd = tool.utilDateSangLocalDate(dateBD);
        LocalDate kt = tool.utilDateSangLocalDate(dateKT);

        if(kt == null) kt = LocalDate.now();
        if(bd == null) bd = LocalDate.now().withDayOfMonth(1);

        if (bd.isAfter(kt)) {
            tool.hienThiThongBao("Lỗi ngày", "Ngày bắt đầu phải trước ngày kết thúc", false);
            return;
        }

        // Gọi qua Service, ép sang ArrayList
        ArrayList<Object[]> listTK = new ArrayList<>(thuocService.thongKeThuocBanChay(bd, kt));

        tkThuoc.dataset.clear();
        tkThuoc.model.setRowCount(0);
        int stt = 1;

        if (listTK.isEmpty()) {
            tool.hienThiThongBao("Thông báo", "Không có dữ liệu trong khoảng thời gian này", false);
        }

        for(Object[] row : listTK) {
            String maThuoc = (String) row[0];
            String tenThuoc = (String) row[1];
            int soLuongBan = (int) (row[2] != null ? Long.parseLong(row[2].toString()) : 0);
            double doanhThu = (double) (row[3] != null ? Double.parseDouble(row[3].toString()) : 0.0);

            tkThuoc.dataset.addValue(soLuongBan, "Số lượng bán", tenThuoc);

            tkThuoc.model.addRow(new Object[] {
                    stt++, maThuoc, tenThuoc, soLuongBan, tool.dinhDangVND(doanhThu)
            });
        }
    }

    public void onLamMoi() {
        tkThuoc.dpNgayBD.setDate(null);
        tkThuoc.dpNgayKT.setDate(null);
        ((DefaultTableModel) tkThuoc.tblThongKe.getModel()).setRowCount(0);
        tkThuoc.dataset.clear();
    }

    public void xoaThuoc() {
        if(listThuoc == null) lamMoiDuLieu();

        boolean isXoa = thuoc.btnXoa.getText().equalsIgnoreCase("Xoá");
        String title = isXoa ? "Xóa thuốc" : "Khôi phục thuốc";

        if (tool.hienThiXacNhan(title, "Xác nhận thực hiện?", null)) {
            int viewRow = thuoc.tblThuoc.getSelectedRow();
            if (viewRow != -1) {
                int modelRow = thuoc.tblThuoc.convertRowIndexToModel(viewRow);
                String maTh = thuoc.tblThuoc.getModel().getValueAt(modelRow, 0).toString();

                // Nâng cấp: Dùng chung hàm capNhatTrangThaiThuoc trong Service (false: Xóa, true: Khôi phục)
                boolean kq = thuocService.capNhatTrangThaiThuoc(maTh, !isXoa);
                if (kq) {
                    tool.hienThiThongBao(title, "Thành công!", true);
                    lamMoiDuLieu();
                    locTatCa(isHienThi);
                } else {
                    tool.hienThiThongBao("Lỗi", "Thất bại!", false);
                }
            } else {
                tool.hienThiThongBao("Lỗi", "Vui lòng chọn thuốc!", false);
            }
        }
    }

    public void xemChiTiet() {
        int selectedRow = thuoc.tblThuoc.getSelectedRow();
        if(selectedRow == -1) {
            tool.hienThiThongBao("Lỗi", "Vui lòng chọn 1 thuốc để xem chi tiết", false);
            return;
        }

        int modelRow = thuoc.tblThuoc.convertRowIndexToModel(selectedRow);
        String maThuoc = thuoc.tblThuoc.getModel().getValueAt(modelRow, 0).toString();

        ChiTietThuoc_GUI ctThuoc_GUI = new ChiTietThuoc_GUI(maThuoc);
        tool.doiPanel(thuoc, ctThuoc_GUI);
    }

    public ArrayList<String> layDSTenThuoc(){
        if(listThuoc == null) lamMoiDuLieu();
        ArrayList<String> listTen = new ArrayList<>();
        for(Thuoc t : listThuoc) {
            listTen.add(t.getTenThuoc());
        }
        return listTen;
    }

    public void xuLyBtnLichSuXoa() {
        isHienThi = !isHienThi;
        thuoc.btnLichSuXoa.setText(!isHienThi ? "Danh sách hiện tại" : "Lịch sử xoá");
        thuoc.btnXoa.setText(!isHienThi ? "Khôi phục" : "Xoá");
        locTatCa(isHienThi);
    }

    public void locTatCa(boolean isHienThi) {
        if(listThuoc == null) lamMoiDuLieu();
        ArrayList<Thuoc> ketQua = new ArrayList<>();

        String tuKhoaTen = thuoc.txtTenThuoc.getText().trim().toLowerCase();
        Object loaiObj = thuoc.cmbLoaiThuoc.getSelectedItem();
        String tuKhoaLoai = (loaiObj != null) ? loaiObj.toString().trim().toLowerCase() : "";

        if (tuKhoaLoai.equals("tất cả")) {
            tuKhoaLoai = "";
        }

        for (Thuoc t : listThuoc) {
            boolean matchTen = tuKhoaTen.isEmpty() || t.getTenThuoc().toLowerCase().contains(tuKhoaTen);
            boolean matchLoai = tuKhoaLoai.isEmpty() || (t.getKeThuoc() != null && t.getKeThuoc().getLoaiKe().toLowerCase().contains(tuKhoaLoai));
            boolean matchTrangThai = (t.getTrangThai() == isHienThi);

            if (matchTen && matchLoai && matchTrangThai) {
                ketQua.add(t);
            }
        }
        setDataChoTable(ketQua);
    }

    public void setDataChoTable(ArrayList<Thuoc> list) {
        thuoc.model.setRowCount(0);
        for(Thuoc t : list) {
            thuoc.model.addRow(new Object[] {
                    t.getMaThuoc(),
                    t.getTenThuoc(),
                    t.getKeThuoc().getLoaiKe(),
                    tool.dinhDangVND(t.getGiaBan()),
                    t.getSoLuong(),
                    t.getQuocGia() != null ? t.getQuocGia().getTenQuocGia() : "",
                    t.getDonViTinh().getTenDVT(),
                    t.getHanSuDung()
            });
        }
    }

    public void xuatFileExcel() {
        if (tkThuoc.tblThongKe.getRowCount() == 0) {
            tool.hienThiThongBao("Cảnh báo", "Không có dữ liệu để xuất file!", false);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu tệp Excel");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files (.xlsx)", "xlsx"));
        fileChooser.setSelectedFile(new File("ThongKeThuocBanChay.xlsx"));

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getAbsolutePath().endsWith(".xlsx")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".xlsx");
            }

            try (XSSFWorkbook workbook = new XSSFWorkbook()) {
                XSSFSheet sheet = workbook.createSheet("Thống kê thuốc");
                CellStyle titleStyle = workbook.createCellStyle();
                XSSFFont titleFont = workbook.createFont();
                titleFont.setFontName("Times New Roman");
                titleFont.setBold(true);
                titleFont.setFontHeightInPoints((short) 16);
                titleStyle.setFont(titleFont);
                titleStyle.setAlignment(HorizontalAlignment.CENTER);

                CellStyle headerStyle = workbook.createCellStyle();
                XSSFFont headerFont = workbook.createFont();
                headerFont.setFontName("Times New Roman");
                headerFont.setBold(true);
                headerFont.setFontHeightInPoints((short) 12);
                headerStyle.setFont(headerFont);
                headerStyle.setBorderBottom(BorderStyle.THIN);
                headerStyle.setBorderTop(BorderStyle.THIN);
                headerStyle.setBorderLeft(BorderStyle.THIN);
                headerStyle.setBorderRight(BorderStyle.THIN);
                headerStyle.setAlignment(HorizontalAlignment.CENTER);

                CellStyle dataStyle = workbook.createCellStyle();
                dataStyle.setBorderBottom(BorderStyle.THIN);
                dataStyle.setBorderTop(BorderStyle.THIN);
                dataStyle.setBorderLeft(BorderStyle.THIN);
                dataStyle.setBorderRight(BorderStyle.THIN);
                dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);

                XSSFRow rowTitle = sheet.createRow(0);
                Cell cellTitle = rowTitle.createCell(0);
                cellTitle.setCellValue("THỐNG KÊ TOP THUỐC BÁN CHẠY");
                cellTitle.setCellStyle(titleStyle);
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));

                XSSFRow rowDate = sheet.createRow(1);
                Cell cellDate = rowDate.createCell(0);

                String ngayBD = tkThuoc.dpNgayBD.getDate() != null ?
                        tool.dinhDangLocalDate(tool.utilDateSangLocalDate(tkThuoc.dpNgayBD.getDate())) : "...";
                String ngayKT = tkThuoc.dpNgayKT.getDate() != null ?
                        tool.dinhDangLocalDate(tool.utilDateSangLocalDate(tkThuoc.dpNgayKT.getDate())) : "...";

                cellDate.setCellValue("Thời gian: Từ " + ngayBD + " đến " + ngayKT);
                sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 4));

                int rowNum = 3;
                XSSFRow rowHeader = sheet.createRow(rowNum++);
                String[] headers = { "STT", "Mã thuốc", "Tên thuốc", "Số lượng bán", "Doanh thu" };

                for (int i = 0; i < headers.length; i++) {
                    Cell cell = rowHeader.createCell(i);
                    cell.setCellValue(headers[i]);
                    cell.setCellStyle(headerStyle);
                }

                JTable table = tkThuoc.tblThongKe;
                for (int i = 0; i < table.getRowCount(); i++) {
                    XSSFRow row = sheet.createRow(rowNum++);
                    createCell(row, 0, table.getValueAt(i, 0).toString(), dataStyle);
                    createCell(row, 1, table.getValueAt(i, 1).toString(), dataStyle);
                    createCell(row, 2, table.getValueAt(i, 2).toString(), dataStyle);
                    Cell cellSL = row.createCell(3);
                    cellSL.setCellValue(Integer.parseInt(table.getValueAt(i, 3).toString()));
                    cellSL.setCellStyle(dataStyle);
                    createCell(row, 4, table.getValueAt(i, 4).toString(), dataStyle);
                }
                for (int i = 0; i < 5; i++) {
                    sheet.autoSizeColumn(i);
                }

                try (FileOutputStream out = new FileOutputStream(fileToSave)) {
                    workbook.write(out);
                    tool.hienThiThongBao("Thành công", "Xuất file Excel thành công!", true);

                    int confirm = JOptionPane.showConfirmDialog(null, "Bạn có muốn mở file?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                    if(confirm == JOptionPane.YES_OPTION) {
                        Desktop.getDesktop().open(fileToSave);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                tool.hienThiThongBao("Lỗi", "Đã xảy ra lỗi khi ghi file: " + e.getMessage(), false);
            }
        }
    }

    private void createCell(XSSFRow row, int colIndex, String value, CellStyle style) {
        Cell cell = row.createCell(colIndex);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }
}