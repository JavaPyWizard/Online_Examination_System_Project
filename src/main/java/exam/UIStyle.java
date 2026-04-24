package exam;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UIStyle {

    public static final Color bg = new Color(248, 250, 252); // Slate 50
    public static final Color primary = new Color(79, 70, 229); // Indigo 600
    public static final Color primaryHover = new Color(67, 56, 202); // Indigo 700
    public static final Color text = new Color(15, 23, 42); // Slate 900
    public static final Color textMuted = new Color(100, 116, 139); // Slate 500
    public static final Color cardBg = Color.WHITE;
    public static final Color border = new Color(226, 232, 240); // Slate 200
    public static final Color secondaryBtn = new Color(241, 245, 249); // Slate 100
    public static final Color secondaryHover = new Color(226, 232, 240); // Slate 200
    public static final Color secondaryBtnText = new Color(71, 85, 105); // Slate 600

    public static final Font titleFont = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font subtitleFont = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font normalFont = new Font("Segoe UI", Font.PLAIN, 14);

    public static void stylePrimaryButton(JButton btn) {
        btn.setBackground(primary);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(primaryHover);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(primary);
            }
        });
    }

    public static void styleSecondaryButton(JButton btn) {
        btn.setBackground(secondaryBtn);
        btn.setForeground(secondaryBtnText);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(secondaryHover);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(secondaryBtn);
            }
        });
    }

    public static void styleCard(JPanel panel) {
        panel.setBackground(cardBg);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(border, 1),
                new EmptyBorder(10, 10, 10, 10)));
    }

    public static void styleTextField(JTextField field) {
        field.setFont(normalFont);
        field.setForeground(text);
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(border, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
    }

    public static void styleLabel(JLabel label) {
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(textMuted);
    }
}