package controller;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

// NÂNG CẤP: Dùng Service thay vì DAO
import service.DonViTinhService;
import service.KhachHangService;
import service.KhuyenMaiService;
import service.PhieuDatHangService;
import service.ThuocService;

import entity.DonViTinh;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.NhanVien;
import entity.PhieuDatHang;
import entity.QuocGia;
import entity.Thuoc;
import gui.ChiTietPhieuDatHang_GUI;
import gui.LapPhieuDatHang_GUI;
import utils.ToolCtrl;

public class LapPhieuDatHangController {

    public LapPhieuDatHang_GUI lpdhGUI;

    // NÂNG CẤP: Khởi tạo các Service
    public PhieuDatHangService pdhService = new PhieuDatHangService();
    public DonViTinhService dvtService = new DonViTinhService();
    public ThuocService thService = new ThuocService();
    public KhachHangService khService = new KhachHangService();
    public KhuyenMaiService kmService = new KhuyenMaiService();

    public List<DonViTinh> listDVT;
    public ArrayList<Thuoc> listThuoc;
    public ToolCtrl tool = new ToolCtrl();
    public ArrayList<KhachHang> listKH = (ArrayList<KhachHang>) khService.layListKhachHang();

    public LapPhieuDatHangController(LapPhieuDatHang_GUI lpdhGUI) {
        super();
        this.lpdhGUI = lpdhGUI;
    }

    public boolean ktTenKhachHangHopLe() {
        String ten = lpdhGUI.txtTenKH.getText().trim();
        String regex = "^[\\p{L}\\s]+$";

        if (ten.isEmpty()) {
            tool.hienThiThongBao("Tên khách hàng không hợp lệ!", "Tên không được để trống", false);
            lpdhGUI.txtTenKH.requestFocus();
            return false;
        } else if (!ten.matches(regex)) {
            tool.hienThiThongBao("Tên khách hàng không hợp lệ!", "Tên không được chứa số hoặc ký tự đặc biệt", false);
            lpdhGUI.txtTenKH.requestFocus();
            lpdhGUI.txtTenKH.selectAll();
            return false;
        }
        lpdhGUI.txtSdt.requestFocus();
        return true;
    }

    public boolean ktSoDienThoaiHopLe() {
        String sdt = lpdhGUI.txtSdt.getText().trim();
        String regex = "^0\\d{9}$";

        if (sdt.isEmpty()) {
            tool.hienThiThongBao("Số điện thoại không hợp lệ!", "Không được để trống", false);
            lpdhGUI.txtSdt.requestFocus();
            return false;
        } else if (!sdt.matches(regex)) {
            tool.hienThiThongBao("Số điện thoại không hợp lệ!", "Phải gồm 10 chữ số và bắt đầu bằng 0", false);
            lpdhGUI.txtSdt.requestFocus();
            lpdhGUI.txtSdt.selectAll();
            return false;
        }
        lpdhGUI.txtTuoi.requestFocus();
        return true;
    }

    public boolean ktTuoiHopLe() {
        String tuoiStr = lpdhGUI.txtTuoi.getText().trim();

        try {
            int tuoi = Integer.parseInt(tuoiStr);

            if (tuoi < 0) {
                tool.hienThiThongBao("Tuổi không hợp lệ!", "Tuổi không được là số âm.", false);
                lpdhGUI.txtTuoi.requestFocus();
                lpdhGUI.txtTuoi.selectAll();
                return false;
            }
        } catch (NumberFormatException e) {
            tool.hienThiThongBao("Tuổi không hợp lệ!", "Tuổi phải là số nguyên.", false);
            lpdhGUI.txtTuoi.requestFocus();
            lpdhGUI.txtTuoi.selectAll();
            return false;
        }
        return true;
    }

