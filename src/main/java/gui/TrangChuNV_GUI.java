package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;

//import controller.DangNhapCtrl;
//import controller.ToolCtrl;
import entity.NhanVien;
import entity.TaiKhoan;
import utils.ToolCtrl;

public class TrangChuNV_GUI extends JFrame {

	public static final long serialVersionUID = 1L;
	public ToolCtrl tool = new ToolCtrl();
	public JPanel leftPanel, topPanel, contentPanel;
	public JLabel lblTenHieuThuoc, lblTenNV, lblChucVu;
	public JLabel imgTaiKhoan, imgDangXuat, imgLogo;
	public Map<String, JPanel> menuItems = new HashMap<>();
	public Map<String, String> iconPaths = new HashMap<>();
	public Map<String, JPanel> panelMapping = new HashMap<>();
	public String selectedMenu = "";
	Font font1 = new Font("Arial", Font.BOLD, 18);
	Font font2 = new Font("Arial", Font.PLAIN, 15);
	public TaiKhoan taiKhoan;
	public NhanVien nhanVien;

	public TrangChuNV_GUI(TaiKhoan tk) {
		setTitle("Quản lý hiệu thuốc Tam Thanh");
		setSize(1500, 800);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLayout(new BorderLayout());

		// ========== THANH TRÊN ==========
		topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(Color.WHITE);
		topPanel.setBorder(new MatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));

		// LEFT SIDE
		JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		logoPanel.setBackground(Color.WHITE);
		imgLogo = new JLabel(setUpIcon("/picture/trangChu/logo.jpg", 40, 40));
		lblTenHieuThuoc = new JLabel("NHÀ THUỐC TAM THANH");
		lblTenHieuThuoc.setFont(font1);
		logoPanel.add(imgLogo);
		logoPanel.add(lblTenHieuThuoc);
		topPanel.add(logoPanel, BorderLayout.WEST);

		// RIGHT SIDE
		JPanel nvPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
		nvPanel.setBackground(Color.WHITE);
		lblTenNV = new JLabel("Trần Thanh Toàn");
		lblTenNV.setFont(font2);
		lblChucVu = new JLabel("Nhân viên bán hàng");
		JPanel namePanel = new JPanel(new GridLayout(2, 1));
		namePanel.setBackground(Color.WHITE);
		namePanel.add(lblTenNV);
		namePanel.add(lblChucVu);
		imgTaiKhoan = new JLabel(setUpIcon("/picture/trangChu/user.png", 20, 20));
		imgDangXuat = new JLabel(setUpIcon("/picture/trangChu/signOut.png", 20, 20));
		imgDangXuat.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		nvPanel.add(namePanel);
		nvPanel.add(imgTaiKhoan);
		nvPanel.add(imgDangXuat);
		topPanel.add(nvPanel, BorderLayout.EAST);
		add(topPanel, BorderLayout.NORTH);

		// ========== THANH MENU TRÁI ==========
		leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.setBackground(new Color(245, 247, 250));
		leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		JScrollPane scroll = new JScrollPane(leftPanel);
		scroll.setBorder(null);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scroll, BorderLayout.WEST);

		// ========== KHU VỰC CHÍNH ==========
		contentPanel = new JPanel(new BorderLayout());
		contentPanel.setBackground(Color.WHITE);
		add(contentPanel, BorderLayout.CENTER);

		// ========== THÊM MENU CHÍNH ==========
		thietLapMenu("Trang chủ", "/picture/trangChu/dashboard.png", taoPanelTrangChu());
		thietLapMenu("Thuốc", "/picture/trangChu/addMedicine.png", taoPanelTam("Thuốc"));
		thietLapMenu("Kệ Thuốc", "/picture/trangChu/shelf.png", taoPanelTam("Kệ Thuốc"));
		thietLapMenu("Khách Hàng", "/picture/trangChu/customer.png", taoPanelTam("Khách Hàng"));
		thietLapMenu("Hóa Đơn", "/picture/trangChu/order.png", taoPanelTam("Hóa Đơn"));

		this.taiKhoan = tk;
