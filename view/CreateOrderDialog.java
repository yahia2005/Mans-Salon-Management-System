package view;

import data.Worker;
import data.Order;
import data.Service;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.util.List;

import dao.OrderDAO;
import dao.ServiceDAO;
import dao.WorkerDAO;


public class CreateOrderDialog extends JDialog {
    private JComboBox <Worker>workerComboBox;
    private JComboBox<Service> serviceCombBox;
    private JLabel lblPrice;
    private JButton btnsave;
    private JButton btnCancel;

    private OrderDAO orderDAO;
    private WorkerDAO workerDAO;
    private ServiceDAO serviceDAO;

    public CreateOrderDialog(Frame owner) {
        super(owner, "Create New Order", true);

        this.orderDAO = OrderDAO.getinstanOrderDao();
        this.workerDAO = WorkerDAO.getinstanOrderDao();
        this.serviceDAO = ServiceDAO.getinstanServiceDAO();

        setSize(400, 250);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initComponents();

        loadWorkersAndServices();

        setupevents();
    }

    private void initComponents(){
        setLayout(new GridLayout(4,2,10,10));


        add(new JLabel("Select Worker"));
        workerComboBox = new JComboBox<>();
        add(workerComboBox);
        

        add(new JLabel("Select Service"));
        serviceCombBox = new JComboBox<>();
        add(serviceCombBox);

        add(new JLabel(" Price (EGP"));
        lblPrice = new JLabel();
        lblPrice.setFont(new Font("Arial", Font.BOLD, 15));
        lblPrice.setForeground(new Color(40, 167, 69));
        add(lblPrice);

        btnsave =new JButton("Save Order");
        btnCancel = new JButton("Cancel");
        btnsave.setBorderPainted(false);
        btnsave.setFocusPainted(false);
        btnsave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.setBorderPainted(false);
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));


        add(btnsave);
        add(btnCancel);
    }

    private void loadWorkersAndServices() {
        List<Worker> workers = workerDAO.getAllWorker();
        if (workers != null) {
            for (Worker worker : workers) {
                workerComboBox.addItem(worker);
            }
        }

        List<Service> services = serviceDAO.getAllservice();
        if (services != null) {
            for (Service service : services) {
                serviceCombBox.addItem(service);
            }
        }
    }
    private void setupevents(){
        serviceCombBox.addActionListener(e -> {
        Service selService = (Service) serviceCombBox.getSelectedItem();
            if (selService != null) {
                lblPrice.setText(String.valueOf(selService.getPrice()));
            }
        });

        btnsave.addActionListener(e->saveOrder());
        btnCancel.addActionListener(e-> dispose());

    }
    private void saveOrder(){
        Worker selWorker = (Worker) workerComboBox.getSelectedItem();
        Service selService = (Service) serviceCombBox.getSelectedItem();

        if(selService == null || selWorker == null){
           JOptionPane.showMessageDialog(this, "Please select both a worker and a service." , "Warning", JOptionPane.WARNING_MESSAGE);
           return ; 
        }
        try{
            int price = selService.getPrice();
            
            Order newOrder = new Order();
            newOrder.setWorkerId(selWorker.getId());
            newOrder.setServiceId(selService.getId());
            newOrder.setPrice(price);
            newOrder.setStatus("in_queue");
            newOrder.setOrderDate(new java.sql.Date(System.currentTimeMillis()));
            boolean success = orderDAO.insertOrder(newOrder);
            if(success){
                JOptionPane.showMessageDialog(this, "Order created successfully!");
                dispose();
            }else{
                JOptionPane.showMessageDialog(this, "Failed to create order.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(this, "Please enter a valid price.", "Error" , JOptionPane.ERROR_MESSAGE);
        }
    }

}
