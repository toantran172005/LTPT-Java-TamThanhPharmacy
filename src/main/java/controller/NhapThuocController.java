package controller;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import entity.DonViTinh;
import entity.QuocGia;
import entity.Thue;
import entity.Thuoc;
import gui.NhapThuoc_GUI;
import service.ThuocService; // Sử dụng Service thay vì DAO
import utils.ToolCtrl;

public class NhapThuocController {

    private final NhapThuoc_GUI nhapThuoc_gui;
    private final ToolCtrl tool = new ToolCtrl();
    private final ThuocService thuocService = new ThuocService(); // Tiêm Service vào

    public NhapThuocController(NhapThuoc_GUI ntGUI) {
        this.nhapThuoc_gui = ntGUI;
    }

    /**
     * Mở hộp thoại chọn file Excel
     */
    public void chonFileExcel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn tệp Excel để nhập thuốc");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "File Excel (.xlsx, .xls)", "xlsx", "xls"));

            if (fileChooser.showOpenDialog(nhapThuoc_gui) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                docFileExcel(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Đọc file Excel và đổ dữ liệu vào Table
     */
    public void docFileExcel(File file) {
        DefaultTableModel model = (DefaultTableModel) nhapThuoc_gui.tblNhapThuoc.getModel();
        model.setRowCount(0);

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            boolean isFirstRow = true;
            int stt = 1;

            while(rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if(isFirstRow) { isFirstRow = false; continue; }

                Object[] rowData = new Object[13];
                rowData[0] = stt++;

                for(int i = 1; i < 13; i++) {
                    Cell cell = row.getCell(i);
                    if(cell == null) {
                        rowData[i] = "";
                    } else {
                        // Xử lý định dạng ngày tháng cho cột Hạn dùng (index 7)
                        if (i == 7 && cell.getCellType() == org.apache.poi.ss.usermodel.CellType.NUMERIC) {
                            Date date = cell.getDateCellValue();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            rowData[i] = sdf.format(date);
                        } else {
                            switch (cell.getCellType()) {
                                case STRING: rowData[i] = cell.getStringCellValue(); break;
                                case NUMERIC:
                                    if(DateUtil.isCellDateFormatted(cell)) {
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                        rowData[i] = sdf.format(cell.getDateCellValue());
                                    } else {
                                        rowData[i] = cell.getNumericCellValue();
                                    }
                                    break;
                                case BOOLEAN: rowData[i] = cell.getBooleanCellValue(); break;
                                default: rowData[i] = "";
                            }
                        }
                    }
                }
                model.addRow(rowData);
            }
            tool.hienThiThongBao("Thông báo", "Đọc file Excel thành công", true);

        } catch (Exception e) {
            e.printStackTrace();
            tool.hienThiThongBao("Lỗi", "Không thể đọc file Excel", false);
        }
    }

    /**
     * Lưu dữ liệu từ Table vào Cơ sở dữ liệu thông qua Service
     */
    public void luuDataTuTable() {
        DefaultTableModel model = (DefaultTableModel) nhapThuoc_gui.tblNhapThuoc.getModel();
        int rowCount = model.getRowCount();

        if(rowCount == 0) {
            tool.hienThiThongBao("Lỗi", "Không có dữ liệu trên bảng để lưu", false);
            return;
        }

        try {
            List<Thuoc> listThuoc = new ArrayList<>();
            for(int i = 0; i < rowCount; i++) {
                // Lấy dữ liệu từ Table
                String maThuoc = model.getValueAt(i, 1).toString();
                String tenThuoc = model.getValueAt(i, 2).toString();
                //int soLo = (int) Double.parseDouble(model.getValueAt(i, 3).toString());
                String dangThuoc = model.getValueAt(i, 3).toString();
                String tenDvt = model.getValueAt(i, 4).toString();
                String tenQuocGia = model.getValueAt(i, 5).toString();
                LocalDate hanDung = tool.convertExcelDate(model.getValueAt(i, 6));
                int soLuong = (int) Double.parseDouble(model.getValueAt(i, 7).toString());
                double donGia = Double.parseDouble(model.getValueAt(i, 8).toString());
                double tyLeThueVal = Double.parseDouble(model.getValueAt(i, 9).toString());
                String loaiThue = model.getValueAt(i, 11).toString();

                // 1. Xử lý Thuế thông qua Service
                Thue thueObj = new Thue();
                thueObj.setLoaiThue(loaiThue);
                thueObj.setTyLeThue(tyLeThueVal);
                String maThue = thuocService.layHoacTaoThue(thueObj);
                thueObj.setMaThue(maThue);

                // 2. Xử lý Đơn vị tính thông qua Service
                DonViTinh dvtObj = new DonViTinh();
                dvtObj.setTenDVT(tenDvt);
                String maDVT = thuocService.layHoacTaoDVT(dvtObj);
                dvtObj.setMaDVT(maDVT);

                // 3. Xử lý Quốc gia thông qua Service
                String maQG = thuocService.layMaQuocGiaTheoTen(tenQuocGia);
                QuocGia qg = thuocService.layQuocGiaTheoMa(maQG);

                // 4. Tạo đối tượng Thuốc
                Thuoc thuoc = new Thuoc();
                thuoc.setMaThuoc(maThuoc);
                thuoc.setTenThuoc(tenThuoc);
//                thuoc.setSoLuong(soLo);
                thuoc.setDangThuoc(dangThuoc);
                thuoc.setDonViTinh(dvtObj);
                thuoc.setQuocGia(qg);
                thuoc.setHanSuDung(hanDung);
                thuoc.setSoLuong(soLuong);
                thuoc.setGiaBan(donGia);
                thuoc.setThue(thueObj);
                thuoc.setTrangThai(true);

                listThuoc.add(thuoc);
            }

            // Chuẩn bị các thông tin phiếu nhập (Hardcode theo mẫu cũ của bạn)
            String maPNT = tool.taoKhoaChinh("PNT");
            String maNCC = "TTNCC1";
            String maNV = "TTNV1";
            LocalDate ngayNhap = LocalDate.now();

            // 5. Gọi Service để thực hiện transaction lưu data
            boolean res = thuocService.luuData(maPNT, maNCC, maNV, ngayNhap, listThuoc);

            if(res) {
                tool.hienThiThongBao("Thông báo", "Lưu dữ liệu thành công", true);
                lamMoiBang();
            } else {
                tool.hienThiThongBao("Lỗi", "Lưu dữ liệu thất bại (Lỗi hệ thống)", false);
            }

        } catch (Exception e) {
            e.printStackTrace();
            tool.hienThiThongBao("Lỗi", "Dữ liệu trên bảng không hợp lệ: " + e.getMessage(), false);
        }
    }

    public void lamMoiBang() {
        DefaultTableModel model = (DefaultTableModel) nhapThuoc_gui.tblNhapThuoc.getModel();
        model.setRowCount(0);
    }
}