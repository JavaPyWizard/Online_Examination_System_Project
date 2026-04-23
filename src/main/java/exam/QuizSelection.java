package exam;

import javax.swing.*;
import java.sql.*;
import java.awt.*;
import java.util.HashMap;

public class QuizSelection extends JFrame {

    int student_id;
    JComboBox<String> list;
    HashMap<String,Integer> map=new HashMap<>();

    QuizSelection(int id) {
        student_id=id;

        setTitle("Select Quiz");
        setSize(450, 300);
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

        JLabel title = new JLabel("Select a Quiz", JLabel.CENTER);
        title.setFont(UIStyle.titleFont);
        title.setForeground(UIStyle.text);
        g.gridx = 0; g.gridy = 0;
        card.add(title, g);

        g.gridy++;
        g.insets = new Insets(10, 20, 20, 20);
        list = new JComboBox<>();
        list.setFont(UIStyle.normalFont);
        list.setBackground(Color.WHITE);
        card.add(list, g);

        g.gridy++;
        g.insets = new Insets(0, 20, 20, 20);
        JButton start = new JButton("Start");
        UIStyle.stylePrimaryButton(start);
        card.add(start, g);

        GridBagConstraints rc = new GridBagConstraints();
        rc.gridx = 0; rc.gridy = 0;
        rc.weightx = 1.0; rc.weighty = 1.0;
        rc.fill = GridBagConstraints.HORIZONTAL;
        rc.insets = new Insets(20, 40, 20, 40);
        root.add(card, rc);

        add(root);

        load();

        start.addActionListener(e -> {
            String s=(String)list.getSelectedItem();
            if(s == null) return;
            int qid = map.get(s);

            // Check if student has already attempted this quiz
            try {
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(
                    "SELECT COUNT(*) FROM results WHERE student_id=? AND quiz_id=?"
                );
                ps.setInt(1, student_id);
                ps.setInt(2, qid);
                ResultSet rs = ps.executeQuery();
                rs.next();

                if(rs.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(this,
                        "You have already attempted this quiz. Multiple attempts are not allowed.",
                        "Already Attempted", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch(Exception ex) { ex.printStackTrace(); }

            new AttemptQuiz(student_id, qid);
            dispose();
        });

        setVisible(true);
    }

    void load() {
        try {
            Connection con=DBConnection.getConnection();

            ResultSet rs=con.createStatement().executeQuery(
                "SELECT quizzes.quiz_id,title,username FROM quizzes JOIN users ON quizzes.created_by=users.user_id"
            );

            while(rs.next()) {
                String name=rs.getString("title")+" (by "+rs.getString("username")+")";
                list.addItem(name);
                map.put(name,rs.getInt("quiz_id"));
            }

        } catch(Exception e){ e.printStackTrace(); }
    }
}