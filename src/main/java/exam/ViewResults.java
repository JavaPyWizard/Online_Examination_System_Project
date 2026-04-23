package exam;

import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;
import java.awt.*;
import java.io.FileWriter;

public class ViewResults extends JFrame {

    JTable table;
    DefaultTableModel model;
    JTextField searchField;
    TableRowSorter<DefaultTableModel> sorter;

    ViewResults(int teacher_id) {

        setTitle("Leaderboard");
        setSize(650, 420);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        getContentPane().setBackground(UIStyle.bg);

        // Title
        JLabel title = new JLabel("Leaderboard", JLabel.CENTER);
        title.setFont(UIStyle.titleFont);
        title.setForeground(UIStyle.text);
        title.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
        add(title, BorderLayout.NORTH);

        // Center: table
        String[] columns = {"Rank", "Quiz", "Student", "Score", "Percentage"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        table.setRowHeight(28);
        table.setFont(UIStyle.normalFont);
        table.setBackground(UIStyle.cardBg);
        table.setForeground(UIStyle.text);
        table.setGridColor(UIStyle.border);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(0, 1));

        table.setSelectionBackground(UIStyle.primary);
        table.setSelectionForeground(Color.WHITE);

        JTableHeader header = table.getTableHeader();
        header.setBackground(UIStyle.primary);
        header.setForeground(Color.WHITE);
        header.setFont(UIStyle.subtitleFont);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, UIStyle.primary));

        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(UIStyle.border));

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(UIStyle.bg);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        tablePanel.add(scroll, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        // Bottom: search + export
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 8));
        bottomPanel.setBackground(UIStyle.bg);

        JLabel searchLabel = new JLabel("Search:");
        UIStyle.styleLabel(searchLabel);

        searchField = new JTextField(18);
        UIStyle.styleTextField(searchField);

        JButton exportBtn = new JButton("Export CSV");
        UIStyle.stylePrimaryButton(exportBtn);

        bottomPanel.add(searchLabel);
        bottomPanel.add(searchField);
        bottomPanel.add(exportBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        load(teacher_id);

        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchField.getText(), 1, 2));
            }
        });

        exportBtn.addActionListener(e -> exportToCSV());

        setVisible(true);
    }

    void load(int teacher_id) {

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                "SELECT u.username, r.score, q.title, COUNT(qu.question_id) AS total_q " +
                "FROM results r " +
                "JOIN quizzes q ON r.quiz_id = q.quiz_id " +
                "JOIN users u ON r.student_id = u.user_id " +
                "JOIN questions qu ON q.quiz_id = qu.quiz_id " +
                "WHERE q.created_by = ? " +
                "GROUP BY u.username, r.score, q.title " +
                "ORDER BY r.score DESC"
            );

            ps.setInt(1, teacher_id);

            ResultSet rs = ps.executeQuery();

            int rank = 1;

            while (rs.next()) {

                int score = rs.getInt("score");
                int total = rs.getInt("total_q");

                int percent = (total != 0) ? (score * 100) / total : 0;

                model.addRow(new Object[]{
                        rank++,
                        rs.getString("title"),
                        rs.getString("username"),
                        score,
                        percent + "%"
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void exportToCSV() {

        try {
            FileWriter writer = new FileWriter("results.csv");

            for (int i = 0; i < model.getColumnCount(); i++) {
                writer.write(model.getColumnName(i) + ",");
            }
            writer.write("\n");

            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    writer.write(model.getValueAt(i, j).toString() + ",");
                }
                writer.write("\n");
            }

            writer.close();

            JOptionPane.showMessageDialog(this, "Exported as results.csv");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}