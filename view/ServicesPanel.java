package view;

import dao.ServiceDAO;
import data.Service;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ServicesPanel extends JPanel {

    private ServiceDAO serviceDAO;
    private JTable servicesTable;
    private DefaultTableModel tableModel;
    
    private JButton btnAddService;
    private JButton btnEditService;
    private JButton btnDeleteService;
    private JButton btnRefresh;

    public ServicesPanel() {
        this.serviceDAO = ServiceDAO.getinstanServiceDAO(); 
        initComponents();
        loadServicesData();
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

        JLabel titleLabel = new JLabel("Services Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        btnAddService = new JButton("+ Add New Service");
        btnAddService.setBackground(new Color(40, 167, 69));
        btnAddService.setForeground(Color.WHITE);
        btnAddService.setFocusPainted(false);
        btnAddService.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAddService.addActionListener(e -> openServiceDialog(null)); // null تعني إضافة جديدة

        btnRefresh = new JButton("Refresh");
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> loadServicesData());

        topPanel.add(titleLabel);
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(btnAddService);
        topPanel.add(btnRefresh);

        add(topPanel, BorderLayout.NORTH);
    }

    private void initTablePanel() {
        String[] columns = {"Service ID", "Service Name", "Price (EGP)"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // جدول غير قابل للتعديل المباشر
            }
        };

        servicesTable = new JTable(tableModel);
        servicesTable.setRowHeight(28);

        JScrollPane scrollPane = new JScrollPane(servicesTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void initActionPanel() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        btnEditService = new JButton("Edit Service");
        btnEditService.setBackground(new Color(23, 162, 184));
        btnEditService.setForeground(Color.WHITE);
        btnEditService.setFocusPainted(false);
        btnEditService.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEditService.addActionListener(e -> editSelectedService());

        btnDeleteService = new JButton("Delete Service");
        btnDeleteService.setBackground(new Color(220, 53, 69));
        btnDeleteService.setForeground(Color.WHITE);
        btnDeleteService.setFocusPainted(false);
        btnDeleteService.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDeleteService.addActionListener(e -> deleteSelectedService());

        actionPanel.add(btnEditService);
        actionPanel.add(btnDeleteService);

        add(actionPanel, BorderLayout.SOUTH);
    }

    public void loadServicesData() {
        tableModel.setRowCount(0);
        List<Service> services = serviceDAO.getAllservice();
        if (services != null) {
            for (Service service : services) {
                tableModel.addRow(new Object[]{
                    service.getId(),
                    service.getName(),
                    service.getPrice()
                });
            }
        }
    }

    private void openServiceDialog(Service service) {
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        Frame ownerFrame = (parentWindow instanceof Frame) ? (Frame) parentWindow : null;
        
        ServiceDialog dialog = new ServiceDialog(ownerFrame, service);
        dialog.setVisible(true);

        loadServicesData();
    }

    private void editSelectedService() {
        int selectedRow = servicesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a service from the table to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) servicesTable.getValueAt(selectedRow, 0);
        String name = (String) servicesTable.getValueAt(selectedRow, 1);
        int price = Integer.parseInt(servicesTable.getValueAt(selectedRow, 2).toString());

        Service serviceToEdit = new Service(id, name, price);
        openServiceDialog(serviceToEdit);
    }

    private void deleteSelectedService() {
        int selectedRow = servicesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a service from the table to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int serviceId = (int) servicesTable.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this service?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = serviceDAO.deleteService(serviceId);
            if (deleted) {
                JOptionPane.showMessageDialog(this, "Service deleted successfully!");
                loadServicesData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete service.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}