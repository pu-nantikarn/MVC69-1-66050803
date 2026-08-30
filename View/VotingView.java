package View;

import Controller.VotingController;
import Controller.DashboardController;
import Model.CandidateModel;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class VotingView extends JFrame {
    private VotingController controller;
    private DashboardController dashCtrl;
    private String loggedInVoterId;
    
    private JTextField txtVoterId;
    private JTextField txtRank1;
    private JTextField txtRank2;
    private JTextField txtRank3;

    public VotingView(VotingController controller, DashboardController dashCtrl, String voterId) {
        this.controller = controller;
        this.dashCtrl = dashCtrl;
        this.loggedInVoterId = voterId;
        
        setTitle("Voting System - " + voterId);
        setSize(400, 480); // ปรับขนาดหน้าต่างขึ้นเล็กน้อยเผื่อปุ่มใหม่
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initComponents();
    }

    private void initComponents() {
        JTextArea candidateList = new JTextArea();
        candidateList.setEditable(false);
        candidateList.setText(" Candidate List:\n");
        for (CandidateModel c : controller.getCandidates()) {
            candidateList.append(" - " + c.id + " : " + c.name + "\n");
        }
        add(new JScrollPane(candidateList), BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        formPanel.add(new JLabel("Voter ID:"));
        txtVoterId = new JTextField(loggedInVoterId);
        txtVoterId.setEditable(false); 
        formPanel.add(txtVoterId);

        formPanel.add(new JLabel("Rank 1 (e.g., C01):"));
        txtRank1 = new JTextField();
        formPanel.add(txtRank1);

        formPanel.add(new JLabel("Rank 2:"));
        txtRank2 = new JTextField();
        formPanel.add(txtRank2);

        formPanel.add(new JLabel("Rank 3:"));
        txtRank3 = new JTextField();
        formPanel.add(txtRank3);

        add(formPanel, BorderLayout.CENTER);

        // สร้าง Panel ด้านล่างสำหรับรวมปุ่ม
        JPanel bottomPanel = new JPanel(new FlowLayout());
        
        JButton btnSubmit = new JButton("Submit Vote");
        btnSubmit.addActionListener(e -> submitVote());
        
        // ปุ่มเข้าสู่ Dashboard
        JButton btnDashboard = new JButton("View Dashboard");
        btnDashboard.addActionListener(e -> new DashboardView(dashCtrl).setVisible(true));

        bottomPanel.add(btnSubmit);
        bottomPanel.add(btnDashboard);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }

    //ส่งข้อมูลโหวต
    private void submitVote() {
        String voterId = txtVoterId.getText().trim();
        String r1 = txtRank1.getText().trim();
        String r2 = txtRank2.getText().trim();
        String r3 = txtRank3.getText().trim();

        try {
            controller.submitVote(voterId, Arrays.asList(r1, r2, r3));
            JOptionPane.showMessageDialog(this, "Your vote has been submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            txtRank1.setText("");
            txtRank2.setText("");
            txtRank3.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}