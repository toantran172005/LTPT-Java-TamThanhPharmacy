package controller;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import entity.Thue;
import gui.Thue_GUI;
import service.ThueService; // Nâng cấp: Gọi Service thay vì gọi DAO
import utils.ToolCtrl;

public class ThueController {

    public Thue_GUI thueGUI;
    // NÂNG CẤP: Sử dụng ThueService
    public ThueService thueService = new ThueService();
    public ToolCtrl tool = new ToolCtrl();

    public ThueController(Thue_GUI thue_GUI) {
        this.thueGUI = thue_GUI;
    }

    // Hàm tải dữ liệu lên bảng
    public void loadData() {
        ArrayList<Thue> list = thueService.layListThue();
        hienThiLenBang(list);
    }

    // Hàm hiển thị danh sách lên bảng
    private void hienThiLenBang(ArrayList<Thue> list) {
        DefaultTableModel model = (DefaultTableModel) thueGUI.tblThue.getModel();
        model.setRowCount(0);
        for (Thue t : list) {
            double tyLeHienThi = t.getTyLeThue() * 100;
            String tyLeStr;
            if (tyLeHienThi == (long) tyLeHienThi) {
                tyLeStr = String.format("%d%%", (long) tyLeHienThi);
            } else {
                tyLeStr = String.format("%s%%", tyLeHienThi);
            }

            model.addRow(new Object[] {
                    t.getMaThue(),
                    t.getLoaiThue(),
                    tyLeStr,
                    t.getMoTa()
            });
        }
    }

    // Xử lý thêm thuế mới
    public void themThue() {
        String loaiThue = thueGUI.txtLoaiThue.getText().trim();
        String tyLeStr = thueGUI.txtTyLeThue.getText().trim();
        String moTa = thueGUI.txtMoTa.getText().trim();

        if (loaiThue.isEmpty() || tyLeStr.isEmpty()) {
            tool.hienThiThongBao("Lỗi", "Vui lòng nhập tên loại thuế và tỷ lệ!", false);
            return;
        }

        try {
            double tyLeInput = Double.parseDouble(tyLeStr);
            if (tyLeInput < 0) {
                tool.hienThiThongBao("Lỗi", "Tỷ lệ thuế không được âm!", false);
                return;
            }

            double tyLeLuu = tyLeInput / 100.0;

            String maThue = tool.taoKhoaChinh("T");
            Boolean trangThai = true;

            // Chú ý: constructor cần tương thích với Entity JPA của bạn (có thể cần bổ sung thêm tham số trangThai)
            Thue t = new Thue(maThue, loaiThue, tyLeLuu, moTa, trangThai);

            if (thueService.themThue(t)) {
                tool.hienThiThongBao("Thành công", "Thêm loại thuế mới thành công!", true);
                lamMoi();
                loadData();
            } else {
                tool.hienThiThongBao("Thất bại", "Thêm thất bại!", false);
            }
        } catch (NumberFormatException e) {
            tool.hienThiThongBao("Lỗi", "Tỷ lệ thuế phải là số!", false);
        }
    }

    // Xử lý cập nhật thuế
    public void suaThue() {
        int row = thueGUI.tblThue.getSelectedRow();
        if (row < 0) {
            tool.hienThiThongBao("Lỗi", "Vui lòng chọn dòng cần sửa!", false);
            return;
        }

        String maThue = thueGUI.txtMaThue.getText().trim();
        String loaiThue = thueGUI.txtLoaiThue.getText().trim();
        String tyLeStr = thueGUI.txtTyLeThue.getText().trim();
        String moTa = thueGUI.txtMoTa.getText().trim();
        Boolean trangThai = true;

        try {
            double tyLeInput = Double.parseDouble(tyLeStr);
            double tyLeLuu = tyLeInput / 100.0;

            // Trong JPA, em.merge() sẽ update toàn bộ cột. Nếu Constructor này không set trạng thái,
            // bạn hãy đảm bảo bổ sung t.setTrangThai(true) hoặc query Entity lên trước khi merge nhé!
            Thue t = new Thue(maThue, loaiThue, tyLeLuu, moTa, trangThai);
            t.setTrangThai(true); // Đảm bảo không bị null trạng thái khi merge

            if (thueService.capNhatThue(t)) {
                tool.hienThiThongBao("Thành công", "Cập nhật thành công!", true);
                lamMoi();
                loadData();
            } else {
                tool.hienThiThongBao("Thất bại", "Cập nhật thất bại!", false);
            }
        } catch (NumberFormatException e) {
            tool.hienThiThongBao("Lỗi", "Tỷ lệ thuế không hợp lệ!", false);
        }
    }

    // Xử lý xóa thuế
    public void xoaThue() {
        int row = thueGUI.tblThue.getSelectedRow();
        if (row < 0) {
            tool.hienThiThongBao("Lỗi", "Vui lòng chọn dòng cần xóa!", false);
            return;
        }
        String maThue = thueGUI.tblThue.getValueAt(row, 0).toString();

        int confirm = JOptionPane.showConfirmDialog(thueGUI,
                "Bạn có chắc chắn muốn NGƯNG SỬ DỤNG loại thuế này?\n",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (thueService.xoaThue(maThue)) {
                    tool.hienThiThongBao("Thành công", "Đã xóa (ngưng sử dụng) loại thuế!", true);
                    lamMoi();
                    loadData();
                } else {
                    tool.hienThiThongBao("Thất bại", "Không tìm thấy dữ liệu hoặc có lỗi xảy ra!", false);
                }

            } catch (Exception e) { // JPA không throw SQLException, nên ta đổi thành Exception
                e.printStackTrace();
                tool.hienThiThongBao("Lỗi hệ thống", "Lỗi CSDL: " + e.getMessage(), false);
            }
        }
    }

    // Làm mới form
    public void lamMoi() {
        thueGUI.txtMaThue.setText("Tự động tạo...");
        thueGUI.txtLoaiThue.setText("");
        thueGUI.txtTyLeThue.setText("");
        thueGUI.txtMoTa.setText("");
        thueGUI.txtTimKiem.setText("");
        thueGUI.tblThue.clearSelection();
        loadData();
    }

    // Tìm kiếm
    public void timKiem() {
        String tuKhoa = thueGUI.txtTimKiem.getText().trim();
        ArrayList<Thue> list = thueService.timKiem(tuKhoa);
        hienThiLenBang(list);
    }
}