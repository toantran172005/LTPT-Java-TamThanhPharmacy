package controller;

import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import entity.NhanVien;
import gui.ChiTietNhanVien_GUI;
import gui.TimKiemNV_GUI;
import gui.TrangChuQL_GUI;
import service.NhanVienService; // Nâng cấp: Gọi Service thay vì DAO
import utils.ToolCtrl;

public class TimKiemNhanVienController {

    public TimKiemNV_GUI gui;
    public NhanVienService nvService = new NhanVienService(); // Đã đổi sang Service
    public ToolCtrl tool = new ToolCtrl();
    public TrangChuQL_GUI trangChuQL;

    public boolean tblChuaXoa = true;
    public ArrayList<NhanVien> listNV;
    public ArrayList<NhanVien> listNVDaXoa;

    public TimKiemNhanVienController(TimKiemNV_GUI gui) {
        super();
        this.gui = gui;
        this.trangChuQL = gui.getMainFrame();

        // Service đã tự động ép kiểu về ArrayList để tránh lỗi Cast
        listNV = nvService.layNhanVienDangLam();
        listNVDaXoa = nvService.layNhanVienNghiLam();

        capNhatBang(listNV);
        suKien();

        gui.addAncestorListener(new javax.swing.event.AncestorListener() {
            @Override
            public void ancestorAdded(javax.swing.event.AncestorEvent event) {
                moLaiForm();
            }

            @Override
            public void ancestorRemoved(javax.swing.event.AncestorEvent event) {
            }

            @Override
            public void ancestorMoved(javax.swing.event.AncestorEvent event) {
            }
        });
    }

