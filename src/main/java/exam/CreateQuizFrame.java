package exam;

import javax.swing.*;
import java.sql.*;
import java.awt.*;

public class CreateQuizFrame extends JFrame {

    int teacher_id, quiz_id;
    JTextField title, timeField, q,o1,o2,o3,o4,correct;

    CreateQuizFrame(int id) {

        teacher_id = id;

        setTitle("Create Quiz");
        setSize(450, 350);
        setMinimumSize(new Dimension(350, 300));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(UIStyle.bg);

        JPanel card = new JPanel(new GridBagLayout());
        UIStyle.styleCard(card);

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 20, 5, 20);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0;

        JLabel l0 = new JLabel("Quiz Title:");
        JLabel ltime = new JLabel("Time (seconds):");
        UIStyle.styleLabel(l0);
        UIStyle.styleLabel(ltime);

        title = new JTextField();
        timeField = new JTextField();
        UIStyle.styleTextField(title);
        UIStyle.styleTextField(timeField);

        JButton createQuiz = new JButton("Create Quiz");
        UIStyle.stylePrimaryButton(createQuiz);

        g.gridx = 0; g.gridy = 0;
        card.add(l0, g);

        g.gridy++;
        g.insets = new Insets(0, 20, 15, 20);
        card.add(title, g);

        g.gridy++;
        g.insets = new Insets(5, 20, 5, 20);
        card.add(ltime, g);

        g.gridy++;
        g.insets = new Insets(0, 20, 20, 20);
        card.add(timeField, g);

        g.gridy++;
        g.insets = new Insets(10, 20, 10, 20);
        card.add(createQuiz, g);

        GridBagConstraints rc = new GridBagConstraints();
        rc.gridx = 0; rc.gridy = 0;
        rc.weightx = 1.0; rc.weighty = 1.0;
        rc.fill = GridBagConstraints.HORIZONTAL;
        rc.insets = new Insets(20, 40, 20, 40);
        root.add(card, rc);

        add(root);

        createQuiz.addActionListener(e -> createQuiz());

        setVisible(true);
    }

    void createQuiz() {
        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO quizzes(title,created_by,time_limit) VALUES(?,?,?)",
                Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1,title.getText());
            ps.setInt(2,teacher_id);
            ps.setInt(3,Integer.parseInt(timeField.getText()));

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            quiz_id = rs.getInt(1);

            showForm();

        } catch(Exception e){ e.printStackTrace(); }
    }

    void showForm() {

        getContentPane().removeAll();
        setSize(480, 550);
        setMinimumSize(new Dimension(400, 500));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(UIStyle.bg);

        JPanel form = new JPanel(new GridBagLayout());
        UIStyle.styleCard(form);

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 15, 2, 15);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0;

        JLabel heading = new JLabel("Add Question", JLabel.CENTER);
        heading.setFont(UIStyle.titleFont);
        heading.setForeground(UIStyle.text);

        JLabel l1=new JLabel("Question:");
        JLabel l2=new JLabel("Option A:");
        JLabel l3=new JLabel("Option B:");
        JLabel l4=new JLabel("Option C:");
        JLabel l5=new JLabel("Option D:");
        JLabel l6=new JLabel("Correct (A-D):");

        UIStyle.styleLabel(l1);
        UIStyle.styleLabel(l2);
        UIStyle.styleLabel(l3);
        UIStyle.styleLabel(l4);
        UIStyle.styleLabel(l5);
        UIStyle.styleLabel(l6);

        q=new JTextField();
        o1=new JTextField();
        o2=new JTextField();
        o3=new JTextField();
        o4=new JTextField();
        correct=new JTextField();

        UIStyle.styleTextField(q);
        UIStyle.styleTextField(o1);
        UIStyle.styleTextField(o2);
        UIStyle.styleTextField(o3);
        UIStyle.styleTextField(o4);
        UIStyle.styleTextField(correct);

        g.gridx = 0; g.gridy = 0;
        g.insets = new Insets(10, 15, 15, 15);
        form.add(heading, g);

        JLabel[] labels = {l1, l2, l3, l4, l5, l6};
        JTextField[] fields = {q, o1, o2, o3, o4, correct};

        for (int i = 0; i < labels.length; i++) {
            g.gridy++;
            g.insets = new Insets(5, 15, 2, 15);
            form.add(labels[i], g);
            
            g.gridy++;
            g.insets = new Insets(0, 15, 10, 15);
            form.add(fields[i], g);
        }

        JButton addQ=new JButton("Add");
        JButton finish=new JButton("Finish");

        UIStyle.stylePrimaryButton(addQ);
        UIStyle.styleSecondaryButton(finish);

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        btnPanel.setBackground(UIStyle.cardBg);
        btnPanel.add(addQ);
        btnPanel.add(finish);

        g.gridy++;
        g.insets = new Insets(10, 15, 10, 15);
        form.add(btnPanel, g);

        GridBagConstraints rc = new GridBagConstraints();
        rc.gridx = 0; rc.gridy = 0;
        rc.weightx = 1.0; rc.weighty = 1.0;
        rc.fill = GridBagConstraints.HORIZONTAL;
        rc.insets = new Insets(20, 30, 20, 30);
        root.add(form, rc);

        add(root);

        addQ.addActionListener(e -> saveQ());
        finish.addActionListener(e -> dispose());

        revalidate();
        repaint();
    }

    void saveQ() {
        try {
            Connection con = DBConnection.getConnection();

            int correctOption = switch(correct.getText().toUpperCase()) {
                case "A" -> 1;
                case "B" -> 2;
                case "C" -> 3;
                case "D" -> 4;
                default -> 0;
            };

            if(correctOption==0) {
                JOptionPane.showMessageDialog(this,"Enter A-D");
                return;
            }

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO questions VALUES(NULL,?,?,?,?,?,?,?)"
            );

            ps.setInt(1,quiz_id);
            ps.setString(2,q.getText());
            ps.setString(3,o1.getText());
            ps.setString(4,o2.getText());
            ps.setString(5,o3.getText());
            ps.setString(6,o4.getText());
            ps.setInt(7,correctOption);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,"Question Added!");

            q.setText("");o1.setText("");o2.setText("");
            o3.setText("");o4.setText("");correct.setText("");

        } catch(Exception e){ e.printStackTrace(); }
    }
}