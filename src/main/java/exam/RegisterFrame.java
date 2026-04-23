package exam;

import javax.swing.*;
import java.sql.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    JTextField username;
    JPasswordField password, confirm;
    JComboBox<String> roleBox;

    RegisterFrame() {

        setTitle("Register");
        setSize(400, 500);
        setMinimumSize(new Dimension(350, 450));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(UIStyle.bg);

        JPanel card = new JPanel(new GridBagLayout());
        UIStyle.styleCard(card);

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 20, 2, 20);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0;

        // Title
        JLabel title = new JLabel("Create Account", JLabel.CENTER);
        title.setFont(UIStyle.titleFont);
        title.setForeground(UIStyle.text);
        g.gridx = 0; g.gridy = 0;
        g.insets = new Insets(10, 20, 15, 20);
        card.add(title, g);

        // Fields
        g.insets = new Insets(5, 20, 2, 20);

        JLabel l1 = new JLabel("Username");
        UIStyle.styleLabel(l1);
        g.gridy++;
        card.add(l1, g);

        username = new JTextField();
        UIStyle.styleTextField(username);
        g.gridy++;
        g.insets = new Insets(0, 20, 10, 20);
        card.add(username, g);

        g.insets = new Insets(5, 20, 2, 20);
        JLabel l2 = new JLabel("Password");
        UIStyle.styleLabel(l2);
        g.gridy++;
        card.add(l2, g);

        password = new JPasswordField();
        UIStyle.styleTextField(password);
        g.gridy++;
        g.insets = new Insets(0, 20, 10, 20);
        card.add(password, g);

        g.insets = new Insets(5, 20, 2, 20);
        JLabel l3 = new JLabel("Confirm Password");
        UIStyle.styleLabel(l3);
        g.gridy++;
        card.add(l3, g);

        confirm = new JPasswordField();
        UIStyle.styleTextField(confirm);
        g.gridy++;
        g.insets = new Insets(0, 20, 10, 20);
        card.add(confirm, g);

        g.insets = new Insets(5, 20, 2, 20);
        JLabel l4 = new JLabel("Role");
        UIStyle.styleLabel(l4);
        g.gridy++;
        card.add(l4, g);

        roleBox = new JComboBox<>(new String[]{"student", "teacher"});
        roleBox.setFont(UIStyle.normalFont);
        roleBox.setBackground(Color.WHITE);
        g.gridy++;
        g.insets = new Insets(0, 20, 10, 20);
        card.add(roleBox, g);

        // Checkbox
        JCheckBox show = new JCheckBox("Show Passwords");
        show.setBackground(UIStyle.cardBg);
        show.setFont(UIStyle.normalFont);
        show.setForeground(UIStyle.textMuted);
        show.addActionListener(e -> {
            char ch = show.isSelected() ? (char)0 : '*';
            password.setEchoChar(ch);
            confirm.setEchoChar(ch);
        });
        g.gridy++;
        g.insets = new Insets(0, 15, 15, 20);
        card.add(show, g);

        // Register Button
        JButton register = new JButton("Register");
        UIStyle.stylePrimaryButton(register);
        g.gridy++;
        g.insets = new Insets(5, 20, 15, 20);
        card.add(register, g);

        // Root
        GridBagConstraints rc = new GridBagConstraints();
        rc.gridx = 0; rc.gridy = 0;
        rc.weightx = 1.0; rc.weighty = 1.0;
        rc.fill = GridBagConstraints.HORIZONTAL;
        rc.insets = new Insets(20, 40, 20, 40);
        root.add(card, rc);

        add(root);

        register.addActionListener(e -> registerUser());

        setVisible(true);
    }

    void registerUser() {

        String pass1 = new String(password.getPassword());
        String pass2 = new String(confirm.getPassword());

        if(pass1.length() < 6) {
            JOptionPane.showMessageDialog(this,"Password must be 6+ chars");
            return;
        }

        if(!pass1.equals(pass2)) {
            JOptionPane.showMessageDialog(this,"Passwords do not match");
            return;
        }

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO users(username,password,role) VALUES(?,?,?)"
            );

            ps.setString(1, username.getText());
            ps.setString(2, pass1);
            ps.setString(3, roleBox.getSelectedItem().toString());

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,"Registered Successfully!");
            dispose();

        } catch(Exception e){
            e.printStackTrace();
        }
    }
}