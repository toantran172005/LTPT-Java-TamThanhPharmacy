package app;

import controller.DangNhapController;
import db.ConnectDb;
import entity.TaiKhoan;
import gui.DangNhap_GUI;
import gui.TrangChuQL_GUI;
import jakarta.persistence.EntityManager;

public class RunApplication {
    public static void main(String[] args) {
        EntityManager em = ConnectDb.getEntityManager();
//        new TrangChuQL_GUI(new TaiKhoan()).setVisible(true);
        DangNhap_GUI dangNhapGUI = new DangNhap_GUI();

        // 3. Khởi tạo Controller và truyền giao diện vào để gắn sự kiện
        new DangNhapController(dangNhapGUI);

        // 4. Hiển thị màn hình đăng nhập lên
        dangNhapGUI.setVisible(true);
    }
}