    public void suKien() {
        ActionListener setAction = e -> locNhanVien();
        gui.getTxtTenNV().addActionListener(setAction);
        gui.getTxtSdt().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                locNhanVien();
            }
        });
        gui.txtTenNV.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                locNhanVien();
            }
        });

        gui.getBtnLamMoi().addActionListener(e -> lamMoiBang());
        gui.getBtnXemChiTiet().addActionListener(e -> xemChiTiet());
        gui.getBtnLichSuXoa().addActionListener(e -> xemLichSuXoa());
        gui.getBtnXoaHoanTac().addActionListener(e -> xoaHoacHoanTac());

        gui.getTblNhanVien().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    capNhatTenNutXoaHoanTac();
                }
            }
        });
    }

    // ========== TÌM KIẾM NHÂN VIÊN ==========
    public void locNhanVien() {
        String sdt = gui.getTxtSdt().getText();
        String tenNV = gui.getTxtTenNV().getText().trim().toLowerCase();
        List<NhanVien> danhSach = tblChuaXoa ? listNV : listNVDaXoa;

        List<NhanVien> ketQua = danhSach.stream()
                .filter(nv -> tenNV.isEmpty() || (nv.getTenNV() != null && nv.getTenNV().toLowerCase().contains(tenNV)))
                .filter(nv -> sdt.isEmpty()
                        || (nv.getSdt() != null && tool.chuyenSoDienThoai(nv.getSdt()).contains(sdt)))
                .collect(Collectors.toList());

        if (ketQua.isEmpty()) {
            capNhatBang(new ArrayList<>(danhSach));
        } else {
            capNhatBang(new ArrayList<>(ketQua));
        }
    }

    // ========== XEM CHI TIẾT ==========
    public void xemChiTiet() {
        int row = gui.getTblNhanVien().getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn một nhân viên để xem chi tiết!");
            return;
        }

        String maNV = gui.getTblNhanVien().getValueAt(row, 0).toString().trim();
        NhanVien nv = nvService.timNhanVienTheoMa(maNV);

        if (nv == null) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy thông tin nhân viên '" + maNV + "'");
            return;
        }

        ChiTietNhanVien_GUI chiTietPanel;
        if (trangChuQL != null) {
            chiTietPanel = new ChiTietNhanVien_GUI(trangChuQL);
            trangChuQL.setUpNoiDung(chiTietPanel);
        } else
            return;

        chiTietPanel.getCtrl().setNhanVienHienTai(nv);
    }

    // ========== XEM LỊCH SỬ XÓA ==========
    public void xemLichSuXoa() {
        tblChuaXoa = !tblChuaXoa;
        gui.getBtnLichSuXoa().setText(tblChuaXoa ? "Lịch sử xoá" : "Danh sách hiện tại");
        capNhatBang(tblChuaXoa ? listNV : listNVDaXoa);
        gui.getTxtTenNV().setText("");
        gui.getTxtSdt().setText("");
        gui.getTblNhanVien().clearSelection();
        capNhatTenNutXoaHoanTac();
    }

    // ========== XÓA VÀ HOÀN TÁC ==========
    public void xoaHoacHoanTac() {
        int row = gui.getTblNhanVien().getSelectedRow();
        if (row == -1) {
            tool.hienThiThongBao("Thông báo", "Vui lòng chọn một nhân viên!", false);
            return;
        }

        String maNV = (String) gui.getTblNhanVien().getValueAt(row, 0);
        NhanVien nhanVien = nvService.timNhanVienTheoMa(maNV);
        if (nhanVien == null) {
            tool.hienThiThongBao("Lỗi", "Không tìm thấy nhân viên!", false);
            return;
        }

        boolean daXoa = !nhanVien.getTrangThai();
        boolean dangXemLichSu = !tblChuaXoa;

        String action = dangXemLichSu ? "khôi phục" : (daXoa ? "hoàn tác" : "xóa");

        if (JOptionPane.showConfirmDialog(gui, "Bạn có chắc muốn " + action + " nhân viên này không?", "Xác nhận",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
            return;

        try {
            if (dangXemLichSu || daXoa) {
                nvService.khoiPhucNhanVien(maNV);
            } else {
                nvService.xoaNhanVien(maNV);
            }

            listNV = nvService.layNhanVienDangLam();
            listNVDaXoa = nvService.layNhanVienNghiLam();

            capNhatBang(tblChuaXoa ? listNV : listNVDaXoa);

            tool.hienThiThongBao("Thành công", "Đã " + action + " nhân viên!", true);
        } catch (Exception e) {
            tool.hienThiThongBao("Lỗi", e.getMessage(), false);
        }
    }

    // ========== CẬP NHẬT TÊN NÚT XÓA VÀ HOÀN TÁC ==========
    public void capNhatTenNutXoaHoanTac() {
        int row = gui.getTblNhanVien().getSelectedRow();

        if (row == -1) {
            gui.getBtnXoaHoanTac().setText(tblChuaXoa ? "Xóa" : "Khôi phục");
            return;
        }

        String maNV = (String) gui.getTblNhanVien().getValueAt(row, 0);
        NhanVien nv = nvService.timNhanVienTheoMa(maNV);

        if (nv == null) {
            gui.getBtnXoaHoanTac().setText(tblChuaXoa ? "Xóa" : "Khôi phục");
            return;
        }

        boolean daXoa = !nv.getTrangThai();
        boolean dangXemLichSu = !tblChuaXoa;

        if (dangXemLichSu) {
            gui.getBtnXoaHoanTac().setText("Khôi phục");
        } else {
            gui.getBtnXoaHoanTac().setText(daXoa ? "Hoàn tác" : "Xóa");
        }
    }

    // ========== MỞ LẠI FORM ==========
    public void moLaiForm() {
        tblChuaXoa = true;
        listNV = nvService.layNhanVienDangLam();
        listNVDaXoa = nvService.layNhanVienNghiLam();
        capNhatBang(listNV);
        gui.getBtnLichSuXoa().setText("Lịch sử xóa");
        gui.getBtnXoaHoanTac().setText("Xóa");
        gui.getTxtTenNV().setText("");
        gui.getTxtSdt().setText("");
        gui.getTblNhanVien().clearSelection();
    }

    // ========== LÀM MỚI BẢNG ==========
    public void lamMoiBang() {
        gui.getTxtSdt().setText("");
        gui.getTxtTenNV().setText("");
        capNhatBang(tblChuaXoa ? listNV : listNVDaXoa);
    }

    // ========== CẬP NHẬT BẢNG ==========
    public void capNhatBang(ArrayList<NhanVien> list) {
        DefaultTableModel model = (DefaultTableModel) gui.tblNhanVien.getModel();
        model.setRowCount(0);

        for (NhanVien nv : list) {
            Object[] row = { nv.getMaNV(), nv.getTenNV(), tool.chuyenSoDienThoai(nv.getSdt()),
                    nv.getGioiTinh() ? "Nam" : "Nữ", nv.getChucVu() };
            model.addRow(row);
        }
        capNhatTenNutXoaHoanTac();
    }
}