    public void taoPhieuDat() {
        if (ktSoDienThoaiHopLe() && ktTenKhachHangHopLe() && ktTuoiHopLe()) {
            String maPDH = tool.taoKhoaChinh("PDH");

            String sdtChuan = tool.chuyenSoDienThoai(lpdhGUI.txtSdt.getText());
            KhachHang kh = khService.timKhachHangTheoSDT(sdtChuan);
            String maKH;

            if (kh == null) {
                maKH = tool.taoKhoaChinh("KH");
                KhachHang khMoi = new KhachHang();
                khMoi.setMaKH(maKH);
                khMoi.setTenKH(lpdhGUI.txtTenKH.getText().trim());
                khMoi.setTuoi(Integer.parseInt(lpdhGUI.txtTuoi.getText().trim()));
                khMoi.setSdt(sdtChuan);
                boolean themKH = khService.themKhachHang(khMoi); // Đã chuẩn hoá theo Service
                if (!themKH) {
                    tool.hienThiThongBao("Lỗi", "Không thể thêm khách hàng mới!", false);
                    return;
                }
            } else {
                maKH = kh.getMaKH();
            }

            NhanVien nvDangNhap = null;
            if (lpdhGUI.getTrangChuQL() != null) {
                nvDangNhap = lpdhGUI.getTrangChuQL().layNhanVien();
            } else if (lpdhGUI.getTrangChuNV() != null) {
                nvDangNhap = lpdhGUI.getTrangChuNV().layNhanVien();
            }

            String maNV = nvDangNhap.getMaNV();
//            String maNV = "TTNV1";
            Date ngayDat = new Date();
            Date ngayHen = lpdhGUI.ngayHen.getDate();
            LocalDate localNgayDat = ngayDat.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate localNgayHen = (ngayHen != null) ? ngayHen.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
            String ghiChu = lpdhGUI.txaGhiChu.getText().trim();

            List<Object[]> dsChiTiet = new ArrayList<>();
            DefaultTableModel model = (DefaultTableModel) lpdhGUI.tblThuoc.getModel();

            if (model.getRowCount() == 0) {
                tool.hienThiThongBao("Lỗi", "Danh sách thuốc trống!", false);
                return;
            }

            for (int i = 0; i < model.getRowCount(); i++) {
                String maThuoc = model.getValueAt(i, 0).toString();
                int soLuong = Integer.parseInt(model.getValueAt(i, 4).toString());
                String tenDVT = model.getValueAt(i, 5).toString();

                String donGiaStr = model.getValueAt(i, 6).toString();
                double donGia = tool.chuyenTienSangSo(donGiaStr);

                String maDVT = dvtService.timMaDVTTheoTen(tenDVT);

                if (maDVT == null) {
                    tool.hienThiThongBao("Chi tiết phiếu đặt", "Đơn vị tính '" + tenDVT + "' không hợp lệ!", false);
                    return;
                }

                dsChiTiet.add(new Object[] { maThuoc, soLuong, maDVT, donGia });
            }

            int ketQua = pdhService.taoPhieuDatHangVaChiTiet(maPDH, maKH, maNV, localNgayDat, localNgayHen, ghiChu, dsChiTiet);

            switch (ketQua) {
                case 1:
                    tool.hienThiThongBao("Thêm phiếu đặt thuốc", "Thêm phiếu đặt thuốc thành công!", true);

                    PhieuDatHang pdh = pdhService.timTheoMa(maPDH);
                    if (pdh == null) {
                        tool.hienThiThongBao("Lỗi", "Không tìm thấy thông tin phiếu đặt!", false);
                        return;
                    }

                    ChiTietPhieuDatHang_GUI chiTiet;
                    if (lpdhGUI.getTrangChuQL() != null) {
                        chiTiet = new ChiTietPhieuDatHang_GUI(lpdhGUI.getTrangChuQL(), pdh);
                        lpdhGUI.getTrangChuQL().setUpNoiDung(chiTiet);
                    } else {
                        chiTiet = new ChiTietPhieuDatHang_GUI(lpdhGUI.getTrangChuNV(), pdh);
                        lpdhGUI.getTrangChuNV().setUpNoiDung(chiTiet);
                    }

                    lamMoi();
                    break;
                case 0:
                    tool.hienThiThongBao("Thêm phiếu đặt thuốc", "Không đủ tồn kho cho thuốc trong bảng!", false);
                    break;
                case -1:
                default:
                    tool.hienThiThongBao("Thêm phiếu đặt thuốc", "Đã xảy ra lỗi khi thêm phiếu đặt!", false);
                    break;
            }
        }
    }

