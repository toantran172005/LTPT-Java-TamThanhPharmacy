package controller;

import service.HoaDonService; // Đã đổi import
import entity.HoaDon;
import gui.ChiTietHoaDon_GUI;
import gui.TimKiemHD_GUI;
import gui.TrangChuNV_GUI;
import gui.TrangChuQL_GUI;
import utils.ToolCtrl;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList; // Để cast List
import java.util.List;
import java.util.stream.Collectors;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TimKiemHDController {
    public TimKiemHD_GUI gui;
    public HoaDonService hdService = new HoaDonService();
    public ToolCtrl tool = new ToolCtrl();
    public TrangChuQL_GUI trangChuQL;
    public TrangChuNV_GUI trangChuNV;

    public boolean tblChuaXoa = true;
    public List<HoaDon> listHD;
    public List<HoaDon> listHDDaXoa;

    public TimKiemHDController(TimKiemHD_GUI gui) {
        this.gui = gui;
        this.trangChuQL = gui.getMainFrame();
        this.trangChuNV = gui.getMainFrameNV();

        // Ép kiểu sang ArrayList để swing nhận bình thường
        listHD = new ArrayList<>(hdService.layListHoaDon());
        listHDDaXoa = new ArrayList<>(hdService.layListHDDaXoa());

        capNhatBang(listHD);
        suKien();

        gui.addAncestorListener(new javax.swing.event.AncestorListener() {
            @Override
            public void ancestorAdded(javax.swing.event.AncestorEvent event) {
                moLaiForm();
            }
            @Override public void ancestorRemoved(javax.swing.event.AncestorEvent event) {}
            @Override public void ancestorMoved(javax.swing.event.AncestorEvent event) {}
        });
    }

    public void suKien() {
        gui.getTxtKhachHang().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) { locHoaDon(); }
        });
        gui.getTxtTenNV().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) { locHoaDon(); }
        });

        gui.getBtnLamMoi().addActionListener(e -> lamMoiBang());
        gui.getBtnChiTiet().addActionListener(e -> xemChiTiet());
        gui.getBtnLichSuXoa().addActionListener(e -> xemLichSuXoa());
        gui.getBtnXoaHoanTac().addActionListener(e -> xoaHoacHoanTac());

        gui.getTblHoaDon().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) capNhatTenNutXoaHoanTac();
            }
        });
    }

    public void locHoaDon() {
        String tenKH = gui.getTxtKhachHang().getText().trim().toLowerCase();
        String tenNV = gui.getTxtTenNV().getText().trim().toLowerCase();
        List<HoaDon> danhSach = tblChuaXoa ? listHD : listHDDaXoa;

        List<HoaDon> ketQua = danhSach.stream()
                .filter(hd -> tenKH.isEmpty() || (hd.getKhachHang() != null && hd.getKhachHang().getTenKH().toLowerCase().contains(tenKH)))
                .filter(hd -> tenNV.isEmpty() || (hd.getNhanVien() != null && hd.getNhanVien().getTenNV().toLowerCase().contains(tenNV)))
                .collect(Collectors.toList());

        capNhatBang(ketQua);
    }

    public void lamMoiBang() {
        gui.getTxtKhachHang().setText("");
        gui.getTxtTenNV().setText("");
        capNhatBang(tblChuaXoa ? listHD : listHDDaXoa);
    }

    public void xemChiTiet() {
        int row = gui.getTblHoaDon().getSelectedRow();
        if (row == -1) {
            tool.hienThiThongBao("Thông báo", "Vui lòng chọn một hóa đơn!", false);
            return;
        }
        String maHD = (String) gui.getTblHoaDon().getValueAt(row, 0);
        HoaDon hoaDon = hdService.timHoaDonTheoMa(maHD);

        ChiTietHoaDon_GUI chiTietPanel;
        if (trangChuQL != null) {
            chiTietPanel = new ChiTietHoaDon_GUI(trangChuQL);
            trangChuQL.setUpNoiDung(chiTietPanel);
        } else if (trangChuNV != null) {
            chiTietPanel = new ChiTietHoaDon_GUI(trangChuNV);
            trangChuNV.setUpNoiDung(chiTietPanel);
        } else return;

        //chiTietPanel.getCtrl().hienThiThongTinHoaDon(hoaDon);
    }

    public void xemLichSuXoa() {
        tblChuaXoa = !tblChuaXoa;
        gui.getBtnLichSuXoa().setText(tblChuaXoa ? "Lịch sử xoá" : "Danh sách hiện tại");
        capNhatBang(tblChuaXoa ? listHD : listHDDaXoa);
        gui.getTxtKhachHang().setText("");
        gui.getTxtTenNV().setText("");
        gui.getTblHoaDon().clearSelection();
        capNhatTenNutXoaHoanTac();
    }

    public void xoaHoacHoanTac() {
        int row = gui.getTblHoaDon().getSelectedRow();
        if (row == -1) {
            tool.hienThiThongBao("Thông báo", "Vui lòng chọn một hóa đơn!", false);
            return;
        }

        String maHD = (String) gui.getTblHoaDon().getValueAt(row, 0);
        HoaDon hoaDon = hdService.timHoaDonTheoMa(maHD);
        if (hoaDon == null) {
            tool.hienThiThongBao("Lỗi", "Không tìm thấy hóa đơn!", false);
            return;
        }

        boolean daXoa = !hoaDon.getTrangThai();
        boolean dangXemLichSu = !tblChuaXoa;
        String action = dangXemLichSu ? "khôi phục" : (daXoa ? "hoàn tác" : "xóa");

        int confirm = JOptionPane.showConfirmDialog(gui, "Bạn có chắc muốn " + action + " hóa đơn này không?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            if (dangXemLichSu || daXoa) {
                hdService.khoiPhucHD(maHD);
                if(dangXemLichSu) listHDDaXoa.removeIf(hd -> hd.getMaHD().equals(maHD));
                else listHD.removeIf(hd -> hd.getMaHD().equals(maHD));
                listHD = new ArrayList<>(hdService.layListHoaDon());
                tool.hienThiThongBao("Thành công", "Đã khôi phục hóa đơn!", true);
            } else {
                hdService.xoaHD(maHD);
                listHD.removeIf(hd -> hd.getMaHD().equals(maHD));
                listHDDaXoa = new ArrayList<>(hdService.layListHDDaXoa());
                tool.hienThiThongBao("Thành công", "Đã xóa hóa đơn!", true);
            }
            capNhatBang(tblChuaXoa ? listHD : listHDDaXoa);
        } catch (Exception ex) {
            tool.hienThiThongBao("Lỗi", "Khôi phục thất bại: " + ex.getMessage(), false);
        }
    }

    public void capNhatTenNutXoaHoanTac() {
        int row = gui.getTblHoaDon().getSelectedRow();
        if (row == -1) {
            gui.getBtnXoaHoanTac().setText(tblChuaXoa ? "Xóa" : "Khôi phục");
            return;
        }

        String maHD = (String) gui.getTblHoaDon().getValueAt(row, 0);
        HoaDon hd = hdService.timHoaDonTheoMa(maHD);

        if (hd == null) {
            gui.getBtnXoaHoanTac().setText(tblChuaXoa ? "Xóa" : "Khôi phục");
            return;
        }
        gui.getBtnXoaHoanTac().setText(tblChuaXoa ? (!hd.getTrangThai() ? "Hoàn tác" : "Xóa") : "Khôi phục");
    }

    public void capNhatBang(List<HoaDon> data) {
        DefaultTableModel model = (DefaultTableModel) gui.getTblHoaDon().getModel();
        model.setRowCount(0);
        for (HoaDon hd : data) {
            String tenNV = hd.getNhanVien() != null ? hd.getNhanVien().getTenNV() : "Không xác định";
            String tenKH = hd.getKhachHang() != null ? hd.getKhachHang().getTenKH() : "Không xác định";
            double tongTien = hdService.tinhTongTienTheoHoaDon(hd.getMaHD());

            model.addRow(new Object[] { hd.getMaHD(), tenNV, tenKH, tool.dinhDangLocalDate(hd.getNgayLap()), tool.dinhDangVND(tongTien) });
        }
        capNhatTenNutXoaHoanTac();
    }

    public void moLaiForm() {
        tblChuaXoa = true;
        gui.getBtnLichSuXoa().setText("Lịch sử xóa");
        gui.getBtnXoaHoanTac().setText("Xóa");
        listHD = new ArrayList<>(hdService.layListHoaDon());
        listHDDaXoa = new ArrayList<>(hdService.layListHDDaXoa());
        capNhatBang(listHD);
        gui.getTxtKhachHang().setText("");
        gui.getTxtTenNV().setText("");
        gui.getTblHoaDon().clearSelection();
    }
}