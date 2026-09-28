package view;

import dao.WorkerDAO;
import data.Worker;

import javax.swing.*;
import java.awt.*;

public class WorkerDialog extends JDialog {

    private JTextField txtName;
    private JTextField txtSalary; 
    private JButton btnSave;
    private JButton btnCancel;

    private WorkerDAO workerDAO;
    private Worker currentWorker;

    public WorkerDialog(Frame owner, Worker worker) {
        super(owner, worker == null ? "Add New Worker" : "Edit Worker", true);
        this.currentWorker = worker;
        this.workerDAO = WorkerDAO.getInstance();

        setSize(350, 200);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initComponents();
        populateFieldsIfEditing();
        setupEvents();
    }

    private void initComponents() {
        setLayout(new GridLayout(3, 2, 10, 10)); 

        add(new JLabel("  Worker Name:"));
        txtName = new JTextField();
        add(txtName);

        add(new JLabel("  Salary (EGP):"));
        txtSalary = new JTextField();
        add(txtSalary);

        btnSave = new JButton("Save");
        btnSave.setBackground(new Color(40, 167, 69));
        btnSave.setForeground(Color.WHITE);

        btnCancel = new JButton("Cancel");

        add(btnSave);
        add(btnCancel);
    }

    private void populateFieldsIfEditing() {
        if (currentWorker != null) {
            txtName.setText(currentWorker.getName());
            txtSalary.setText(String.valueOf(currentWorker.getDailySalary())); 
        }
    }

    private void setupEvents() {
        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(e -> saveWorker());
    }

    private void saveWorker() {
        String name = txtName.getText().trim();
        String salaryText = txtSalary.getText().trim();

        if (name.isEmpty() || salaryText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int salary = Integer.parseInt(salaryText); 

            boolean success;
            if (currentWorker == null) {
                // INSERT
                Worker newWorker = new Worker(0, name, salary);
                success = workerDAO.insertworker(newWorker);
            } else {
                // UPDATE
                currentWorker.setName(name);
                currentWorker.setDailySalary(salary);
                success = workerDAO.updateworker(currentWorker);
            }

            if (success) {
                JOptionPane.showMessageDialog(this, "Worker saved successfully!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save worker.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric salary.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}