    public void xoaThuoc() {
        int selectedRow = lpdhGUI.tblThuoc.getSelectedRow();

        if (selectedRow == -1) {
            tool.hienThiThongBao("Lỗi xóa thuốc", "Vui lòng chọn một dòng để xóa!", false);
            return;
        }

        boolean xacNhan = tool.hienThiXacNhan("Xác nhận", "Bạn có chắc muốn xóa thuốc này?", null);
        if (!xacNhan) {
            return;
        }

        DefaultTableModel model = (DefaultTableModel) lpdhGUI.tblThuoc.getModel();
        model.removeRow(selectedRow);

        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(i + 1, i, 0);
        }

        tool.hienThiThongBao("Thành công", "Đã xóa thuốc khỏi danh sách!", true);
    }

    public static String boDau(String s) {
        if (s == null)
            return "";
        return Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase().trim();
    }

    public void themVaoTable() {
        String sdt = lpdhGUI.txtSdt.getText().trim();
        String tenKH = lpdhGUI.txtTenKH.getText().trim();
        String tuoiStr = lpdhGUI.txtTuoi.getText().trim();
        String tenThuoc = (String) lpdhGUI.cmbSanPham.getSelectedItem();
        String donVi = (String) lpdhGUI.cmbDonVi.getSelectedItem();
        String soLuongStr = lpdhGUI.txtSoLuong.getText().trim();
        String tenQG = (String) lpdhGUI.getCmbQuocGia().getSelectedItem();
        java.util.Date ngayUtil = lpdhGUI.ngayHen.getDate();

        if (sdt.isEmpty() || tenKH.isEmpty() || tuoiStr.isEmpty() || tenThuoc == null || tenThuoc.isEmpty()
                || donVi == null || donVi.isEmpty() || soLuongStr.isEmpty() || ngayUtil == null) {
            tool.hienThiThongBao("Lỗi nhập liệu", "Vui lòng nhập đầy đủ thông tin trước khi thêm!", false);
            return;
        }

        int tuoi;
        try {
            tuoi = Integer.parseInt(tuoiStr);
            if (tuoi <= 0 || tuoi > 120)
                throw new NumberFormatException();
        } catch (NumberFormatException e) {
            tool.hienThiThongBao("Lỗi dữ liệu", "Tuổi phải là số nguyên hợp lệ (1–120)!", false);
            return;
        }

        int soLuongNhap;
        try {
            soLuongNhap = Integer.parseInt(soLuongStr);
            if (soLuongNhap <= 0)
                throw new NumberFormatException();
        } catch (NumberFormatException e) {
            tool.hienThiThongBao("Lỗi dữ liệu", "Số lượng phải là số nguyên dương!", false);
            return;
        }

        LocalDate ngayHen = ngayUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate today = LocalDate.now();

        if (ngayHen.isBefore(today)) {
            tool.hienThiThongBao("Lỗi ngày hẹn", "Ngày hẹn phải là hôm nay hoặc sau hôm nay!", false);
            return;
        }

        String maThuoc = thService.layMaThuocTheoTenVaQG(tenThuoc, tenQG);
        Thuoc thuoc = thService.timThuocTheoMa(maThuoc);

        if (thuoc == null) {
            tool.hienThiThongBao("Lỗi", "Không tìm thấy thông tin thuốc!", false);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) lpdhGUI.tblThuoc.getModel();
        int slHienTaiTrenBang = 0;
        int rowIndex = -1;

        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).toString().equals(maThuoc)) {
                slHienTaiTrenBang = Integer.parseInt(model.getValueAt(i, 4).toString());
                rowIndex = i;
                break;
            }
        }

        int tonKho = thService.laySoLuongTon(maThuoc);
        if (soLuongNhap + slHienTaiTrenBang > tonKho) {
            tool.hienThiThongBao("Lỗi",
                    "Tồn kho không đủ! (Tồn: " + tonKho + ", Đang có trong bảng: " + slHienTaiTrenBang + ")", false);
            return;
        }

        double donGiaGoc = thuoc.getGiaBan();
        double donGiaHienThi = donGiaGoc;
        double thanhTien = 0;
        int soLuongThucTe = soLuongNhap;
        String moTaKM = "Không có";
        String maKM = thService.layMaKMTheoMaThuoc(maThuoc);
        LocalDate homNay = LocalDate.now();

        if (maKM != null && !maKM.isEmpty()) {
            KhuyenMai km = kmService.layKhuyenMaiTheoMa(maKM);
            if (km != null && !homNay.isBefore(km.getNgayBD()) && !homNay.isAfter(km.getNgayKT())) {
                String loaiKM = km.getLoaiKM().toLowerCase();
                if (loaiKM.equals("giảm giá")) {
                    double mucGiam = km.getMucKM();
                    donGiaHienThi = donGiaGoc * (1 - mucGiam / 100.0);
                    thanhTien = donGiaHienThi * soLuongNhap;
                    moTaKM = "Giảm " + mucGiam + "%";
                } else if (loaiKM.equals("mua tặng")) {
                    int soLuongTang = (soLuongNhap / km.getSoLuongMua()) * km.getSoLuongTang();
                    if (soLuongTang > 0) {
                        soLuongThucTe = soLuongNhap + soLuongTang;
                        thanhTien = donGiaGoc * soLuongNhap;
                        moTaKM = String.format("Mua %d tặng %d (Tặng: %d)", km.getSoLuongMua(), km.getSoLuongTang(), soLuongTang);
                    } else {
                        thanhTien = donGiaGoc * soLuongNhap;
                    }
                } else {
                    thanhTien = donGiaGoc * soLuongNhap;
                }
            } else {
                thanhTien = donGiaGoc * soLuongNhap;
            }
        } else {
            thanhTien = donGiaGoc * soLuongNhap;
        }

        if (rowIndex != -1) {
            int slMoi = slHienTaiTrenBang + soLuongThucTe;
            double thanhTienCu = tool.chuyenTienSangSo(model.getValueAt(rowIndex, 7).toString());

            model.setValueAt(slMoi, rowIndex, 4);
            model.setValueAt(tool.dinhDangVND(donGiaHienThi), rowIndex, 6);
            model.setValueAt(tool.dinhDangVND(thanhTienCu + thanhTien), rowIndex, 7);

            if (!moTaKM.equals("Không có")) {
                model.setValueAt(moTaKM, rowIndex, 8);
            }
        } else {
            int stt = model.getRowCount() + 1;
            Object[] row = { maThuoc, stt, tenThuoc, tenQG, soLuongThucTe, donVi, tool.dinhDangVND(donGiaHienThi),
                    tool.dinhDangVND(thanhTien), moTaKM };
            model.addRow(row);
        }

        lpdhGUI.cmbSanPham.setSelectedIndex(-1);
        lpdhGUI.cmbDonVi.setSelectedIndex(-1);
        lpdhGUI.txtSoLuong.setText("");
    }

    public void setUpGoiY() {
        batGoiYChoTextField(lpdhGUI.txtSdt, listKH, kh -> {
            lpdhGUI.txtTenKH.setText(kh.getTenKH());
            lpdhGUI.txtTuoi.setText(String.valueOf(kh.getTuoi()));
        });
        ArrayList<String> listTenSP = new ArrayList<>();
        for (Thuoc t : listThuoc)
            listTenSP.add(t.getTenThuoc());
        batGoiYChoComboBox(lpdhGUI.cmbSanPham, listTenSP);
    }

    public void setUpComboBox() {
        listDVT = dvtService.layListDVT();
        lpdhGUI.cmbDonVi.addItem("");
        for (DonViTinh dvt : listDVT) {
            lpdhGUI.cmbDonVi.addItem(dvt.getTenDVT());
        }
        listThuoc = (ArrayList<Thuoc>) thService.layListThuoc();
        lpdhGUI.cmbSanPham.addItem("");
        for (Thuoc th : listThuoc) {
            if (th.getTrangThai()) {
                lpdhGUI.cmbSanPham.addItem(th.getTenThuoc());
            }
        }

        lpdhGUI.cmbSanPham.addActionListener(e -> {
            String tenThuoc = (String) lpdhGUI.cmbSanPham.getSelectedItem();

            if (tenThuoc == null || tenThuoc.isEmpty()) {
                lpdhGUI.cmbDonVi.setSelectedIndex(-1);
                return;
            }

            String maThuoc = thService.layMaThuocTheoTen(tenThuoc);
            if (maThuoc == null) {
                lpdhGUI.cmbDonVi.setSelectedIndex(-1);
                return;
            }
            String donVi = thService.layTenDonViTinhTheoMaThuoc(maThuoc);
            if (donVi == null) {
                lpdhGUI.cmbDonVi.setSelectedIndex(-1);
                return;
            }
            lpdhGUI.cmbDonVi.setSelectedItem(donVi);

            lpdhGUI.getCmbQuocGia().removeAllItems();

            ArrayList<QuocGia> listQG = thService.layListQuocGiaTheoThuoc(tenThuoc);
            if (listQG != null) {
                for (QuocGia qg : listQG) {
                    lpdhGUI.getCmbQuocGia().addItem(qg.getTenQuocGia());
                }
            }
            lpdhGUI.cmbQuocGia.setSelectedIndex(0);
        });
    }

    public void lamMoi() {
        lpdhGUI.txtSdt.setText("");
        lpdhGUI.txtTenKH.setText("");
        lpdhGUI.txtTuoi.setText("");
        lpdhGUI.txtSoLuong.setText("");
        lpdhGUI.txaGhiChu.setText("");
        lpdhGUI.cmbDonVi.setSelectedItem("");
        lpdhGUI.cmbSanPham.setSelectedItem("");
        lpdhGUI.model.setRowCount(0);
    }

    public void batGoiYChoComboBox(JComboBox<String> comboBox, java.util.List<String> danhSach) {
        comboBox.setEditable(true);
        JTextField editor = (JTextField) comboBox.getEditor().getEditorComponent();

        JPopupMenu popup = new JPopupMenu();
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> list = new JList<>(listModel);
        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(null);
        popup.add(scroll);

        popup.setFocusable(false);

        final int DEBOUNCE_MS = 180;
        Timer debounce = new Timer(DEBOUNCE_MS, e -> SwingUtilities.invokeLater(() -> {
            String text = editor.getText().trim().toLowerCase();
            listModel.clear();

            if (text.isEmpty()) {
                popup.setVisible(false);
                return;
            }

            int LIMIT = 20;
            int added = 0;
            for (String s : danhSach) {
                if (s.toLowerCase().contains(text)) {
                    listModel.addElement(s);
                    if (++added >= LIMIT)
                        break;
                }
            }

            if (listModel.isEmpty()) {
                popup.setVisible(false);
                return;
            }

            list.setSelectedIndex(0);

            if (!popup.isVisible()) {
                int popupHeight = Math.min(180, list.getPreferredSize().height + 4);
                popup.setPopupSize(editor.getWidth(), popupHeight);
                popup.show(editor, 0, editor.getHeight());
            }
        }));
        debounce.setRepeats(false);

        editor.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void restart() {
                if (debounce.isRunning())
                    debounce.restart();
                else
                    debounce.start();
            }

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                restart();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                restart();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                restart();
            }
        });

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int idx = list.locationToIndex(e.getPoint());
                if (idx >= 0) {
                    String sel = listModel.get(idx);
                    editor.setText(sel);
                    popup.setVisible(false);
                }
            }
        });

        editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!popup.isVisible())
                    return;
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    int next = Math.min(listModel.size() - 1, list.getSelectedIndex() + 1);
                    list.setSelectedIndex(next);
                    list.ensureIndexIsVisible(next);
                    e.consume();
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    int prev = Math.max(0, list.getSelectedIndex() - 1);
                    list.setSelectedIndex(prev);
                    list.ensureIndexIsVisible(prev);
                    e.consume();
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    int idx = list.getSelectedIndex();
                    if (idx >= 0) {
                        editor.setText(listModel.get(idx));
                        popup.setVisible(false);
                    }
                    e.consume();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    popup.setVisible(false);
                }
            }
        });

        editor.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                SwingUtilities.invokeLater(() -> popup.setVisible(false));
            }
        });

    }

    public void batGoiYChoTextField(JTextField txt, ArrayList<KhachHang> danhSachKH, Consumer<KhachHang> onSelect) {
        JPopupMenu popup = new JPopupMenu();
        popup.setFocusable(false);
        List<JMenuItem> items = new ArrayList<>();
        final boolean[] dangCapNhat = { false };
        final int[] selectedIndex = { -1 };

        DocumentListener listener = new DocumentListener() {
            private void updateSuggestions() {
                if (dangCapNhat[0])
                    return;

                SwingUtilities.invokeLater(() -> {
                    String text = txt.getText().trim();
                    popup.removeAll();
                    items.clear();
                    selectedIndex[0] = -1;

                    if (text.isEmpty()) {
                        popup.setVisible(false);
                        return;
                    }

                    int count = 0;
                    for (KhachHang kh : danhSachKH) {
                        String sdt = tool.chuyenSoDienThoai(kh.getSdt());
                        if (sdt.contains(text)) {
                            JMenuItem item = new JMenuItem(sdt + " - " + kh.getTenKH());
                            item.addMouseListener(new MouseAdapter() {
                                @Override
                                public void mousePressed(MouseEvent e) {
                                    dangCapNhat[0] = true;
                                    txt.setText(sdt);
                                    popup.setVisible(false);
                                    dangCapNhat[0] = false;
                                    if (onSelect != null)
                                        onSelect.accept(kh);
                                }
                            });
                            popup.add(item);
                            items.add(item);
                            count++;
                            if (count >= 10)
                                break;
                        }
                    }

                    if (count > 0) {
                        popup.pack();
                        popup.show(txt, 0, txt.getHeight());
                    } else {
                        popup.setVisible(false);
                    }
                });
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSuggestions();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSuggestions();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        };

        txt.getDocument().addDocumentListener(listener);

        txt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!popup.isVisible() || items.isEmpty())
                    return;

                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    selectedIndex[0] = (selectedIndex[0] + 1) % items.size();
                    highlightSelected();
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    selectedIndex[0] = (selectedIndex[0] - 1 + items.size()) % items.size();
                    highlightSelected();
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER && selectedIndex[0] >= 0) {
                    JMenuItem item = items.get(selectedIndex[0]);
                    item.doClick();
                    popup.setVisible(false);
                    e.consume();
                }
            }

            private void highlightSelected() {
                for (int i = 0; i < items.size(); i++) {
                    JMenuItem it = items.get(i);
                    it.setBackground(i == selectedIndex[0] ? new Color(0xE0E0E0) : Color.WHITE);
                }
            }
        });
    }
}