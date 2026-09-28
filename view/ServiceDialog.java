package view;

import dao.ServiceDAO;
import data.Service;

import javax.swing.*;
import java.awt.*;

public class ServiceDialog extends JDialog {

    private JTextField txtName;
    private JTextField txtPrice;
    private JButton btnSave;
    private JButton btnCancel;

    private ServiceDAO serviceDAO;
    private Service currentService; 

    public ServiceDialog(Frame owner, Service service) {
        super(owner, service == null ? "Add New Service" : "Edit Service", true);
        this.currentService = service;
        this.serviceDAO = ServiceDAO.getinstanServiceDAO(); 

        setSize(350, 200);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initComponents();
        populateFieldsIfEditing();
        setupEvents();
    }

    private void initComponents() {
        setLayout(new GridLayout(3, 2, 10, 10));

        add(new JLabel("  Service Name:"));
        txtName = new JTextField();
        add(txtName);

        add(new JLabel("  Price (EGP):"));
        txtPrice = new JTextField();
        add(txtPrice);

        btnSave = new JButton("Save");
        btnSave.setBackground(new Color(40, 167, 69));
        btnSave.setForeground(Color.WHITE);

        btnCancel = new JButton("Cancel");

        add(btnSave);
        add(btnCancel);
    }

    private void populateFieldsIfEditing() {
        if (currentService != null) {
            txtName.setText(currentService.getName());
            txtPrice.setText(String.valueOf(currentService.getPrice()));
        }
    }

    private void setupEvents() {
        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(e -> saveService());
    }

    private void saveService() {
        String name = txtName.getText().trim();
        String priceText = txtPrice.getText().trim();

        if (name.isEmpty() || priceText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int price = Integer.parseInt(priceText);

            boolean success;
            if (currentService == null) {
                Service newService = new Service(0, name, price);
                success = serviceDAO.getInstance(newService);
            } else {
                currentService.setName(name);
                currentService.setPrice(price);
                success = serviceDAO.updateService(currentService);
            }

            if (success) {
                JOptionPane.showMessageDialog(this, "Service saved successfully!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save service.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric price.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}