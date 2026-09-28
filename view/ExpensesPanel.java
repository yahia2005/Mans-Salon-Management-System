package view;

import dao.ExpenseDAO;
import data.Expense;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.util.List;

public class ExpensesPanel extends JPanel {

    private ExpenseDAO expenseDAO;
    private JTable expensesTable;
    private DefaultTableModel tableModel;

    private JButton btnAddExpense;
    private JButton btnEditExpense;
    private JButton btnDeleteExpense;
    private JButton btnRefresh;

    public ExpensesPanel() {
        this.expenseDAO = ExpenseDAO.getinstanOrderDao();
        initComponents();
        loadExpensesData();
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

        JLabel titleLabel = new JLabel("Expenses Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        btnAddExpense = new JButton("+ Add Expense");
        btnAddExpense.setBackground(new Color(40, 167, 69));
        btnAddExpense.setForeground(Color.WHITE);
        btnAddExpense.setFocusPainted(false);
        btnAddExpense.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAddExpense.addActionListener(e -> openExpenseDialog(null));

        btnRefresh = new JButton("Refresh");
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> loadExpensesData());

        topPanel.add(titleLabel);
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(btnAddExpense);
        topPanel.add(btnRefresh);

        add(topPanel, BorderLayout.NORTH);
    }

    private void initTablePanel() {
        String[] columns = {"Expense ID", "Date", "Description", "Amount (EGP)"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        expensesTable = new JTable(tableModel);
        expensesTable.setRowHeight(28);

        JScrollPane scrollPane = new JScrollPane(expensesTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void initActionPanel() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        btnEditExpense = new JButton("Edit Expense");
        btnEditExpense.setBackground(new Color(23, 162, 184));
        btnEditExpense.setForeground(Color.WHITE);
        btnEditExpense.setFocusPainted(false);
        btnEditExpense.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEditExpense.addActionListener(e -> editSelectedExpense());

        btnDeleteExpense = new JButton("Delete Expense");
        btnDeleteExpense.setBackground(new Color(220, 53, 69));
        btnDeleteExpense.setForeground(Color.WHITE);
        btnDeleteExpense.setFocusPainted(false);
        btnDeleteExpense.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDeleteExpense.addActionListener(e -> deleteSelectedExpense());

        actionPanel.add(btnEditExpense);
        actionPanel.add(btnDeleteExpense);

        add(actionPanel, BorderLayout.SOUTH);
    }

    public void loadExpensesData() {
        tableModel.setRowCount(0);
        List<Expense> expenses = expenseDAO.getAllexpenses();
        if (expenses != null) {
            for (Expense expense : expenses) {
                tableModel.addRow(new Object[]{
                    expense.getId(),
                    expense.getExpenseDate(),
                    expense.getDescription(),
                    expense.getAmount()
                });
            }
        }
    }

    private void openExpenseDialog(Expense expense) {
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        Frame ownerFrame = (parentWindow instanceof Frame) ? (Frame) parentWindow : null;

        ExpenseDialog dialog = new ExpenseDialog(ownerFrame, expense);
        dialog.setVisible(true);

        loadExpensesData();
    }

    private void editSelectedExpense() {
        int selectedRow = expensesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an expense from the table to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) expensesTable.getValueAt(selectedRow, 0);
        Date date = (Date) expensesTable.getValueAt(selectedRow, 1);
        String description = (String) expensesTable.getValueAt(selectedRow, 2);
        int amount = (int) expensesTable.getValueAt(selectedRow, 3);

        Expense expenseToEdit = new Expense(id, date, description, amount);
        openExpenseDialog(expenseToEdit);
    }

    private void deleteSelectedExpense() {
        int selectedRow = expensesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an expense from the table to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int expenseId = (int) expensesTable.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this expense?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = expenseDAO.deleteExpense(expenseId);
            if (deleted) {
                JOptionPane.showMessageDialog(this, "Expense deleted successfully!");
                loadExpensesData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete expense.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}