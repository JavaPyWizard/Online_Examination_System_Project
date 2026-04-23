package exam;

import javax.swing.*;
import java.awt.*;

public class StudentDashboard extends JFrame {

    int student_id;

    StudentDashboard(int id) {
        student_id=id;

        setTitle("Student Dashboard");
        setSize(400,300);
        setMinimumSize(new Dimension(350, 250));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(UIStyle.bg);

        JPanel card = new JPanel(new GridBagLayout());
        UIStyle.styleCard(card);

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 20, 10, 20);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0;

        JLabel title = new JLabel("Student Panel", JLabel.CENTER);
        title.setFont(UIStyle.titleFont);
        title.setForeground(UIStyle.text);
        g.gridx = 0; g.gridy = 0;
        card.add(title, g);

        g.gridy++;
        g.insets = new Insets(20, 20, 20, 20);
        JButton attempt = new JButton("Attempt Quiz");
        UIStyle.stylePrimaryButton(attempt);
        card.add(attempt, g);

        GridBagConstraints rc = new GridBagConstraints();
        rc.gridx = 0; rc.gridy = 0;
        rc.weightx = 1.0; rc.weighty = 1.0;
        rc.fill = GridBagConstraints.HORIZONTAL;
        rc.insets = new Insets(20, 40, 20, 40);
        root.add(card, rc);

        add(root);

        attempt.addActionListener(e -> new QuizSelection(student_id));

        setVisible(true);
    }
}