package controller;

import service.PhieuDoiTraService;
import entity.PhieuDoiTra;
import gui.*;
import utils.ToolCtrl;

import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TimKiemPhieuDoiTraController {
    public TimKiemPhieuDoiTra_GUI gui;
    // NÂNG CẤP: Thay DAO bằng Service
    public PhieuDoiTraService pdtService = new PhieuDoiTraService();
    public ToolCtrl tool = new ToolCtrl();
    public TrangChuQL_GUI trangChuQL;
    public TrangChuNV_GUI trangChuNV;

    public ArrayList<PhieuDoiTra> danhSachGoc;

    public TimKiemPhieuDoiTraController(TimKiemPhieuDoiTra_GUI gui) {
        this.gui = gui;
        this.trangChuQL = gui.getMainFrame();
        this.trangChuNV = gui.getMainFrameNV();

        loadDataTuCSDL();
        suKien();

        gui.addAncestorListener(new javax.swing.event.AncestorListener() {
            @Override
            public void ancestorAdded(javax.swing.event.AncestorEvent event) {
                loadDataTuCSDL();
            }
            @Override
            public void ancestorRemoved(javax.swing.event.AncestorEvent event) {}
            @Override
            public void ancestorMoved(javax.swing.event.AncestorEvent event) {}
        });
    }

    // Hàm riêng để tải từ DB qua Service và Ép kiểu về ArrayList
    public void loadDataTuCSDL() {
        List<PhieuDoiTra> resultList = pdtService.layListPDT();
        // Ép kiểu List thành ArrayList để phù hợp cấu trúc cũ
        danhSachGoc = (resultList instanceof ArrayList) ? (ArrayList<PhieuDoiTra>) resultList : new ArrayList<>(resultList);
        capNhatBang(danhSachGoc);
    }

    public void suKien() {
        KeyAdapter searchAction = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                locPhieuDoiTra();
            }
        };

        gui.getTxtKhachHang().addKeyListener(searchAction);
        gui.getTxtTenNV().addKeyListener(searchAction);

        gui.getBtnTimKiem().addActionListener(e -> locPhieuDoiTra());
        gui.getBtnLamMoi().addActionListener(e -> lamMoiBang());
        gui.getBtnChiTiet().addActionListener(e -> xemChiTiet());
    }

    // ========== TỐI ƯU: LỌC TRÊN RAM (LOCAL) ==========
    public void locPhieuDoiTra() {
        String tenKH = gui.getTxtKhachHang().getText().trim().toLowerCase();
        String tenNV = gui.getTxtTenNV().getText().trim().toLowerCase();

        if (danhSachGoc == null) return;

        List<PhieuDoiTra> listLoc = danhSachGoc.stream().filter(pdt -> {
            boolean matchKH = true;
            boolean matchNV = true;

            // Kiểm tra tên khách hàng (nếu có nhập)
            if (!tenKH.isEmpty()) {
                String ten = pdt.getHoaDon().getKhachHang().getTenKH();
                matchKH = (ten != null && ten.toLowerCase().contains(tenKH));
            }

            // Kiểm tra tên nhân viên (nếu có nhập)
            if (!tenNV.isEmpty()) {
                String ten = pdt.getNhanVien().getTenNV();
                matchNV = (ten != null && ten.toLowerCase().contains(tenNV));
            }

            return matchKH && matchNV;
        }).collect(Collectors.toList());

        capNhatBang(listLoc);
    }

    public void lamMoiBang() {
        gui.getTxtKhachHang().setText("");
        gui.getTxtTenNV().setText("");
        loadDataTuCSDL();
    }

    public void xemChiTiet() {
        int row = gui.getTblPhieuDoiTra().getSelectedRow();
        if (row == -1) {
            tool.hienThiThongBao("Thông báo", "Vui lòng chọn một phiếu đổi trả!", false);
            return;
        }

        String maPDT = (String) gui.getTblPhieuDoiTra().getValueAt(row, 0);
        PhieuDoiTra pdt = pdtService.timPhieuDoiTraTheoMa(maPDT);

        ChiTietPhieuDoiTra_GUI chiTietPanel;
        if (trangChuQL != null) {
            chiTietPanel = new ChiTietPhieuDoiTra_GUI(trangChuQL);
            trangChuQL.setUpNoiDung(chiTietPanel);
        } else if (trangChuNV != null) {
            chiTietPanel = new ChiTietPhieuDoiTra_GUI(trangChuNV);
            trangChuNV.setUpNoiDung(chiTietPanel);
        } else return;

        chiTietPanel.getCtrl().hienThiThongTinPhieuDT(pdt);
    }

    public void capNhatBang(List<PhieuDoiTra> list) {
        DefaultTableModel model = (DefaultTableModel) gui.getTblPhieuDoiTra().getModel();
        model.setRowCount(0);

        for (PhieuDoiTra pdt : list) {
            model.addRow(new Object[] {
                    pdt.getMaPhieuDT(),
                    pdt.getNhanVien().getTenNV(),
                    pdt.getHoaDon().getKhachHang().getTenKH(),
                    tool.dinhDangLocalDate(pdt.getNgayDoiTra()),
                    pdt.getLyDo()
            });
        }
    }
}