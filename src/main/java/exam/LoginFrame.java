package exam;

import javax.swing.*;
import java.sql.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    JTextField user;
    JPasswordField pass;

    LoginFrame() {

        setTitle("Online Exam System");
        setSize(400, 450);
        setMinimumSize(new Dimension(350, 400));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Root panel with BorderLayout to allow background to fill
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(UIStyle.bg);

        // Card panel
        JPanel card = new JPanel(new GridBagLayout());
        UIStyle.styleCard(card);

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 20, 10, 20);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0;

        // Title
        JLabel title = new JLabel("Login System", JLabel.CENTER);
        title.setFont(UIStyle.titleFont);
        title.setForeground(UIStyle.text);
        g.gridx = 0;
        g.gridy = 0;
        card.add(title, g);

        JLabel subtitle = new JLabel("Please enter your login credentials", JLabel.CENTER);
        subtitle.setFont(UIStyle.normalFont);
        subtitle.setForeground(UIStyle.textMuted);
        g.gridy++;
        g.insets = new Insets(0, 20, 20, 20);
        card.add(subtitle, g);

        // Username
        JLabel userLabel = new JLabel("Username");
        UIStyle.styleLabel(userLabel);
        g.gridy++;
        g.insets = new Insets(5, 20, 2, 20);
        card.add(userLabel, g);

        user = new JTextField();
        UIStyle.styleTextField(user);
        g.gridy++;
        g.insets = new Insets(0, 20, 15, 20);
        card.add(user, g);

        // Password
        JLabel passLabel = new JLabel("Password");
        UIStyle.styleLabel(passLabel);
        g.gridy++;
        g.insets = new Insets(5, 20, 2, 20);
        card.add(passLabel, g);

        pass = new JPasswordField();
        UIStyle.styleTextField(pass);
        g.gridy++;
        g.insets = new Insets(0, 20, 5, 20);
        card.add(pass, g);

        // Show password
        JCheckBox show = new JCheckBox("Show Password");
        show.setBackground(UIStyle.cardBg);
        show.setFont(UIStyle.normalFont);
        show.setForeground(UIStyle.textMuted);
        show.addActionListener(e -> pass.setEchoChar(show.isSelected() ? (char) 0 : '*'));
        g.gridy++;
        g.insets = new Insets(0, 15, 15, 20);
        card.add(show, g);

        // Login Button
        JButton login = new JButton("Login");
        UIStyle.stylePrimaryButton(login);
        g.gridy++;
        g.insets = new Insets(10, 20, 10, 20);
        card.add(login, g);

        // Register Button
        JButton register = new JButton("Create an Account");
        UIStyle.styleSecondaryButton(register);
        g.gridy++;
        g.insets = new Insets(0, 20, 10, 20);
        card.add(register, g);

        // Forgot password link
        JButton forgot = new JButton("Forgot Password?");
        forgot.setBorderPainted(false);
        forgot.setContentAreaFilled(false);
        forgot.setForeground(UIStyle.primary);
        forgot.setFont(UIStyle.normalFont);
        forgot.setCursor(new Cursor(Cursor.HAND_CURSOR));
        g.gridy++;
        g.fill = GridBagConstraints.NONE;
        g.anchor = GridBagConstraints.CENTER;
        g.insets = new Insets(5, 20, 10, 20);
        card.add(forgot, g);

        // Add card to root
        GridBagConstraints rc = new GridBagConstraints();
        rc.gridx = 0;
        rc.gridy = 0;
        rc.weightx = 1.0;
        rc.weighty = 1.0;
        rc.fill = GridBagConstraints.HORIZONTAL;
        rc.insets = new Insets(20, 40, 20, 40); // Max width padding

        // Use a wrapper to prevent infinite horizontal stretching
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(card, BorderLayout.CENTER);

        // Ensure the card doesn't stretch too wide
        wrapper.setMaximumSize(new Dimension(400, 600));

        root.add(card, rc);

        add(root);

        login.addActionListener(e -> loginUser());
        register.addActionListener(e -> new RegisterFrame());
        forgot.addActionListener(e -> new ForgotPasswordFrame());

        setVisible(true);
    }

    void loginUser() {
        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM users WHERE username=? AND password=?");

            ps.setString(1, user.getText());
            ps.setString(2, new String(pass.getPassword()));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                if (rs.getString("role").equals("teacher"))
                    new TeacherDashboard(rs.getInt("user_id"));
                else
                    new StudentDashboard(rs.getInt("user_id"));

                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Login");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}