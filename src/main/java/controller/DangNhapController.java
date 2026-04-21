package controller;

import entity.TaiKhoan;
import gui.DangNhap_GUI;
import gui.TrangChuNV_GUI;
import gui.TrangChuQL_GUI;
import service.TaiKhoanService;

import javax.swing.*;

public class DangNhapController {

    public DangNhap_GUI gui;
    public TaiKhoanService tkService = new TaiKhoanService();

    public DangNhapController(DangNhap_GUI gui) {
        this.gui = gui;
        ganSuKien();
    }

    public void ganSuKien() {
        gui.getBtnDangNhap().addActionListener(e -> xuLyDangNhap());

        gui.txtTenDangNhap.addActionListener(e -> {
            if (gui.hienMatKhau) {
                gui.txtPassOpen.requestFocus();
            } else {
                gui.txpPassClose.requestFocus();
            }
        });

        gui.txpPassClose.addActionListener(e -> xuLyDangNhap());
        gui.txtPassOpen.addActionListener(e -> xuLyDangNhap());
    }

    public void xuLyDangNhap() {
        String tenDangNhap = gui.txtTenDangNhap.getText().trim();

        String matKhau = gui.hienMatKhau
                ? gui.txtPassOpen.getText().trim()
                : new String(gui.txpPassClose.getPassword()).trim();

        if (tenDangNhap.isEmpty() || matKhau.isEmpty()) {
            JOptionPane.showMessageDialog(gui,
                    "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        TaiKhoan tkParam = new TaiKhoan();
        tkParam.setTenDangNhap(tenDangNhap);
        tkParam.setMatKhau(matKhau);

        SwingWorker<TaiKhoan, Void> worker = new SwingWorker<>() {

            @Override
            protected TaiKhoan doInBackground() throws Exception {
                return tkService.kiemTraDangNhap(tkParam);
            }

            @Override
            protected void done() {
                try {
                    TaiKhoan tk = get();

                    if (tk == null) {
                        JOptionPane.showMessageDialog(gui,
                                "Tên đăng nhập hoặc mật khẩu không đúng!",
                                "Đăng nhập thất bại",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    gui.dispose();

                    if (tk.getLoaiTK().equalsIgnoreCase("Quản lý")) {
                        new TrangChuQL_GUI(tk);
                    } else {
                        new TrangChuNV_GUI(tk);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(gui,
                            "Lỗi đăng nhập hệ thống: " + e.getMessage(),
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
    }
}