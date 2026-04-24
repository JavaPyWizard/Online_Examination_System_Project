package exam;

import javax.swing.*;
import java.sql.*;
import java.awt.*;

public class GradeCardFrame extends JFrame {

    GradeCardFrame(int student_id, int quiz_id, int score, int total) {

        setTitle("Grade Card");
        setSize(450, 550);
        setMinimumSize(new Dimension(400, 500));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(UIStyle.bg);

        JPanel card = new JPanel(new GridBagLayout());
        UIStyle.styleCard(card);

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0;
        g.gridx = 0;
        g.gridy = 0;

        // Header
        JLabel heading = new JLabel("Quiz Results", JLabel.CENTER);
        heading.setFont(UIStyle.titleFont);
        heading.setForeground(UIStyle.text);
        g.insets = new Insets(10, 20, 20, 20);
        card.add(heading, g);

        // Big Percentage Display
        int percent = (total != 0) ? (score * 100) / total : 0;
        boolean passed = percent >= 50;
        Color resultColor = passed ? new Color(22, 163, 74) : new Color(220, 38, 38);

        JLabel percentLabel = new JLabel(percent + "%", JLabel.CENTER);
        percentLabel.setFont(new Font("Segoe UI", Font.BOLD, 64));
        percentLabel.setForeground(resultColor);
        g.gridy++;
        g.insets = new Insets(10, 20, 0, 20);
        card.add(percentLabel, g);

        JLabel passFail = new JLabel(passed ? "PASSED" : "FAILED", JLabel.CENTER);
        passFail.setFont(new Font("Segoe UI", Font.BOLD, 18));
        passFail.setForeground(resultColor);
        g.gridy++;
        g.insets = new Insets(0, 20, 25, 20);
        card.add(passFail, g);

        // Separator
        JSeparator sep = new JSeparator();
        sep.setForeground(UIStyle.border);
        g.gridy++;
        g.insets = new Insets(0, 30, 20, 30);
        card.add(sep, g);

        // Score
        JLabel scoreLabel = new JLabel("Final Score: " + score + " / " + total, JLabel.CENTER);
        scoreLabel.setFont(UIStyle.subtitleFont);
        scoreLabel.setForeground(UIStyle.text);
        g.gridy++;
        g.insets = new Insets(0, 20, 15, 20);
        card.add(scoreLabel, g);

        // Info Panel
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 0, 8));
        infoPanel.setBackground(UIStyle.secondaryBtn);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIStyle.border, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel studentLabel = new JLabel("Student: Loading...");
        JLabel quizLabel = new JLabel("Quiz: Loading...");
        JLabel teacherLabel = new JLabel("Teacher: Loading...");

        studentLabel.setFont(UIStyle.normalFont);
        quizLabel.setFont(UIStyle.normalFont);
        teacherLabel.setFont(UIStyle.normalFont);

        infoPanel.add(studentLabel);
        infoPanel.add(quizLabel);
        infoPanel.add(teacherLabel);

        g.gridy++;
        g.insets = new Insets(10, 30, 25, 30);
        card.add(infoPanel, g);

        // Load data from DB
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "SELECT u.username, q.title, t.username AS teacher " +
                "FROM users u, quizzes q, users t " +
                "WHERE u.user_id=? AND q.quiz_id=? AND t.user_id=q.created_by"
            );
            ps.setInt(1, student_id);
            ps.setInt(2, quiz_id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                studentLabel.setText("Student: " + rs.getString("username"));
                quizLabel.setText("Quiz: " + rs.getString("title"));
                teacherLabel.setText("Teacher: " + rs.getString("teacher"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Close Button
        JButton close = new JButton("Close Dashboard");
        UIStyle.stylePrimaryButton(close);
        close.addActionListener(e -> dispose());
        
        g.gridy++;
        g.insets = new Insets(10, 30, 20, 30);
        card.add(close, g);

        // Root constraints
        GridBagConstraints rc = new GridBagConstraints();
        rc.gridx = 0; rc.gridy = 0;
        rc.weightx = 1.0; rc.weighty = 1.0;
        rc.fill = GridBagConstraints.HORIZONTAL;
        rc.insets = new Insets(20, 40, 20, 40);
        root.add(card, rc);
        
        add(root);

        setVisible(true);
    }
}