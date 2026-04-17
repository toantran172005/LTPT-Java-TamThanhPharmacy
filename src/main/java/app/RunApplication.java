package app;

import db.ConnectDb;
import entity.TaiKhoan;
import gui.TrangChuQL_GUI;
import jakarta.persistence.EntityManager;

public class RunApplication {
    public static void main(String[] args) {
        EntityManager em = ConnectDb.getEntityManager();
        new TrangChuQL_GUI(new TaiKhoan()).setVisible(true);
    }
}
