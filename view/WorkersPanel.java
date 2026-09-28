package view;

import dao.WorkerDAO;
import data.Worker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class WorkersPanel extends JPanel {

    private WorkerDAO workerDAO;
    private JTable workersTable;
    private DefaultTableModel tableModel;

    private JButton btnAddWorker;
    private JButton btnEditWorker;
    private JButton btnDeleteWorker;
    private JButton btnRefresh;

    public WorkersPanel() {
        this.workerDAO = WorkerDAO.getinstanOrderDao();
        initComponents();
        loadWorkersData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        initTopPanel();
        initTablePanel();
        initActionPanel();
    }

    private void initTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

        JLabel titleLabel = new JLabel("Workers Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        btnAddWorker = new JButton("+ Add New Worker");
        btnAddWorker.setBackground(new Color(40, 167, 69));
        btnAddWorker.setForeground(Color.WHITE);
        btnAddWorker.setFocusPainted(false);
        btnAddWorker.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAddWorker.addActionListener(e -> openWorkerDialog(null));

        btnRefresh = new JButton("Refresh");
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> loadWorkersData());

        topPanel.add(titleLabel);
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(btnAddWorker);
        topPanel.add(btnRefresh);

        add(topPanel, BorderLayout.NORTH);
    }

    private void initTablePanel() {
        // إضافة عمود Salary (EGP)
        String[] columns = {"Worker ID", "Worker Name", "Salary (EGP)"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        workersTable = new JTable(tableModel);
        workersTable.setRowHeight(28);

        JScrollPane scrollPane = new JScrollPane(workersTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void initActionPanel() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        btnEditWorker = new JButton("Edit Worker");
        btnEditWorker.setBackground(new Color(23, 162, 184));
        btnEditWorker.setForeground(Color.WHITE);
        btnEditWorker.setFocusPainted(false);
        btnEditWorker.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEditWorker.addActionListener(e -> editSelectedWorker());

        btnDeleteWorker = new JButton("Delete Worker");
        btnDeleteWorker.setBackground(new Color(220, 53, 69));
        btnDeleteWorker.setForeground(Color.WHITE);
        btnDeleteWorker.setFocusPainted(false);
        btnDeleteWorker.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDeleteWorker.addActionListener(e -> deleteSelectedWorker());

        actionPanel.add(btnEditWorker);
        actionPanel.add(btnDeleteWorker);

        add(actionPanel, BorderLayout.SOUTH);
    }

    public void loadWorkersData() {
        tableModel.setRowCount(0);
        List<Worker> workers = workerDAO.getAllWorker();
        if (workers != null) {
            for (Worker worker : workers) {
                tableModel.addRow(new Object[]{
                    worker.getId(),
                    worker.getName(),
                    worker.getDailySalary() 
                });
            }
        }
    }

    private void openWorkerDialog(Worker worker) {
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        Frame ownerFrame = (parentWindow instanceof Frame) ? (Frame) parentWindow : null;

        WorkerDialog dialog = new WorkerDialog(ownerFrame, worker);
        dialog.setVisible(true);

        loadWorkersData();
    }

    private void editSelectedWorker() {
        int selectedRow = workersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a worker from the table to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) workersTable.getValueAt(selectedRow, 0);
        String name = (String) workersTable.getValueAt(selectedRow, 1);
        int salary = Integer.parseInt(workersTable.getValueAt(selectedRow, 2).toString()); 

        Worker workerToEdit = new Worker(id, name, salary);
        openWorkerDialog(workerToEdit);
    }

    private void deleteSelectedWorker() {
        int selectedRow = workersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a worker from the table to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int workerId = (int) workersTable.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this worker?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = workerDAO.deleteworker(workerId);
            if (deleted) {
                JOptionPane.showMessageDialog(this, "Worker deleted successfully!");
                loadWorkersData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete worker.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}