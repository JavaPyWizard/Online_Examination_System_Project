package exam;

import javax.swing.*;
import java.awt.*;

public class TeacherDashboard extends JFrame {

    int teacher_id;

    TeacherDashboard(int id) {
        teacher_id = id;

        setTitle("Teacher Dashboard");
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

        JLabel title = new JLabel("Teacher Panel", JLabel.CENTER);
        title.setFont(UIStyle.titleFont);
        title.setForeground(UIStyle.text);
        g.gridx = 0; g.gridy = 0;
        card.add(title, g);

        g.gridy++;
        g.insets = new Insets(20, 20, 10, 20);
        JButton create = new JButton("Create Quiz");
        UIStyle.stylePrimaryButton(create);
        card.add(create, g);

        g.gridy++;
        g.insets = new Insets(0, 20, 20, 20);
        JButton view = new JButton("View Results");
        UIStyle.styleSecondaryButton(view);
        card.add(view, g);

        GridBagConstraints rc = new GridBagConstraints();
        rc.gridx = 0; rc.gridy = 0;
        rc.weightx = 1.0; rc.weighty = 1.0;
        rc.fill = GridBagConstraints.HORIZONTAL;
        rc.insets = new Insets(20, 40, 20, 40);
        root.add(card, rc);

        add(root);

        create.addActionListener(e -> new CreateQuizFrame(teacher_id));
        view.addActionListener(e -> new ViewResults(teacher_id));

        setVisible(true);
    }
}