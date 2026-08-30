package View;

import Controller.OfficerController;
import Controller.DashboardController; // Imported the DashboardController
import Model.BallotModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OfficerView extends JFrame {
    private OfficerController controller;
    private DashboardController dashCtrl; // Added DashboardController reference
    private String loggedInOfficerId;
    
    private JTable ballotTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> comboPendingPatterns;
    private JButton btnApprove;
    private JButton btnReject;
    private JButton btnCloseElection;

    public OfficerView(OfficerController controller, DashboardController dashCtrl, String officerId) {
        this.controller = controller;
        this.dashCtrl = dashCtrl;
        this.loggedInOfficerId = officerId;
        
        setTitle("Management System (Officer) - " + officerId);
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        initComponents();
        refreshData(); 
    }

    private void initComponents() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Officer ID: " + loggedInOfficerId));
        
        btnCloseElection = new JButton("Close Election & Check Patterns");
        JButton btnRefresh = new JButton("Refresh Table");
        JButton btnDashboard = new JButton("View Dashboard"); // Added View Dashboard button

        btnCloseElection.addActionListener(e -> handleCloseElection());
        btnRefresh.addActionListener(e -> refreshData());
        btnDashboard.addActionListener(e -> new DashboardView(dashCtrl).setVisible(true)); // Opens the Dashboard

        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(btnCloseElection);
        topPanel.add(btnRefresh);
        topPanel.add(btnDashboard); // Added button to the panel
        add(topPanel, BorderLayout.NORTH);

        String[] columnNames = {"Ballot ID", "Voter ID", "Ranking Pattern", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        ballotTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(ballotTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("All Ballots"));
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Review Pending Ballots (One by One)"));
        
        bottomPanel.add(new JLabel("Select Ballot ID:"));
        comboPendingPatterns = new JComboBox<>();
        bottomPanel.add(comboPendingPatterns);

        btnApprove = new JButton("Approve");
        btnApprove.setBackground(new Color(144, 238, 144)); 
        
        btnReject = new JButton("Reject");
        btnReject.setBackground(new Color(255, 182, 193)); 

        btnApprove.addActionListener(e -> handleReviewBallot(true));
        btnReject.addActionListener(e -> handleReviewBallot(false));

        bottomPanel.add(btnApprove);
        bottomPanel.add(btnReject);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void refreshData() {
        tableModel.setRowCount(0);
        List<BallotModel> ballots = controller.getAllBallots();
        for (BallotModel b : ballots) {
            tableModel.addRow(new Object[]{
                b.id, 
                b.voterId, 
                b.getPatternKey(), 
                b.getStatus().name()
            });
        }

        comboPendingPatterns.removeAllItems();
        List<String> pendingBallots = controller.getPendingPatterns();
        for (String ballotId : pendingBallots) {
            comboPendingPatterns.addItem(ballotId);
        }

        String electionStatus = controller.getElectionStatus();
        
        boolean canReview = (comboPendingPatterns.getItemCount() > 0) && electionStatus.equals("CLOSED");
        btnApprove.setEnabled(canReview);
        btnReject.setEnabled(canReview);

        if (!electionStatus.equals("OPEN")) {
            btnCloseElection.setEnabled(false);
            btnCloseElection.setText("Election Closed");
        }
    }

    //สั่งปิดการเลือกตั้ง
    private void handleCloseElection() {
        try {
            controller.closeElection(loggedInOfficerId);
            JOptionPane.showMessageDialog(this, "Election closed. Patterns have been checked.", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            if (controller.getElectionStatus().equals("SUMMARIZED")) {
                JOptionPane.showMessageDialog(this, "No pending ballots found.\nElection is automatically SUMMARIZED.", "Election Summarized", JOptionPane.INFORMATION_MESSAGE);
            }
            
            refreshData(); 
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //ส่งผลตรวจสอบ
    private void handleReviewBallot(boolean isApproved) {
        String selectedBallot = (String) comboPendingPatterns.getSelectedItem();
        if (selectedBallot == null || selectedBallot.isEmpty()) {
            return;
        }

        String action = isApproved ? "Approve" : "Reject";
        int choice = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to " + action + " ballot [" + selectedBallot + "]?", 
            "Confirm Decision", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            try {
                controller.reviewPatternGroup(loggedInOfficerId, selectedBallot, isApproved);
                JOptionPane.showMessageDialog(this, "Decision recorded successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                
                if (controller.getElectionStatus().equals("SUMMARIZED")) {
                    JOptionPane.showMessageDialog(this, "All pending ballots have been reviewed.\nElection is now automatically SUMMARIZED.", "Election Summarized", JOptionPane.INFORMATION_MESSAGE);
                }
                
                refreshData(); 
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}