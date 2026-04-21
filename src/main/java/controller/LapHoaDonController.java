package controller;

import service.*; // Import toàn bộ các lớp Service
import entity.*;
import gui.ChiTietHoaDon_GUI;
import gui.LapHoaDon_GUI;
import gui.TrangChuNV_GUI;
import gui.TrangChuQL_GUI;
import utils.ToolCtrl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LapHoaDonController {
    public LapHoaDon_GUI gui;
    public ToolCtrl tool = new ToolCtrl();
    public TrangChuQL_GUI trangChuQL;
    public TrangChuNV_GUI trangChuNV;

    // Đổi toàn bộ DAO thành Service
    public ThuocService thuocService = new ThuocService();
    public KhachHangService khService = new KhachHangService();
    public NhanVienService nvService = new NhanVienService();
    public DonViTinhService dvtService = new DonViTinhService();
    public KhuyenMaiService kmService = new KhuyenMaiService();
    public HoaDonService hdService = new HoaDonService();
    public PhieuDatHangService pdhService = new PhieuDatHangService();

    public List<KhachHang> dsKhachHang;
    public List<Thuoc> dsThuoc;
    public DefaultTableModel tableModel;
    public boolean dangSetTenKH = false;
    public boolean dangSetSdtKH = false;
    public boolean lapTuPhieuDatHang = false;
    public boolean daLapHoaDon = false;

    public LapHoaDonController() {
        this(null);
    }

    public LapHoaDonController(LapHoaDon_GUI gui) {
        this.gui = gui;
        if (gui == null) return;
        this.trangChuQL = gui.getMainFrame();
        this.trangChuNV = gui.getMainFrameNV();
        this.tableModel = (DefaultTableModel) gui.getTblThuoc().getModel();
        loadData();
    }

    // ========== TẢI DỮ LIỆU ==========
    public void loadData() {
        taiDuLieu();
        suKien();
        goiYKhachHang();
        goiYThuoc();
        setComboxQuocGia();
    }

    // ========== SỰ KIỆN ==========
    public void suKien() {
        resetActionListener(gui.getBtnTaoHD());
        resetActionListener(gui.getBtnThem());
        resetActionListener(gui.getBtnXoa());
        resetActionListener(gui.getBtnLamMoi());

        gui.getBtnThem().addActionListener(e -> xuLyThemThuocVaoBang());
        gui.getBtnXoa().addActionListener(e -> xuLyXoaDong());
        gui.getBtnLamMoi().addActionListener(e -> lamMoi());
        gui.getBtnTaoHD().addActionListener(e -> xuLyXuatHoaDon());
        gui.getCmbHTThanhToan().addActionListener(e -> {
            String hinhThuc = gui.getCmbHTThanhToan().getSelectedItem().toString();

            if (hinhThuc.equalsIgnoreCase("Tiền mặt")) {
                gui.getTxtTienNhan().setEditable(true);
                gui.getTxtTienNhan().setText("");
                gui.getTxtTienNhan().requestFocus();
                tinhTienThua();
            } else {
                gui.getTxtTienNhan().setEditable(false);
                String tongTien = gui.getLblTongTien().getText().trim();
                gui.getTxtTienNhan().setText(tongTien);
            }
        });
        gui.getTxtTienNhan().addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { tinhTienThua(); }
        });

        gui.getCmbSanPham().addActionListener(e -> setComboxQuocGia());
    }

    public void resetActionListener(AbstractButton btn) {
        for (ActionListener al : btn.getActionListeners()) {
            btn.removeActionListener(al);
        }
    }

    // ========== SETUP COMBOX ==========
    public void setComboxQuocGia() {
        Object selected = gui.getCmbSanPham().getSelectedItem();
        if (selected == null) return;

        String tenThuoc = selected.toString();
        gui.getCmbQuocGia().removeAllItems();

        ArrayList<QuocGia> listQG = new ArrayList<>(thuocService.layListQuocGiaTheoThuoc(tenThuoc));
        if (listQG != null) {
            for (QuocGia qg : listQG) gui.getCmbQuocGia().addItem(qg.getTenQuocGia());
        }

        String maThuoc = thuocService.layMaThuocTheoTen(tenThuoc);
        String donVi = thuocService.layTenDonViTinhTheoMaThuoc(maThuoc);
        if (donVi == null) {
            gui.cmbDonVi.setSelectedIndex(-1);
            return;
        }
        gui.cmbDonVi.setSelectedItem(donVi);
    }

    // ========== TẢI DỮ LIỆU ==========
    public void taiDuLieu() {
        dsKhachHang = new ArrayList<>(khService.layListKhachHang());
        dsThuoc = new ArrayList<>(thuocService.layListThuoc(true));

        gui.getCmbSanPham().removeAllItems();
        gui.getCmbSanPham().addItem("");
        for (Thuoc t : dsThuoc) gui.getCmbSanPham().addItem(t.getTenThuoc());

        List<DonViTinh> dsDVT = new ArrayList<>(dvtService.layListDVT());
        gui.getCmbDonVi().removeAllItems();
        for (DonViTinh dvt : dsDVT) gui.getCmbDonVi().addItem(dvt.getTenDVT());
        if (!dsDVT.isEmpty()) gui.getCmbDonVi().setSelectedIndex(0);

        gui.getCmbHTThanhToan().setSelectedItem("Tiền mặt");
    }

    // ========== TÌM KHÁCH HÀNG ==========
    public void goiYKhachHang() {
        gui.getTxtTenKH().addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (dangSetTenKH || gui.getTxtTenKH().getText().trim().isEmpty()) return;
                String input = gui.getTxtTenKH().getText().trim().toLowerCase();
                List<KhachHang> ketQua = dsKhachHang.stream().filter(kh -> kh.getTenKH().toLowerCase().contains(input)).limit(5).toList();
                if (!ketQua.isEmpty()) hienThiListKhachHang(gui.getTxtTenKH(), ketQua, true);
            }
        });

        gui.getTxtSdt().addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (dangSetSdtKH || gui.getTxtSdt().getText().trim().isEmpty()) return;
                String input = gui.getTxtSdt().getText().trim();
                List<KhachHang> ketQua = dsKhachHang.stream().filter(kh -> tool.chuyenSoDienThoai(kh.getSdt()).contains(input)).limit(5).toList();
                if (!ketQua.isEmpty()) hienThiListKhachHang(gui.getTxtSdt(), ketQua, false);
            }
        });
    }

    // ========== HIỂN THỊ KHÁCH HÀNG ĐÃ CÓ ==========
    public void hienThiListKhachHang(JTextField tf, List<KhachHang> list, boolean isTen) {
        JPopupMenu pop = new JPopupMenu();
        for (KhachHang kh : list) {
            String text = isTen ? kh.getTenKH() + " - " + tool.chuyenSoDienThoai(kh.getSdt())
                    : tool.chuyenSoDienThoai(kh.getSdt()) + " - " + kh.getTenKH();
            JMenuItem item = new JMenuItem(text);
            item.addActionListener(e -> {
                if (isTen) {
                    dangSetSdtKH = true;
                    gui.getTxtTenKH().setText(kh.getTenKH());
                    gui.getTxtSdt().setText(tool.chuyenSoDienThoai(kh.getSdt()));
                    gui.getTxtTuoi().setText(String.valueOf(kh.getTuoi()));
                    gui.getTxtTuoi().setEditable(false);
                    dangSetSdtKH = false;
                } else {
                    dangSetTenKH = true;
                    gui.getTxtSdt().setText(tool.chuyenSoDienThoai(kh.getSdt()));
                    gui.getTxtTenKH().setText(kh.getTenKH());
                    gui.getTxtTuoi().setText(String.valueOf(kh.getTuoi()));
                    gui.getTxtTuoi().setEditable(false);
                    dangSetTenKH = false;
                }
                pop.setVisible(false);
            });
            pop.add(item);
        }
        SwingUtilities.invokeLater(() -> pop.show(tf, 0, tf.getHeight()));
    }

    // ========== THÊM THUỐC XUỐNG BẢNG ==========
    public void xuLyThemThuocVaoBang() {
        String tenThuoc = gui.getCmbSanPham().getEditor().getItem().toString().trim();
        if (tenThuoc.isEmpty()) {
            tool.hienThiThongBao("Lỗi", "Vui lòng chọn thuốc!", false);
            return;
        }

        String tenQG = (String) gui.getCmbQuocGia().getSelectedItem();
        if (tenQG == null || tenQG.isEmpty()) {
            tool.hienThiThongBao("Lỗi", "Vui lòng chọn quốc gia!", false);
            return;
        }

        String maThuoc = thuocService.layMaThuocTheoTenVaQG(tenThuoc, tenQG);
        Thuoc thuoc = thuocService.timThuocTheoMa(maThuoc);
        if (thuoc == null) {
            tool.hienThiThongBao("Lỗi", "Không tìm thấy thuốc!", false);
            return;
        }

        int slNhap;
        try {
            slNhap = Integer.parseInt(gui.getTxtSoLuong().getText().trim());
            if (slNhap <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            tool.hienThiThongBao("Lỗi", "Số lượng phải lớn hơn 0!", false);
            return;
        }

        int tonKho = thuocService.laySoLuongTon(thuoc.getMaThuoc());
        DefaultTableModel model = (DefaultTableModel) gui.getTblThuoc().getModel();
        int slDangCoTrenBang = 0;
        int rowIndex = -1;

        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 1).toString().equalsIgnoreCase(thuoc.getTenThuoc())) {
                slDangCoTrenBang = Integer.parseInt(model.getValueAt(i, 3).toString());
                rowIndex = i;
                break;
            }
        }

        if (slNhap + slDangCoTrenBang > tonKho) {
            tool.hienThiThongBao("Lỗi", "Tồn kho không đủ! (Tồn: " + tonKho + ", Đã nhập: " + slDangCoTrenBang + ")", false);
            return;
        }

        String tenDVT = (String) gui.getCmbDonVi().getSelectedItem();
        DonViTinh dvt = dvtService.timTheoTen(tenDVT);

        double donGiaGoc = thuoc.getGiaBan();
        double donGiaSauKM = donGiaGoc;
        double thanhTienDotNay = 0;
        int slThucTeVaoBang = slNhap;

        String moTaKM = "Không có KM";
        String maKM = thuocService.layMaKMTheoMaThuoc(thuoc.getMaThuoc());
        LocalDate homNay = LocalDate.now();

        boolean coKM = false;
        if (maKM != null && !maKM.isEmpty()) {
            KhuyenMai km = kmService.layKhuyenMaiTheoMa(maKM);
            if (km != null && !homNay.isBefore(km.getNgayBD()) && !homNay.isAfter(km.getNgayKT())) {
                coKM = true;
                if (km.getLoaiKM().equalsIgnoreCase("giảm giá")) {
                    double mucGiam = km.getMucKM();
                    donGiaSauKM = donGiaGoc * (1 - mucGiam / 100.0);
                    thanhTienDotNay = donGiaSauKM * slNhap;
                    moTaKM = "Giảm " + mucGiam + "%";
                } else if (km.getLoaiKM().equalsIgnoreCase("mua tặng")) {
                    int soLuongTang = (slNhap / km.getSoLuongMua()) * km.getSoLuongTang();
                    if (soLuongTang > 0) {
                        slThucTeVaoBang = slNhap + soLuongTang;
                        moTaKM = String.format("Mua %d tặng %d (Tặng thêm: %d)", km.getSoLuongMua(), km.getSoLuongTang(), soLuongTang);
                    }
                    donGiaSauKM = donGiaGoc;
                    thanhTienDotNay = donGiaGoc * slNhap;
                }
            }
        }

        if (!coKM) thanhTienDotNay = donGiaGoc * slNhap;

        if (rowIndex != -1) {
            int tongSoLuongMoi = slDangCoTrenBang + slThucTeVaoBang;
            double tienCuTrongBang = tool.chuyenTienSangSo(model.getValueAt(rowIndex, 7).toString());
            double tongTienMoi = tienCuTrongBang + thanhTienDotNay;

            model.setValueAt(tongSoLuongMoi, rowIndex, 3);
            model.setValueAt(tool.dinhDangVND(tongTienMoi), rowIndex, 7);

            if (moTaKM.contains("Tặng thêm")) model.setValueAt(moTaKM, rowIndex, 8);
        } else {
            model.addRow(new Object[] { model.getRowCount() + 1, thuoc.getTenThuoc(), tenQG, slThucTeVaoBang,
                    dvt != null ? dvt.getTenDVT() : "", tool.dinhDangVND(donGiaGoc), tool.dinhDangVND(donGiaSauKM),
                    tool.dinhDangVND(thanhTienDotNay), moTaKM, "Xóa" });
        }

        tinhTongTien();
        resetFormNhap();
    }

    public void resetFormNhap() {
        gui.getTxtSoLuong().setText("");
        gui.getCmbSanPham().setSelectedIndex(0);
        gui.getCmbDonVi().setSelectedIndex(0);
    }

    public void xuLyXoaDong() {
        JTable table = gui.getTblThuoc();
        int row = table.getSelectedRow();

        if (row == -1) {
            tool.hienThiThongBao("Thông báo", "Vui lòng chọn dòng cần xóa!", false);
            return;
        }

        boolean confirm = tool.hienThiXacNhan("Xác nhận", "Bạn có chắc chắn muốn xoá dòng này?", null);
        if (confirm) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.removeRow(row);
            for (int i = 0; i < model.getRowCount(); i++) model.setValueAt(i + 1, i, 0);
            tinhTongTien();
        }
    }

    public void capNhatSTT() {
        for (int i = 0; i < tableModel.getRowCount(); i++) tableModel.setValueAt(i + 1, i, 0);
    }

    public void tinhTongTien() {
        double tong = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Object giaTri = tableModel.getValueAt(i, 7);
            if (giaTri != null) {
                String text = giaTri.toString().trim();
                tong += tool.chuyenTienSangSo(text);
            }
        }
        gui.getLblTongTien().setText(tool.dinhDangVND(tong));
    }

    public void tinhTienThua() {
        if (!"Tiền mặt".equals(gui.getCmbHTThanhToan().getSelectedItem())) {
            gui.getLblTienThua().setText("0 VNĐ");
            return;
        }
        try {
            double tong = tool.chuyenTienSangSo(gui.getLblTongTien().getText());
            double nhan = tool.chuyenTienSangSo(gui.getTxtTienNhan().getText());
            double tienThua = nhan - tong;
            if (tienThua < 0) tienThua = 0;
            gui.getLblTienThua().setText(tool.dinhDangVND(tienThua));
        } catch (Exception e) {
            gui.getLblTienThua().setText("0 VNĐ");
        }
    }

    public void lamMoi() {
        lapTuPhieuDatHang = false;
        gui.getTxtSdt().setText("");
        gui.getTxtTenKH().setText("");
        gui.getTxtTuoi().setText("");
        gui.getTxtTuoi().setEditable(true);
        gui.getTxtSoLuong().setText("");
        gui.getTxtTienNhan().setText("");
        gui.getCmbSanPham().setSelectedItem("");
        tableModel.setRowCount(0);
        tinhTongTien();
    }

    public boolean ktTenKhachHangHopLe() {
        String ten = gui.txtTenKH.getText().trim();
        String regex = "^[\\p{L}\\s]+$";
        if (ten.isEmpty()) {
            tool.hienThiThongBao("Tên khách hàng không hợp lệ!", "Tên không được để trống", false);
            gui.txtTenKH.requestFocus();
            return false;
        } else if (!ten.matches(regex)) {
            tool.hienThiThongBao("Tên khách hàng không hợp lệ!", "Tên không được chứa số hoặc ký tự đặc biệt", false);
            gui.txtTenKH.requestFocus();
            gui.txtTenKH.selectAll();
            return false;
        }
        gui.txtSdt.requestFocus();
        return true;
    }

    public boolean ktSoDienThoaiHopLe() {
        String sdt = gui.txtSdt.getText().trim();
        String regex = "^0\\d{9}$";
        if (sdt.isEmpty()) {
            tool.hienThiThongBao("Số điện thoại không hợp lệ!", "Không được để trống", false);
            gui.txtSdt.requestFocus();
            return false;
        } else if (!sdt.matches(regex)) {
            tool.hienThiThongBao("Số điện thoại không hợp lệ!", "Phải gồm 10 chữ số và bắt đầu bằng 0", false);
            gui.txtSdt.requestFocus();
            gui.txtSdt.selectAll();
            return false;
        }
        gui.txtTuoi.requestFocus();
        return true;
    }

    public boolean ktTuoiHopLe() {
        String tuoiStr = gui.txtTuoi.getText().trim();
        try {
            int tuoi = Integer.parseInt(tuoiStr);
            if (tuoi < 0) {
                tool.hienThiThongBao("Tuổi không hợp lệ!", "Tuổi không được là số âm.", false);
                gui.txtTuoi.requestFocus();
                gui.txtTuoi.selectAll();
                return false;
            }
        } catch (NumberFormatException e) {
            tool.hienThiThongBao("Tuổi không hợp lệ!", "Tuổi phải là số nguyên.", false);
            gui.txtTuoi.requestFocus();
            gui.txtTuoi.selectAll();
            return false;
        }
        return true;
    }

    // ========== XUẤT/LƯU HOÁ ĐƠN THEO CHUẨN JPA ==========
    public void xuLyXuatHoaDon() {
        if (daLapHoaDon) return;

        try {
            // ==== 1. Kiểm tra dữ liệu ====
            if (tableModel.getRowCount() == 0) {
                tool.hienThiThongBao("Thông báo", "Chưa có thuốc trong hóa đơn!", false);
                return;
            }

            if (ktSoDienThoaiHopLe() && ktTenKhachHangHopLe() && ktTuoiHopLe()) {
                String tenKH = gui.getTxtTenKH().getText().trim();
                String sdt = gui.getTxtSdt().getText().trim();
                String tuoiText = gui.getTxtTuoi().getText().trim();
                String hinhThucTT = (String) gui.getCmbHTThanhToan().getSelectedItem();

                // ==== 2. Xử lý khách hàng (Dùng Service) ====
                String sdtChuan = tool.chuyenSoDienThoai(sdt);
                KhachHang kh = khService.timKhachHangTheoSDT(sdtChuan);
                String maKH;

                if (kh == null) {
                    maKH = tool.taoKhoaChinh("KH");
                    boolean themKH = khService.themKhachHang(maKH, tenKH, sdtChuan, String.valueOf(Integer.parseInt(tuoiText)));
                    if (!themKH) {
                        tool.hienThiThongBao("Lỗi", "Không thể thêm khách hàng mới!", false);
                        return;
                    }
                } else {
                    maKH = kh.getMaKH();
                }

                // ==== 3. Tạo hóa đơn (Truyền vào Entity) ====
                String maHD = tool.taoKhoaChinh("HD");
                String maNV;
                if (trangChuNV != null) {
                    NhanVien nv = trangChuNV.layNhanVien();
                    maNV = nv.getMaNV();
                } else {
                    NhanVien nv = trangChuQL.layNhanVien();
                    maNV = nv.getMaNV();
                }
                String diaChiHT = "456 Nguyễn Huệ, TP.HCM";
                String tenHT = "Hiệu Thuốc Tâm Thanh";
                String hotline = "+84-912345689";

                double tienNhan = 0;
                double tienCanTra = tool.chuyenTienSangSo(gui.getLblTongTien().getText());

                if (hinhThucTT.equalsIgnoreCase("Tiền mặt")) {
                    String tienNhanStr = gui.getTxtTienNhan().getText().trim().replace(",", "");
                    if (tienNhanStr.isEmpty()) {
                        tool.hienThiThongBao("Lỗi", "Tiền nhận không được để trống", false);
                        return;
                    }
                    try {
                        tienNhan = Double.parseDouble(tienNhanStr);
                        if (tienNhan < tienCanTra) {
                            tool.hienThiThongBao("Lỗi", "Tiền nhận không đủ để thanh toán!", false);
                            return;
                        }
                    } catch (NumberFormatException e) {
                        tool.hienThiThongBao("Lỗi!", "Số tiền nhận không hợp lệ!", false);
                        return;
                    }
                } else {
                    tienNhan = tienCanTra;
                }

                KhachHang khHD = khService.timKhachHangTheoMa(maKH);
                NhanVien nv = nvService.timNhanVienTheoMa(maNV);
                StringBuilder ghiChu = new StringBuilder();
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    Object tenThuoc = tableModel.getValueAt(i, 1);
                    Object ghiChuKM = tableModel.getValueAt(i, 8);
                    if (ghiChuKM != null && !ghiChuKM.toString().isBlank()) {
                        ghiChu.append(tenThuoc).append(" : ").append(ghiChuKM).append("; ");
                    }
                }

                HoaDon hd = new HoaDon(maHD, khHD, nv, hinhThucTT, LocalDate.now(), diaChiHT, tenHT, ghiChu.toString(), hotline, tienNhan, true);

                if (!hdService.themHoaDon(hd)) {
                    tool.hienThiThongBao("Lỗi", "Không thể tạo hóa đơn!", false);
                    return;
                }

                // ==== 4. Thêm chi tiết hóa đơn (Chuẩn Object Mapping) ====
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    String tenThuoc = tableModel.getValueAt(i, 1).toString();
                    Thuoc t = dsThuoc.stream().filter(x -> x.getTenThuoc().equals(tenThuoc)).findFirst().orElse(null);

                    if (t == null) continue;

                    int soLuong = Integer.parseInt(tableModel.getValueAt(i, 3).toString());
                    double donGia = tool.chuyenTienSangSo(tableModel.getValueAt(i, 5).toString());
                    String tenDVT = tableModel.getValueAt(i, 4).toString();
                    String maDVT = dvtService.timMaDVTTheoTen(tenDVT);

                    // Tạo đối tượng CT_HoaDon theo Entity để giao tiếp với JPA
                    CTHoaDon ct = new CTHoaDon();

                    HoaDon hdProxy = new HoaDon(); hdProxy.setMaHD(maHD);
                    Thuoc thuocProxy = new Thuoc(); thuocProxy.setMaThuoc(t.getMaThuoc());
                    DonViTinh dvtProxy = new DonViTinh(); dvtProxy.setMaDVT(maDVT);

                    ct.setHoaDon(hdProxy);
                    ct.setThuoc(thuocProxy);
                    ct.setDonViTinh(dvtProxy);
                    ct.setSoLuong(soLuong);
                    ct.setDonGia(donGia);

                    // Lưu qua Service thay vì DAO
                    hdService.themChiTietHoaDon(ct);
                    thuocService.giamSoLuongTon(t.getMaThuoc(), maDVT, soLuong);
                }

                // ==== 5. Thông báo thành công & làm mới giao diện ====
                tool.hienThiThongBao("Thành công", "Xuất hóa đơn thành công!", true);
                daLapHoaDon = true;
                lapTuPhieuDatHang = false;

                HoaDon hoaDon = hdService.timHoaDonTheoMa(maHD);

                ChiTietHoaDon_GUI chiTietPanel;
                if (trangChuQL != null) {
                    chiTietPanel = new ChiTietHoaDon_GUI(trangChuQL);
                    trangChuQL.setUpNoiDung(chiTietPanel);
                } else if (trangChuNV != null) {
                    chiTietPanel = new ChiTietHoaDon_GUI(trangChuNV);
                    trangChuNV.setUpNoiDung(chiTietPanel);
                } else return;

                chiTietPanel.getCtrl().hienThiThongTinHoaDon(hoaDon);
                lamMoi();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            tool.hienThiThongBao("Lỗi", "Đã xảy ra lỗi khi xuất hóa đơn!", false);
        }
    }