//		this.nhanVien = tk.getNhanVien();
		setThuocTinhMainMenu();
		hienThiTrangChu();
		// taoMappingPanel();
//		hienThiThongTinNhanVien();
//		ganSuKien();

		setVisible(true);
	}

//	public void hienThiThongTinNhanVien() {
//		if (nhanVien != null) {
//			hienThiAnhNhanVien();
//			lblTenNV.setText(nhanVien.getTenNV());
//			lblChucVu.setText(
//					taiKhoan.getLoaiTK().equalsIgnoreCase("Quản lý") ? "Nhân viên quản lý" : "Nhân viên bán hàng");
//		}
//	}

	// ========== TẢI HÌNH ẢNH ==========
	public Image taiAnh(String duongDanTuDB) {
		try {
			if (duongDanTuDB == null || duongDanTuDB.trim().isEmpty()) {
				return new ImageIcon(System.getProperty("user.dir") + "/resource/picture/default.png").getImage();
			}

			File file = new File(System.getProperty("user.dir") + "/resource" + duongDanTuDB);

			if (file.exists()) {
				return new ImageIcon(file.getAbsolutePath()).getImage();
			} else {
				return new ImageIcon(System.getProperty("user.dir") + "/resource/picture/default.png").getImage();
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ImageIcon(System.getProperty("user.dir") + "/resource/picture/default.png").getImage();
		}
	}

//	public void hienThiAnhNhanVien() {
//		if (nhanVien == null || nhanVien.getAnh() == null) {
//
//			imgTaiKhoan.setIcon(setUpIcon("/picture/trangChu/user.png", 20, 20));
//			return;
//		}
//
//		Image img = taiAnh(nhanVien.getAnh());
//		ImageIcon icon = new ImageIcon(img.getScaledInstance(30, 30, Image.SCALE_SMOOTH));
//		imgTaiKhoan.setIcon(icon);
//	}

//	private void ganSuKien() {
//		imgTaiKhoan.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				xemThongTinNhanVienDangNhap();
//			}
//		});
//
//		imgDangXuat.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				int result = JOptionPane.showConfirmDialog(TrangChuNV_GUI.this, "Bạn có chắc chắn muốn đăng xuất?",
//						"Xác nhận", JOptionPane.YES_NO_OPTION);
//				if (result == JOptionPane.YES_OPTION) {
//					dispose();
//					DangNhap_GUI gui = new DangNhap_GUI();
//					new DangNhapCtrl(gui);
//					gui.setVisible(true);
//				}
//			}
//		});
//	}

	public void xemThongTinNhanVienDangNhap() {
		if (nhanVien == null) {
			JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin nhân viên!", "Lỗi",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		ChiTietNhanVien_GUI chiTietPanel = new ChiTietNhanVien_GUI(this);
		setUpNoiDung(chiTietPanel);

//		chiTietPanel.getCtrl().setNhanVienHienTai(nhanVien);
	}

	/** Ánh xạ tên menu hoặc menu con sang panel tương ứng */
//	public void taoMappingPanel() {
//		// Menu chính
//		panelMapping.put("Trang chủ", taoPanelTrangChu());
//		panelMapping.put("Thuốc", taoPanelTam("Thuốc"));
//		panelMapping.put("Kệ Thuốc", taoPanelTam("Kệ Thuốc"));
//		panelMapping.put("Khách Hàng", taoPanelTam("Khách Hàng"));
//		panelMapping.put("Hóa Đơn", taoPanelTam("Hóa Đơn"));
//
//		// Menu con
//		panelMapping.put("Tìm kiếm thuốc", new TimKiemThuoc_GUI());
//		panelMapping.put("Thêm thuốc", new ThemThuoc_GUI());
//		panelMapping.put("Đơn vị", new DonVi_GUI());
//		panelMapping.put("Danh sách kệ", new DanhSachKeThuoc_GUI());
//		panelMapping.put("Thêm kệ thuốc", new ThemKeThuoc_GUI());
//		panelMapping.put("Tìm kiếm khách hàng", new TimKiemKH_GUI());
//		panelMapping.put("Thêm khách hàng", new ThemKhachHang_GUI());
//		panelMapping.put("Khiếu nại & Hỗ trợ", new DanhSachKhieuNaiVaHoTroHK_GUI());
//		panelMapping.put("Tìm kiếm hóa đơn", new TimKiemHD_GUI(this));
//		panelMapping.put("Danh sách phiếu đặt thuốc", new TimKiemPhieuDatHang_GUI(this));
//		panelMapping.put("Lập hóa đơn", new LapHoaDon_GUI(this));
//		panelMapping.put("Đặt thuốc", new LapPhieuDatHang_GUI(this));
//		panelMapping.put("Danh sách phiếu đổi trả", new TimKiemPhieuDoiTra_GUI(this));
//	}

	public JPanel taoPanelTheoTenMenu(String tenMenu) {
		switch (tenMenu) {
		// === TRANG CHỦ ===
		case "Trang chủ":
			return taoPanelTrangChu();

		// === MENU CON CỦA THUỐC ===
		case "Tìm kiếm thuốc":
			return new TimKiemThuoc_GUI();
		case "Thêm thuốc":
			return new ThemThuoc_GUI();
		case "Đơn vị":
			return new DonVi_GUI();

		// === MENU CON CỦA KỆ THUỐC ===
		case "Danh sách kệ":
			return new DanhSachKeThuoc_GUI();
		case "Thêm kệ thuốc":
			return new ThemKeThuoc_GUI();

		// === MENU CON CỦA KHÁCH HÀNG ===
		case "Tìm kiếm khách hàng":
			return new TimKiemKH_GUI();
		case "Thêm khách hàng":
			return new ThemKhachHang_GUI();
		case "Khiếu nại & Hỗ trợ":
			return new DanhSachKhieuNaiVaHoTroHK_GUI();

		// === MENU CON CỦA HÓA ĐƠN ===
		// Lưu ý: Các GUI này cần truyền 'this' (TrangChuNV_GUI) vào constructor
		case "Tìm kiếm hóa đơn":
			return new TimKiemHD_GUI(this);
		case "Danh sách phiếu đặt thuốc":
			return new TimKiemPhieuDatHang_GUI(this);
		case "Lập hóa đơn":
			return new LapHoaDon_GUI(this);
		case "Đặt thuốc":
//			return new LapPhieuDatHang_GUI(this);
		case "Danh sách phiếu đổi trả":
			return new TimKiemPhieuDoiTra_GUI(this);

		// === MENU CHA (Dùng panel tạm) ===
		case "Thuốc":
		case "Kệ Thuốc":
		case "Khách Hàng":
		case "Hóa Đơn":
			return taoPanelTam(tenMenu);

		default:
			return taoPanelTam(tenMenu);
		}
	}

	// ================== THÊM MENU ==================
	public void thietLapMenu(String text, String iconPath, JPanel linkedPanel) {
		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		container.setBackground(new Color(245, 247, 250));

		JPanel item = new JPanel(new GridBagLayout());
		item.setPreferredSize(new Dimension(220, 50));
		item.setMaximumSize(new Dimension(220, 50));
		item.setBackground(Color.WHITE);
		item.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
		item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.insets = new Insets(0, 0, 0, 8);
		JLabel icon = new JLabel(setUpIcon(iconPath, 22, 22));
		JLabel label = new JLabel(text);
		label.setFont(font2);
		item.add(icon, gbc);
		gbc.gridx = 1;
		item.add(label, gbc);

		item.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				highlightSelectedMenu(text);

				// CHỈ "TRANG CHỦ" MỚI ĐỔI NỘI DUNG
				if ("Trang chủ".equals(text)) {
					JPanel p = taoPanelTheoTenMenu(text);
					setUpNoiDung(p);
				}
				// Các menu khác → chỉ mở submenu, không đổi nội dung
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				if (!text.equals(selectedMenu))
					item.setBackground(new Color(230, 245, 255));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (!text.equals(selectedMenu))
					item.setBackground(Color.WHITE);
			}
		});

		container.add(item);
		leftPanel.add(container);
		leftPanel.add(Box.createVerticalStrut(5));
		menuItems.put(text, item);
		iconPaths.put(text, iconPath);
	}

	// ================== MENU CON (CĂN GIỮA DỌC, CĂN TRÁI NGANG) ==================
	public void setMauClickVaMenuCon(JPanel menuChinh, List<String> menuCons) {
		if (menuChinh == null)
			return;

		JPanel container = (JPanel) menuChinh.getParent();
		JPanel subMenuPanel = new JPanel();
		subMenuPanel.setLayout(new BoxLayout(subMenuPanel, BoxLayout.Y_AXIS));
		subMenuPanel.setBackground(new Color(245, 247, 250));
		subMenuPanel.setBorder(new EmptyBorder(5, 0, 5, 0));
		subMenuPanel.setVisible(false);

		for (String sub : menuCons) {
			JPanel item = new JPanel(new GridBagLayout());
			item.setPreferredSize(new Dimension(220, 40));
			item.setMaximumSize(new Dimension(220, 40));
			item.setBackground(Color.WHITE);
			item.setOpaque(true);

			// Shadow + padding
			item.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
					BorderFactory.createEmptyBorder(0, 15, 0, 15)));

			// CĂN GIỮA DỌC, CĂN TRÁI NGANG
			GridBagConstraints gbcLabel = new GridBagConstraints();
			gbcLabel.gridx = 0;
			gbcLabel.gridy = 0;
			gbcLabel.weightx = 1.0;
			gbcLabel.weighty = 1.0;
			gbcLabel.anchor = GridBagConstraints.WEST;
			gbcLabel.insets = new Insets(0, 0, 0, 0);

			JLabel lbl = new JLabel(sub);
			lbl.setFont(font2);
			lbl.setForeground(Color.BLACK);
			item.add(lbl, gbcLabel);

			// === HOVER + SELECTED ===
			item.addMouseListener(new MouseAdapter() {
				private boolean isSelected = false;

				@Override
				public void mouseEntered(MouseEvent e) {
					if (!isSelected) {
						lbl.setForeground(new Color(0, 120, 215));
					}
				}

				@Override
				public void mouseExited(MouseEvent e) {
					if (!isSelected) {
						lbl.setForeground(Color.BLACK);
					}
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					// Reset tất cả
					for (Component comp : subMenuPanel.getComponents()) {
						if (comp instanceof JPanel) {
							JPanel p = (JPanel) comp;
							JLabel l = (JLabel) p.getComponent(0);
							l.setForeground(Color.BLACK);
							l.setFont(font2);
						}
					}

					// Chọn hiện tại
					isSelected = true;
					lbl.setForeground(Color.decode("#00ADFE"));
					lbl.setFont(font2.deriveFont(Font.BOLD));

					JPanel p = taoPanelTheoTenMenu(sub);
					setUpNoiDung(p);

					// Tự động mở submenu khi chọn menu con
					subMenuPanel.setVisible(true);
					leftPanel.revalidate();
					leftPanel.repaint();
				}
			});

			subMenuPanel.add(item);
		}

		container.add(subMenuPanel);

		// Mở/đóng submenu khi click menu chính
		menuChinh.addMouseListener(new MouseAdapter() {
			boolean isOpen = false;

			@Override
			public void mouseClicked(MouseEvent e) {
				// 🔹 Trước tiên: đóng tất cả các submenu khác
				for (Component c : leftPanel.getComponents()) {
					if (c instanceof JPanel) {
						JPanel container = (JPanel) c;
						if (container.getComponentCount() > 1) {
							Component comp = container.getComponent(1);
							if (comp instanceof JPanel && comp != subMenuPanel) {
								comp.setVisible(false);
								// Reset cờ của menu khác
								for (MouseListener ml : container.getComponent(0).getMouseListeners()) {
									if (ml instanceof MouseAdapter) {
										try {
											// Dò biến isOpen nếu tồn tại trong anonymous class
											var field = ml.getClass().getDeclaredField("isOpen");
											field.setAccessible(true);
											field.setBoolean(ml, false);
										} catch (Exception ignored) {
										}
									}
								}
							}
						}
					}
				}

				// 🔹 Đóng/mở submenu hiện tại
				isOpen = !isOpen;
				subMenuPanel.setVisible(isOpen);

				// Cập nhật giao diện
				leftPanel.revalidate();
				leftPanel.repaint();
			}
		});

	}

	// ================== MENU CHÍNH & MENU CON ==================
	public void setThuocTinhMainMenu() {
		setMauClickVaMenuCon(menuItems.get("Trang chủ"), List.of());
		setMauClickVaMenuCon(menuItems.get("Thuốc"), List.of("Tìm kiếm thuốc", "Thêm thuốc", "Đơn vị"));
		setMauClickVaMenuCon(menuItems.get("Kệ Thuốc"), List.of("Danh sách kệ", "Thêm kệ thuốc"));
		setMauClickVaMenuCon(menuItems.get("Khách Hàng"),
				List.of("Tìm kiếm khách hàng", "Thêm khách hàng", "Khiếu nại & Hỗ trợ"));
		setMauClickVaMenuCon(menuItems.get("Hóa Đơn"), List.of("Tìm kiếm hóa đơn", "Danh sách phiếu đặt thuốc",
				"Lập hóa đơn", "Đặt thuốc", "Danh sách phiếu đổi trả"));
	}

	// ================== HỖ TRỢ ==================
	public void highlightSelectedMenu(String text) {
		// Reset menu chính
		for (Map.Entry<String, JPanel> entry : menuItems.entrySet()) {
			JPanel item = entry.getValue();
			JLabel label = (JLabel) item.getComponent(1);
			if (entry.getKey().equals(text)) {
				selectedMenu = text;
				item.setBackground(Color.decode("#00ADFE"));
				label.setForeground(Color.WHITE);
			} else {
				item.setBackground(Color.WHITE);
				label.setForeground(Color.BLACK);
			}
		}

		// Nếu là menu con → không cần reset menu chính
		if (menuItems.containsKey(text))
			return;

		// Reset tất cả menu con
		Component[] containers = leftPanel.getComponents();
		for (Component c : containers) {
			if (c instanceof JPanel) {
				JPanel container = (JPanel) c;
				if (container.getComponentCount() > 1) {
					JPanel subMenu = (JPanel) container.getComponent(1);
					for (Component sub : subMenu.getComponents()) {
						if (sub instanceof JPanel) {
							JPanel item = (JPanel) sub;
							JLabel lbl = (JLabel) item.getComponent(0);
							lbl.setForeground(Color.BLACK);
							lbl.setFont(font2);
						}
					}
				}
			}
		}
	}

	public void setUpNoiDung(JPanel panel) {
		contentPanel.removeAll();
		contentPanel.add(panel, BorderLayout.CENTER);
		contentPanel.revalidate();
		contentPanel.repaint();
	}

	public ImageIcon setUpIcon(String path, int width, int height) {
		URL imgURL = getClass().getResource(path);
		if (imgURL == null) {
			System.err.println("Không tìm thấy ảnh: " + path);
			return null;
		}
		ImageIcon icon = new ImageIcon(imgURL);
		Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return new ImageIcon(scaled);
	}

	public void hienThiTrangChu() {
		setUpNoiDung(taoPanelTrangChu());
		highlightSelectedMenu("Trang chủ");
	}

	public JPanel taoPanelTam(String title) {
		JPanel panel = new JPanel();
		panel.add(new JLabel("Đây là trang: " + title));
		return panel;
	}

	public JPanel taoPanelTrangChu() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.WHITE);
		JLabel lblAnh = new JLabel(setUpIcon("/picture/trangChu/AnhTrangChu.png", 1200, 550));
		lblAnh.setHorizontalAlignment(JLabel.CENTER);
		panel.add(lblAnh, BorderLayout.CENTER);

		JPanel pnThaoTac = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
		pnThaoTac.setBackground(Color.WHITE);
		JPanel pnBottom = new JPanel(new BorderLayout());
		pnBottom.setBackground(Color.WHITE);

		pnBottom.add(pnThaoTac, BorderLayout.CENTER);
		panel.add(pnBottom, BorderLayout.SOUTH);
		return panel;
	}

	public NhanVien layNhanVien() {
		return nhanVien;
	}
}