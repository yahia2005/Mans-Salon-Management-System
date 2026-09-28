package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainFrame extends JFrame {
    
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public MainFrame() {
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Man's Salon Management System");
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(new Color(236, 240, 241));

        ImageIcon image = new ImageIcon("D:/المختبر/Salon/view/icon.JPG");
        this.setIconImage(image.getImage());

        mainPanel.add(new OrdersPanel(), "OrdersPanel");
        mainPanel.add(new WorkersPanel(), "WorkersPanel");
        mainPanel.add(new ServicesPanel(), "ServicesPanel");
        mainPanel.add(new ExpensesPanel(), "ExpensesPanel");
        mainPanel.add(new AttendancePanel(), "AttendancePanel");
        mainPanel.add(new ReportsPanel(), "ReportsPanel"); 

        JPanel sidePanel = createSidePanel();
        add(sidePanel, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
        
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createSidePanel() {
        JPanel sidePanel = new JPanel();
        sidePanel.setPreferredSize(new Dimension(240, 800));
        sidePanel.setBackground(new Color(44, 62, 80));
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        
        JLabel title = new JLabel("Salon System");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        sidePanel.add(title);
        sidePanel.add(Box.createVerticalStrut(20));

        String[][] menuItems = {
            {"Orders", "OrdersPanel"},
            {"Workers", "WorkersPanel"},
            {"Services", "ServicesPanel"},
            {"Expenses", "ExpensesPanel"},
            {"Attendance", "AttendancePanel"},
            {"Reports", "ReportsPanel"} 
        };

        for (String[] item : menuItems) {
            String displayText = item[0];
            String cardName = item[1];
            JButton btn = createButton(displayText, cardName);
            sidePanel.add(btn);
            sidePanel.add(Box.createVerticalStrut(8));
        }

        sidePanel.add(Box.createVerticalGlue());
        return sidePanel;
    }

    private JButton createButton(String displayText, String cardName) {
        JButton btn = new JButton(displayText);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(200, 50));
        btn.setFont(new Font("Arial", Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
        
        Color defaultColor = new Color(52, 73, 94);
        btn.setBackground(defaultColor);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(41, 128, 185));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(52, 73, 94));
            }
        });

        btn.addActionListener(e -> cardLayout.show(mainPanel, cardName));
        return btn;
    } 
}