//    public void loadTuPhieuDatHang(String maPDH) {
//        lapTuPhieuDatHang = true;
//
//        PhieuDatHang pdh = pdhService.timTheoMa(maPDH);
//        if (pdh == null) {
//            tool.hienThiThongBao("Lỗi", "Không tìm thấy phiếu đặt hàng!", false);
//            return;
//        }
//
//        KhachHang kh = pdh.getKhachHang();
//        gui.getTxtTenKH().setText(kh.getTenKH());
//        gui.getTxtSdt().setText(tool.chuyenSoDienThoai(kh.getSdt()));
//        gui.getTxtTuoi().setText(String.valueOf(kh.getTuoi()));
//        gui.getTxtTuoi().setEditable(false);
//
//        DefaultTableModel model = (DefaultTableModel) gui.getTblThuoc().getModel();
//        model.setRowCount(0);
//
//        List<Object[]> chiTiet = new ArrayList<>(pdhService.layDanhSachThuocTheoPDH(maPDH));
//
//        for (Object[] ct : chiTiet) {
//            String maThuoc = ct[1].toString();
//            int sl = Integer.parseInt(ct[3].toString());
//            String tenQG = thuocService.timTenQGTheoMaThuoc(maThuoc);
//            String tenDVT = ct[5].toString();
//            double donGia = Double.parseDouble(ct[6].toString());
//            double thanhTien = Double.parseDouble(ct[7].toString());
//
//            double donGiaGoc = donGia;
//            double donGiaSauKM = donGiaGoc;
//            double mucGiam;
//            String moTaKM = "Không có KM";
//            String maKM = thuocService.layMaKMTheoMaThuoc(maThuoc);
//            LocalDate homNay = LocalDate.now();
//
//            if (maKM != null && !maKM.isEmpty()) {
//                KhuyenMai km = kmService.layKhuyenMaiTheoMa(maKM);
//                if (km != null && !homNay.isBefore(km.getNgayBD()) && !homNay.isAfter(km.getNgayKT())) {
//                    switch (km.getLoaiKM().toLowerCase()) {
//                        case "giảm giá":
//                            mucGiam = (double) km.getMucKM();
//                            donGiaSauKM = donGiaGoc * (1 - mucGiam / 100.0);
//                            moTaKM = "Giảm " + mucGiam + "%";
//                            break;
//                        case "mua tặng":
//                            int soLuongTang = (sl / km.getSoLuongMua()) * km.getSoLuongTang();
//                            if (soLuongTang > 0) {
//                                moTaKM = String.format("Mua %d tặng %d (Tặng: %d)", km.getSoLuongMua(), km.getSoLuongTang(), soLuongTang);
//                            }
//                            donGiaSauKM = donGiaGoc;
//                            break;
//                    }
//                }
//            }
//
//            model.addRow(new Object[] { model.getRowCount() + 1, ct[2], tenQG, sl, tenDVT, tool.dinhDangVND(donGia),
//                    tool.dinhDangVND(donGiaSauKM), tool.dinhDangVND(thanhTien), moTaKM, "Xóa" });
//        }
//        tinhTongTien();
//    }

    public void goiYThuoc() {
        JTextField txtTimThuoc = (JTextField) gui.getCmbSanPham().getEditor().getEditorComponent();

        txtTimThuoc.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_UP
                        || e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_LEFT
                        || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    return;
                }

                String input = txtTimThuoc.getText().trim().toLowerCase();
                if (input.isEmpty()) return;

                List<Thuoc> ketQua = dsThuoc.stream().filter(t -> t.getTenThuoc().toLowerCase().contains(input)).limit(10).toList();

                if (!ketQua.isEmpty()) hienThiListThuoc(txtTimThuoc, ketQua);
            }
        });
    }

    public void hienThiListThuoc(JTextField tf, List<Thuoc> list) {
        JPopupMenu pop = new JPopupMenu();

        for (Thuoc t : list) {
            JMenuItem item = new JMenuItem(t.getTenThuoc());
            item.addActionListener(e -> {
                tf.setText(t.getTenThuoc());
                gui.getCmbSanPham().setSelectedItem(t.getTenThuoc());
                setComboxQuocGia();
                pop.setVisible(false);
            });
            pop.add(item);
        }

        SwingUtilities.invokeLater(() -> {
            pop.show(tf, 0, tf.getHeight());
            tf.requestFocus();
        });
    }
}