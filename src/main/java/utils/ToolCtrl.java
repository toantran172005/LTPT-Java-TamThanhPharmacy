package utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;
import javax.imageio.ImageIO;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import com.toedter.calendar.JDateChooser;

public class ToolCtrl {
	
	public LocalDate convertExcelDate(Object input) {
	    if (input == null || input.toString().trim().isEmpty()) {
	        return null;
	    }

	    String dateStr = input.toString();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	    try {
	        return LocalDate.parse(dateStr, formatter);
	    } catch (DateTimeParseException e) {
	        e.printStackTrace();
	        return null; 
	    }
	}

	// ===== ĐỔI PANEL =====
	public void doiPanel(JPanel pnlCha, JPanel pnlMoi) {
		if (pnlCha == null || pnlMoi == null) {
			System.err.println("❌ Lỗi: Panel cha hoặc panel mới bị null trong ToolCtrl.doiPanel()");
			return;
		}
		pnlCha.removeAll();
		pnlCha.setLayout(new BorderLayout());
		pnlCha.add(pnlMoi, BorderLayout.CENTER);
		pnlCha.revalidate();
		pnlCha.repaint();

	}

	public JDateChooser taoDateChooser() {
		JDateChooser dateChooser = new JDateChooser();
		dateChooser.setPreferredSize(new Dimension(220, 35)); // chiều rộng và cao chuẩn
		dateChooser.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		dateChooser.setDateFormatString("dd-MM-yyyy"); // định dạng hiển thị ngày
		return dateChooser;
	}

	public JTextArea taoTextArea(int chieuCao) {
		JTextArea area = new JTextArea();
		area.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		area.setForeground(new Color(0x33, 0x33, 0x33));
		area.setBackground(Color.WHITE);
		area.setBorder(new CompoundBorder(new LineBorder(new Color(0xD1, 0xD1, 0xD1), 1, true),
				new EmptyBorder(8, 12, 8, 12)));
		area.setLineWrap(true);
		area.setWrapStyleWord(true);

		// Chiều rộng mặc định 220, chiều cao theo tham số
		area.setPreferredSize(new Dimension(220, chieuCao));
		area.setMinimumSize(new Dimension(220, chieuCao));

		return area;
	}

	public JLabel taoLabel(String noiDung) {
		JLabel lbl = new JLabel(noiDung);

		// Font và màu chữ
		lbl.setFont(new Font("Times New Roman", Font.BOLD, 17));
		lbl.setForeground(new Color(0x33, 0x33, 0x33)); // gần màu #333333

		// Căn lề theo chiều dọc giữa
		lbl.setVerticalAlignment(SwingConstants.CENTER);

		// Kích thước: cao 25, rộng tự động theo text
//		lbl.setPreferredSize(new Dimension(lbl.getPreferredSize().width, 30));

		return lbl;
	}

	public JComboBox<String> taoComboBox(String[] items) {
		JComboBox<String> combo = new JComboBox<>(items);

		// Font và màu
		combo.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		combo.setBackground(Color.WHITE);
		combo.setForeground(Color.BLACK);

		// Bo góc + viền xám nhạt
		combo.setBorder(
				new CompoundBorder(new LineBorder(new Color(0xCC, 0xCC, 0xCC), 1, true), new EmptyBorder(3, 3, 3, 3)));

		combo.setPreferredSize(new Dimension(220, 35));

		// Cho phép nhập (editable)
		combo.setEditable(true);

		// Style cho editor (JTextField bên trong)
		Component editorComp = combo.getEditor().getEditorComponent();
		if (editorComp instanceof JTextField txt) {
			txt.setFont(new Font("Times New Roman", Font.PLAIN, 14));
			txt.setBackground(Color.WHITE);
			txt.setForeground(Color.BLACK);
			txt.setBorder(new EmptyBorder(5, 10, 5, 10));
		}

		// Renderer tùy chỉnh cho danh sách
		combo.setRenderer(new DefaultListCellRenderer() {
			private final EmptyBorder padding = new EmptyBorder(5, 10, 5, 10);

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {

				JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

				lbl.setBorder(padding);
				lbl.setBackground(isSelected ? new Color(0xE5, 0xF0, 0xFF) : Color.WHITE);
				lbl.setForeground(Color.BLACK);
				lbl.setFont(new Font("Times New Roman", Font.PLAIN, 14));
				return lbl;
			}
		});

		return combo;
	}

