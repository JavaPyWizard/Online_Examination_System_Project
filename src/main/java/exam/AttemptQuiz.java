package exam;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.awt.*;

public class AttemptQuiz extends JFrame {

    int student_id, quiz_id, index = 0, score = 0;
    int timeLeft;

    ArrayList<String[]> qs = new ArrayList<>();
    int[] selectedAnswers; // track selected answers for backtracking

    JLabel questionLabel, counter, timerLabel;
    JRadioButton opt1, opt2, opt3, opt4;
    ButtonGroup group;
    JButton next, prev;

    javax.swing.Timer timer;

    AttemptQuiz(int sid, int qid) {

        student_id = sid;
        quiz_id = qid;

        setTitle("Quiz Attempt");
        setSize(600,450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        getContentPane().setBackground(UIStyle.bg);

        // Top bar: counter + timer
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UIStyle.bg);
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 20));

        counter = new JLabel();
        counter.setFont(UIStyle.subtitleFont);
        counter.setForeground(UIStyle.text);

        timerLabel = new JLabel();
        timerLabel.setFont(UIStyle.subtitleFont);
        timerLabel.setForeground(UIStyle.primary);
        timerLabel.setHorizontalAlignment(JLabel.RIGHT);

        topBar.add(counter, BorderLayout.WEST);
        topBar.add(timerLabel, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        // Center: question card
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(UIStyle.bg);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        UIStyle.styleCard(card);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStyle.border, 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        questionLabel = new JLabel();
        questionLabel.setFont(UIStyle.subtitleFont);
        questionLabel.setForeground(UIStyle.text);
        questionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        opt1 = new JRadioButton();
        opt2 = new JRadioButton();
        opt3 = new JRadioButton();
        opt4 = new JRadioButton();

        group = new ButtonGroup();
        group.add(opt1);
        group.add(opt2);
        group.add(opt3);
        group.add(opt4);

        for (JRadioButton rb : new JRadioButton[]{opt1, opt2, opt3, opt4}) {
            rb.setBackground(UIStyle.cardBg);
            rb.setFont(UIStyle.normalFont);
            rb.setForeground(UIStyle.text);
            rb.setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        card.add(questionLabel);
        card.add(Box.createVerticalStrut(12));
        card.add(opt1);
        card.add(Box.createVerticalStrut(4));
        card.add(opt2);
        card.add(Box.createVerticalStrut(4));
        card.add(opt3);
        card.add(Box.createVerticalStrut(4));
        card.add(opt4);

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.BOTH;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        gc.insets = new Insets(5, 20, 5, 20);
        centerWrapper.add(card, gc);
        add(centerWrapper, BorderLayout.CENTER);

        // Bottom bar: prev + next buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setBackground(UIStyle.bg);

        prev = new JButton("Previous");
        UIStyle.styleSecondaryButton(prev);
        prev.setPreferredSize(new Dimension(120, 35));

        next = new JButton("Next");
        UIStyle.stylePrimaryButton(next);
        next.setPreferredSize(new Dimension(120, 35));

        btnPanel.add(prev);
        btnPanel.add(next);
        add(btnPanel, BorderLayout.SOUTH);

        loadTime();
        loadQ();
        startTimer();

        next.addActionListener(e -> goNext());
        prev.addActionListener(e -> goPrev());

        setVisible(true);
    }

    void loadTime() {
        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                "SELECT time_limit FROM quizzes WHERE quiz_id=?"
            );

            ps.setInt(1, quiz_id);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                timeLeft = rs.getInt("time_limit");
                timerLabel.setText("Time: " + timeLeft + "s");
            }

        } catch(Exception e){ e.printStackTrace(); }
    }

    void startTimer() {
        timer = new javax.swing.Timer(1000, e -> {
            timeLeft--;
            timerLabel.setText("Time: " + timeLeft + "s");

            if(timeLeft <= 0) {
                timer.stop();
                JOptionPane.showMessageDialog(this,"Time's Up!");
                finishQuiz();
            }
        });
        timer.start();
    }

    void loadQ() {
        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM questions WHERE quiz_id=?"
            );

            ps.setInt(1, quiz_id);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                qs.add(new String[]{
                        rs.getString("question"),
                        rs.getString("option1"),
                        rs.getString("option2"),
                        rs.getString("option3"),
                        rs.getString("option4"),
                        String.valueOf(rs.getInt("correct_option"))
                });
            }

            selectedAnswers = new int[qs.size()];

            showQuestion();

        } catch(Exception e){ e.printStackTrace(); }
    }

    void showQuestion() {
        if(index < qs.size()) {

            String[] q = qs.get(index);

            counter.setText("Question " + (index+1) + "/" + qs.size());
            questionLabel.setText(q[0]);

            opt1.setText("A. " + q[1]);
            opt2.setText("B. " + q[2]);
            opt3.setText("C. " + q[3]);
            opt4.setText("D. " + q[4]);

            group.clearSelection();

            // Restore previously selected answer if backtracking
            int saved = selectedAnswers[index];
            if(saved == 1) opt1.setSelected(true);
            else if(saved == 2) opt2.setSelected(true);
            else if(saved == 3) opt3.setSelected(true);
            else if(saved == 4) opt4.setSelected(true);

            if(index == qs.size()-1) next.setText("Submit");
            else next.setText("Next");

            prev.setEnabled(index > 0);

        } else finishQuiz();
    }

    int getSelectedOption() {
        if(opt1.isSelected()) return 1;
        if(opt2.isSelected()) return 2;
        if(opt3.isSelected()) return 3;
        if(opt4.isSelected()) return 4;
        return 0;
    }

    void saveCurrentAnswer() {
        if (selectedAnswers != null && index >= 0 && index < selectedAnswers.length) {
            selectedAnswers[index] = getSelectedOption();
        }
    }

    void goNext() {
        saveCurrentAnswer();
        index++;
        if(index < qs.size()) {
            showQuestion();
        } else {
            finishQuiz();
        }
    }

    void goPrev() {
        if(index > 0) {
            saveCurrentAnswer();
            index--;
            showQuestion();
        }
    }

    void finishQuiz() {
        try {
            if(timer != null) timer.stop();

            // Calculate score from saved answers
            score = 0;
            for(int i = 0; i < qs.size(); i++) {
                int correct = Integer.parseInt(qs.get(i)[5]);
                if(selectedAnswers[i] == correct) score++;
            }

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO results(student_id,quiz_id,score) VALUES(?,?,?)"
            );

            ps.setInt(1, student_id);
            ps.setInt(2, quiz_id);
            ps.setInt(3, score);

            ps.executeUpdate();

            // Show Grade Card
            new GradeCardFrame(student_id, quiz_id, score, qs.size());

        } catch(Exception e){ e.printStackTrace(); }

        dispose();
    }
}