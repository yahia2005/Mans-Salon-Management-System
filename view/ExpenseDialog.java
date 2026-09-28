package view;

import dao.ExpenseDAO;
import data.Expense;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;

public class ExpenseDialog extends JDialog {

    private JTextField txtDescription;
    private JTextField txtAmount;
    private JTextField txtDate; 
    private JButton btnSave;
    private JButton btnCancel;

    private ExpenseDAO expenseDAO;
    private Expense currentExpense;

    public ExpenseDialog(Frame owner, Expense expense) {
        super(owner, expense == null ? "Add New Expense" : "Edit Expense", true);
        this.currentExpense = expense;
        this.expenseDAO = ExpenseDAO.getinstanOrderDao();

        setSize(380, 240);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initComponents();
        populateFieldsIfEditing();
        setupEvents();
    }

    private void initComponents() {
        setLayout(new GridLayout(4, 2, 10, 10));

        add(new JLabel("  Description:"));
        txtDescription = new JTextField();
        add(txtDescription);

        add(new JLabel("  Amount (EGP):"));
        txtAmount = new JTextField();
        add(txtAmount);

        add(new JLabel("  Date (YYYY-MM-DD):"));
        txtDate = new JTextField();
        // افتراضياً يوضع تاريخ اليوم عند الإضافة
        txtDate.setText(LocalDate.now().toString());
        add(txtDate);

        btnSave = new JButton("Save");
        btnSave.setBackground(new Color(40, 167, 69));
        btnSave.setForeground(Color.WHITE);

        btnCancel = new JButton("Cancel");

        add(btnSave);
        add(btnCancel);
    }

    private void populateFieldsIfEditing() {
        if (currentExpense != null) {
            txtDescription.setText(currentExpense.getDescription());
            txtAmount.setText(String.valueOf(currentExpense.getAmount()));
            txtDate.setText(currentExpense.getExpenseDate().toString());
        }
    }

    private void setupEvents() {
        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(e -> saveExpense());
    }

    private void saveExpense() {
        String description = txtDescription.getText().trim();
        String amountText = txtAmount.getText().trim();
        String dateText = txtDate.getText().trim();

        if (description.isEmpty() || amountText.isEmpty() || dateText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int amount = Integer.parseInt(amountText);
            Date expenseDate = Date.valueOf(dateText); 

            boolean success;
            if (currentExpense == null) {
                // INSERT
                Expense newExpense = new Expense(expenseDate, description, amount);
                success = expenseDAO.insertexpense(newExpense);
            } else {
                // UPDATE
                currentExpense.setDescription(description);
                currentExpense.setAmount(amount);
                currentExpense.setExpenseDate(expenseDate);
                success = expenseDAO.updateExpense(currentExpense);
            }

            if (success) {
                JOptionPane.showMessageDialog(this, "Expense saved successfully!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save expense.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric amount.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Please enter date in valid format (YYYY-MM-DD).", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}