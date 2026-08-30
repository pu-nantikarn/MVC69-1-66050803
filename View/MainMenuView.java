package View;

import Controller.*;
import Model.DataCenter;

import javax.swing.*;
import java.awt.*;

public class MainMenuView extends JFrame {
    private DataCenter db;
    private VotingController votingCtrl;
    private OfficerController officerCtrl;
    private DashboardController dashCtrl;
    private JTextField txtLoginId;

    public MainMenuView() {
        db = new DataCenter();
        db.initSeedData();
        votingCtrl = new VotingController(db);
        officerCtrl = new OfficerController(db);
        dashCtrl = new DashboardController(db);

        setTitle("System Login");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1, 10, 10));

        JLabel titleLabel = new JLabel("Club President Election System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(titleLabel);

        JPanel loginPanel = new JPanel(new FlowLayout());
        loginPanel.add(new JLabel("Enter ID :"));
        txtLoginId = new JTextField(10);
        loginPanel.add(txtLoginId);
        add(loginPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnLogin = new JButton("Login");
        JButton btnExit = new JButton("Exit");

        btnLogin.addActionListener(e -> handleLogin());
        btnExit.addActionListener(e -> System.exit(0));

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnExit);
        add(buttonPanel);
    }

    //ตรวจสอบสิทธ์เข้าถึง
    private void handleLogin() {
        String id = txtLoginId.getText().trim();
        
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your ID.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (db.officers.containsKey(id)) {
            new OfficerView(officerCtrl, dashCtrl, id).setVisible(true);
        } 
        else if (db.voters.containsKey(id)) {
            new VotingView(votingCtrl, dashCtrl, id).setVisible(true);
        } 
        else {
            JOptionPane.showMessageDialog(this, "ID not found in the system.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}