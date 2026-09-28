package view;

import dao.ExpenseDAO;
import dao.OrderDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;

public class ReportsPanel extends JPanel {

    private OrderDAO orderDAO;
    private ExpenseDAO expenseDAO;

    private JTextField txtDate;
    private JButton btnCalculate;

    private JLabel lblTotalIncome;
    private JLabel lblTotalExpenses;
    private JLabel lblNetProfit;

    public ReportsPanel() {
        this.orderDAO = OrderDAO.getinstanOrderDao();
        this.expenseDAO = ExpenseDAO.getinstanOrderDao();

        initComponents();
        calculateReportForDate(Date.valueOf(LocalDate.now())); 
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filter Options"));

        filterPanel.add(new JLabel("Select Date (YYYY-MM-DD):"));
        txtDate = new JTextField(12);
        txtDate.setText(LocalDate.now().toString());
        filterPanel.add(txtDate);

        btnCalculate = new JButton("Calculate Report");
        btnCalculate.setBackground(new Color(23, 162, 184));
        btnCalculate.setForeground(Color.WHITE);
        btnCalculate.setFocusPainted(false);
        btnCalculate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCalculate.addActionListener(e -> onCalculateClick());
        filterPanel.add(btnCalculate);

        add(filterPanel, BorderLayout.NORTH);

        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 20, 0));

        lblTotalIncome = createCard("Total Income", "0 EGP", new Color(40, 167, 69), cardsPanel);
        lblTotalExpenses = createCard("Total Expenses", "0 EGP", new Color(220, 53, 69), cardsPanel);
        lblNetProfit = createCard("Net Profit", "0 EGP", new Color(0, 123, 255), cardsPanel);

        add(cardsPanel, BorderLayout.CENTER);
    }

    private JLabel createCard(String title, String initialValue, Color headerColor, JPanel parentPanel) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(headerColor);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setPreferredSize(new Dimension(0, 40));

        JLabel valueLabel = new JLabel(initialValue, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 22));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        parentPanel.add(card);
        return valueLabel;
    }

    private void onCalculateClick() {
        String dateStr = txtDate.getText().trim();
        if (dateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid date.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Date selectedDate = Date.valueOf(dateStr);
            calculateReportForDate(selectedDate);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Please enter date in format (YYYY-MM-DD).", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calculateReportForDate(Date date) {
        int totalIncome = orderDAO.getTotaleIncomeByDate(date);
        int totalExpenses = expenseDAO.getTotalExpensesByDate(date);
        int netProfit = totalIncome - totalExpenses;

        lblTotalIncome.setText(totalIncome + " EGP");
        lblTotalExpenses.setText(totalExpenses + " EGP");
        lblNetProfit.setText(netProfit + " EGP");

        if (netProfit < 0) {
            lblNetProfit.setForeground(new Color(220, 53, 69)); 
        } else {
            lblNetProfit.setForeground(new Color(40, 167, 69));
        }
    }
}