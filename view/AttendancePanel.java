package view;

import dao.AttendanceDAO;
import dao.WorkerDAO;
import data.Attendance;
import data.Worker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttendancePanel extends JPanel {

    private AttendanceDAO attendanceDAO;
    private WorkerDAO workerDAO;

    private JTextField txtDate;
    private JTable attendanceTable;
    private DefaultTableModel tableModel;
    private JButton btnLoadDate;
    private JButton btnSaveAttendance;

    private Map<Integer, String> workerNameMap;

    public AttendancePanel() {
        this.attendanceDAO = AttendanceDAO.getinstanOrderDao();
        this.workerDAO = WorkerDAO.getInstance();
        this.workerNameMap = new HashMap<>();

        initComponents();
        loadAttendanceData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        initTopPanel();
        initTablePanel();
        initBottomPanel();
    }

    private void initTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));

        JLabel lblTitle = new JLabel("Worker Attendance System");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));

        topPanel.add(lblTitle);
        topPanel.add(Box.createHorizontalStrut(30));

        topPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        txtDate = new JTextField(10);
        txtDate.setText(LocalDate.now().toString());
        topPanel.add(txtDate);

        btnLoadDate = new JButton("Load Attendance");
        btnLoadDate.setBackground(new Color(23, 162, 184));
        btnLoadDate.setForeground(Color.WHITE);
        btnLoadDate.setFocusPainted(false);
        btnLoadDate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLoadDate.addActionListener(e -> loadAttendanceData());
        topPanel.add(btnLoadDate);

        add(topPanel, BorderLayout.NORTH);
    }

    private void initTablePanel() {
        String[] columns = {"Worker ID", "Worker Name", "Is Present?"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2) {
                    return Boolean.class;
                }
                return Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; 
            }
        };

        attendanceTable = new JTable(tableModel);
        attendanceTable.setRowHeight(30);
        attendanceTable.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void initBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        btnSaveAttendance = new JButton("Save Attendance");
        btnSaveAttendance.setBackground(new Color(40, 167, 69));
        btnSaveAttendance.setForeground(Color.WHITE);
        btnSaveAttendance.setFont(new Font("Arial", Font.BOLD, 14));
        btnSaveAttendance.setFocusPainted(false);
        btnSaveAttendance.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSaveAttendance.addActionListener(e -> saveAttendanceData());

        bottomPanel.add(btnSaveAttendance);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void loadAttendanceData() {
        String dateStr = txtDate.getText().trim();
        if (dateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a date.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Date date = Date.valueOf(dateStr);
            tableModel.setRowCount(0);
            workerNameMap.clear();

            List<Worker> workers = workerDAO.getAllWorker();
            
            List<Attendance> attendanceList = attendanceDAO.getAttendanceByDate(date);

            Map<Integer, Boolean> attendanceStatusMap = new HashMap<>();
            if (attendanceList != null) {
                for (Attendance att : attendanceList) {
                    attendanceStatusMap.put(att.getWorkerId(), att.isPresent());
                }
            }

            if (workers != null) {
                for (Worker w : workers) {
                    workerNameMap.put(w.getId(), w.getName());
                    boolean isPresent = attendanceStatusMap.getOrDefault(w.getId(), false);

                    tableModel.addRow(new Object[]{
                        w.getId(),
                        w.getName(),
                        isPresent
                    });
                }
            }

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Please enter date in valid format (YYYY-MM-DD).", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveAttendanceData() {
        String dateStr = txtDate.getText().trim();
        if (dateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid date.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Date date = Date.valueOf(dateStr);
            boolean allSaved = true;

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                int workerId = (int) tableModel.getValueAt(i, 0);
                boolean isPresent = (boolean) tableModel.getValueAt(i, 2);

                Attendance attendance = new Attendance();
                attendance.setWorkerId(workerId);
                attendance.setWorkDate(date);
                attendance.setPresent(isPresent);

                boolean success = attendanceDAO.recordAttendance(attendance);
                if (!success) {
                    allSaved = false;
                }
            }

            if (allSaved) {
                JOptionPane.showMessageDialog(this, "Attendance saved successfully for " + dateStr + "!");
            } else {
                JOptionPane.showMessageDialog(this, "Some attendance records failed to save.", "Warning", JOptionPane.WARNING_MESSAGE);
            }

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Please enter date in valid format (YYYY-MM-DD).", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}