package view;

import dao.OrderDAO;
import data.Order;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Window;
import java.util.List;
import java.awt.Cursor;

import javax.swing.table.DefaultTableModel;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;

public class OrdersPanel extends JPanel {
    private  OrderDAO orderDAO;

    private JSpinner dateSpinner;
    private JTable ordersTable;
    private DefaultTableModel tableModel;
    private JButton btnaccapted;
    private JButton btnRejeact;

    public OrdersPanel(){
        this.orderDAO = OrderDAO.getinstanOrderDao();
        intiComponants();

    }

    private void intiComponants(){
        setLayout(new BorderLayout(15,15));
        setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        initTopPanel();
        initTablePanel();
        initActionPanel();


    }
    private void initTablePanel(){
        String[] columns = {"Order ID", "Worker ID", "Service ID", "Price", "Status", "Date"};

        tableModel = new DefaultTableModel(columns,0){
            @Override 
            public boolean isCellEditable(int row,int column){
                return false;
            }
        };

        ordersTable = new JTable(tableModel);
        ordersTable.setRowHeight(28);

        JScrollPane scrollPane = new JScrollPane(ordersTable);
        add(scrollPane,BorderLayout.CENTER);
    }
    private void initActionPanel(){
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT,10 ,10));
        
        btnaccapted = new JButton("Accept Order");
        btnaccapted.setBackground(new Color(40, 167, 69));
        btnaccapted.setForeground(Color.white);
        btnaccapted.setBorderPainted(false);
        btnaccapted.setFocusPainted(false);
        btnaccapted.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnaccapted.addActionListener(e -> updateOrderstatus("ACCEPTED"));

        btnRejeact = new JButton("Reject Order");
        btnRejeact.setBackground(new Color(220, 53, 69));
        btnRejeact.setForeground(Color.WHITE);
        btnRejeact.setFocusPainted(false);
        btnRejeact.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRejeact.addActionListener(e -> updateOrderstatus("REJECTED"));

        actionPanel.add(btnaccapted);
        actionPanel.add(btnRejeact);

        add(actionPanel, BorderLayout.SOUTH);
    }

    private void updateOrderstatus(String newstatu){
        int selectedRow = ordersTable.getSelectedRow();
        if(selectedRow == -1){
            JOptionPane.showMessageDialog(this,"Please select an order from the table first!", "Warning", JOptionPane.WARNING_MESSAGE);
            return ;
        }

        int orderid = (int) ordersTable.getValueAt(selectedRow, 0);
        boolean update = orderDAO.updateStatus(newstatu , orderid);
        if(update){
            JOptionPane.showMessageDialog(this,"Order status updated to "+ newstatu + " successfully!");
            loadOrdersBySelectedDate();
        }else{
            JOptionPane.showMessageDialog(this,"Failed to update order status. ","Error",JOptionPane.ERROR_MESSAGE);
        }
    }
    private void  initTopPanel(){
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,10,0));

        JLabel labelDate = new JLabel("Chose Date");
        labelDate.setFont(new Font("Arial",Font.BOLD,14));

        SpinnerDateModel dateModel = new SpinnerDateModel();
        dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner , "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.addChangeListener(e-> loadOrdersBySelectedDate());

        JButton btnNewOrder = new JButton("New Order +");
        btnNewOrder.setBackground(new Color(40,167,69));
        btnNewOrder.setForeground(Color.WHITE);
        btnNewOrder.setBorderPainted(false);
        btnNewOrder.setFocusPainted(false);
        btnNewOrder.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNewOrder.addActionListener(e -> openCreateOrderDialog()); 

        JButton btnRefresh = new JButton("Refresh");

        btnRefresh.setBorderPainted(false);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> loadOrdersBySelectedDate());


        topPanel.add(labelDate);
        topPanel.add(dateSpinner);
        topPanel.add(btnRefresh);
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(btnNewOrder);

        add(topPanel, BorderLayout.NORTH);
    }
    void openCreateOrderDialog(){
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        Frame owFrame =  (parentWindow instanceof Frame) ? (Frame) parentWindow : null;;
        CreateOrderDialog dialog = new CreateOrderDialog(owFrame);

        dialog.setVisible(true); 

        dateSpinner.setValue(new java.util.Date());

        loadOrdersBySelectedDate();
    }
    public void loadOrdersBySelectedDate() {
        tableModel.setRowCount(0);
        dateSpinner.getAccessibleContext();
        java.util.Date utilDate = (java.util.Date) dateSpinner.getValue();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

        List<Order> orders = orderDAO.getOrdersBydate(sqlDate);
        if(orders != null){
            for(Order order : orders){
                tableModel.addRow(new Object[]{
                    order.getId(),
                    order.getWorkerId(),
                    order.getServiceId(),
                    order.getPrice(),
                    order.getStatus(),
                    order.getOrderDate()
                });
            }
        }
    }
}