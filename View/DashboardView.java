package View;

import Controller.DashboardController;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class DashboardView extends JFrame {
    private DashboardController controller;
    private JTextArea displayArea;

    public DashboardView(DashboardController controller) {
        this.controller = controller;
        setTitle("Election Dashboard");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        JButton btnRefresh = new JButton("Refresh Data");
        btnRefresh.addActionListener(e -> renderDashboardData());
        add(btnRefresh, BorderLayout.SOUTH);

        renderDashboardData();
    }

    //ดึงข้อมูลจาก controller
    private void renderDashboardData() {
        try {
            Map<String, Object> data = controller.getDashboardData();
            String status = (String) data.get("election_status");
            StringBuilder sb = new StringBuilder();

            sb.append("================ DASHBOARD ================\n");
            sb.append("Election Status: ").append(status).append("\n");
            sb.append("-------------------------------------------\n");

            if (status.equals("OPEN")) {
                sb.append("Total Ballots Received: ").append(data.get("total_received_ballots")).append(" ballots\n");
            } 
            else if (status.equals("CLOSED")) {
                sb.append(">> Pending Patterns for Review:\n");
                Map<String, Integer> pending = (Map<String, Integer>) data.get("pending_patterns");
                if (pending.isEmpty()) {
                    sb.append("  - No pending patterns\n");
                } else {
                    // ปรับแก้ตรงนี้ให้แสดงเฉพาะจำนวน ballots โดยไม่แสดง Pattern [...]
                    pending.forEach((k, v) -> sb.append("  - Count: ").append(v).append(" ballots\n"));
                }

                sb.append("\n>> Temporary Scores:\n");
                List<String> scores = (List<String>) data.get("temporary_scores");
                scores.forEach(s -> sb.append("  ").append(s).append("\n"));
            } 
            else if (status.equals("SUMMARIZED")) {
                sb.append(">> Final Official Scores:\n");
                List<String> finalScores = (List<String>) data.get("final_scores");
                finalScores.forEach(s -> sb.append("  ").append(s).append("\n"));
                
                sb.append("\n>> Ballot Information:\n");
                sb.append("  - Approved/Counted Ballots: ").append(data.get("total_approved_ballots")).append(" ballots\n");
                sb.append("  - Rejected Ballots: ").append(data.get("total_rejected_ballots")).append(" ballots\n");
            }
            sb.append("===========================================\n");

            displayArea.setText(sb.toString());
        } catch (Exception ex) {
            displayArea.setText("Error retrieving data: " + ex.getMessage());
        }
    }
}