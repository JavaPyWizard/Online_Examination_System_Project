package exam;

import javax.swing.*;
import java.sql.*;
import java.awt.*;

public class ForgotPasswordFrame extends JFrame {

    JTextField username;
    JPasswordField newPass;

    ForgotPasswordFrame() {

        setTitle("Reset Password");
        setSize(400, 320);
        setMinimumSize(new Dimension(350, 300));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(UIStyle.bg);

        JPanel card = new JPanel(new GridBagLayout());
        UIStyle.styleCard(card);

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 20, 10, 20);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0;

        // Title
        JLabel title = new JLabel("Reset Password", JLabel.CENTER);
        title.setFont(UIStyle.titleFont);
        title.setForeground(UIStyle.text);
        g.gridx = 0; g.gridy = 0;
        card.add(title, g);

        // Fields
        JLabel l1 = new JLabel("Username");
        UIStyle.styleLabel(l1);
        g.gridy++;
        g.insets = new Insets(10, 20, 2, 20);
        card.add(l1, g);

        username = new JTextField();
        UIStyle.styleTextField(username);
        g.gridy++;
        g.insets = new Insets(0, 20, 10, 20);
        card.add(username, g);

        JLabel l2 = new JLabel("New Password");
        UIStyle.styleLabel(l2);
        g.gridy++;
        g.insets = new Insets(5, 20, 2, 20);
        card.add(l2, g);

        newPass = new JPasswordField();
        UIStyle.styleTextField(newPass);
        g.gridy++;
        g.insets = new Insets(0, 20, 15, 20);
        card.add(newPass, g);

        JButton reset = new JButton("Reset");
        UIStyle.stylePrimaryButton(reset);
        g.gridy++;
        g.insets = new Insets(10, 20, 20, 20);
        card.add(reset, g);

        // Root
        GridBagConstraints rc = new GridBagConstraints();
        rc.gridx = 0; rc.gridy = 0;
        rc.weightx = 1.0; rc.weighty = 1.0;
        rc.fill = GridBagConstraints.HORIZONTAL;
        rc.insets = new Insets(20, 40, 20, 40);
        root.add(card, rc);

        add(root);

        reset.addActionListener(e -> resetPassword());

        setVisible(true);
    }

    void resetPassword() {
        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "UPDATE users SET password=? WHERE username=?"
            );

            ps.setString(1, new String(newPass.getPassword()));
            ps.setString(2, username.getText());

            int rows = ps.executeUpdate();

            if(rows > 0)
                JOptionPane.showMessageDialog(this,"Password Updated!");
            else
                JOptionPane.showMessageDialog(this,"User not found");

            dispose();

        } catch(Exception e){
            e.printStackTrace();
        }
    }
}