	public JTextField taoTextField(String promptText) {
		JTextField txt = new JTextField() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				// Hiển thị prompt text khi rỗng và chưa focus
				if (getText().isEmpty() && !isFocusOwner()) {
					Graphics2D g2 = (Graphics2D) g.create();
					g2.setFont(getFont().deriveFont(Font.PLAIN));
					g2.setColor(new Color(200, 200, 200)); // siêu nhạt, kiểu placeholder hiện đại
					Insets ins = getInsets();
					g2.drawString(promptText, ins.left + 2, getHeight() / 2 + getFont().getSize() / 2 - 3);
					g2.dispose();
				}
			}
		};

		// Font và màu chữ
		txt.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		txt.setForeground(new Color(0x33, 0x33, 0x33));
		txt.setBackground(Color.WHITE);

		// Bo góc + viền
		txt.setBorder(new CompoundBorder(new LineBorder(new Color(0xD1, 0xD1, 0xD1), 1, true),
				new EmptyBorder(8, 12, 8, 12)));

		// Kích thước cố định
		txt.setPreferredSize(new Dimension(220, 35));

		// Nền hiển thị
		txt.setOpaque(true);

		// Làm mới khi focus để ẩn/hiện prompt
		txt.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				txt.repaint();
			}

			@Override
			public void focusLost(FocusEvent e) {
				txt.repaint();
			}
		});

		return txt;
	}

	public JButton taoButton(String noiDung, String duongDanAnh) {
		JButton btn = new JButton(noiDung);

		// Font và màu
		btn.setFont(new Font("Arial", Font.BOLD, 15));
		btn.setForeground(Color.BLACK);
		btn.setBackground(Color.WHITE);

		// Bỏ viền focus mặc định
		btn.setFocusPainted(false);
		btn.setContentAreaFilled(false);
		btn.setOpaque(true);

		// Con trỏ bàn tay
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

		// Bo góc và đổ bóng nhẹ
		btn.setBorder(
				new CompoundBorder(new LineBorder(new Color(220, 220, 220), 1, true), new EmptyBorder(5, 10, 5, 10)));

		// Thêm ảnh (nếu có)
		if (duongDanAnh != null && !duongDanAnh.isEmpty()) {
			try {
				BufferedImage img = null;

				// ✅ Ưu tiên tìm ảnh trong classpath (tính từ src/)
				java.net.URL imgUrl = getClass().getResource(duongDanAnh);
				if (imgUrl != null) {
					img = ImageIO.read(imgUrl);
				} else {
					// ⚙️ Nếu không có trong classpath, thử đọc file ngoài (đường dẫn tuyệt đối)
					File file = new File(duongDanAnh);
					if (file.exists()) {
						img = ImageIO.read(file);
					} else {
						System.err.println("Không tìm thấy ảnh: " + duongDanAnh);
					}
				}

				// Nếu đọc được ảnh → scale và gán icon
				if (img != null) {
					Image scaledImg = img.getScaledInstance(20, 25, Image.SCALE_SMOOTH);
					btn.setIcon(new ImageIcon(scaledImg));
					btn.setHorizontalAlignment(SwingConstants.LEFT);
				}

			} catch (Exception e) {
				System.err.println("Không thể đọc ảnh: " + duongDanAnh);
				e.printStackTrace();
			}
		}

		// Kích thước tự co giãn theo nội dung
		btn.setPreferredSize(null);

		return btn;
	}

	// ==========================
	// Hiển thị thông báo
	// ==========================
	public void hienThiThongBao(String tieuDe, String noiDung, boolean trangThai) {
		String iconPath;
		if (trangThai) {
			iconPath = "/picture/thuoc/check.png";
		} else {
			iconPath = "/picture/thuoc/cross.png";
		}

		// Tạo icon và thu nhỏ lại
		ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
		Image scaledImage = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
		ImageIcon scaledIcon = new ImageIcon(scaledImage);

		int messageType = trangThai ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
		JOptionPane.showMessageDialog(null, noiDung, tieuDe, messageType, scaledIcon);
	}

	// ==========================
	// 2️⃣ Hiển thị xác nhận
	// ==========================
	public boolean hienThiXacNhan(String tieuDe, String noiDung, JFrame frameCha) {
		int option = JOptionPane.showConfirmDialog(frameCha, noiDung, tieuDe, JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		return option == JOptionPane.OK_OPTION;
	}

	// ==========================
	// 3️⃣ Định dạng ngày
	// ==========================
	public String dinhDangLocalDate(LocalDate date) {
		if (date == null)
			return "";
		return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
	}

	public java.sql.Date localDateSangSqlDate(LocalDate localDate) {
		return (localDate != null) ? java.sql.Date.valueOf(localDate) : null;
	}

	public LocalDate sqlDateSangLocalDate(java.sql.Date sqlDate) {
		return (sqlDate != null) ? sqlDate.toLocalDate() : null;
	}

	// ==========================
	// 4️⃣ Chuyển Date của Swing sang LocalDate
	// ==========================
	public LocalDate utilDateSangLocalDate(Date date) {
		if (date == null)
			return null;
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public Date localDateSangUtilDate(LocalDate localDate) {
		if (localDate == null)
			return null;
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	// ==========================
	// 5️⃣ Định dạng tiền tệ
	// ==========================
	public String dinhDangVND(double amount) {
		Locale localeVN = new Locale("vi", "VN");
		NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(localeVN);
		return currencyFormatter.format(amount);
	}

	public double chuyenTienSangSo(String text) {
		if (text == null || text.isEmpty())
			return 0;
		text = text.replaceAll("[^\\d]", ""); // bỏ ký tự thừa
		try {
			return Double.parseDouble(text);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	// ==========================
	// 6️⃣ Đổi số điện thoại
	// ==========================
	public String chuyenSoDienThoai(String sdt) {
		if (sdt == null || sdt.isEmpty())
			return sdt;

		sdt = sdt.trim();

		if (sdt.startsWith("+84-")) {
			return "0" + sdt.substring(4);
		} else if (sdt.startsWith("0")) {
			return "+84-" + sdt.substring(1);
		}

		return sdt;
	}

	// ==========================
	// 7️⃣ Sinh khóa chính tự động
	// ==========================
//	public String taoKhoaChinh(String tenBangVietTat) {
//		Map<String, String> mapBang = new HashMap<>();
//		mapBang.put("PNT", "PhieuNhapThuoc");
//		mapBang.put("K", "Kho");
//		mapBang.put("TK", "TaiKhoan");
//		mapBang.put("PKN", "Phieu_KhieuNai_HoTroKH");
//		mapBang.put("T", "Thue");
//		mapBang.put("KH", "KhachHang");
//		mapBang.put("NV", "NhanVien");
//		mapBang.put("KT", "KeThuoc");
//		mapBang.put("NCC", "NhaCungCap");
//		mapBang.put("TH", "Thuoc");
//		mapBang.put("HD", "HoaDon");
//		mapBang.put("KM", "KhuyenMai");
//		mapBang.put("PDT", "PhieuDoiTra");
//		mapBang.put("PDTH", "PhieuDatThuoc");
//		mapBang.put("DVT", "DonViTinh");
//		mapBang.put("PDH", "PhieuDatHang");
//
//		if (!mapBang.containsKey(tenBangVietTat)) {
//			throw new IllegalArgumentException("Không tìm thấy bảng tương ứng với mã: " + tenBangVietTat);
//		}
//
//		String tenBang = mapBang.get(tenBangVietTat);
//		String cotKhoa;
//
//		switch (tenBangVietTat) {
//		case "TH":
//            cotKhoa = "maThuoc";
//            break;
//		case "KT":
//			cotKhoa = "maKe";
//			break;
//		case "K":
//			cotKhoa = "maKho";
//			break;
//		case "PDT":
//			cotKhoa = "maPhieuDT";
//			break;
//		case "PDTH":
//			cotKhoa = "maPDT";
//			break;
//		case "PKN":
//			cotKhoa = "maPhieu";
//			break;
//		case "T":
//			cotKhoa = "maThue";
//			break;
//		default:
//			cotKhoa = "ma" + tenBangVietTat;
//			break;
//		}
//
//		String prefix = "TT" + tenBangVietTat;
//
//		String sql = String.format(
//				"SELECT TOP 1 %s FROM %s WHERE %s LIKE ? ORDER BY TRY_CAST(REPLACE(%s, '%s', '') AS INT) DESC", cotKhoa,
//				tenBang, cotKhoa, cotKhoa, prefix);
//
//		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
//			ps.setString(1, prefix + "%");
//			ResultSet rs = ps.executeQuery();
//
//			int so = 0;
//			if (rs.next()) {
//				String maMax = rs.getString(1);
//				String soStr = maMax.replace(prefix, "");
//				so = Integer.parseInt(soStr);
//			}
//
//			so++;
//			return prefix + so;
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
}
