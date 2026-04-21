package controller;

import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

import service.DonViTinhService; // Import Service
import entity.DonViTinh;
import gui.DonVi_GUI;
import utils.ToolCtrl;

public class DonViTinhController {

    public DonVi_GUI dvGUI;
    public ToolCtrl tool = new ToolCtrl();

    // Thay thế DAO bằng Service
    public DonViTinhService dvService = new DonViTinhService();

    ArrayList<DonViTinh> listDVT = new ArrayList<DonViTinh>();
    public boolean isHienThi = true;

    public DonViTinhController(DonVi_GUI donVi_GUI) {
        this.dvGUI = donVi_GUI;
    }

    // Đưa dữ liệu lên bảng
    public void setDataChoTable(ArrayList<DonViTinh> list) {
        DefaultTableModel model = (DefaultTableModel) dvGUI.tblDonVi.getModel();
        model.setRowCount(0);
        int stt = 1;

        for(DonViTinh dvt : list) {
            model.addRow(new Object[] {
                    stt++,
                    dvt.getMaDVT(),
                    dvt.getTenDVT()
            });
        }
    }

    // Hàm lọc tổng hợp (Tìm kiếm theo tên + Trạng thái hiển thị)
    public void locTatCa(boolean isHienThi) {
        // Lấy ArrayList từ Service
        ArrayList<DonViTinh> fullList = dvService.layDanhSachTheoTrangThai(isHienThi);

        ArrayList<DonViTinh> ketQua = new ArrayList<>();
        String tuKhoa = dvGUI.txtTimDV.getText().trim().toLowerCase();

        if (fullList != null) {
            for (DonViTinh dvt : fullList) {
                boolean matchTen = tuKhoa.isEmpty()
                        || dvt.getTenDVT().toLowerCase().contains(tuKhoa);

                // Do Entity dùng class Boolean nên xài getTrangThai()
                boolean matchTrangThai = (dvt.getTrangThai() == isHienThi);

                if (matchTen && matchTrangThai) {
                    ketQua.add(dvt);
                }
            }
        }
        setDataChoTable(ketQua);
    }

    // Thêm đơn vị
    public void themDonVi() {
        String tenDV = dvGUI.txtTenDV.getText().trim();

        // Kiểm tra rỗng
        if (tenDV.isEmpty()) {
            tool.hienThiThongBao("Lỗi", "Vui lòng nhập tên đơn vị!", false);
            dvGUI.txtTenDV.requestFocus();
            return;
        }

        // Kiểm tra trùng tên (Gọi Service)
        if (dvService.timTheoTen(tenDV) != null) {
            tool.hienThiThongBao("Lỗi", "Tên đơn vị đã tồn tại!", false);
            return;
        }

        String maDV = tool.taoKhoaChinh("DVT");
        DonViTinh dvt = new DonViTinh(maDV, tenDV, true);

        if (dvService.themDVT(dvt)) {
            tool.hienThiThongBao("Thành công", "Thêm đơn vị thành công!", true);
            dvGUI.txtTenDV.setText("");
            locTatCa(isHienThi);
        } else {
            tool.hienThiThongBao("Thất bại", "Thêm đơn vị thất bại!", false);
        }
    }

    // Xử lý nút Xóa / Khôi phục
    public void xoaDonVi() {
        int selectedRow = dvGUI.tblDonVi.getSelectedRow();
        if (selectedRow == -1) {
            tool.hienThiThongBao("Lỗi", "Vui lòng chọn đơn vị cần thao tác!", false);
            return;
        }

        String maDVT = dvGUI.tblDonVi.getValueAt(selectedRow, 1).toString();

        if (dvGUI.btnXoa.getText().equalsIgnoreCase("Xoá")) {
            // Xoá
            if (tool.hienThiXacNhan("Xóa đơn vị", "Xác nhận xóa đơn vị này?", null)) {
                if (dvService.xoaDVT(maDVT)) {
                    tool.hienThiThongBao("Thông báo", "Đã xóa đơn vị thành công!", true);
                    locTatCa(isHienThi);
                } else {
                    tool.hienThiThongBao("Lỗi", "Xóa thất bại!", false);
                }
            }
        } else {
            // Khôi phục
            if (tool.hienThiXacNhan("Khôi phục đơn vị", "Xác nhận khôi phục đơn vị này?", null)) {
                if (dvService.khoiPhucDVT(maDVT)) {
                    tool.hienThiThongBao("Thông báo", "Đã khôi phục đơn vị thành công!", true);
                    locTatCa(isHienThi);
                } else {
                    tool.hienThiThongBao("Lỗi", "Khôi phục thất bại!", false);
                }
            }
        }
    }

    // Xử lý nút Lịch sử xóa
    public void xuLyBtnLichSuXoa() {
        isHienThi = !isHienThi;

        dvGUI.btnLichSuXoa.setText(!isHienThi ? "Danh sách hiện tại" : "Lịch sử xoá");
        dvGUI.btnXoa.setText(!isHienThi ? "Khôi phục" : "Xoá");

        locTatCa(isHienThi);
    }

    // Làm mới (Reset form tìm kiếm)
    public void lamMoi() {
        dvGUI.txtTimDV.setText("");
        if (!isHienThi) {
            xuLyBtnLichSuXoa();
        } else {
            locTatCa(true);
        }
    }
}